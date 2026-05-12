package com.upc.ss.entities;

import jakarta.persistence.*;
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

@Entity
public class ActividadCalendario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actividad")
    private Long idActividad;

    @NotBlank(message = "El título es obligatorio")
    @Column(name = "titulo", nullable = false)
    private String titulo;
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;
    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado;

    @Column(name = "recordatorio_en")
    private LocalDateTime recordatorioEn;
    @Column(name = "notificado")
    private Boolean notificado = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "creado_por", nullable = false)
    private CreadoPor creadoPor;

    public enum Estado {
        PENDIENTE, EN_CURSO, COMPLETADA, CANCELADA
    }
    public enum CreadoPor {
        ADULTO_MAYOR, CUIDADOR
    }

    @ManyToOne
    @JoinColumn(name = "id_asignacion", nullable = false)
    private Asignacion asignacion;
}
