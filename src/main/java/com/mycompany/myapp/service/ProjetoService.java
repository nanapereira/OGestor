package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Projeto;
import com.mycompany.myapp.repository.ProjetoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Projeto}.
 */
@Service
@Transactional
public class ProjetoService {

    private final Logger log = LoggerFactory.getLogger(ProjetoService.class);

    private final ProjetoRepository projetoRepository;

    public ProjetoService(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    /**
     * Save a projeto.
     *
     * @param projeto the entity to save.
     * @return the persisted entity.
     */
    public Projeto save(Projeto projeto) {
        log.debug("Request to save Projeto : {}", projeto);
        return projetoRepository.save(projeto);
    }

    /**
     * Get all the projetos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Projeto> findAll() {
        log.debug("Request to get all Projetos");
        return projetoRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the projetos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Projeto> findAllWithEagerRelationships(Pageable pageable) {
        return projetoRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one projeto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Projeto> findOne(Long id) {
        log.debug("Request to get Projeto : {}", id);
        return projetoRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the projeto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Projeto : {}", id);
        projetoRepository.deleteById(id);
    }
}
