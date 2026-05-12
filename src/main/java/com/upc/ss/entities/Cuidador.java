package com.upc.ss.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "cuidador")
public class Cuidador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuidador;
    private String telefono;
    private String especialidad;
    private String biografia;
    private Boolean notificacionEncendida = true;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id_cuidador")
    private Usuario usuario;
}
