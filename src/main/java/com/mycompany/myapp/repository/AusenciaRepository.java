package com.mycompany.myapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.Ausencia;
import com.mycompany.myapp.domain.Empregado;

/**
 * Spring Data  repository for the Ausencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AusenciaRepository extends JpaRepository<Ausencia, Long>, JpaSpecificationExecutor<Ausencia> {
	@Query("select distinct ausencia from Ausencia ausencia left join fetch ausencia.empregado e left join fetch e.projetos")
    List<Ausencia> findAllWithEagerRelationships();

	@Query("select distinct ausencia from Ausencia ausencia left join fetch ausencia.empregado e left join fetch e.projetos p where p.id =:idProjeto")
    List<Ausencia> findAllWithEagerRelationshipsByIdProjeto(@Param("idProjeto") Long idProjeto);
}
