package com.upc.ss.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "adulto_mayor")
public class AdultoMayor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAdultoMayor;
    private LocalDate fechaNacimiento;
    private String direccion;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id_adulto_mayor")
    private Usuario usuario;
}
