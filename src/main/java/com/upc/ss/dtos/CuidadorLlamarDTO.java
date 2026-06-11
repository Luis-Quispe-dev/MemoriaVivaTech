package com.upc.ss.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class CuidadorLlamarDTO {
    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;
    @Email(message = "El email no es válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    private String contrasena;
    private String telefono;
    private String biografia;
    private String contenidoFoto;
}
