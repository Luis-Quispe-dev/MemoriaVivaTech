package com.upc.ss.services;

import com.upc.ss.dtos.ActividadLlamadoDTO;
import com.upc.ss.dtos.ActividadRespondeDTO;
import com.upc.ss.entities.ActividadCalendario;
import com.upc.ss.entities.Asignacion;
import com.upc.ss.repositories.ActividadRepository;
import com.upc.ss.repositories.AsignacionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActividadService {
    @Autowired
    private ActividadRepository actividadRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AsignacionRepository asignacionRepository;

    private ActividadRespondeDTO armarResponse(ActividadCalendario actividad) {
        ActividadRespondeDTO response = modelMapper.map(actividad, ActividadRespondeDTO.class);
        response.setIdAsignacion(actividad.getAsignacion().getIdAsignacion());
        response.setNombreAdultoMayor(
                actividad.getAsignacion().getAdultoMayor().getUsuario().getNombreCompleto());
        response.setNombreCuidador(
                actividad.getAsignacion().getCuidador().getUsuario().getNombreCompleto());
        return response;
    }
    public ActividadRespondeDTO crearActividad(ActividadLlamadoDTO dto) {
        Asignacion asignacion = asignacionRepository
                .findById(dto.getIdAsignacion())
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        if (!asignacion.getEstado().equals(Asignacion.Estado.ACTIVA)) {
            throw new RuntimeException("No se pueden agregar actividades a una asignación finalizada");
        }
        ActividadCalendario actividad = new ActividadCalendario();
        actividad.setAsignacion(asignacion);
        actividad.setTitulo(dto.getTitulo());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setFechaHoraInicio(dto.getFechaHoraInicio());
        actividad.setFechaHoraFin(dto.getFechaHoraFin());
        actividad.setRecordatorioEn(dto.getRecordatorioEn());
        actividad.setEstado(ActividadCalendario.Estado.PENDIENTE);
        actividad.setCreadoPor(ActividadCalendario.CreadoPor.valueOf(dto.getCreadoPor()));
        actividad.setNotificado(false);
        ActividadCalendario guardada = actividadRepository.save(actividad);
        return armarResponse(guardada);
    }

    public List<ActividadRespondeDTO> obtenerActividadesPorAsignacion(Long idAsignacion) {
        asignacionRepository.findById(idAsignacion)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        List<ActividadCalendario> actividades = actividadRepository
                .findByAsignacionIdAsignacionOrderByFechaHoraInicioAsc(idAsignacion);
        return actividades.stream()
                .map(this::armarResponse)
                .collect(Collectors.toList());
    }

    public List<ActividadRespondeDTO> obtenerPorEstado(Long idAsignacion, String estado) {
        asignacionRepository.findById(idAsignacion)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        List<ActividadCalendario> actividades = actividadRepository
                .findByAsignacionIdAsignacionAndEstado(
                        idAsignacion,
                        ActividadCalendario.Estado.valueOf(estado));
        return actividades.stream()
                .map(this::armarResponse)
                .collect(Collectors.toList());
    }

    public ActividadRespondeDTO actualizarEstado(Long idActividad, String nuevoEstado) {
        ActividadCalendario actividad = actividadRepository.findById(idActividad)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
        actividad.setEstado(ActividadCalendario.Estado.valueOf(nuevoEstado));
        ActividadCalendario guardada = actividadRepository.save(actividad);
        return armarResponse(guardada);
    }

    public ActividadRespondeDTO editarActividad(Long idActividad, ActividadLlamadoDTO dto) {
        ActividadCalendario actividad = actividadRepository.findById(idActividad)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
        actividad.setTitulo(dto.getTitulo());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setFechaHoraInicio(dto.getFechaHoraInicio());
        actividad.setFechaHoraFin(dto.getFechaHoraFin());
        actividad.setRecordatorioEn(dto.getRecordatorioEn());
        actividad.setNotificado(false); // se resetea si cambia la fecha
        ActividadCalendario guardada = actividadRepository.save(actividad);
        return armarResponse(guardada);
    }

    public void eliminarActividad(Long idActividad) {
        ActividadCalendario actividad = actividadRepository.findById(idActividad)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
        actividadRepository.delete(actividad);
    }
}
