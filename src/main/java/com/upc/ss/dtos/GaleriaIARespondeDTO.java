package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class GaleriaIARespondeDTO {
    private Long idRetratoIa;
    private Long idAdultoMayor;
    private String nombreAdultoMayor;
    private String promptDescripcion;
    private String titulo;
    private String urlImagen;
    private LocalDateTime fechaCreacion;
}
