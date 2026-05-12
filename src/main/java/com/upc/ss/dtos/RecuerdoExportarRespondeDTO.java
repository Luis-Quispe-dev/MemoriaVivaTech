package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RecuerdoExportarRespondeDTO {
    private Long idRecuerdo;
    private String tituloRecuerdo;
    private String tipoRecuerdo;
    private String contenido;
    private String formato;

    private String linkWhatsapp;
    private String linkFacebook;
    private String linkInstagram;
}
