package com.upc.ss.controller;

import com.upc.ss.dtos.AsignacionRespondeDTO;
import com.upc.ss.services.AsignacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Asignaciones", description = "Consulta de asignaciones activas")

public class AsignacionController {
    @Autowired
    private AsignacionService asignacionService;
    @Operation(summary = "Ver la asignación activa de un adulto mayor: ")
    @GetMapping("/adulto/{idAdultoMayor}/activa")
    public ResponseEntity<AsignacionRespondeDTO> asignacionActivaDeAdulto(
            @PathVariable Long idAdultoMayor) {

        AsignacionRespondeDTO response =
                asignacionService.obtenerAsignacionActivaDeAdulto(idAdultoMayor);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Ver todos los adultos mayores activos de un cuidador: ")
    @GetMapping("/cuidador/{idCuidador}/activas")
    public ResponseEntity<List<AsignacionRespondeDTO>> asignacionesActivasDeCuidador(
            @PathVariable Long idCuidador) {

        List<AsignacionRespondeDTO> response =
                asignacionService.obtenerAsignacionesActivasDeCuidador(idCuidador);
        return ResponseEntity.ok(response);
    }
}
