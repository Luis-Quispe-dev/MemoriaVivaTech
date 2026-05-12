package com.upc.ss.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class GaleriaIALlamadoDTO {
    @NotNull(message = "El id del adulto mayor es obligatorio")
    private Long idAdultoMayor;

    @NotBlank(message = "El prompt es obligatorio")
    private String promptDescripcion;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;
}
