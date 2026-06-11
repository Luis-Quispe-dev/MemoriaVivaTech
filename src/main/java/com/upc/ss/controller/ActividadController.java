package com.upc.ss.controller;

import com.upc.ss.dtos.ActividadLlamadoDTO;
import com.upc.ss.dtos.ActividadRespondeDTO;
import com.upc.ss.services.ActividadService;
import com.upc.ss.security.services.CustomUserDetails; // Importa tu clase
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication; // ¡Importación clave!
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", exposedHeaders = "Authorization")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @PostMapping("/actividad/registrar")
    @PreAuthorize("hasRole('ROLE_CUIDADOR') or hasRole('ROLE_ADULTO_MAYOR')")
    public ResponseEntity<ActividadRespondeDTO> crearActividad(
            @RequestBody @Valid ActividadLlamadoDTO dto,
            Authentication authentication) {

        CustomUserDetails usuarioLogueado = (CustomUserDetails) authentication.getPrincipal();
        ActividadRespondeDTO response = actividadService.crearActividad(dto, usuarioLogueado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/asignacion/actividades/{idAsignacion}")
    @PreAuthorize("hasRole('ROLE_CUIDADOR') or hasRole('ROLE_ADULTO_MAYOR')")
    public ResponseEntity<List<ActividadRespondeDTO>> obtenerActividades(
            @PathVariable Long idAsignacion,
            Authentication authentication) {

        CustomUserDetails usuarioLogueado = (CustomUserDetails) authentication.getPrincipal();
        List<ActividadRespondeDTO> response =
                actividadService.obtenerActividadesPorAsignacion(idAsignacion, usuarioLogueado.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/asignacion/{idAsignacion}/estado/{estado}")
    @PreAuthorize("hasRole('ROLE_CUIDADOR') or hasRole('ROLE_ADULTO_MAYOR')")
    public ResponseEntity<List<ActividadRespondeDTO>> obtenerPorEstado(
            @PathVariable Long idAsignacion,
            @PathVariable String estado,
            Authentication authentication) {

        CustomUserDetails usuarioLogueado = (CustomUserDetails) authentication.getPrincipal();
        List<ActividadRespondeDTO> response =
                actividadService.obtenerPorEstado(idAsignacion, estado, usuarioLogueado.getId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{idActividad}/estado/{nuevoEstado}")
    @PreAuthorize("hasRole('ROLE_CUIDADOR') or hasRole('ROLE_ADULTO_MAYOR')")
    public ResponseEntity<ActividadRespondeDTO> actualizarEstado(
            @PathVariable Long idActividad,
            @PathVariable String nuevoEstado,
            Authentication authentication) {

        CustomUserDetails usuarioLogueado = (CustomUserDetails) authentication.getPrincipal();
        ActividadRespondeDTO response =
                actividadService.actualizarEstado(idActividad, nuevoEstado, usuarioLogueado.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/actividad/editar/{idActividad}")
    @PreAuthorize("hasRole('ROLE_CUIDADOR') or hasRole('ROLE_ADULTO_MAYOR')")
    public ResponseEntity<ActividadRespondeDTO> editarActividad(
            @PathVariable Long idActividad,
            @RequestBody @Valid ActividadLlamadoDTO dto,
            Authentication authentication) {

        CustomUserDetails usuarioLogueado = (CustomUserDetails) authentication.getPrincipal();
        ActividadRespondeDTO response =
                actividadService.editarActividad(idActividad, dto, usuarioLogueado.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{idActividad}")
    @PreAuthorize("hasRole('ROLE_CUIDADOR')")
    public ResponseEntity<Void> eliminarActividad(
            @PathVariable Long idActividad,
            Authentication authentication) {

        CustomUserDetails usuarioLogueado = (CustomUserDetails) authentication.getPrincipal();
        actividadService.eliminarActividad(idActividad, usuarioLogueado.getId());
        return ResponseEntity.noContent().build();
    }
}