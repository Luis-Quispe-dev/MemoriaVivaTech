package com.upc.ss.repositories;

import com.upc.ss.entities.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByAsignacionIdAsignacionOrderByFechaHoraAsc(Long idAsignacion);
    List<Mensaje> findByAsignacionIdAsignacionAndLeidoFalse(Long idAsignacion);
}
