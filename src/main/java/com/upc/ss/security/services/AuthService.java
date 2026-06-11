package com.upc.ss.security.services;

import com.upc.ss.security.entities.User;
import com.upc.ss.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String cambiarContrasena(String email,
                                    String contrasenaActual,
                                    String nuevaContrasena,
                                    String confirmarContrasena) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email no registrado"));

        if (!passwordEncoder.matches(contrasenaActual, user.getPassword()))
            throw new RuntimeException("La contraseña actual es incorrecta");

        if (!nuevaContrasena.equals(confirmarContrasena))
            throw new RuntimeException("Las contraseñas nuevas no coinciden");

        if (nuevaContrasena.length() < 6)
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");

        user.setPassword(passwordEncoder.encode(nuevaContrasena));
        userRepository.save(user);

        return "Contraseña actualizada correctamente";
    }

    public String recuperarContrasena(String email,
                                      String nuevaContrasena,
                                      String confirmarContrasena) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email no registrado"));

        if (!nuevaContrasena.equals(confirmarContrasena))
            throw new RuntimeException("Las contraseñas no coinciden");

        if (nuevaContrasena.length() < 6)
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");

        user.setPassword(passwordEncoder.encode(nuevaContrasena));
        userRepository.save(user);

        return "Contraseña recuperada correctamente";
    }
}
