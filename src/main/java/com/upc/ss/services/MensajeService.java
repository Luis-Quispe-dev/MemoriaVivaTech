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
                mensaje.getAsignacion().getAdultoMayor().getUsuario().getNombreCompleto());
        response.setNombreCuidador(
                mensaje.getAsignacion().getCuidador().getUsuario().getNombreCompleto());

        return response;
    }

    public MensajeRespondeDTO enviarMensaje(MensajeLlamarDTO dto) {
        Asignacion asignacion = asignacionRepository
                .findById(dto.getIdAsignacion())
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        if (!asignacion.getEstado().equals(Asignacion.Estado.ACTIVA)) {
            throw new RuntimeException("No se puede enviar mensajes en una asignación finalizada");
        }

        Mensaje mensaje = new Mensaje();
        mensaje.setAsignacion(asignacion);
        mensaje.setContenido(dto.getContenido());
        mensaje.setFechaHora(LocalDateTime.now());
        mensaje.setLeido(false);
        mensaje.setTipoRemitente(Mensaje.TipoRemitente.valueOf(dto.getTipoRemitente()));

        Mensaje guardado = mensajeRepository.save(mensaje);

        return armarResponse(guardado);
    }
    public List<MensajeRespondeDTO> obtenerMensajesPorAsignacion(Long idAsignacion) {
        asignacionRepository.findById(idAsignacion)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        List<Mensaje> mensajes = mensajeRepository
                .findByAsignacionIdAsignacionOrderByFechaHoraAsc(idAsignacion);

        return mensajes.stream()
                .map(this::armarResponse)
                .collect(Collectors.toList());
    }
    public void marcarComoLeidos(Long idAsignacion) {

        List<Mensaje> noLeidos = mensajeRepository
                .findByAsignacionIdAsignacionAndLeidoFalse(idAsignacion);

        noLeidos.forEach(m -> m.setLeido(true));

        mensajeRepository.saveAll(noLeidos);
    }
}
