package com.upc.ss.services;

import com.upc.ss.dtos.RecuerdoExportarRespondeDTO;
import com.upc.ss.dtos.RecuerdoLlamadoDTO;
import com.upc.ss.dtos.RecuerdoRespondeDTO;
import com.upc.ss.entities.AdultoMayor;
import com.upc.ss.entities.Recuerdo;
import com.upc.ss.repositories.AdultoMayorRepository;
import com.upc.ss.repositories.RecuerdoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecuerdoService {
    @Autowired
    private RecuerdoRepository recuerdoRepository;

    @Autowired
    private AdultoMayorRepository adultoMayorRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final List<String> FORMATOS_AUDIO = List.of("mp3", "wav");
    private static final List<String> FORMATOS_FOTO  = List.of("jpg", "png", "webp");

    public RecuerdoRespondeDTO crearRecuerdoTexto(RecuerdoLlamadoDTO dto) {

        AdultoMayor adultoMayor = adultoMayorRepository.findById(dto.getIdAdultoMayor())
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        if (dto.getContenido() == null || dto.getContenido().isBlank())
            throw new RuntimeException("El contenido del texto no puede estar vacío");

        Recuerdo recuerdo = new Recuerdo();
        recuerdo.setAdultoMayor(adultoMayor);
        recuerdo.setTituloRecuerdo(dto.getTituloRecuerdo());
        recuerdo.setTipoRecuerdo(Recuerdo.TipoRecuerdo.valueOf("TEXTO"));
        recuerdo.setContenido(dto.getContenido());
        recuerdo.setFormato(null);
        recuerdo.setFechaCreacion(LocalDate.now());
        recuerdo.setFavorito(false);

        return armarResponse(recuerdoRepository.save(recuerdo));
    }

    public RecuerdoRespondeDTO crearRecuerdoAudio(RecuerdoLlamadoDTO dto) {

        AdultoMayor adultoMayor = adultoMayorRepository.findById(dto.getIdAdultoMayor())
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        if (dto.getContenido() == null || dto.getContenido().isBlank())
            throw new RuntimeException("La URL del audio no puede estar vacía");

        if (dto.getFormato() == null ||
                !FORMATOS_AUDIO.contains(dto.getFormato().toLowerCase()))
            throw new RuntimeException("Formato inválido, debe ser mp3 o wav");

        Recuerdo recuerdo = new Recuerdo();
        recuerdo.setAdultoMayor(adultoMayor);
        recuerdo.setTituloRecuerdo(dto.getTituloRecuerdo());
        recuerdo.setTipoRecuerdo(Recuerdo.TipoRecuerdo.valueOf("AUDIO"));
        recuerdo.setContenido(dto.getContenido()); // URL del audio
        recuerdo.setFormato(dto.getFormato().toLowerCase());
        recuerdo.setFechaCreacion(LocalDate.now());
        recuerdo.setFavorito(false);

        return armarResponse(recuerdoRepository.save(recuerdo));
    }

    public RecuerdoRespondeDTO crearRecuerdoFoto(RecuerdoLlamadoDTO dto) {

        AdultoMayor adultoMayor = adultoMayorRepository.findById(dto.getIdAdultoMayor())
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        if (dto.getContenido() == null || dto.getContenido().isBlank())
            throw new RuntimeException("La URL de la foto no puede estar vacía");

        if (dto.getFormato() == null ||
                !FORMATOS_FOTO.contains(dto.getFormato().toLowerCase()))
            throw new RuntimeException("Formato inválido, debe ser jpg, png o webp");

        Recuerdo recuerdo = new Recuerdo();
        recuerdo.setAdultoMayor(adultoMayor);
        recuerdo.setTituloRecuerdo(dto.getTituloRecuerdo());
        recuerdo.setTipoRecuerdo(Recuerdo.TipoRecuerdo.valueOf("FOTO"));
        recuerdo.setContenido(dto.getContenido());
        recuerdo.setFormato(dto.getFormato().toLowerCase());
        recuerdo.setFechaCreacion(LocalDate.now());
        recuerdo.setFavorito(false);

        return armarResponse(recuerdoRepository.save(recuerdo));
    }

    public List<RecuerdoRespondeDTO> obtenerTodos(Long idAdultoMayor) {

        adultoMayorRepository.findById(idAdultoMayor)
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        return recuerdoRepository
                .findByAdultoMayorIdAdultoMayorOrderByFechaCreacionDesc(idAdultoMayor)
                .stream().map(this::armarResponse).collect(Collectors.toList());
    }

    public List<RecuerdoRespondeDTO> obtenerPorTipo(Long idAdultoMayor, String tipo) {

        adultoMayorRepository.findById(idAdultoMayor)
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        return recuerdoRepository
                .findByAdultoMayorIdAdultoMayorAndTipoRecuerdoOrderByFechaCreacionDesc(
                        idAdultoMayor,
                        Recuerdo.TipoRecuerdo.valueOf(tipo))
                .stream().map(this::armarResponse).collect(Collectors.toList());
    }

    public RecuerdoRespondeDTO obtenerPorId(Long idRecuerdo) {

        return armarResponse(recuerdoRepository.findById(idRecuerdo)
                .orElseThrow(() -> new RuntimeException("Recuerdo no encontrado")));
    }

    public RecuerdoRespondeDTO editarRecuerdo(Long idRecuerdo, RecuerdoLlamadoDTO dto) {

        Recuerdo recuerdo = recuerdoRepository.findById(idRecuerdo)
                .orElseThrow(() -> new RuntimeException("Recuerdo no encontrado"));

        recuerdo.setTituloRecuerdo(dto.getTituloRecuerdo());
        recuerdo.setContenido(dto.getContenido());
        recuerdo.setFormato(dto.getFormato());

        return armarResponse(recuerdoRepository.save(recuerdo));
    }

    public void eliminarRecuerdo(Long idRecuerdo) {

        Recuerdo recuerdo = recuerdoRepository.findById(idRecuerdo)
                .orElseThrow(() -> new RuntimeException("Recuerdo no encontrado"));

        recuerdoRepository.delete(recuerdo);
    }

    private RecuerdoRespondeDTO armarResponse(Recuerdo recuerdo) {

        RecuerdoRespondeDTO response = modelMapper.map(recuerdo, RecuerdoRespondeDTO.class);
        response.setIdAdultoMayor(recuerdo.getAdultoMayor().getIdAdultoMayor());
        response.setNombreAdultoMayor(recuerdo.getAdultoMayor().getUser().getNombreCompleto());
        response.setTipoRecuerdo(recuerdo.getTipoRecuerdo().name());
        return response;
    }

    public List<RecuerdoRespondeDTO> obtenerMiLegado(Long idAdultoMayor) {

        adultoMayorRepository.findById(idAdultoMayor)
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        return recuerdoRepository
                .findByAdultoMayorIdAdultoMayorAndFavoritoTrueOrderByFechaCreacionDesc(idAdultoMayor)
                .stream()
                .map(this::armarResponse)
                .collect(Collectors.toList());
    }

    public RecuerdoRespondeDTO toggleFavorito(Long idRecuerdo) {

        Recuerdo recuerdo = recuerdoRepository.findById(idRecuerdo)
                .orElseThrow(() -> new RuntimeException("Recuerdo no encontrado"));

        recuerdo.setFavorito(!recuerdo.getFavorito());

        return armarResponse(recuerdoRepository.save(recuerdo));
    }

    public RecuerdoExportarRespondeDTO exportarRecuerdo(Long idRecuerdo) {
        Recuerdo recuerdo = recuerdoRepository.findById(idRecuerdo)
                .orElseThrow(() -> new RuntimeException("Recuerdo no encontrado"));

        RecuerdoExportarRespondeDTO response = new RecuerdoExportarRespondeDTO();
        response.setIdRecuerdo(recuerdo.getIdRecuerdo());
        response.setTituloRecuerdo(recuerdo.getTituloRecuerdo());
        response.setTipoRecuerdo(recuerdo.getTipoRecuerdo().name());
        response.setContenido(recuerdo.getContenido());
        response.setFormato(recuerdo.getFormato());

        String urlContenido = recuerdo.getContenido();
        String titulo = recuerdo.getTituloRecuerdo();

        response.setLinkWhatsapp("https://api.whatsapp.com/send?text=" + titulo + " " + urlContenido);
        response.setLinkFacebook("https://www.facebook.com/sharer/sharer.php?u=" + urlContenido);
        response.setLinkInstagram("https://www.instagram.com/");
        return response;
    }
}
