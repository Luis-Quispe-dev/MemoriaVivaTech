package com.upc.ss.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class PagoLlamadoDTO {
    @NotNull(message = "El id del adulto mayor es obligatorio")
    private Long idAdultoMayor;

    @NotNull(message = "El id del plan es obligatorio")
    private Long idPlan;

    @NotNull(message = "El método de pago es obligatorio")
    private String metodoPago;
    private String numeroTarjeta;
    private String cvc;
    private String fechaVencimiento;
    private String correoElectronico;
    private String codigoZip;
}
