package com.upc.ss.services;

import com.upc.ss.dtos.AsignacionRespondeDTO;
import com.upc.ss.dtos.SolicitudLlamarDTO;
import com.upc.ss.dtos.SolicitudRespondeDTO;
import com.upc.ss.dtos.SolicitudRespuestaDTO;
import com.upc.ss.entities.AdultoMayor;
import com.upc.ss.entities.Asignacion;
import com.upc.ss.entities.Cuidador;
import com.upc.ss.entities.Solicitud;
import com.upc.ss.repositories.AdultoMayorRepository;
import com.upc.ss.repositories.AsignacionRepository;
import com.upc.ss.repositories.CuidadorRepository;
import com.upc.ss.repositories.SolicitudRepository;
import com.upc.ss.security.services.CustomUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SolicitudService {
    @Autowired
    private SolicitudRepository solicitudRepository;
    @Autowired
    private AsignacionRepository asignacionRepository;
    @Autowired
    private AdultoMayorRepository adultoMayorRepository;
    @Autowired
    private CuidadorRepository cuidadorRepository;
    @Autowired
    private ModelMapper modelMapper;

    public SolicitudRespondeDTO crearSolicitud(SolicitudLlamarDTO dto, CustomUserDetails usuarioLogueado) {
        Long idLogueado = usuarioLogueado.getId();
        boolean esAdulto = usuarioLogueado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADULTO_MAYOR"));
        boolean esCuidador = usuarioLogueado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CUIDADOR"));

        if (esAdulto) {
            if (!dto.getIdAdultoMayor().equals(idLogueado)) {
                throw new RuntimeException("Acceso denegado: No puedes crear una solicitud usando el ID de otro adulto mayor.");
            }
            dto.setIniciadoPor("ADULTO_MAYOR");
        }
        else if (esCuidador) {
            if (!dto.getIdCuidador().equals(idLogueado)) {
                throw new RuntimeException("Acceso denegado: No puedes crear una solicitud usando el ID de otro cuidador.");
            }
            dto.setIniciadoPor("CUIDADOR");
        }

        AdultoMayor adultoMayor = adultoMayorRepository
                .findById(dto.getIdAdultoMayor())
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        Cuidador cuidador = cuidadorRepository
                .findById(dto.getIdCuidador())
                .orElseThrow(() -> new RuntimeException("Cuidador no encontrado"));

        Optional<Solicitud> solicitudExistente = solicitudRepository
                .findByAdultoMayorIdAdultoMayorAndCuidadorIdCuidadorAndEstado(
                        dto.getIdAdultoMayor(),
                        dto.getIdCuidador(),
                        Solicitud.Estado.PENDIENTE);

        if (solicitudExistente.isPresent()) {
            throw new RuntimeException("Ya existe una solicitud pendiente entre este adulto mayor y cuidador");
        }

        Optional<Asignacion> asignacionActiva = asignacionRepository
                .findByAdultoMayorIdAdultoMayorAndEstado(
                        dto.getIdAdultoMayor(),
                        Asignacion.Estado.ACTIVA);

        if (asignacionActiva.isPresent()) {
            throw new RuntimeException("El adulto mayor ya tiene un cuidador asignado activo");
        }

        Solicitud solicitud = new Solicitud();
        solicitud.setAdultoMayor(adultoMayor);
        solicitud.setCuidador(cuidador);
        solicitud.setIniciadoPor(Solicitud.IniciadoPor.valueOf(dto.getIniciadoPor()));
        solicitud.setEstado(Solicitud.Estado.PENDIENTE);
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setMensaje(dto.getMensaje());

        Solicitud guardada = solicitudRepository.save(solicitud);

        SolicitudRespondeDTO response = modelMapper.map(guardada, SolicitudRespondeDTO.class);
        response.setNombreAdultoMayor(adultoMayor.getUser().getNombreCompleto());
        response.setNombreCuidador(cuidador.getUser().getNombreCompleto());
        response.setFotoCuidador(cuidador.getUser().getContenidoFoto());

        return response;
    }

    public AsignacionRespondeDTO responderSolicitud(SolicitudRespuestaDTO dto, Long idUsuarioLogueado) {
        Solicitud solicitud = solicitudRepository
                .findById(dto.getIdSolicitud())
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        Long idUserAdulto = solicitud.getAdultoMayor().getUser().getId();
        Long idUserCuidador = solicitud.getCuidador().getUser().getId();

        if (!idUserAdulto.equals(idUsuarioLogueado) && !idUserCuidador.equals(idUsuarioLogueado)) {
            throw new RuntimeException("Acceso denegado: No perteneces a esta solicitud.");
        }
        if (solicitud.getIniciadoPor() == Solicitud.IniciadoPor.ADULTO_MAYOR) {
            // Si la inició el Adulto Mayor, el usuario logueado TIENE que ser el Cuidador
            if (!idUserCuidador.equals(idUsuarioLogueado)) {
                throw new RuntimeException("Acceso denegado: Esperando la respuesta del Cuidador.");
            }
        } else if (solicitud.getIniciadoPor() == Solicitud.IniciadoPor.CUIDADOR) {
            // Si la inició el Cuidador, el usuario logueado TIENE que ser el Adulto Mayor
            if (!idUserAdulto.equals(idUsuarioLogueado)) {
                throw new RuntimeException("Acceso denegado: Esperando la respuesta del Adulto Mayor.");
            }
        }

        if (!solicitud.getEstado().equals(Solicitud.Estado.PENDIENTE)) {
            throw new RuntimeException("La solicitud ya fue respondida anteriormente");
        }

        Solicitud.Estado nuevaRespuesta = Solicitud.Estado.valueOf(dto.getRespuesta());

        if (nuevaRespuesta.equals(Solicitud.Estado.RECHAZADA)) {
            solicitud.setEstado(Solicitud.Estado.RECHAZADA);
            solicitudRepository.save(solicitud);
            throw new RuntimeException("Solicitud rechazada correctamente");
        }

        solicitud.setEstado(Solicitud.Estado.ACEPTADA);
        solicitudRepository.save(solicitud);

        Optional<Asignacion> asignacionActiva = asignacionRepository
                .findByAdultoMayorIdAdultoMayorAndEstado(
                        solicitud.getAdultoMayor().getIdAdultoMayor(),
                        Asignacion.Estado.ACTIVA);

        if (asignacionActiva.isPresent()) {
            Asignacion anterior = asignacionActiva.get();
            anterior.setEstado(Asignacion.Estado.FINALIZADA);
            anterior.setFechaFin(LocalDateTime.now());
            asignacionRepository.save(anterior);
        }

        Asignacion nueva = new Asignacion();
        nueva.setCuidador(solicitud.getCuidador());
        nueva.setAdultoMayor(solicitud.getAdultoMayor());
        nueva.setSolicitud(solicitud);
        nueva.setEstado(Asignacion.Estado.ACTIVA);
        nueva.setFechaInicio(LocalDateTime.now());

        Asignacion guardada = asignacionRepository.save(nueva);

        AsignacionRespondeDTO response = modelMapper.map(guardada, AsignacionRespondeDTO.class);
        response.setNombreAdultoMayor(solicitud.getAdultoMayor().getUser().getNombreCompleto());
        response.setNombreCuidador(solicitud.getCuidador().getUser().getNombreCompleto());
        response.setIdSolicitud(solicitud.getIdSolicitud());

        return response;
    }

    public List<SolicitudRespondeDTO> obtenerSolicitudesPendientesDeCuidador(Long idCuidador) {
        List<Solicitud> solicitudes = solicitudRepository
                .findByCuidadorIdCuidadorAndEstado(idCuidador, Solicitud.Estado.PENDIENTE);
        return solicitudes.stream().map(s -> {
            SolicitudRespondeDTO dto = modelMapper
                    .map(s, SolicitudRespondeDTO.class);
            dto.setNombreAdultoMayor(s.getAdultoMayor().getUser().getNombreCompleto());
            dto.setNombreCuidador(s.getCuidador().getUser().getNombreCompleto());
            dto.setFotoCuidador(s.getCuidador().getUser().getContenidoFoto());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<SolicitudRespondeDTO> obtenerSolicitudesPendientesDeAdulto(Long idAdultoMayor) {
        List<Solicitud> solicitudes = solicitudRepository
                .findByAdultoMayorIdAdultoMayorAndEstado(idAdultoMayor, Solicitud.Estado.PENDIENTE);
        return solicitudes.stream().map(s -> {
            SolicitudRespondeDTO dto = modelMapper.map(s, SolicitudRespondeDTO.class);
            dto.setNombreAdultoMayor(s.getAdultoMayor().getUser().getNombreCompleto());
            dto.setNombreCuidador(s.getCuidador().getUser().getNombreCompleto());
            dto.setFotoCuidador(s.getCuidador().getUser().getContenidoFoto());
            return dto;
        }).collect(Collectors.toList());
    }
}
