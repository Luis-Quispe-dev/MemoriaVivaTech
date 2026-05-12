package com.upc.ss.dtos;

import com.upc.ss.entities.AdultoMayor;
import com.upc.ss.entities.Cuidador;
import com.upc.ss.entities.Solicitud;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AsignacionDTO {
    private Long idAsignacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private AsignacionDTO.Estado estado;

    private enum Estado{
        ACTIVA, FINALIZADA
    }
    private Cuidador cuidador;
    private AdultoMayor adultoMayor;
    private Solicitud solicitud;
}
