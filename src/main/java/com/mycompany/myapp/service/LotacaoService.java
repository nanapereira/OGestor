package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Lotacao;
import com.mycompany.myapp.repository.LotacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Lotacao}.
 */
@Service
@Transactional
public class LotacaoService {

    private final Logger log = LoggerFactory.getLogger(LotacaoService.class);

    private final LotacaoRepository lotacaoRepository;

    public LotacaoService(LotacaoRepository lotacaoRepository) {
        this.lotacaoRepository = lotacaoRepository;
    }

    /**
     * Save a lotacao.
     *
     * @param lotacao the entity to save.
     * @return the persisted entity.
     */
    public Lotacao save(Lotacao lotacao) {
        log.debug("Request to save Lotacao : {}", lotacao);
        return lotacaoRepository.save(lotacao);
    }

    /**
     * Get all the lotacaos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Lotacao> findAll() {
        log.debug("Request to get all Lotacaos");
        return lotacaoRepository.findAll();
    }

    /**
     * Get one lotacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Lotacao> findOne(Long id) {
        log.debug("Request to get Lotacao : {}", id);
        return lotacaoRepository.findById(id);
    }

    /**
     * Delete the lotacao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Lotacao : {}", id);
        lotacaoRepository.deleteById(id);
    }
}
