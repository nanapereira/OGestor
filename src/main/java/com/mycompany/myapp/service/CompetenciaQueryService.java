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

import com.mycompany.myapp.domain.Competencia;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.CompetenciaRepository;
import com.mycompany.myapp.service.dto.CompetenciaCriteria;

/**
 * Service for executing complex queries for {@link Competencia} entities in the database.
 * The main input is a {@link CompetenciaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Competencia} or a {@link Page} of {@link Competencia} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompetenciaQueryService extends QueryService<Competencia> {

    private final Logger log = LoggerFactory.getLogger(CompetenciaQueryService.class);

    private final CompetenciaRepository competenciaRepository;

    public CompetenciaQueryService(CompetenciaRepository competenciaRepository) {
        this.competenciaRepository = competenciaRepository;
    }

    /**
     * Return a {@link List} of {@link Competencia} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Competencia> findByCriteria(CompetenciaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Competencia> specification = createSpecification(criteria);
        return competenciaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Competencia} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Competencia> findByCriteria(CompetenciaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Competencia> specification = createSpecification(criteria);
        return competenciaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompetenciaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Competencia> specification = createSpecification(criteria);
        return competenciaRepository.count(specification);
    }

    /**
     * Function to convert {@link CompetenciaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Competencia> createSpecification(CompetenciaCriteria criteria) {
        Specification<Competencia> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Competencia_.id));
            }
            if (criteria.getCodigo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCodigo(), Competencia_.codigo));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Competencia_.nome));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Competencia_.descricao));
            }
            if (criteria.getEmpregadosId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmpregadosId(),
                    root -> root.join(Competencia_.empregados, JoinType.LEFT).get(Empregado_.id)));
            }
            if (criteria.getListaEmpregadosId() != null) {
                specification = specification.and(buildSpecification(criteria.getListaEmpregadosId(),
                    root -> root.join(Competencia_.listaEmpregados, JoinType.LEFT).get(Empregado_.id)));
            }
        }
        return specification;
    }
}
