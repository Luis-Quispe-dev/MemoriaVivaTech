package com.upc.ss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class PagoRespondeDTO {
    private Long idPago;
    private Long idAdultoMayor;
    private String nombreAdultoMayor;
    private String nombrePlan;
    private Double monto;
    private String metodoPago;
    private LocalDate fechaPago;
    private Boolean estado;
}
