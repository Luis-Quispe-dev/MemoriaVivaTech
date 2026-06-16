package com.upc.ss.services;

import com.upc.ss.dtos.*;
import com.upc.ss.entities.*;
import com.upc.ss.repositories.*;
import com.upc.ss.security.entities.Role;
import com.upc.ss.security.entities.User;
import com.upc.ss.security.repositories.RoleRepository;
import com.upc.ss.security.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdultoMayorRepository adultoMayorRepository;

    @Autowired
    private CuidadorRepository cuidadorRepository;

    @Autowired
    private ConfiguracionService configuracionService;

    @Autowired
    private SuscripcionService suscripcionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    // ─── REGISTRAR ADULTO MAYOR ───────────────────────────────────────────────

    public AdultoMayorRespuestaDTO registrarAdultoMayor(AdultoMayorLlamarDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail()))
            throw new RuntimeException("El email ya está registrado");

        Role rol = roleRepository.findByName("ROLE_ADULTO_MAYOR")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Crear User de security
        User user = new User();
        user.setUsername(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getContrasena()));
        user.setEmail(dto.getEmail());
        user.setNombreCompleto(dto.getNombreCompleto());
        user.getRoles().add(rol);
        User userGuardado = userRepository.save(user);

        // Crear perfil AdultoMayor
        AdultoMayor adultoMayor = new AdultoMayor();
        adultoMayor.setUser(userGuardado);
        adultoMayor.setFechaNacimiento(dto.getFechaNacimiento());
        AdultoMayor guardado = adultoMayorRepository.save(adultoMayor);

        configuracionService.crearConfiguracionInicial(userGuardado.getId());
        suscripcionService.crearSuscripcionGratis(guardado.getIdAdultoMayor());

        return armarResponseAdulto(guardado);
    }

    // ─── REGISTRAR CUIDADOR ───────────────────────────────────────────────────

    public CuidadorRespuestaDTO registrarCuidador(CuidadorLlamarDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail()))
            throw new RuntimeException("El email ya está registrado");

        Role rol = roleRepository.findByName("ROLE_CUIDADOR")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Crear User de security
        User user = new User();
        user.setUsername(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getContrasena()));
        user.setEmail(dto.getEmail());
        user.setNombreCompleto(dto.getNombreCompleto());
        user.getRoles().add(rol);
        User userGuardado = userRepository.save(user);

        // Crear perfil Cuidador
        Cuidador cuidador = new Cuidador();
        cuidador.setUser(userGuardado);
        cuidador.setTelefono(dto.getTelefono());
        cuidador.setBiografia(dto.getBiografia());
        cuidador.setNotificacionEncendida(true);
        Cuidador guardado = cuidadorRepository.save(cuidador);

        configuracionService.crearConfiguracionInicial(userGuardado.getId());

        return armarResponseCuidador(guardado);
    }

    // ─── LISTAR ───────────────────────────────────────────────────────────────

    public List<AdultoMayorRespuestaDTO> listarAdultosMayores() {
        return adultoMayorRepository.findAll()
                .stream().map(this::armarResponseAdulto)
                .collect(Collectors.toList());
    }

    public List<CuidadorRespuestaDTO> listarCuidadores() {
        return cuidadorRepository.findAll()
                .stream().map(this::armarResponseCuidador)
                .collect(Collectors.toList());
    }

    // ─── OBTENER POR ID ───────────────────────────────────────────────────────

    public AdultoMayorRespuestaDTO obtenerAdultoMayorPorId(Long id) {
        return armarResponseAdulto(adultoMayorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado")));
    }

    public CuidadorRespuestaDTO obtenerCuidadorPorId(Long id) {
        return armarResponseCuidador(cuidadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuidador no encontrado")));
    }

    // ─── EDITAR ───────────────────────────────────────────────────────────────

    public AdultoMayorRespuestaDTO editarAdultoMayor(Long id, AdultoMayorLlamarDTO dto) {

        AdultoMayor adultoMayor = adultoMayorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        User user = adultoMayor.getUser();
        user.setNombreCompleto(dto.getNombreCompleto());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getEmail());
        userRepository.save(user);

        adultoMayor.setFechaNacimiento(dto.getFechaNacimiento());

        return armarResponseAdulto(adultoMayorRepository.save(adultoMayor));
    }

    public CuidadorRespuestaDTO editarCuidador(Long id, CuidadorLlamarDTO dto) {

        Cuidador cuidador = cuidadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuidador no encontrado"));

        User user = cuidador.getUser();
        user.setNombreCompleto(dto.getNombreCompleto());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getEmail());
        userRepository.save(user);

        cuidador.setTelefono(dto.getTelefono());
        cuidador.setBiografia(dto.getBiografia());

        return armarResponseCuidador(cuidadorRepository.save(cuidador));
    }

    // ─── HELPERS ──────────────────────────────────────────────────────────────

    private AdultoMayorRespuestaDTO armarResponseAdulto(AdultoMayor adultoMayor) {
        AdultoMayorRespuestaDTO response =
                modelMapper.map(adultoMayor, AdultoMayorRespuestaDTO.class);
        response.setNombreCompleto(adultoMayor.getUser().getNombreCompleto());
        response.setEmail(adultoMayor.getUser().getEmail());
        return response;
    }

    private CuidadorRespuestaDTO armarResponseCuidador(Cuidador cuidador) {
        CuidadorRespuestaDTO response =
                modelMapper.map(cuidador, CuidadorRespuestaDTO.class);
        response.setNombreCompleto(cuidador.getUser().getNombreCompleto());
        response.setEmail(cuidador.getUser().getEmail());
        return response;
    }
}