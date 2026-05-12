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
@Table(name = "galeria_ia")
public class GaleriaIA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRetratoIa;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String prompt_descripcion;
    @Column(name = "titulo", nullable = false)
    private String  titulo;
    @Column(name = "url_imagen", nullable = false, columnDefinition = "TEXT")
    private String urlImagen;
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adulto_mayor", nullable = false)
    private AdultoMayor adultoMayor;
}
