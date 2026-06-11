package com.upc.ss.services;

import com.upc.ss.dtos.*;
import com.upc.ss.entities.*;
import com.upc.ss.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuscripcionService {
    @Autowired
    private SuscripcionRepository suscripcionRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private AdultoMayorRepository adultoMayorRepository;

    @Autowired
    private ModelMapper modelMapper;

    public void crearSuscripcionGratis(Long idAdultoMayor) {

        AdultoMayor adultoMayor = adultoMayorRepository.findById(idAdultoMayor)
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        Plan planGratis = planRepository.findByNombrePlan("Libreta Familiar")
                .orElseThrow(() -> new RuntimeException("Plan gratis no encontrado"));

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setAdultoMayor(adultoMayor);
        suscripcion.setPlan(planGratis);
        suscripcion.setFechaInicio(LocalDate.now());
        suscripcion.setFechaFin(null); // gratis no vence
        suscripcion.setEstado(true);

        suscripcionRepository.save(suscripcion);
    }

    public List<PlanRespondeDTO> listarPlanes() {

        return planRepository.findAll()
                .stream()
                .map(plan -> modelMapper.map(plan, PlanRespondeDTO.class))
                .collect(Collectors.toList());
    }

    public SuscripcionRespondeDTO obtenerSuscripcionActiva(Long idAdultoMayor) {

        Suscripcion suscripcion = suscripcionRepository
                .findByAdultoMayorIdAdultoMayorAndEstadoTrue(idAdultoMayor)
                .orElseThrow(() -> new RuntimeException("No tiene suscripción activa"));

        return armarResponseSuscripcion(suscripcion);
    }

    public PagoRespondeDTO pagar(PagoLlamadoDTO dto) {

        AdultoMayor adultoMayor = adultoMayorRepository.findById(dto.getIdAdultoMayor())
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        Plan plan = planRepository.findById(dto.getIdPlan())
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

        if (!dto.getMetodoPago().equals("QR") && !dto.getMetodoPago().equals("TARJETA")) {
            throw new RuntimeException("Método de pago inválido. Usa QR o TARJETA");
        }

        if (dto.getMetodoPago().equals("TARJETA")) {
            if (dto.getNumeroTarjeta() == null || dto.getCvc() == null
                    || dto.getFechaVencimiento() == null) {
                throw new RuntimeException("Faltan datos de la tarjeta");
            }
        }

        suscripcionRepository
                .findByAdultoMayorIdAdultoMayorAndEstadoTrue(dto.getIdAdultoMayor())
                .ifPresent(s -> {
                    s.setEstado(false);
                    s.setFechaFin(LocalDate.now());
                    suscripcionRepository.save(s);
                });

        Suscripcion nuevaSuscripcion = new Suscripcion();
        nuevaSuscripcion.setAdultoMayor(adultoMayor);
        nuevaSuscripcion.setPlan(plan);
        nuevaSuscripcion.setFechaInicio(LocalDate.now());
        nuevaSuscripcion.setFechaFin(LocalDate.now().plusDays(30));
        nuevaSuscripcion.setEstado(true);
        suscripcionRepository.save(nuevaSuscripcion);

        Pago pago = new Pago();
        pago.setAdultoMayor(adultoMayor);
        pago.setPlan(plan);
        pago.setMonto(plan.getPrecio());
        pago.setFechaPago(LocalDate.now());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstado(true);

        Pago guardado = pagoRepository.save(pago);

        PagoRespondeDTO response = new PagoRespondeDTO();
        response.setIdPago(guardado.getIdPago());
        response.setIdAdultoMayor(adultoMayor.getIdAdultoMayor());
        response.setNombreAdultoMayor(adultoMayor.getUser().getNombreCompleto());
        response.setNombrePlan(plan.getNombrePlan());
        response.setMonto(guardado.getMonto());
        response.setMetodoPago(guardado.getMetodoPago());
        response.setFechaPago(guardado.getFechaPago());
        response.setEstado(guardado.getEstado());

        return response;
    }

    public List<PagoRespondeDTO> historialPagos(Long idAdultoMayor) {

        return pagoRepository
                .findByAdultoMayorIdAdultoMayorOrderByFechaPagoDesc(idAdultoMayor)
                .stream()
                .map(pago -> {
                    PagoRespondeDTO dto = new PagoRespondeDTO();
                    dto.setIdPago(pago.getIdPago());
                    dto.setIdAdultoMayor(pago.getAdultoMayor().getIdAdultoMayor());
                    dto.setNombreAdultoMayor(
                            pago.getAdultoMayor().getUser().getNombreCompleto());
                    dto.setNombrePlan(pago.getPlan().getNombrePlan());
                    dto.setMonto(pago.getMonto());
                    dto.setMetodoPago(pago.getMetodoPago());
                    dto.setFechaPago(pago.getFechaPago());
                    dto.setEstado(pago.getEstado());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private SuscripcionRespondeDTO armarResponseSuscripcion(Suscripcion suscripcion) {

        SuscripcionRespondeDTO response = new SuscripcionRespondeDTO();
        response.setIdSuscripcion(suscripcion.getIdSuscripcion());
        response.setIdAdultoMayor(suscripcion.getAdultoMayor().getIdAdultoMayor());
        response.setNombreAdultoMayor(
                suscripcion.getAdultoMayor().getUser().getNombreCompleto());
        response.setNombrePlan(suscripcion.getPlan().getNombrePlan());
        response.setPrecio(suscripcion.getPlan().getPrecio());
        response.setLimiteRecuerdos(suscripcion.getPlan().getLimiteRecuerdos());
        response.setFechaInicio(suscripcion.getFechaInicio());
        response.setFechaFin(suscripcion.getFechaFin());
        response.setEstado(suscripcion.getEstado());

        return response;
    }
}
