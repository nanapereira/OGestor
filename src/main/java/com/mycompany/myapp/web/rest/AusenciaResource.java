package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Ausencia;
import com.mycompany.myapp.repository.AusenciaRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Ausencia}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AusenciaResource {

    private final Logger log = LoggerFactory.getLogger(AusenciaResource.class);

    private static final String ENTITY_NAME = "ausencia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AusenciaRepository ausenciaRepository;

    public AusenciaResource(AusenciaRepository ausenciaRepository) {
        this.ausenciaRepository = ausenciaRepository;
    }

    /**
     * {@code POST  /ausencias} : Create a new ausencia.
     *
     * @param ausencia the ausencia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ausencia, or with status {@code 400 (Bad Request)} if the ausencia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ausencias")
    public ResponseEntity<Ausencia> createAusencia(@RequestBody Ausencia ausencia) throws URISyntaxException {
        log.debug("REST request to save Ausencia : {}", ausencia);
        if (ausencia.getId() != null) {
            throw new BadRequestAlertException("A new ausencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ausencia result = ausenciaRepository.save(ausencia);
        return ResponseEntity.created(new URI("/api/ausencias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ausencias} : Updates an existing ausencia.
     *
     * @param ausencia the ausencia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ausencia,
     * or with status {@code 400 (Bad Request)} if the ausencia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ausencia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ausencias")
    public ResponseEntity<Ausencia> updateAusencia(@RequestBody Ausencia ausencia) throws URISyntaxException {
        log.debug("REST request to update Ausencia : {}", ausencia);
        if (ausencia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ausencia result = ausenciaRepository.save(ausencia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ausencia.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ausencias} : get all the ausencias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ausencias in body.
     */
    @GetMapping("/ausencias")
    public List<Ausencia> getAllAusencias() {
        log.debug("REST request to get all Ausencias");
        return ausenciaRepository.findAll();
    }

    /**
     * {@code GET  /ausencias/:id} : get the "id" ausencia.
     *
     * @param id the id of the ausencia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ausencia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ausencias/{id}")
    public ResponseEntity<Ausencia> getAusencia(@PathVariable Long id) {
        log.debug("REST request to get Ausencia : {}", id);
        Optional<Ausencia> ausencia = ausenciaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ausencia);
    }

    /**
     * {@code DELETE  /ausencias/:id} : delete the "id" ausencia.
     *
     * @param id the id of the ausencia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ausencias/{id}")
    public ResponseEntity<Void> deleteAusencia(@PathVariable Long id) {
        log.debug("REST request to delete Ausencia : {}", id);
        ausenciaRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
