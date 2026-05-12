package com.upc.ss.controller;

import com.upc.ss.dtos.CambiarContrasenaDTO;
import com.upc.ss.dtos.LoginLlamadoDTO;
import com.upc.ss.dtos.LoginRespondeDTO;
import com.upc.ss.dtos.RecuperarContresenaDTO;
import com.upc.ss.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Login y recuperación de contraseña")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Operation(
            summary = "Iniciar sesión",
            description = "Devuelve los datos del usuario y su id de perfil según el rol"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "400", description = "Email no registrado o contraseña incorrecta")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginRespondeDTO> login(
            @RequestBody @Valid LoginLlamadoDTO dto) {

        return ResponseEntity.ok(authService.login(dto));
    }

    @Operation(
            summary = "Solicitar recuperación de contraseña — botón Recuperar contraseña",
            description = "Verifica que el email existe y simula el envío del correo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Instrucciones enviadas al correo"),
            @ApiResponse(responseCode = "404", description = "Email no registrado")
    })
    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<String> solicitarRecuperacion(
            @RequestBody @Valid RecuperarContresenaDTO dto) {

        return ResponseEntity.ok(authService.solicitarRecuperacion(dto));
    }

    @Operation(
            summary = "Cambiar contraseña",
            description = "Actualiza la contraseña del usuario verificando que ambas coincidan"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada"),
            @ApiResponse(responseCode = "400", description = "Las contraseñas no coinciden o son muy cortas"),
            @ApiResponse(responseCode = "404", description = "Email no registrado")
    })
    @PutMapping("/cambiar-contrasena")
    public ResponseEntity<String> cambiarContrasena(
            @RequestBody @Valid CambiarContrasenaDTO dto) {

        return ResponseEntity.ok(authService.cambiarContrasena(dto));
    }
}
