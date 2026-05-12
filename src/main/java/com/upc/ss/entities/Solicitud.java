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
@Table(name = "solicitud")
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitud;
    @Enumerated(EnumType.STRING)
    private IniciadoPor iniciadoPor;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    private LocalDateTime fechaCreacion;
    private String mensaje;
    @OneToOne(mappedBy = "solicitud")
    private Asignacion asignacion;
    public enum IniciadoPor {
        ADULTO_MAYOR, CUIDADOR
    }
    public enum Estado {
        PENDIENTE, ACEPTADA, RECHAZADA
    }
    @ManyToOne
    @JoinColumn(name = "id_adulto_mayor", nullable = false)
    private AdultoMayor adultoMayor;

    @ManyToOne
    @JoinColumn(name = "id_cuidador", nullable = false)
    private Cuidador cuidador;
}
