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
@Table(name = "configuracion")
public class Configuracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_configuracion")
    private Long idConfiguracion;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "idioma", nullable = false)
    private String idioma;

    @Column(name = "tamanio_fuente", nullable = false)
    private String tamanioFuente;

    @Column(name = "tema_visual", nullable = false)
    private String temaVisual;

    @Column(name = "notificaciones_sonido", nullable = false)
    private Boolean notificacionesSonido;
}
