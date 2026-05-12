package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class GaleriaIAGenerarRespuestaDTO {
    private String urlImagen;
    private String promptDescripcion;
}
