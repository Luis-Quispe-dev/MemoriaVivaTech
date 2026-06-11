package com.upc.ss.repositories;

import com.upc.ss.entities.AdultoMayor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdultoMayorRepository extends JpaRepository<AdultoMayor, Long> {
    Optional<AdultoMayor> findByUserEmail(String email);
}
