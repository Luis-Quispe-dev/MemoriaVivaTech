package com.upc.ss.controller;

import com.upc.ss.dtos.ActividadLlamadoDTO;
import com.upc.ss.dtos.ActividadRespondeDTO;
import com.upc.ss.services.ActividadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Actividades", description = "Calendario compartido entre adulto mayor y cuidador")
public class ActividadController {
    @Autowired
    private ActividadService actividadService;

    @Operation(summary = "Crear una actividad en el calendario",
            description = "Solo se puede crear si la asignación está ACTIVA")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Actividad creada correctamente"),
            @ApiResponse(responseCode = "400", description = "La asignación no está activa"),
            @ApiResponse(responseCode = "404", description = "Asignación no encontrada")
    })
    @PostMapping("/actividad/registrar")
    public ResponseEntity<ActividadRespondeDTO> crearActividad(
            @RequestBody @Valid ActividadLlamadoDTO dto) {

        ActividadRespondeDTO response = actividadService.crearActividad(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener todas las actividades del calendario de una asignación")
    @ApiResponse(responseCode = "200", description = "Lista de actividades obtenida")
    @GetMapping("/asignacion/actividades/{idAsignacion}")
    public ResponseEntity<List<ActividadRespondeDTO>> obtenerActividades(
            @PathVariable Long idAsignacion) {

        List<ActividadRespondeDTO> response =
                actividadService.obtenerActividadesPorAsignacion(idAsignacion);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener actividades por estado",
            description = "Estados: PENDIENTE, EN_CURSO, COMPLETADA, CANCELADA")
    @GetMapping("/asignacion/{idAsignacion}/estado/{estado}")
    public ResponseEntity<List<ActividadRespondeDTO>> obtenerPorEstado(
            @PathVariable Long idAsignacion,
            @PathVariable String estado) {

        List<ActividadRespondeDTO> response =
                actividadService.obtenerPorEstado(idAsignacion, estado);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar el estado de una actividad")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @PatchMapping("/{idActividad}/estado/{nuevoEstado}")
    public ResponseEntity<ActividadRespondeDTO> actualizarEstado(
            @PathVariable Long idActividad,
            @PathVariable String nuevoEstado) {

        ActividadRespondeDTO response =
                actividadService.actualizarEstado(idActividad, nuevoEstado);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Editar una actividad del calendario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actividad editada correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @PutMapping("/actividad/editar/{idActividad}")
    public ResponseEntity<ActividadRespondeDTO> editarActividad(
            @PathVariable Long idActividad,
            @RequestBody @Valid ActividadLlamadoDTO dto) {

        ActividadRespondeDTO response =
                actividadService.editarActividad(idActividad, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar una actividad del calendario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Actividad eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @DeleteMapping("/{idActividad}")
    public ResponseEntity<Void> eliminarActividad(
            @PathVariable Long idActividad) {

        actividadService.eliminarActividad(idActividad);
        return ResponseEntity.noContent().build();
    }
}
