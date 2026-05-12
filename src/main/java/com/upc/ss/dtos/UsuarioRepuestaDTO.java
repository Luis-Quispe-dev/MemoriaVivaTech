package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioRepuestaDTO {
    private Long idUsuario;
    private String nombreCompleto;
    private String email;
    private String rol;
}
