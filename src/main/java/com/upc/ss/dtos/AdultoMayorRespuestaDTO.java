package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class AdultoMayorRespuestaDTO {
    private Long idAdultoMayor;
    private String nombreCompleto;
    private String email;
    private LocalDate fechaNacimiento;
    private String direccion;
}
