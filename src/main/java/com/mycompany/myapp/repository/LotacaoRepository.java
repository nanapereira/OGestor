package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lotacao;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Lotacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LotacaoRepository extends JpaRepository<Lotacao, Long>, JpaSpecificationExecutor<Lotacao> {
}
