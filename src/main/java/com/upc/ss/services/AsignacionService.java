package com.upc.ss.services;

import com.upc.ss.dtos.AsignacionRespondeDTO;
import com.upc.ss.entities.Asignacion;
import com.upc.ss.repositories.AsignacionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class AsignacionService {
    @Autowired
    private AsignacionRepository asignacionRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AsignacionRespondeDTO obtenerAsignacionActivaDeAdulto(Long idAdultoMayor) {

        Asignacion asignacion = asignacionRepository
                .findByAdultoMayorIdAdultoMayorAndEstado(idAdultoMayor, Asignacion.Estado.ACTIVA)
                .orElseThrow(() -> new RuntimeException("El adulto mayor no tiene una asignación activa"));

        AsignacionRespondeDTO response = modelMapper.map(asignacion, AsignacionRespondeDTO.class);
        response.setNombreAdultoMayor(asignacion.getAdultoMayor().getUsuario().getNombreCompleto());
        response.setNombreCuidador(asignacion.getCuidador().getUsuario().getNombreCompleto());
        response.setIdSolicitud(asignacion.getSolicitud().getIdSolicitud());

        return response;
    }
    public List<AsignacionRespondeDTO> obtenerAsignacionesActivasDeCuidador(Long idCuidador) {

        List<Asignacion> asignaciones = asignacionRepository
                .findByCuidadorIdCuidadorAndEstado(idCuidador, Asignacion.Estado.ACTIVA);

        return asignaciones.stream().map(a -> {
            AsignacionRespondeDTO dto = modelMapper.map(a, AsignacionRespondeDTO.class);
            dto.setNombreAdultoMayor(a.getAdultoMayor().getUsuario().getNombreCompleto());
            dto.setNombreCuidador(a.getCuidador().getUsuario().getNombreCompleto());
            dto.setIdSolicitud(a.getSolicitud().getIdSolicitud());
            return dto;
        }).collect(Collectors.toList());
    }
}
