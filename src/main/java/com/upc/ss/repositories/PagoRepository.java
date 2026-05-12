package com.upc.ss.repositories;

import com.upc.ss.entities.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByAdultoMayorIdAdultoMayorOrderByFechaPagoDesc(Long idAdultoMayor);
}
