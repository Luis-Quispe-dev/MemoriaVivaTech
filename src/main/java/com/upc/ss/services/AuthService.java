package com.upc.ss.services;

import com.upc.ss.dtos.CambiarContrasenaDTO;
import com.upc.ss.dtos.LoginLlamadoDTO;
import com.upc.ss.dtos.LoginRespondeDTO;
import com.upc.ss.dtos.RecuperarContresenaDTO;
import com.upc.ss.entities.Usuario;
import com.upc.ss.repositories.AdultoMayorRepository;
import com.upc.ss.repositories.CuidadorRepository;
import com.upc.ss.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AdultoMayorRepository adultoMayorRepository;

    @Autowired
    private CuidadorRepository cuidadorRepository;

    public LoginRespondeDTO login(LoginLlamadoDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("El email no está registrado"));

        if (!usuario.getContrasena().equals(dto.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        LoginRespondeDTO response = new LoginRespondeDTO();
        response.setIdUsuario(usuario.getIdUsuario());
        response.setNombreCompleto(usuario.getNombreCompleto());
        response.setEmail(usuario.getEmail());
        response.setRol(usuario.getRol().name());

        if (usuario.getRol() == Usuario.Rol.ADULTO_MAYOR) {
            adultoMayorRepository
                    .findByUsuarioIdUsuario(usuario.getIdUsuario())
                    .ifPresent(a -> response.setIdAdultoMayor(a.getIdAdultoMayor()));
        }

        if (usuario.getRol() == Usuario.Rol.CUIDADOR) {
            cuidadorRepository
                    .findByUsuarioIdUsuario(usuario.getIdUsuario())
                    .ifPresent(c -> response.setIdCuidador(c.getIdCuidador()));
        }

        return response;
    }

    public String solicitarRecuperacion(RecuperarContresenaDTO dto) {
        usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("El email no está registrado"));
        return "Se enviaron instrucciones al correo " + dto.getEmail();
    }

    public String cambiarContrasena(CambiarContrasenaDTO dto) {
        if (!dto.getNuevaContrasena().equals(dto.getConfirmarContrasena())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }
        if (dto.getNuevaContrasena().length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("El email no está registrado"));

        usuario.setContrasena(dto.getNuevaContrasena());
        usuarioRepository.save(usuario);

        return "Contraseña actualizada correctamente";
    }
}
