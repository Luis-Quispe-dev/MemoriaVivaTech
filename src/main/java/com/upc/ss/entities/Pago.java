package com.upc.ss.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;
    @ManyToOne
    @JoinColumn(name = "id_adulto_mayor", nullable = false)
    private AdultoMayor adultoMayor;
    @ManyToOne
    @JoinColumn(name = "id_plan", nullable = false)
    private Plan plan;
    @Column(name = "monto", nullable = false)
    private Double monto;
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;
    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;
    @Column(name = "estado", nullable = false)
    private Boolean estado;
}
