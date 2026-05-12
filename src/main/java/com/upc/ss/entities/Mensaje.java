package com.upc.ss.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "mensaje")
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long idMensaje;
    @ManyToOne
    @JoinColumn(name = "id_asignacion", nullable = false)
    private Asignacion asignacion;
    @NotBlank(message = "El contenido no puede estar vacío")
    @Column(name = "contenido", nullable = false, length = 500)
    private String contenido;
    @NotNull
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    @Column(name = "leido")
    private Boolean leido = false;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_remitente", nullable = false)
    private TipoRemitente tipoRemitente;
    public enum TipoRemitente {
        ADULTO_MAYOR, CUIDADOR
    }
}
