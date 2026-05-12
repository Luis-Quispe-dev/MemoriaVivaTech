package com.upc.ss.entities;

import jakarta.persistence.*;
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
@Table(name = "asignacion")
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsignacion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    public enum Estado{
        ACTIVA, FINALIZADA
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_Cuidador")
    private Cuidador cuidador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_Adulto_Mayor")
    private AdultoMayor adultoMayor;

    @OneToOne
    @JoinColumn(name = "id_solicitud", nullable = false, unique = true)
    private Solicitud solicitud;
}
