package com.upc.ss.controller;

import com.upc.ss.dtos.GaleriaIAGenerarLlamadoDTO;
import com.upc.ss.dtos.GaleriaIAGenerarRespuestaDTO;
import com.upc.ss.dtos.GaleriaIALlamadoDTO;
import com.upc.ss.dtos.GaleriaIARespondeDTO;
import com.upc.ss.services.GaleriaIAService;
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
@RequestMapping("/api/galeria_ia")
@Tag(name = "Galeria IA", description = "Generacion y guardado de imagenes con Inteligencia Artificial")
public class GaleriaIAController {
    @Autowired
    private GaleriaIAService galeriaIAService;


    //Generar
    @Operation(summary = "Paso 1 - Generar Imagen con IA")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagen genereada correctamente"),
            @ApiResponse(responseCode = "400", description = "Prompt vacio o invalido")
    })
    @PostMapping("/generar")
    public ResponseEntity<GaleriaIAGenerarRespuestaDTO> generarImagen(@RequestBody @Valid GaleriaIAGenerarLlamadoDTO dto){
        return ResponseEntity.ok(galeriaIAService.generarImagen(dto));
    }
    //Guardar
    @Operation(summary = "Paso 2 - Guardar imagen generada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagen guardada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Ocurrio un error")
    })
    @PostMapping("/guardar")
    public ResponseEntity<GaleriaIARespondeDTO> guardarImagen(@RequestBody @Valid GaleriaIALlamadoDTO dto, @RequestParam String urlImamgen){
        return ResponseEntity.status(HttpStatus.CREATED).body(galeriaIAService.guardarImagen(dto, urlImamgen));
    }
    //Mostrar Imagenes Guardadas
    @Operation(summary = "Ver todas las imágenes guardadas del adulto mayor")
    @GetMapping("/adulto/{idAdultoMayor}")
    public ResponseEntity<List<GaleriaIARespondeDTO>> obtenerGaleria(
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(galeriaIAService.obtenerGaleria(idAdultoMayor));
    }
    //Mostrar por Id
    @Operation(summary = "Buscar imagen oir id")
    @GetMapping("/buscar/{idRetratoIa}")
    public ResponseEntity<GaleriaIARespondeDTO> obtenerPorId(@PathVariable Long idRetratoIa) {
        return ResponseEntity.ok(galeriaIAService.obtenerPorId(idRetratoIa));
    }
    //Eliminar imagen
    @Operation(summary = "Eliminar una imagen de la galeria")
    @ApiResponse(responseCode = "200", description = "Imagen eliminada correctamente")
    @DeleteMapping("/borrar/{idRetratoIa}")
    public ResponseEntity<Void> borrarImagen(@PathVariable Long idRetratoIa) {
        galeriaIAService.eliminarRetrato(idRetratoIa);
        return ResponseEntity.noContent().build();
    }
}
