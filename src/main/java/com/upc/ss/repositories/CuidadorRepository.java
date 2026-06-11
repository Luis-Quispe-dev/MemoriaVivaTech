package com.upc.ss.repositories;

import com.upc.ss.entities.Cuidador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuidadorRepository extends JpaRepository<Cuidador, Long> {
    Optional<Cuidador> findByUserEmail(String email);
}
