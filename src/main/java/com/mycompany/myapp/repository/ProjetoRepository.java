package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Projeto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Projeto entity.
 */
@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    @Query(value = "select distinct projeto from Projeto projeto left join fetch projeto.empregados",
        countQuery = "select count(distinct projeto) from Projeto projeto")
    Page<Projeto> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct projeto from Projeto projeto left join fetch projeto.empregados")
    List<Projeto> findAllWithEagerRelationships();

    @Query("select projeto from Projeto projeto left join fetch projeto.empregados where projeto.id =:id")
    Optional<Projeto> findOneWithEagerRelationships(@Param("id") Long id);
}
