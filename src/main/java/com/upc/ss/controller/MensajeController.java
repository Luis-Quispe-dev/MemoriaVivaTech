package com.upc.ss.controller;

import com.upc.ss.dtos.MensajeLlamarDTO;
import com.upc.ss.dtos.MensajeRespondeDTO;
import com.upc.ss.services.MensajeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.upc.ss.security.services.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", exposedHeaders = "Authorization")
public class MensajeController {
    @Autowired
    private MensajeService mensajeService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUIDADOR') or hasRole('ROLE_ADULTO_MAYOR')")
    public ResponseEntity<MensajeRespondeDTO> enviarMensaje(@RequestBody @Valid MensajeLlamarDTO dto, Authentication authentication) {

        CustomUserDetails usuarioLogueado = (CustomUserDetails) authentication.getPrincipal();

        MensajeRespondeDTO response = mensajeService.enviarMensaje(dto, usuarioLogueado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/asignacion/{idAsignacion}")
    @PreAuthorize("hasRole('ROLE_CUIDADOR') or hasRole('ROLE_ADULTO_MAYOR')")
    public ResponseEntity<List<MensajeRespondeDTO>> obtenerMensajes(@PathVariable Long idAsignacion, Authentication authentication) {
        CustomUserDetails usuarioLogueado = (CustomUserDetails) authentication.getPrincipal();
        Long idUsuarioLogueado = usuarioLogueado.getId();

        List<MensajeRespondeDTO> response =
                mensajeService.obtenerMensajesPorAsignacion(idAsignacion, idUsuarioLogueado);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/asignacion/{idAsignacion}/leidos")
    @PreAuthorize("hasRole('ROLE_CUIDADOR') or hasRole('ROLE_ADULTO_MAYOR')")
    public ResponseEntity<Void> marcarComoLeidos(@PathVariable Long idAsignacion, Authentication authentication) {
        CustomUserDetails usuarioLogueado = (CustomUserDetails) authentication.getPrincipal();
        Long idUsuarioLogueado = usuarioLogueado.getId();
        mensajeService.marcarComoLeidos(idAsignacion, idUsuarioLogueado);
        return ResponseEntity.noContent().build();
    }
}
