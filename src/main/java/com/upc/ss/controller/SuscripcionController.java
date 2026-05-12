package com.upc.ss.controller;

import com.upc.ss.dtos.PagoLlamadoDTO;
import com.upc.ss.dtos.PagoRespondeDTO;
import com.upc.ss.dtos.PlanRespondeDTO;
import com.upc.ss.dtos.SuscripcionRespondeDTO;
import com.upc.ss.services.SuscripcionService;
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
@RequestMapping("/api/suscripciones")
@Tag(name = "Suscripciones", description = "Gestión de suscripciones de adultos mayores")
public class SuscripcionController {
    @Autowired
    private SuscripcionService suscripcionService;

    @Operation(summary = "Ver todos los planes disponibles")
    @GetMapping("/suscripciones/planes")
    public ResponseEntity<List<PlanRespondeDTO>> listarPlanes() {

        return ResponseEntity.ok(suscripcionService.listarPlanes());
    }

    @Operation(
            summary = "Ver plan actual — pantalla Tu Plan",
            description = "Muestra el plan activo, días restantes y beneficios"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Suscripción activa obtenida"),
            @ApiResponse(responseCode = "404", description = "No tiene suscripción activa")
    })
    @GetMapping("/adulto/{idAdultoMayor}/activa")
    public ResponseEntity<SuscripcionRespondeDTO> obtenerSuscripcionActiva(
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(
                suscripcionService.obtenerSuscripcionActiva(idAdultoMayor));
    }

    @Operation(
            summary = "Pagar y cambiar de plan — botón Pagar",
            description = """
            Métodos de pago: QR | TARJETA
            Si es TARJETA: enviar numeroTarjeta, cvc, fechaVencimiento
            Si es QR: enviar codigoZip
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pago realizado y plan actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos de pago inválidos"),
            @ApiResponse(responseCode = "404", description = "Plan o adulto mayor no encontrado")
    })
    @PostMapping("/pagar")
    public ResponseEntity<PagoRespondeDTO> pagar(
            @RequestBody @Valid PagoLlamadoDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(suscripcionService.pagar(dto));
    }

    @Operation(summary = "Ver historial de pagos del adulto mayor")
    @GetMapping("/adulto/{idAdultoMayor}/pagos")
    public ResponseEntity<List<PagoRespondeDTO>> historialPagos(
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(suscripcionService.historialPagos(idAdultoMayor));
    }
}
