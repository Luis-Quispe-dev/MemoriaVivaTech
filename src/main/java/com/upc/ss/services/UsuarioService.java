package com.upc.ss.services;

import com.upc.ss.dtos.AdultoMayorLlamarDTO;
import com.upc.ss.dtos.AdultoMayorRespuestaDTO;
import com.upc.ss.dtos.CuidadorLlamarDTO;
import com.upc.ss.dtos.CuidadorRespuestaDTO;
import com.upc.ss.entities.AdultoMayor;
import com.upc.ss.entities.Cuidador;
import com.upc.ss.repositories.AdultoMayorRepository;
import com.upc.ss.repositories.CuidadorRepository;
import com.upc.ss.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AdultoMayorRepository adultoMayorRepository;

    @Autowired
    private CuidadorRepository cuidadorRepository;

    @Autowired
    private ConfiguracionService configuracionService;

    @Autowired
    private SuscripcionService suscripcionService;

    @Autowired
    private ModelMapper modelMapper;

    public AdultoMayorRespuestaDTO registrarAdultoMayor(AdultoMayorLlamarDTO dto) {

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setContrasena(dto.getContrasena());
        usuario.setRol(Usuario.Rol.ADULTO_MAYOR);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        AdultoMayor adultoMayor = new AdultoMayor();
        adultoMayor.setUsuario(usuarioGuardado);
        adultoMayor.setFechaNacimiento(dto.getFechaNacimiento());
        adultoMayor.setDireccion(dto.getDireccion());

        AdultoMayor guardado = adultoMayorRepository.save(adultoMayor);
        AdultoMayorRespuestaDTO response = modelMapper.map(guardado, AdultoMayorRespuestaDTO.class);
        response.setNombreCompleto(usuarioGuardado.getNombreCompleto());
        response.setEmail(usuarioGuardado.getEmail());

        configuracionService.crearConfiguracionInicial(usuarioGuardado.getIdUsuario());

        suscripcionService.crearSuscripcionGratis(guardado.getIdAdultoMayor());

        return response;
    }

    public CuidadorRespuestaDTO registrarCuidador(CuidadorLlamarDTO dto) {

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setContrasena(dto.getContrasena());
        usuario.setRol(Usuario.Rol.CUIDADOR);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Cuidador cuidador = new Cuidador();
        cuidador.setUsuario(usuarioGuardado);
        cuidador.setEspecialidad(dto.getEspecialidad());
        cuidador.setTelefono(dto.getTelefono());
        cuidador.setBiografia(dto.getBiografia());
        cuidador.setNotificacionEncendida(true);

        Cuidador guardado = cuidadorRepository.save(cuidador);
        CuidadorRespuestaDTO response = modelMapper.map(guardado, CuidadorRespuestaDTO.class);
        response.setNombreCompleto(usuarioGuardado.getNombreCompleto());
        response.setEmail(usuarioGuardado.getEmail());

        configuracionService.crearConfiguracionInicial(usuarioGuardado.getIdUsuario());


        return response;
    }

    public List<AdultoMayorRespuestaDTO> listarAdultosMayores() {

        return adultoMayorRepository.findAll()
                .stream()
                .map(adultoMayor -> {
                    AdultoMayorRespuestaDTO dto =
                            modelMapper.map(adultoMayor, AdultoMayorRespuestaDTO.class);
                    dto.setNombreCompleto(adultoMayor.getUsuario().getNombreCompleto());
                    dto.setEmail(adultoMayor.getUsuario().getEmail());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public AdultoMayorRespuestaDTO obtenerAdultoMayorPorId(Long idAdultoMayor) {

        AdultoMayor adultoMayor = adultoMayorRepository.findById(idAdultoMayor)
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        AdultoMayorRespuestaDTO response =
                modelMapper.map(adultoMayor, AdultoMayorRespuestaDTO.class);
        response.setNombreCompleto(adultoMayor.getUsuario().getNombreCompleto());
        response.setEmail(adultoMayor.getUsuario().getEmail());

        return response;
    }

    public List<CuidadorRespuestaDTO> listarCuidadores() {

        return cuidadorRepository.findAll()
                .stream()
                .map(cuidador -> {
                    CuidadorRespuestaDTO dto =
                            modelMapper.map(cuidador, CuidadorRespuestaDTO.class);
                    dto.setNombreCompleto(cuidador.getUsuario().getNombreCompleto());
                    dto.setEmail(cuidador.getUsuario().getEmail());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public CuidadorRespuestaDTO obtenerCuidadorPorId(Long idCuidador) {

        Cuidador cuidador = cuidadorRepository.findById(idCuidador)
                .orElseThrow(() -> new RuntimeException("Cuidador no encontrado"));

        CuidadorRespuestaDTO response =
                modelMapper.map(cuidador, CuidadorRespuestaDTO.class);
        response.setNombreCompleto(cuidador.getUsuario().getNombreCompleto());
        response.setEmail(cuidador.getUsuario().getEmail());

        return response;
    }

    public AdultoMayorRespuestaDTO editarAdultoMayor(Long idAdultoMayor, AdultoMayorLlamarDTO dto) {

        AdultoMayor adultoMayor = adultoMayorRepository.findById(idAdultoMayor)
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        Usuario usuario = adultoMayor.getUsuario();
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setEmail(dto.getEmail());
        usuarioRepository.save(usuario);

        adultoMayor.setFechaNacimiento(dto.getFechaNacimiento());
        adultoMayor.setDireccion(dto.getDireccion());

        AdultoMayor guardado = adultoMayorRepository.save(adultoMayor);

        AdultoMayorRespuestaDTO response =
                modelMapper.map(guardado, AdultoMayorRespuestaDTO.class);
        response.setNombreCompleto(usuario.getNombreCompleto());
        response.setEmail(usuario.getEmail());

        return response;
    }

    public CuidadorRespuestaDTO editarCuidador(Long idCuidador, CuidadorLlamarDTO dto) {

        Cuidador cuidador = cuidadorRepository.findById(idCuidador)
                .orElseThrow(() -> new RuntimeException("Cuidador no encontrado"));

        Usuario usuario = cuidador.getUsuario();
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setEmail(dto.getEmail());
        usuarioRepository.save(usuario);

        cuidador.setEspecialidad(dto.getEspecialidad());
        cuidador.setTelefono(dto.getTelefono());
        cuidador.setBiografia(dto.getBiografia());

        Cuidador guardado = cuidadorRepository.save(cuidador);

        CuidadorRespuestaDTO response =
                modelMapper.map(guardado, CuidadorRespuestaDTO.class);
        response.setNombreCompleto(usuario.getNombreCompleto());
        response.setEmail(usuario.getEmail());

        return response;
    }
}
