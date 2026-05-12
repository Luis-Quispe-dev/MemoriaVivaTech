package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class MensajeRespondeDTO {
    private Long idMensaje;
    private Long idAsignacion;
    private String nombreAdultoMayor;
    private String nombreCuidador;
    private String contenido;
    private LocalDateTime fechaHora;
    private Boolean leido;
    private String tipoRemitente;
}
