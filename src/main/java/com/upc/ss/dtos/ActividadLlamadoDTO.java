package com.upc.ss.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ActividadLlamadoDTO {
    @NotNull(message = "El id de la asignación es obligatorio")
    private Long idAsignacion;
    @NotBlank(message = "El título es obligatorio")
    private String titulo;
    private String descripcion;
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private LocalDateTime recordatorioEn;
    @NotNull(message = "Debes indicar quién crea la actividad")
    private String creadoPor;
}

