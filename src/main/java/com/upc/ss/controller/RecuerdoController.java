package com.upc.ss.controller;

import com.upc.ss.dtos.RecuerdoExportarRespondeDTO;
import com.upc.ss.dtos.RecuerdoLlamadoDTO;
import com.upc.ss.dtos.RecuerdoRespondeDTO;
import com.upc.ss.services.RecuerdoService;
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
@Tag(name = "Recuerdos", description = "Registro de memorias del adulto mayor: texto, audio y foto")
public class RecuerdoController {
    @Autowired
    private RecuerdoService recuerdoService;

    @Operation(summary = "Registrar recuerdo de texto — pantalla Escribir Memoria")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recuerdo de texto guardado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Adulto mayor no encontrado")
    })
    @PostMapping("/texto")
    public ResponseEntity<RecuerdoRespondeDTO> crearRecuerdoTexto(
            @RequestBody @Valid RecuerdoLlamadoDTO dto) {

        dto.setTipoRecuerdo("TEXTO"); // ← fuerza el tipo
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recuerdoService.crearRecuerdoTexto(dto));
    }

    @Operation(summary = "Registrar recuerdo de audio — pantalla Grabar Audio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recuerdo de audio guardado correctamente"),
            @ApiResponse(responseCode = "400", description = "Formato inválido, debe ser mp3 o wav"),
            @ApiResponse(responseCode = "404", description = "Adulto mayor no encontrado")
    })
    @PostMapping("/audio")
    public ResponseEntity<RecuerdoRespondeDTO> crearRecuerdoAudio(
            @RequestBody @Valid RecuerdoLlamadoDTO dto) {

        dto.setTipoRecuerdo("AUDIO"); // ← fuerza el tipo
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recuerdoService.crearRecuerdoAudio(dto));
    }

    @Operation(summary = "Registrar recuerdo de foto — pantalla Tomar Fotografía correctamente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recuerdo de foto guardado"),
            @ApiResponse(responseCode = "400", description = "Formato inválido, debe ser jpg o png"),
            @ApiResponse(responseCode = "404", description = "Adulto mayor no encontrado")
    })
    @PostMapping("/foto")
    public ResponseEntity<RecuerdoRespondeDTO> crearRecuerdoFoto(
            @RequestBody @Valid RecuerdoLlamadoDTO dto) {

        dto.setTipoRecuerdo("FOTO"); // ← fuerza el tipo
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recuerdoService.crearRecuerdoFoto(dto));
    }

    @Operation(summary = "Obtener todos los recuerdos — botón Todos")
    @GetMapping("/adulto/{idAdultoMayor}")
    public ResponseEntity<List<RecuerdoRespondeDTO>> obtenerTodos(
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(recuerdoService.obtenerTodos(idAdultoMayor));
    }

    @Operation(
            summary = "Filtrar recuerdos por tipo — botones Escritos / Audio / Foto",
            description = "Valores válidos para {tipo}: TEXTO | AUDIO | FOTO"
    )
    @GetMapping("/adulto/{idAdultoMayor}/tipo/{tipo}")
    public ResponseEntity<List<RecuerdoRespondeDTO>> obtenerPorTipo(
            @PathVariable Long idAdultoMayor,
            @PathVariable String tipo) {

        return ResponseEntity.ok(recuerdoService.obtenerPorTipo(idAdultoMayor, tipo));
    }

    @Operation(summary = "Obtener un recuerdo por id")
    @GetMapping("/{idRecuerdo}")
    public ResponseEntity<RecuerdoRespondeDTO> obtenerPorId(
            @PathVariable Long idRecuerdo) {

        return ResponseEntity.ok(recuerdoService.obtenerPorId(idRecuerdo));
    }

    @Operation(summary = "Editar título o contenido del recuerdo — botón Editar")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recuerdo editado"),
            @ApiResponse(responseCode = "404", description = "Recuerdo no encontrado")
    })
    @PutMapping("/{idRecuerdo}")
    public ResponseEntity<RecuerdoRespondeDTO> editarRecuerdo(
            @PathVariable Long idRecuerdo,
            @RequestBody @Valid RecuerdoLlamadoDTO dto) {

        return ResponseEntity.ok(recuerdoService.editarRecuerdo(idRecuerdo, dto));
    }

    @Operation(summary = "Eliminar un recuerdo — botón Eliminar")
    @ApiResponse(responseCode = "204", description = "Recuerdo eliminado")
    @DeleteMapping("/{idRecuerdo}")
    public ResponseEntity<Void> eliminarRecuerdo(
            @PathVariable Long idRecuerdo) {

        recuerdoService.eliminarRecuerdo(idRecuerdo);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Mi Legado — listar todos los recuerdos favoritos del adulto mayor")
    @ApiResponse(responseCode = "200", description = "Lista de favoritos obtenida")
    @GetMapping("/adulto/{idAdultoMayor}/mi-legado")
    public ResponseEntity<List<RecuerdoRespondeDTO>> obtenerMiLegado(
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(recuerdoService.obtenerMiLegado(idAdultoMayor));
    }

    @Operation(summary = "Marcar o desmarcar recuerdo como favorito — botón favorito")
    @ApiResponse(responseCode = "200", description = "Favorito actualizado")
    @PatchMapping("/{idRecuerdo}/favorito")
    public ResponseEntity<RecuerdoRespondeDTO> toggleFavorito(
            @PathVariable Long idRecuerdo) {

        return ResponseEntity.ok(recuerdoService.toggleFavorito(idRecuerdo));
    }

    @Operation(
            summary = "Exportar y compartir un recuerdo",
            description = """
        Devuelve la URL del archivo y los links de compartir
        listos para WhatsApp, Facebook e Instagram.
        El frontend solo abre el link en una nueva pestaña.
        """
    )
    @ApiResponse(responseCode = "200", description = "Links de compartir generados")
    @GetMapping("/{idRecuerdo}/exportar")
    public ResponseEntity<RecuerdoExportarRespondeDTO> exportarRecuerdo(@PathVariable Long idRecuerdo) {
        return ResponseEntity.ok(recuerdoService.exportarRecuerdo(idRecuerdo));
    }
}
