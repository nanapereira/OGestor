package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Competencia;
import com.mycompany.myapp.repository.CompetenciaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Competencia}.
 */
@Service
@Transactional
public class CompetenciaService {

    private final Logger log = LoggerFactory.getLogger(CompetenciaService.class);

    private final CompetenciaRepository competenciaRepository;

    public CompetenciaService(CompetenciaRepository competenciaRepository) {
        this.competenciaRepository = competenciaRepository;
    }

    /**
     * Save a competencia.
     *
     * @param competencia the entity to save.
     * @return the persisted entity.
     */
    public Competencia save(Competencia competencia) {
        log.debug("Request to save Competencia : {}", competencia);
        return competenciaRepository.save(competencia);
    }

    /**
     * Get all the competencias.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Competencia> findAll() {
        log.debug("Request to get all Competencias");
        return competenciaRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the competencias with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Competencia> findAllWithEagerRelationships(Pageable pageable) {
        return competenciaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one competencia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Competencia> findOne(Long id) {
        log.debug("Request to get Competencia : {}", id);
        return competenciaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the competencia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Competencia : {}", id);
        competenciaRepository.deleteById(id);
    }
}
