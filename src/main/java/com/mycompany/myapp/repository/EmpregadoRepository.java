package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Empregado;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Empregado entity.
 */
@Repository
public interface EmpregadoRepository extends JpaRepository<Empregado, Long>, JpaSpecificationExecutor<Empregado> {

    @Query(value = "select distinct empregado from Empregado empregado left join fetch empregado.competencias left join fetch empregado.projetos",
        countQuery = "select count(distinct empregado) from Empregado empregado")
    Page<Empregado> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct empregado from Empregado empregado left join fetch empregado.competencias left join fetch empregado.projetos")
    List<Empregado> findAllWithEagerRelationships();

    @Query("select empregado from Empregado empregado left join fetch empregado.competencias left join fetch empregado.projetos where empregado.id =:id")
    Optional<Empregado> findOneWithEagerRelationships(@Param("id") Long id);
}
