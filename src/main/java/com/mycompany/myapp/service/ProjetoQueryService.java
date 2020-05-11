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

import com.mycompany.myapp.domain.Projeto;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.ProjetoRepository;
import com.mycompany.myapp.service.dto.ProjetoCriteria;

/**
 * Service for executing complex queries for {@link Projeto} entities in the database.
 * The main input is a {@link ProjetoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Projeto} or a {@link Page} of {@link Projeto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProjetoQueryService extends QueryService<Projeto> {

    private final Logger log = LoggerFactory.getLogger(ProjetoQueryService.class);

    private final ProjetoRepository projetoRepository;

    public ProjetoQueryService(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    /**
     * Return a {@link List} of {@link Projeto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Projeto> findByCriteria(ProjetoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Projeto> specification = createSpecification(criteria);
        return projetoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Projeto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Projeto> findByCriteria(ProjetoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Projeto> specification = createSpecification(criteria);
        return projetoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProjetoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Projeto> specification = createSpecification(criteria);
        return projetoRepository.count(specification);
    }

    /**
     * Function to convert {@link ProjetoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Projeto> createSpecification(ProjetoCriteria criteria) {
        Specification<Projeto> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Projeto_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Projeto_.nome));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Projeto_.descricao));
            }
            if (criteria.getDataInicio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataInicio(), Projeto_.dataInicio));
            }
            if (criteria.getDataFim() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataFim(), Projeto_.dataFim));
            }
            if (criteria.getGestorId() != null) {
                specification = specification.and(buildSpecification(criteria.getGestorId(),
                    root -> root.join(Projeto_.gestor, JoinType.LEFT).get(Empregado_.id)));
            }
            if (criteria.getEmpregadosId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmpregadosId(),
                    root -> root.join(Projeto_.empregados, JoinType.LEFT).get(Empregado_.id)));
            }
            if (criteria.getListaempregadoId() != null) {
                specification = specification.and(buildSpecification(criteria.getListaempregadoId(),
                    root -> root.join(Projeto_.listaempregados, JoinType.LEFT).get(Empregado_.id)));
            }
        }
        return specification;
    }
}
