package com.upc.ss.security.controllers;

import com.upc.ss.dtos.*;
import com.upc.ss.security.dtos.AuthRequestDTO;
import com.upc.ss.security.dtos.AuthResponseDTO;
import com.upc.ss.security.services.AuthService;
import com.upc.ss.security.services.CustomUserDetailsService;
import com.upc.ss.security.util.JwtUtil;
import com.upc.ss.services.UsuarioService;
import com.upc.ss.security.repositories.UserRepository;
import com.upc.ss.security.entities.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> createAuthenticationToken(@RequestBody AuthRequestDTO authRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new Exception("User not found"));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setRoles(roles);
        authResponseDTO.setJwt(token);
        authResponseDTO.setUserId(user.getId());
        authResponseDTO.setEmail(user.getEmail());
        authResponseDTO.setNombreCompleto(user.getNombreCompleto());
        authResponseDTO.setContenidoFoto(user.getContenidoFoto());
        return ResponseEntity.ok().headers(responseHeaders).body(authResponseDTO);
    }

    @PutMapping("/cambiar-contrasena")
    public ResponseEntity<String> cambiarContrasena(
            @RequestBody @Valid CambiarContrasenaDTO dto) {

        return ResponseEntity.ok(authService.cambiarContrasena(
                dto.getEmail(),
                dto.getContrasenaActual(),
                dto.getNuevaContrasena(),
                dto.getConfirmarContrasena()));
    }

    @PutMapping("/recuperar-contrasena")
    public ResponseEntity<String> recuperarContrasena(
            @RequestBody @Valid RecuperarContresenaDTO dto) {

        return ResponseEntity.ok(authService.recuperarContrasena(
                dto.getEmail(),
                dto.getNuevaContrasena(),
                dto.getConfirmarContrasena()));
    }
}


