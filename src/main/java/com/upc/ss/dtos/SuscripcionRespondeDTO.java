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

public class SuscripcionRespondeDTO {
    private Long idSuscripcion;
    private Long idAdultoMayor;
    private String nombreAdultoMayor;
    private String nombrePlan;
    private Double precio;
    private Integer limiteRecuerdos;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean estado;
}
