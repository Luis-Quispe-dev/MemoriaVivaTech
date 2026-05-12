package com.upc.ss.repositories;

import com.upc.ss.entities.Solicitud;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    Optional<Solicitud> findByAdultoMayorIdAdultoMayorAndCuidadorIdCuidadorAndEstado(
            Long idAdultoMayor, Long idCuidador, Solicitud.Estado estado);

    List<Solicitud> findByCuidadorIdCuidadorAndEstado(
            Long idCuidador, Solicitud.Estado estado);

    List<Solicitud> findByAdultoMayorIdAdultoMayorAndEstado(
            Long idAdultoMayor, Solicitud.Estado estado);
}
