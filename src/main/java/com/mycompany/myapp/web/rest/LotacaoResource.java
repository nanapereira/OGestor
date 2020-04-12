package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Lotacao;
import com.mycompany.myapp.repository.LotacaoRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Lotacao}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LotacaoResource {

    private final Logger log = LoggerFactory.getLogger(LotacaoResource.class);

    private static final String ENTITY_NAME = "lotacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LotacaoRepository lotacaoRepository;

    public LotacaoResource(LotacaoRepository lotacaoRepository) {
        this.lotacaoRepository = lotacaoRepository;
    }

    /**
     * {@code POST  /lotacaos} : Create a new lotacao.
     *
     * @param lotacao the lotacao to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lotacao, or with status {@code 400 (Bad Request)} if the lotacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lotacaos")
    public ResponseEntity<Lotacao> createLotacao(@RequestBody Lotacao lotacao) throws URISyntaxException {
        log.debug("REST request to save Lotacao : {}", lotacao);
        if (lotacao.getId() != null) {
            throw new BadRequestAlertException("A new lotacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Lotacao result = lotacaoRepository.save(lotacao);
        return ResponseEntity.created(new URI("/api/lotacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lotacaos} : Updates an existing lotacao.
     *
     * @param lotacao the lotacao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lotacao,
     * or with status {@code 400 (Bad Request)} if the lotacao is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lotacao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lotacaos")
    public ResponseEntity<Lotacao> updateLotacao(@RequestBody Lotacao lotacao) throws URISyntaxException {
        log.debug("REST request to update Lotacao : {}", lotacao);
        if (lotacao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Lotacao result = lotacaoRepository.save(lotacao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lotacao.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /lotacaos} : get all the lotacaos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lotacaos in body.
     */
    @GetMapping("/lotacaos")
    public List<Lotacao> getAllLotacaos() {
        log.debug("REST request to get all Lotacaos");
        return lotacaoRepository.findAll();
    }

    /**
     * {@code GET  /lotacaos/:id} : get the "id" lotacao.
     *
     * @param id the id of the lotacao to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lotacao, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lotacaos/{id}")
    public ResponseEntity<Lotacao> getLotacao(@PathVariable Long id) {
        log.debug("REST request to get Lotacao : {}", id);
        Optional<Lotacao> lotacao = lotacaoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lotacao);
    }

    /**
     * {@code DELETE  /lotacaos/:id} : delete the "id" lotacao.
     *
     * @param id the id of the lotacao to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lotacaos/{id}")
    public ResponseEntity<Void> deleteLotacao(@PathVariable Long id) {
        log.debug("REST request to delete Lotacao : {}", id);
        lotacaoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
