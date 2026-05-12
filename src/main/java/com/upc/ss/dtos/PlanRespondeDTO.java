package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class PlanRespondeDTO {
    private Long idPlan;
    private String nombrePlan;
    private Double precio;
    private Integer limiteRecuerdos;
    private String descripcion;
}
