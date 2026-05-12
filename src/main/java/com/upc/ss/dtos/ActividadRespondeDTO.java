package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ActividadRespondeDTO {
    private Long idActividad;
    private Long idAsignacion;
    private String nombreAdultoMayor;
    private String nombreCuidador;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String estado;
    private LocalDateTime recordatorioEn;
    private Boolean notificado;
    private String creadoPor;
}
