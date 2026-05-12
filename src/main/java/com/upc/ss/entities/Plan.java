package com.upc.ss.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "plan")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private Long idPlan;
    @Column(name = "nombre_plan", nullable = false)
    private String nombrePlan;
    @Column(name = "precio", nullable = false)
    private Double precio;
    @Column(name = "limite_recuerdos", nullable = false)
    private Integer limiteRecuerdos;
    @Column(name = "descripcion")
    private String descripcion;
}
