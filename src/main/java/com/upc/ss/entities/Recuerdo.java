package com.upc.ss.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "recuerdo")
public class Recuerdo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recuerdo")
    private Long idRecuerdo;
    @ManyToOne
    @JoinColumn(name = "id_adulto_mayor", nullable = false)
    private AdultoMayor adultoMayor;
    @NotBlank(message = "El título es obligatorio")
    @Column(name = "titulo_recuerdo", nullable = false)
    private String tituloRecuerdo;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_recuerdo", nullable = false)
    private TipoRecuerdo tipoRecuerdo;
    @Column(name = "contenido", columnDefinition = "TEXT", nullable = false)
    private String contenido;
    @Column(name = "formato")
    private String formato;
    @Column(name = "favorito")
    private Boolean favorito = false;
    @NotNull
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    public enum TipoRecuerdo {
        TEXTO, AUDIO, FOTO
    }
}
