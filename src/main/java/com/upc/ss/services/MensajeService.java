package com.upc.ss.services;

import com.upc.ss.dtos.MensajeLlamarDTO;
import com.upc.ss.dtos.MensajeRespondeDTO;
import com.upc.ss.entities.Asignacion;
import com.upc.ss.entities.Mensaje;
import com.upc.ss.repositories.AsignacionRepository;
import com.upc.ss.repositories.MensajeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import com.upc.ss.security.services.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MensajeService {
    @Autowired
    private MensajeRepository mensajeRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AsignacionRepository asignacionRepository;

    private MensajeRespondeDTO armarResponse(Mensaje mensaje) {
        MensajeRespondeDTO response = modelMapper.map(mensaje, MensajeRespondeDTO.class);
        response.setIdAsignacion(mensaje.getAsignacion().getIdAsignacion());
        response.setNombreAdultoMayor(
                mensaje.getAsignacion().getAdultoMayor().getUser().getNombreCompleto());
        response.setNombreCuidador(
                mensaje.getAsignacion().getCuidador().getUser().getNombreCompleto());

        return response;
    }

    public MensajeRespondeDTO enviarMensaje(MensajeLlamarDTO dto, CustomUserDetails usuarioLogueado) {
        Asignacion asignacion = asignacionRepository
                .findById(dto.getIdAsignacion())
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        if (!asignacion.getEstado().equals(Asignacion.Estado.ACTIVA)) {
            throw new RuntimeException("No se puede enviar mensajes en una asignación finalizada");
        }

        Long idLogueado = usuarioLogueado.getId();
        Long idUserAdulto = asignacion.getAdultoMayor().getUser().getId();
        Long idUserCuidador = asignacion.getCuidador().getUser().getId();

        if (!idLogueado.equals(idUserAdulto) && !idLogueado.equals(idUserCuidador)) {
            throw new RuntimeException("Acceso denegado: No formas parte de esta asignación de chat.");
        }

        Mensaje mensaje = new Mensaje();
        mensaje.setAsignacion(asignacion);
        mensaje.setContenido(dto.getContenido());
        mensaje.setFechaHora(LocalDateTime.now());
        mensaje.setLeido(false);

        boolean esAdulto = usuarioLogueado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADULTO_MAYOR"));

        if (esAdulto) {
            mensaje.setTipoRemitente(Mensaje.TipoRemitente.ADULTO_MAYOR);
        } else {
            mensaje.setTipoRemitente(Mensaje.TipoRemitente.CUIDADOR);
        }

        Mensaje guardado = mensajeRepository.save(mensaje);

        return armarResponse(guardado);
    }

    public List<MensajeRespondeDTO> obtenerMensajesPorAsignacion(Long idAsignacion, Long idUsuarioLogueado) {
        Asignacion asignacion = asignacionRepository.findById(idAsignacion)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada :c "));

        Long idUserAdulto = asignacion.getAdultoMayor().getUser().getId();
        Long idUserCuidador = asignacion.getCuidador().getUser().getId();

        if (!idUsuarioLogueado.equals(idUserAdulto) && !idUsuarioLogueado.equals(idUserCuidador)) {
            throw new RuntimeException("Acceso denegado: No tienes permiso para ver este chat.");
        }

        List<Mensaje> mensajes = mensajeRepository
                .findByAsignacionIdAsignacionOrderByFechaHoraAsc(idAsignacion);

        return mensajes.stream()
                .map(this::armarResponse)
                .collect(Collectors.toList());
    }
    public void marcarComoLeidos(Long idAsignacion, Long idUsuarioLogueado) {
        Asignacion asignacion = asignacionRepository.findById(idAsignacion)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada :C"));

        Long idUserAdulto = asignacion.getAdultoMayor().getUser().getId();
        Long idUserCuidador = asignacion.getCuidador().getUser().getId();

        if (!idUsuarioLogueado.equals(idUserAdulto) && !idUsuarioLogueado.equals(idUserCuidador)) {
            throw new RuntimeException("Acceso denegado: No tienes permiso para modificar este chat.");
        }

        List<Mensaje> noLeidos = mensajeRepository
                .findByAsignacionIdAsignacionAndLeidoFalse(idAsignacion);

        List<Mensaje> aMarcar = noLeidos.stream().filter(m -> {
            if (idUsuarioLogueado.equals(idUserAdulto)) {
                return m.getTipoRemitente() == Mensaje.TipoRemitente.CUIDADOR;
            } else {
                return m.getTipoRemitente() == Mensaje.TipoRemitente.ADULTO_MAYOR;
            }
        }).collect(Collectors.toList());

        if (!aMarcar.isEmpty()) {
            aMarcar.forEach(m -> m.setLeido(true));
            mensajeRepository.saveAll(aMarcar);
        }
    }
}
