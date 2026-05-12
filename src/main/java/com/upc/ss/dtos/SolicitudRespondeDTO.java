package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class SolicitudRespondeDTO {
    private Long idSolicitud;
    private Long idAdultoMayor;
    private String nombreAdultoMayor;
    private Long idCuidador;
    private String nombreCuidador;
    private String iniciadoPor;
    private String estado;
    private LocalDateTime fechaCreacion;
    private String mensaje;
}
