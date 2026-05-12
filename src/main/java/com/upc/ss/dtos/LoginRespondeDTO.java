package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class LoginRespondeDTO {
    private Long idUsuario;
    private String nombreCompleto;
    private String email;
    private String rol; // ADULTO_MAYOR | CUIDADOR

    // Si es adulto mayor
    private Long idAdultoMayor;

    // Si es cuidador
    private Long idCuidador;
}
