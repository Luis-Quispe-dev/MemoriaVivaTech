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

public class MensajeLlamarDTO {
    @NotNull(message = "El id de la asignación es obligatorio")

    private Long idAsignacion;
    @NotBlank(message = "El contenido no puede estar vacío")

    private String contenido;

    @NotNull(message = "El tipo de remitente es obligatorio")

    private String tipoRemitente;
}
