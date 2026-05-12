package com.upc.ss.repositories;

import com.upc.ss.entities.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findByNombrePlan(String nombrePlan);
}
