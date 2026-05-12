package com.upc.ss.repositories;

import com.upc.ss.entities.Recuerdo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecuerdoRepository extends JpaRepository<Recuerdo, Long> {
    List<Recuerdo> findByAdultoMayorIdAdultoMayorOrderByFechaCreacionDesc(Long idAdultoMayor);
    List<Recuerdo> findByAdultoMayorIdAdultoMayorAndTipoRecuerdoOrderByFechaCreacionDesc(Long idAdultoMayor, Recuerdo.TipoRecuerdo tipoRecuerdo);
    List<Recuerdo> findByAdultoMayorIdAdultoMayorAndFavoritoTrueOrderByFechaCreacionDesc(Long idAdultoMayor);
}
