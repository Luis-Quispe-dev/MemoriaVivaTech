package com.upc.ss.controller;

import com.upc.ss.dtos.ConfiguracionLlamadoDTO;
import com.upc.ss.dtos.ConfiguracionRespondeDTO;
import com.upc.ss.services.ConfiguracionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracion")
@Tag(name = "Configuración", description = "Preferencias de interfaz del usuario")
public class ConfiguracionController {
    @Autowired
    private ConfiguracionService configuracionService;

    @Operation(
            summary = "Obtener configuración del usuario",
            description = "Carga los valores actuales al abrir la pantalla de Configuración"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Configuración obtenida"),
            @ApiResponse(responseCode = "404", description = "Configuración no encontrada")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ConfiguracionRespondeDTO> obtenerConfiguracion(
            @PathVariable Long idUsuario) {

        return ResponseEntity.ok(
                configuracionService.obtenerConfiguracion(idUsuario));
    }

    @Operation(
            summary = "Guardar cambios — botón Guardar del popup",
            description = """
            Aplica para ADULTO_MAYOR y CUIDADOR.
            Valores válidos:
            - idioma: Español | Inglés
            - tamanioFuente: Pequeño | Mediano | Grande
            - temaVisual: Claro | Oscuro
            - notificacionesSonido: true | false
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cambios guardados"),
            @ApiResponse(responseCode = "404", description = "Configuración no encontrada")
    })
    @PutMapping("/guardar")
    public ResponseEntity<ConfiguracionRespondeDTO> guardarCambios(
            @RequestBody @Valid ConfiguracionLlamadoDTO dto) {

        return ResponseEntity.ok(configuracionService.guardarCambios(dto));
    }

}
