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

public class SolicitudRespuestaDTO {
    @NotNull(message = "El id de la solicitud es obligatorio")
    private Long idSolicitud;
    @NotNull(message = "La respuesta es obligatoria")
    private String respuesta;
}
