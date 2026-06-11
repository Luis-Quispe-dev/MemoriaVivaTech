package com.upc.ss.repositories;

import com.upc.ss.entities.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long>{
    Optional<Configuracion> findByUserId(Long idUser);
}
