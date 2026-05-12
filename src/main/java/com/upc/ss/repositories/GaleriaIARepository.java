package com.upc.ss.repositories;

import com.upc.ss.entities.GaleriaIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GaleriaIARepository extends JpaRepository<GaleriaIA, Long> {
    List<GaleriaIA> findByAdultoMayorIdAdultoMayorOrderByFechaCreacionDesc(Long adultoMayorIdAdultoMayor);
}
