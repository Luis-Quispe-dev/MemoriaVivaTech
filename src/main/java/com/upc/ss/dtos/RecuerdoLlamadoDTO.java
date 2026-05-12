package com.upc.ss.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class RecuerdoLlamadoDTO {
    @NotNull(message = "El id del adulto mayor es obligatorio")
    private Long idAdultoMayor;
    @NotBlank(message = "El título es obligatorio")
    private String tituloRecuerdo;
    @NotNull(message = "El tipo de recuerdo es obligatorio")
    private String tipoRecuerdo;
    @NotBlank(message = "El contenido es obligatorio")
    private String contenido;
    private String formato;
}
