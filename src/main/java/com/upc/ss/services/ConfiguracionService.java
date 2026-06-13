package com.upc.ss.services;

import com.upc.ss.dtos.ConfiguracionLlamadoDTO;
import com.upc.ss.dtos.ConfiguracionRespondeDTO;
import com.upc.ss.entities.Configuracion;
import com.upc.ss.repositories.ConfiguracionRepository;
import com.upc.ss.security.entities.Role;
import com.upc.ss.security.entities.User;
import com.upc.ss.security.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracionService {
    @Autowired
    private ConfiguracionRepository configuracionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ConfiguracionRespondeDTO crearConfiguracionInicial(Long idUser) {

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado :c"));

        configuracionRepository.findByUserId(idUser)
                .ifPresent(c -> {
                    throw new RuntimeException("Ya tiene configuración c:");
                });

        Configuracion configuracion = new Configuracion();
        configuracion.setUser(user);
        configuracion.setIdioma("Español");
        configuracion.setTamanioFuente("Mediano");
        configuracion.setTemaVisual("Claro");
        configuracion.setNotificacionesSonido(true);

        return armarRespuesta(configuracionRepository.save(configuracion));
    }

    public ConfiguracionRespondeDTO obtenerConfiguracion(Long idUsuario) {

        Configuracion configuracion = configuracionRepository
                .findByUserId(idUsuario)
                .orElseThrow(() -> new RuntimeException("Configuración no encontrada :c"));

        return armarRespuesta(configuracion);
    }

    public ConfiguracionRespondeDTO guardarCambios(ConfiguracionLlamadoDTO dto) {
        User usuario = userRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado :c"));

        Configuracion configuracion = configuracionRepository
                .findByUserId(dto.getIdUsuario())
                .orElse(new Configuracion());
        modelMapper.map(dto, configuracion);
        configuracion.setUser(usuario);

        return armarRespuesta(configuracionRepository.save(configuracion));
    }

    private ConfiguracionRespondeDTO armarRespuesta(Configuracion configuracion) {

        ConfiguracionRespondeDTO responde =
                modelMapper.map(configuracion, ConfiguracionRespondeDTO.class);
        responde.setIdUsuario(configuracion.getUser().getId());
        responde.setNombreUsuario(configuracion.getUser().getNombreCompleto());

        String rol = configuracion.getUser().getRoles()
                .stream()
                .map(Role::getName)
                .findFirst()
                .orElse("SIN ROL");

        responde.setRol(rol);

        return responde;
    }
}
