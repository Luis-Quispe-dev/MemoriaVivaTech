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

public class ConfiguracionLlamadoDTO {
    @NotNull(message = "El id del usuario es obligatorio")
    private Long idUsuario;

    @NotBlank(message = "El idioma es obligatorio")
    private String idioma;

    @NotBlank(message = "El tamaño de fuente es obligatorio")
    private String tamanioFuente;

    @NotBlank(message = "El tema visual es obligatorio")
    private String temaVisual;

    @NotNull(message = "Las notificaciones son obligatorias")
    private Boolean notificacionesSonido;
}
