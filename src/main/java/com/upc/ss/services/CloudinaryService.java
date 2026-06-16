package com.upc.ss.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;
    public String subirBase64(String base64Completo) {

        try {
            String base64Data = base64Completo.contains(",")
                    ? base64Completo.split(",")[1]
                    : base64Completo;

            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            Map resultado = cloudinary.uploader().upload(
                    imageBytes,
                    ObjectUtils.asMap(
                            "folder", "soul-story/galeria-ia",
                            "resource_type", "image"
                    )
            );

            return (String) resultado.get("secure_url");

        } catch (Exception e) {
            throw new RuntimeException("Error al subir imagen a Cloudinary: " + e.getMessage());
        }
    }

    public String subirArchivo(MultipartFile archivo) {

        try {
            byte[] bytes = archivo.getBytes();

            Map resultado = cloudinary.uploader().upload(
                    bytes,
                    ObjectUtils.asMap(
                            "folder", "soul-story/recuerdos",
                            "resource_type", "auto"
                    )
            );

            return (String) resultado.get("secure_url");

        } catch (Exception e) {
            throw new RuntimeException("Error al subir archivo: " + e.getMessage());
        }
    }
}
