package com.upc.ss.controller;
import org.springframework.security.core.Authentication;
import com.upc.ss.dtos.AsignacionRespondeDTO;
import com.upc.ss.dtos.SolicitudLlamarDTO;
import com.upc.ss.dtos.SolicitudRespondeDTO;
import com.upc.ss.dtos.SolicitudRespuestaDTO;
import com.upc.ss.security.services.CustomUserDetails;
import com.upc.ss.services.SolicitudService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", exposedHeaders = "Authorization")
public class SolicitudController {
    @Autowired
    private SolicitudService solicitudService;

    @PostMapping("/solicitud")
    @PreAuthorize("hasRole('ADULTO_MAYOR ') or hasRole('CUIDADOR ')")
    public ResponseEntity<SolicitudRespondeDTO> crearSolicitud(@RequestBody @Valid SolicitudLlamarDTO dto, Authentication authentication) { // <-- Capturamos el token real
        CustomUserDetails usuarioLogueado = (CustomUserDetails) authentication.getPrincipal();

        SolicitudRespondeDTO response = solicitudService.crearSolicitud(dto, usuarioLogueado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/responder")
    @PreAuthorize("hasRole('ADULTO_MAYOR') or hasRole('CUIDADOR')")
    public ResponseEntity<AsignacionRespondeDTO> responderSolicitud(@RequestBody @Valid SolicitudRespuestaDTO dto,
                                                                    Authentication authentication) {

        CustomUserDetails usuarioLogueado = (CustomUserDetails) authentication.getPrincipal();
        Long idUsuarioActual = usuarioLogueado.getId();

        AsignacionRespondeDTO response = solicitudService.responderSolicitud(dto, idUsuarioActual);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CUIDADOR')")
    @GetMapping("/cuidador/{idCuidador}/pendientes")
    public ResponseEntity<List<SolicitudRespondeDTO>> pendientesDeCuidador(
            @PathVariable Long idCuidador) {

        List<SolicitudRespondeDTO> response =
                solicitudService.obtenerSolicitudesPendientesDeCuidador(idCuidador);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ADULTO_MAYOR')")
    @GetMapping("/adulto/{idAdultoMayor}/pendientes")
    public ResponseEntity<List<SolicitudRespondeDTO>> pendientesDeAdulto(
            @PathVariable Long idAdultoMayor) {

        List<SolicitudRespondeDTO> response =
                solicitudService.obtenerSolicitudesPendientesDeAdulto(idAdultoMayor);
        return ResponseEntity.ok(response);
    }
}
