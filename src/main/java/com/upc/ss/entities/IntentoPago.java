package com.upc.ss.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "intento_pago")
public class IntentoPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime fecha_intenta;
    @Column(nullable = false)
    private MetodoPago metodo;
    @Column(nullable = false)
    private String estado;
    @Column(nullable = false)
    private String codigoReferencia;
    private String mensajeError;

    @ManyToOne
    @JoinColumn(name="id_pago",nullable = false)
    private Pago pago;

    public enum MetodoPago {
        CODIGO_QR,
        TARJETA_CREDITO
    }
}
