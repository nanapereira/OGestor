package com.mycompany.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.Lotacao;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.LotacaoRepository;
import com.mycompany.myapp.service.dto.LotacaoCriteria;

/**
 * Service for executing complex queries for {@link Lotacao} entities in the database.
 * The main input is a {@link LotacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Lotacao} or a {@link Page} of {@link Lotacao} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LotacaoQueryService extends QueryService<Lotacao> {

    private final Logger log = LoggerFactory.getLogger(LotacaoQueryService.class);

    private final LotacaoRepository lotacaoRepository;

    public LotacaoQueryService(LotacaoRepository lotacaoRepository) {
        this.lotacaoRepository = lotacaoRepository;
    }

    /**
     * Return a {@link List} of {@link Lotacao} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Lotacao> findByCriteria(LotacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Lotacao> specification = createSpecification(criteria);
        return lotacaoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Lotacao} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Lotacao> findByCriteria(LotacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Lotacao> specification = createSpecification(criteria);
        return lotacaoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LotacaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Lotacao> specification = createSpecification(criteria);
        return lotacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link LotacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Lotacao> createSpecification(LotacaoCriteria criteria) {
        Specification<Lotacao> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Lotacao_.id));
            }
            if (criteria.getCodigo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCodigo(), Lotacao_.codigo));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Lotacao_.nome));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Lotacao_.descricao));
            }
        }
        return specification;
    }
}
