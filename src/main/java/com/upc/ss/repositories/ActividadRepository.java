package com.upc.ss.repositories;

import com.upc.ss.entities.ActividadCalendario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<ActividadCalendario, Long> {
    List<ActividadCalendario> findByAsignacionIdAsignacionOrderByFechaHoraInicioAsc(Long idAsignacion);
    List<ActividadCalendario> findByAsignacionIdAsignacionAndEstado(Long idAsignacion, ActividadCalendario.Estado estado);
}
