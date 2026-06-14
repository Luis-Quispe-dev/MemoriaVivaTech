package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ConfiguracionRespondeDTO {
    private Long idConfiguracion;
    private Long idUsuario;
    private String nombreUsuario;
    private String rol;
    private String idioma;
    private String tamanioFuente;
    private String temaVisual;
    private Boolean notificacionesSonido;
}
