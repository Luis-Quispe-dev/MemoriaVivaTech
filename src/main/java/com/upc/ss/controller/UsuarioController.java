package com.upc.ss.controller;

import com.upc.ss.dtos.AdultoMayorLlamarDTO;
import com.upc.ss.dtos.AdultoMayorRespuestaDTO;
import com.upc.ss.dtos.CuidadorLlamarDTO;
import com.upc.ss.dtos.CuidadorRespuestaDTO;
import com.upc.ss.services.UsuarioService;
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
@Tag(name = "Usuarios", description = "Todos los usuarios que corresponden al aplicativo web")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Registrar un adulto mayor",
            description = "Crea el usuario base y su perfil de adulto mayor en una sola llamada")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Adulto mayor registrado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya registrado.")
    })
    @PostMapping("/adulto-mayor")
    public ResponseEntity<AdultoMayorRespuestaDTO> registrarAdultoMayor(
            @RequestBody @Valid AdultoMayorLlamarDTO dto) {
        AdultoMayorRespuestaDTO response = usuarioService.registrarAdultoMayor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Registrar un cuidador",
            description = "Crea el usuario base y su perfil de cuidador en una sola llamada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cuidador registrado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya registrado.")
    })
    @PostMapping("/cuidador")
    public ResponseEntity<CuidadorRespuestaDTO> registrarCuidador(
            @RequestBody @Valid CuidadorLlamarDTO dto) {

        CuidadorRespuestaDTO response = usuarioService.registrarCuidador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos los adultos mayores")
    @ApiResponse(responseCode = "200", description = "Lista obtenida")
    @GetMapping("/adultos-mayores")
    public ResponseEntity<List<AdultoMayorRespuestaDTO>> listarAdultosMayores() {

        return ResponseEntity.ok(usuarioService.listarAdultosMayores());
    }

    @Operation(summary = "Obtener adulto mayor por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Adulto mayor encontrado"),
            @ApiResponse(responseCode = "404", description = "Adulto mayor no encontrado")
    })
    @GetMapping("/adulto-mayor/{idAdultoMayor}")
    public ResponseEntity<AdultoMayorRespuestaDTO> obtenerAdultoMayorPorId(
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(
                usuarioService.obtenerAdultoMayorPorId(idAdultoMayor));
    }

    @Operation(summary = "Listar todos los cuidadores")
    @ApiResponse(responseCode = "200", description = "Lista obtenida")
    @GetMapping("/cuidadores")
    public ResponseEntity<List<CuidadorRespuestaDTO>> listarCuidadores() {

        return ResponseEntity.ok(usuarioService.listarCuidadores());
    }

    @Operation(summary = "Obtener cuidador por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuidador encontrado"),
            @ApiResponse(responseCode = "404", description = "Cuidador no encontrado")
    })
    @GetMapping("/cuidador/{idCuidador}")
    public ResponseEntity<CuidadorRespuestaDTO> obtenerCuidadorPorId(
            @PathVariable Long idCuidador) {

        return ResponseEntity.ok(
                usuarioService.obtenerCuidadorPorId(idCuidador));
    }

    @Operation(summary = "Editar información del adulto mayor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Adulto mayor actualizado"),
            @ApiResponse(responseCode = "404", description = "Adulto mayor no encontrado")
    })
    @PutMapping("/adulto-mayor/{idAdultoMayor}")
    public ResponseEntity<AdultoMayorRespuestaDTO> editarAdultoMayor(
            @PathVariable Long idAdultoMayor,
            @RequestBody @Valid AdultoMayorLlamarDTO dto) {

        return ResponseEntity.ok(
                usuarioService.editarAdultoMayor(idAdultoMayor, dto));
    }

    @Operation(summary = "Editar información del cuidador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuidador actualizado"),
            @ApiResponse(responseCode = "404", description = "Cuidador no encontrado")
    })
    @PutMapping("/cuidador/{idCuidador}")
    public ResponseEntity<CuidadorRespuestaDTO> editarCuidador(
            @PathVariable Long idCuidador,
            @RequestBody @Valid CuidadorLlamarDTO dto) {

        return ResponseEntity.ok(
                usuarioService.editarCuidador(idCuidador, dto));
    }
}
