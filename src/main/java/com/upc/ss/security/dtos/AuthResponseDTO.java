package com.upc.ss.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String jwt;
    private Set<String> roles;
    private Long userId;
    private String email;
    private String nombreCompleto;
    private String contenidoFoto;
}