package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class RecuerdoRespondeDTO {
    private Long idRecuerdo;
    private Long idAdultoMayor;
    private String nombreAdultoMayor;
    private String tituloRecuerdo;
    private String tipoRecuerdo;
    private String contenido;
    private String formato;
    private String favorito;
    private LocalDate fechaCreacion;
}
