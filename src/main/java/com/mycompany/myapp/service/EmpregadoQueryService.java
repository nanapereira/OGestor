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

import com.mycompany.myapp.domain.Empregado;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.EmpregadoRepository;
import com.mycompany.myapp.service.dto.EmpregadoCriteria;

/**
 * Service for executing complex queries for {@link Empregado} entities in the database.
 * The main input is a {@link EmpregadoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Empregado} or a {@link Page} of {@link Empregado} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmpregadoQueryService extends QueryService<Empregado> {

    private final Logger log = LoggerFactory.getLogger(EmpregadoQueryService.class);

    private final EmpregadoRepository empregadoRepository;

    public EmpregadoQueryService(EmpregadoRepository empregadoRepository) {
        this.empregadoRepository = empregadoRepository;
    }

    /**
     * Return a {@link List} of {@link Empregado} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Empregado> findByCriteria(EmpregadoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Empregado> specification = createSpecification(criteria);
        return empregadoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Empregado} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Empregado> findByCriteria(EmpregadoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Empregado> specification = createSpecification(criteria);
        return empregadoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmpregadoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Empregado> specification = createSpecification(criteria);
        return empregadoRepository.count(specification);
    }

    /**
     * Function to convert {@link EmpregadoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Empregado> createSpecification(EmpregadoCriteria criteria) {
        Specification<Empregado> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Empregado_.id));
            }
            if (criteria.getMatricula() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMatricula(), Empregado_.matricula));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Empregado_.nome));
            }
            if (criteria.getCpf() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCpf(), Empregado_.cpf));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Empregado_.email));
            }
            if (criteria.getTelefone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefone(), Empregado_.telefone));
            }
            if (criteria.getRamal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRamal(), Empregado_.ramal));
            }
            if (criteria.getLotacaoId() != null) {
                specification = specification.and(buildSpecification(criteria.getLotacaoId(),
                    root -> root.join(Empregado_.lotacao, JoinType.LEFT).get(Lotacao_.id)));
            }
            if (criteria.getCompetenciasId() != null) {
                specification = specification.and(buildSpecification(criteria.getCompetenciasId(),
                    root -> root.join(Empregado_.competencias, JoinType.LEFT).get(Competencia_.id)));
            }
            if (criteria.getProjetosId() != null) {
                specification = specification.and(buildSpecification(criteria.getProjetosId(),
                    root -> root.join(Empregado_.projetos, JoinType.LEFT).get(Projeto_.id)));
            }
            if (criteria.getListaCompetenciasId() != null) {
                specification = specification.and(buildSpecification(criteria.getListaCompetenciasId(),
                    root -> root.join(Empregado_.listaCompetencias, JoinType.LEFT).get(Competencia_.id)));
            }
            if (criteria.getListaProjetosId() != null) {
                specification = specification.and(buildSpecification(criteria.getListaProjetosId(),
                    root -> root.join(Empregado_.listaProjetos, JoinType.LEFT).get(Projeto_.id)));
            }
        }
        return specification;
    }
}
