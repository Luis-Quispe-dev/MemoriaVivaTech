package com.upc.ss.services;

import com.upc.ss.dtos.ActividadLlamadoDTO;
import com.upc.ss.dtos.ActividadRespondeDTO;
import com.upc.ss.entities.ActividadCalendario;
import com.upc.ss.entities.Asignacion;
import com.upc.ss.repositories.ActividadRepository;
import com.upc.ss.repositories.AsignacionRepository;
import com.upc.ss.security.services.CustomUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AsignacionRepository asignacionRepository;

    private void validarPertenenciaAsignacion(Asignacion asignacion, Long idUsuarioLogueado) {
        Long idUserAdulto = asignacion.getAdultoMayor().getUser().getId();
        Long idUserCuidador = asignacion.getCuidador().getUser().getId();

        if (!idUsuarioLogueado.equals(idUserAdulto) && !idUsuarioLogueado.equals(idUserCuidador)) {
            throw new RuntimeException("Acceso denegado: No tienes permisos sobre esta asignación.");
        }
    }

    private ActividadRespondeDTO armarResponse(ActividadCalendario actividad) {
        ActividadRespondeDTO response = modelMapper.map(actividad, ActividadRespondeDTO.class);
        response.setIdAsignacion(actividad.getAsignacion().getIdAsignacion());
        response.setNombreAdultoMayor(
                actividad.getAsignacion().getAdultoMayor().getUser().getNombreCompleto());
        response.setNombreCuidador(
                actividad.getAsignacion().getCuidador().getUser().getNombreCompleto());
        return response;
    }

    public ActividadRespondeDTO crearActividad(ActividadLlamadoDTO dto, CustomUserDetails usuarioLogueado) {
        Asignacion asignacion = asignacionRepository
                .findById(dto.getIdAsignacion())
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        validarPertenenciaAsignacion(asignacion, usuarioLogueado.getId());

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
        actividad.setNotificado(false);

        boolean esAdulto = usuarioLogueado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADULTO_MAYOR"));

        if (esAdulto) {
            actividad.setCreadoPor(ActividadCalendario.CreadoPor.ADULTO_MAYOR);
        } else {
            actividad.setCreadoPor(ActividadCalendario.CreadoPor.CUIDADOR);
        }

        ActividadCalendario guardada = actividadRepository.save(actividad);
        return armarResponse(guardada);
    }

    public List<ActividadRespondeDTO> obtenerActividadesPorAsignacion(Long idAsignacion, Long idUsuarioLogueado) {
        Asignacion asignacion = asignacionRepository.findById(idAsignacion)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        validarPertenenciaAsignacion(asignacion, idUsuarioLogueado);

        List<ActividadCalendario> actividades = actividadRepository
                .findByAsignacionIdAsignacionOrderByFechaHoraInicioAsc(idAsignacion);
        return actividades.stream()
                .map(this::armarResponse)
                .collect(Collectors.toList());
    }

    public List<ActividadRespondeDTO> obtenerPorEstado(Long idAsignacion, String estado, Long idUsuarioLogueado) {
        Asignacion asignacion = asignacionRepository.findById(idAsignacion)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        validarPertenenciaAsignacion(asignacion, idUsuarioLogueado);

        List<ActividadCalendario> actividades = actividadRepository
                .findByAsignacionIdAsignacionAndEstado(
                        idAsignacion,
                        ActividadCalendario.Estado.valueOf(estado));
        return actividades.stream()
                .map(this::armarResponse)
                .collect(Collectors.toList());
    }

    public ActividadRespondeDTO actualizarEstado(Long idActividad, String nuevoEstado, Long idUsuarioLogueado) {
        ActividadCalendario actividad = actividadRepository.findById(idActividad)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        validarPertenenciaAsignacion(actividad.getAsignacion(), idUsuarioLogueado);

        actividad.setEstado(ActividadCalendario.Estado.valueOf(nuevoEstado));
        ActividadCalendario guardada = actividadRepository.save(actividad);
        return armarResponse(guardada);
    }

    public ActividadRespondeDTO editarActividad(Long idActividad, ActividadLlamadoDTO dto, Long idUsuarioLogueado) {
        ActividadCalendario actividad = actividadRepository.findById(idActividad)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        validarPertenenciaAsignacion(actividad.getAsignacion(), idUsuarioLogueado);

        actividad.setTitulo(dto.getTitulo());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setFechaHoraInicio(dto.getFechaHoraInicio());
        actividad.setFechaHoraFin(dto.getFechaHoraFin());
        actividad.setRecordatorioEn(dto.getRecordatorioEn());
        actividad.setNotificado(false);

        ActividadCalendario guardada = actividadRepository.save(actividad);
        return armarResponse(guardada);
    }

    public void eliminarActividad(Long idActividad, Long idUsuarioLogueado) {
        ActividadCalendario actividad = actividadRepository.findById(idActividad)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        validarPertenenciaAsignacion(actividad.getAsignacion(), idUsuarioLogueado);

        actividadRepository.delete(actividad);
    }
}