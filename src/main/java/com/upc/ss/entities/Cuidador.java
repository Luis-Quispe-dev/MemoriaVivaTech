package com.upc.ss.entities;

import com.upc.ss.security.entities.User;
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
    @Column(name = "id_cuidador")
    private Long idCuidador;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_cuidador")
    private User user;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "biografia", columnDefinition = "TEXT")
    private String biografia;

    @Column(name = "notificacion_encendida")
    private Boolean notificacionEncendida = true;
}