package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Ausencia;
import com.mycompany.myapp.repository.AusenciaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Ausencia}.
 */
@Service
@Transactional
public class AusenciaService {

    private final Logger log = LoggerFactory.getLogger(AusenciaService.class);

    private final AusenciaRepository ausenciaRepository;

    public AusenciaService(AusenciaRepository ausenciaRepository) {
        this.ausenciaRepository = ausenciaRepository;
    }

    /**
     * Save a ausencia.
     *
     * @param ausencia the entity to save.
     * @return the persisted entity.
     */
    public Ausencia save(Ausencia ausencia) {
        log.debug("Request to save Ausencia : {}", ausencia);
        return ausenciaRepository.save(ausencia);
    }

    /**
     * Get all the ausencias.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Ausencia> findAll() {
        log.debug("Request to get all Ausencias");
        return ausenciaRepository.findAll();
    }

    /**
     * Get one ausencia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Ausencia> findOne(Long id) {
        log.debug("Request to get Ausencia : {}", id);
        return ausenciaRepository.findById(id);
    }

    /**
     * Delete the ausencia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ausencia : {}", id);
        ausenciaRepository.deleteById(id);
    }
}
