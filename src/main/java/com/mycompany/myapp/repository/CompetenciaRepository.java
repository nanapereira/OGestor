package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Competencia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Competencia entity.
 */
@Repository
public interface CompetenciaRepository extends JpaRepository<Competencia, Long> {

    @Query(value = "select distinct competencia from Competencia competencia left join fetch competencia.empregados",
        countQuery = "select count(distinct competencia) from Competencia competencia")
    Page<Competencia> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct competencia from Competencia competencia left join fetch competencia.empregados")
    List<Competencia> findAllWithEagerRelationships();

    @Query("select competencia from Competencia competencia left join fetch competencia.empregados where competencia.id =:id")
    Optional<Competencia> findOneWithEagerRelationships(@Param("id") Long id);
}
