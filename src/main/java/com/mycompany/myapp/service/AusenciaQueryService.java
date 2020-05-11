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

import com.mycompany.myapp.domain.Ausencia;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.AusenciaRepository;
import com.mycompany.myapp.service.dto.AusenciaCriteria;

/**
 * Service for executing complex queries for {@link Ausencia} entities in the database.
 * The main input is a {@link AusenciaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Ausencia} or a {@link Page} of {@link Ausencia} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AusenciaQueryService extends QueryService<Ausencia> {

    private final Logger log = LoggerFactory.getLogger(AusenciaQueryService.class);

    private final AusenciaRepository ausenciaRepository;

    public AusenciaQueryService(AusenciaRepository ausenciaRepository) {
        this.ausenciaRepository = ausenciaRepository;
    }

    /**
     * Return a {@link List} of {@link Ausencia} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Ausencia> findByCriteria(AusenciaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ausencia> specification = createSpecification(criteria);
        return ausenciaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Ausencia} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Ausencia> findByCriteria(AusenciaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ausencia> specification = createSpecification(criteria);
        return ausenciaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AusenciaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ausencia> specification = createSpecification(criteria);
        return ausenciaRepository.count(specification);
    }

    /**
     * Function to convert {@link AusenciaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ausencia> createSpecification(AusenciaCriteria criteria) {
        Specification<Ausencia> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ausencia_.id));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipo(), Ausencia_.tipo));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Ausencia_.descricao));
            }
            if (criteria.getDataInicio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataInicio(), Ausencia_.dataInicio));
            }
            if (criteria.getDataFim() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataFim(), Ausencia_.dataFim));
            }
            if (criteria.getEmpregadoId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmpregadoId(),
                    root -> root.join(Ausencia_.empregado, JoinType.LEFT).get(Empregado_.id)));
            }
        }
        return specification;
    }
}
