package com.upc.ss.services;

import com.upc.ss.dtos.ConfiguracionLlamadoDTO;
import com.upc.ss.dtos.ConfiguracionRespondeDTO;
import com.upc.ss.entities.Configuracion;
import com.upc.ss.entities.Usuario;
import com.upc.ss.repositories.ConfiguracionRepository;
import com.upc.ss.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracionService {
    @Autowired
    private ConfiguracionRepository configuracionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ConfiguracionRespondeDTO crearConfiguracionInicial(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        configuracionRepository.findByUsuarioIdUsuario(idUsuario)
                .ifPresent(c -> {
                    throw new RuntimeException("El usuario ya tiene configuración");
                });

        Configuracion configuracion = new Configuracion();
        configuracion.setUsuario(usuario);
        configuracion.setIdioma("Español");
        configuracion.setTamanioFuente("Mediano");
        configuracion.setTemaVisual("Claro");
        configuracion.setNotificacionesSonido(true);
        return armarRespuesta(configuracionRepository.save(configuracion));
    }

    public ConfiguracionRespondeDTO obtenerConfiguracion(Long idUsuario) {

        Configuracion configuracion = configuracionRepository
                .findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Configuración no encontrada"));

        return armarRespuesta(configuracion);
    }

    public ConfiguracionRespondeDTO guardarCambios(ConfiguracionLlamadoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Configuracion configuracion = configuracionRepository
                .findByUsuarioIdUsuario(dto.getIdUsuario())
                .orElse(new Configuracion());
        modelMapper.map(dto, configuracion);
        configuracion.setUsuario(usuario);

        return armarRespuesta(configuracionRepository.save(configuracion));
    }

    private ConfiguracionRespondeDTO armarRespuesta(Configuracion configuracion) {

        ConfiguracionRespondeDTO responde = modelMapper.map(configuracion, ConfiguracionRespondeDTO.class);
        responde.setIdUsuario(configuracion.getUsuario().getIdUsuario());
        responde.setNombreUsuario(configuracion.getUsuario().getNombreCompleto());
        responde.setRol(configuracion.getUsuario().getRol().name());

        return responde;
    }
}
