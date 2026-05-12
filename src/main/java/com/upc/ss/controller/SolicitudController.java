package com.upc.ss.controller;

import com.upc.ss.dtos.AsignacionRespondeDTO;
import com.upc.ss.dtos.SolicitudLlamarDTO;
import com.upc.ss.dtos.SolicitudRespondeDTO;
import com.upc.ss.dtos.SolicitudRespuestaDTO;
import com.upc.ss.services.SolicitudService;
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
@Tag(name = "Solicitudes", description = "Gestión de solicitudes de asignación entre adultos mayores y cuidadores")

public class SolicitudController {
    @Autowired
    private SolicitudService solicitudService;

    @Operation(summary = "Crear una solicitud de asignación",
            description = "Un adulto mayor o cuidador inicia la solicitud. El otro debe aceptarla.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitud creada y notificación enviada"),
            @ApiResponse(responseCode = "400", description = "Ya existe una solicitud pendiente"),
            @ApiResponse(responseCode = "404", description = "Adulto mayor o cuidador no encontrado")
    })
    @PostMapping
    public ResponseEntity<SolicitudRespondeDTO> crearSolicitud(
            @RequestBody @Valid SolicitudLlamarDTO dto) {

        SolicitudRespondeDTO response = solicitudService.crearSolicitud(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Responder una solicitud (aceptar o rechazar)",
            description = "Si se acepta, se crea automáticamente la asignación y el chat queda disponible.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud aceptada y asignación creada"),
            @ApiResponse(responseCode = "400", description = "La solicitud ya fue respondida"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @PutMapping("/responder")
    public ResponseEntity<AsignacionRespondeDTO> responderSolicitud(
            @RequestBody @Valid SolicitudRespuestaDTO dto) {

        AsignacionRespondeDTO response = solicitudService.responderSolicitud(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Ver solicitudes pendientes de un cuidador")
    @GetMapping("/cuidador/{idCuidador}/pendientes")
    public ResponseEntity<List<SolicitudRespondeDTO>> pendientesDeCuidador(
            @PathVariable Long idCuidador) {

        List<SolicitudRespondeDTO> response =
                solicitudService.obtenerSolicitudesPendientesDeCuidador(idCuidador);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Ver solicitudes pendientes de un adulto mayor")
    @GetMapping("/adulto/{idAdultoMayor}/pendientes")
    public ResponseEntity<List<SolicitudRespondeDTO>> pendientesDeAdulto(
            @PathVariable Long idAdultoMayor) {

        List<SolicitudRespondeDTO> response =
                solicitudService.obtenerSolicitudesPendientesDeAdulto(idAdultoMayor);
        return ResponseEntity.ok(response);
    }
}
