package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class CuidadorRespuestaDTO {
    private Long idCuidador;
    private String nombreCompleto;
    private String email;
    private String especialidad;
    private String telefono;
    private String biografia;
    private Boolean notificacionActiva;
}
