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
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", exposedHeaders = "Authorization")
public class GaleriaIAController {
    @Autowired
    private GaleriaIAService galeriaIAService;

    @PostMapping("/generar")
    public ResponseEntity<GaleriaIAGenerarRespuestaDTO> generarImagen(@RequestBody @Valid GaleriaIAGenerarLlamadoDTO dto){
        return ResponseEntity.ok(galeriaIAService.generarImagen(dto));
    }

    @PostMapping("/guardar")
    public ResponseEntity<GaleriaIARespondeDTO> guardarImagen(@RequestBody @Valid GaleriaIALlamadoDTO dto, @RequestParam String urlImamgen){
        return ResponseEntity.status(HttpStatus.CREATED).body(galeriaIAService.guardarImagen(dto, urlImamgen));
    }

    @GetMapping("/adulto/{idAdultoMayor}")
    public ResponseEntity<List<GaleriaIARespondeDTO>> obtenerGaleria(
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(galeriaIAService.obtenerGaleria(idAdultoMayor));
    }

    @GetMapping("/buscar/{idRetratoIa}")
    public ResponseEntity<GaleriaIARespondeDTO> obtenerPorId(@PathVariable Long idRetratoIa) {
        return ResponseEntity.ok(galeriaIAService.obtenerPorId(idRetratoIa));
    }

    @DeleteMapping("/borrar/{idRetratoIa}")
    public ResponseEntity<Void> borrarImagen(@PathVariable Long idRetratoIa) {
        galeriaIAService.eliminarRetrato(idRetratoIa);
        return ResponseEntity.noContent().build();
    }
}
