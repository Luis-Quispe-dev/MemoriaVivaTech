package com.upc.ss.services;

import com.upc.ss.config.StabilityAIClient;
import com.upc.ss.dtos.GaleriaIAGenerarLlamadoDTO;
import com.upc.ss.dtos.GaleriaIAGenerarRespuestaDTO;
import com.upc.ss.dtos.GaleriaIALlamadoDTO;
import com.upc.ss.dtos.GaleriaIARespondeDTO;
import com.upc.ss.entities.AdultoMayor;
import com.upc.ss.entities.GaleriaIA;
import com.upc.ss.repositories.AdultoMayorRepository;
import com.upc.ss.repositories.GaleriaIARepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GaleriaIAService {
    @Autowired
    private GaleriaIARepository galeriaIARepository;
    @Autowired
    private AdultoMayorRepository adultoMayorRepository;
    @Autowired
    private StabilityAIClient stabilityAIClient;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CloudinaryService cloudinaryService;

    public GaleriaIAGenerarRespuestaDTO generarImagen(GaleriaIAGenerarLlamadoDTO dto){
        String base64 = stabilityAIClient.generarImagen(dto.getPromptDescripcion());
        String urlImagen = cloudinaryService.subirBase64(base64);

        GaleriaIAGenerarRespuestaDTO response =
                modelMapper.map(dto, GaleriaIAGenerarRespuestaDTO.class);
        response.setUrlImagen(urlImagen);

        return response;
    }

    public GaleriaIARespondeDTO guardarImagen(GaleriaIALlamadoDTO dto, String urlImagen){
        AdultoMayor adultoMayor = adultoMayorRepository
                .findById(dto.getIdAdultoMayor())
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado :C"));

        GaleriaIA retrato = modelMapper.map(dto, GaleriaIA.class);
        retrato.setAdultoMayor(adultoMayor);
        retrato.setPrompt_descripcion(dto.getPromptDescripcion());
        retrato.setTitulo(dto.getTitulo());
        retrato.setUrlImagen(urlImagen);
        retrato.setFechaCreacion(LocalDateTime.now());

        return armarRespuesta(galeriaIARepository.save(retrato));
    }

    public List<GaleriaIARespondeDTO> obtenerGaleria(Long idAdultoMayor) {
        adultoMayorRepository.findById(idAdultoMayor)
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado :c"));

        return galeriaIARepository
                .findByAdultoMayorIdAdultoMayorOrderByFechaCreacionDesc(idAdultoMayor)
                .stream().map(this::armarRespuesta).collect(Collectors.toList());
    }
    public GaleriaIARespondeDTO obtenerPorId(Long idRetratoIa) {

        return armarRespuesta(galeriaIARepository.findById(idRetratoIa)
                .orElseThrow(() -> new RuntimeException("Retrato no encontrado")));
    }

    public void eliminarRetrato(Long idRetratoIa) {

        GaleriaIA retrato = galeriaIARepository.findById(idRetratoIa)
                .orElseThrow(() -> new RuntimeException("Retrato no encontrado"));

        galeriaIARepository.delete(retrato);
    }

    private GaleriaIARespondeDTO armarRespuesta(GaleriaIA retrato){
        GaleriaIARespondeDTO respuesta = modelMapper.map(retrato, GaleriaIARespondeDTO.class);
        respuesta.setIdAdultoMayor(retrato.getAdultoMayor().getIdAdultoMayor());
        respuesta.setNombreAdultoMayor(
                retrato.getAdultoMayor().getUser().getNombreCompleto());
        respuesta.setPromptDescripcion(retrato.getPrompt_descripcion());
        return respuesta;
    }
}
