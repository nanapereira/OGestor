package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Empregado;
import com.mycompany.myapp.repository.EmpregadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Empregado}.
 */
@Service
@Transactional
public class EmpregadoService {

    private final Logger log = LoggerFactory.getLogger(EmpregadoService.class);

    private final EmpregadoRepository empregadoRepository;

    public EmpregadoService(EmpregadoRepository empregadoRepository) {
        this.empregadoRepository = empregadoRepository;
    }

    /**
     * Save a empregado.
     *
     * @param empregado the entity to save.
     * @return the persisted entity.
     */
    public Empregado save(Empregado empregado) {
        log.debug("Request to save Empregado : {}", empregado);
        return empregadoRepository.save(empregado);
    }

    /**
     * Get all the empregados.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Empregado> findAll() {
        log.debug("Request to get all Empregados");
        return empregadoRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the empregados with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Empregado> findAllWithEagerRelationships(Pageable pageable) {
        return empregadoRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one empregado by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Empregado> findOne(Long id) {
        log.debug("Request to get Empregado : {}", id);
        return empregadoRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the empregado by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Empregado : {}", id);
        empregadoRepository.deleteById(id);
    }
}
