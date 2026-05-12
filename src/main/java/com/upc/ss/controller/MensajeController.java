package com.upc.ss.controller;

import com.upc.ss.dtos.MensajeLlamarDTO;
import com.upc.ss.dtos.MensajeRespondeDTO;
import com.upc.ss.services.MensajeService;
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
@RequestMapping("/api/mensajes")
@Tag(name = "Mensajes", description = "Chat entre adulto mayor y cuidador")
public class MensajeController {
    @Autowired
    private MensajeService mensajeService;

    @Operation(summary = "Enviar un mensaje",
            description = "Solo se puede enviar si la asignación está ACTIVA")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mensaje enviado correctamente"),
            @ApiResponse(responseCode = "400", description = "La asignación no está activa"),
            @ApiResponse(responseCode = "404", description = "Asignación no encontrada")
    })
    @PostMapping
    public ResponseEntity<MensajeRespondeDTO> enviarMensaje(
            @RequestBody @Valid MensajeLlamarDTO dto) {

        MensajeRespondeDTO response = mensajeService.enviarMensaje(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener todos los mensajes de una asignación",
            description = "Devuelve el historial del chat ordenado por fecha ascendente")
    @ApiResponse(responseCode = "200", description = "Lista de mensajes obtenida")
    @GetMapping("/asignacion/{idAsignacion}")
    public ResponseEntity<List<MensajeRespondeDTO>> obtenerMensajes(
            @PathVariable Long idAsignacion) {

        List<MensajeRespondeDTO> response =
                mensajeService.obtenerMensajesPorAsignacion(idAsignacion);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Marcar todos los mensajes de una asignación como leídos")
    @ApiResponse(responseCode = "204", description = "Mensajes marcados como leídos")
    @PatchMapping("/asignacion/{idAsignacion}/leidos")
    public ResponseEntity<Void> marcarComoLeidos(
            @PathVariable Long idAsignacion) {

        mensajeService.marcarComoLeidos(idAsignacion);
        return ResponseEntity.noContent().build();
    }
}
