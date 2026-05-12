package com.upc.ss.repositories;

import com.upc.ss.entities.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {
    Optional<Suscripcion> findByAdultoMayorIdAdultoMayorAndEstadoTrue(Long idAdultoMayor);
}
