package com.upc.ss.repositories;

import com.upc.ss.entities.Asignacion;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    Optional<Asignacion> findByAdultoMayorIdAdultoMayorAndEstado(Long idAdultoMayor, Asignacion.Estado estado);
    List<Asignacion> findByCuidadorIdCuidadorAndEstado(Long idCuidador, Asignacion.Estado estado);
}
