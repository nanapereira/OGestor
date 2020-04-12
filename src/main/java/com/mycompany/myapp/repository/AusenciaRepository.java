package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ausencia;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Ausencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AusenciaRepository extends JpaRepository<Ausencia, Long> {
}
