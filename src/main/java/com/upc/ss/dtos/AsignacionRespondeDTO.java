package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AsignacionRespondeDTO {
    private Long idAsignacion;
    private Long idAdultoMayor;
    private String nombreAdultoMayor;
    private Long idCuidador;
    private String nombreCuidador;
    private Long idSolicitud;
    private String estado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String fotoAdultoMayor;
    private String fotoCuidador;
}
