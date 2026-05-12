package com.upc.ss.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor


public class SolicitudLlamarDTO {
    @NotNull(message = "El id del adulto mayor es obligatorio")
    private Long idAdultoMayor;
    @NotNull(message = "El id del cuidador es obligatorio")
    private Long idCuidador;
    @NotNull(message = "Debes indicar quién inicia la solicitud")
    private String iniciadoPor;
    private String mensaje;
}
