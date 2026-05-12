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
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    private String nombreCompleto;
    private String email;
    private String contrasena;
    @Enumerated(EnumType.STRING)
    private Rol rol;

    public enum Rol {
        ADULTO_MAYOR, CUIDADOR;
    }
}
