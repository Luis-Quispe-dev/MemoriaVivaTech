package com.upc.ss.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioLlamarDTO {
    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;
    @Email(message = "El email no es válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
    @NotNull(message = "El rol es obligatorio")
    private String rol;
}
