package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Competencia;
import com.mycompany.myapp.repository.CompetenciaRepository;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Competencia}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CompetenciaResource {

    private final Logger log = LoggerFactory.getLogger(CompetenciaResource.class);

    private static final String ENTITY_NAME = "competencia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompetenciaRepository competenciaRepository;

    public CompetenciaResource(CompetenciaRepository competenciaRepository) {
        this.competenciaRepository = competenciaRepository;
    }

    /**
     * {@code POST  /competencias} : Create a new competencia.
     *
     * @param competencia the competencia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new competencia, or with status {@code 400 (Bad Request)} if the competencia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/competencias")
    public ResponseEntity<Competencia> createCompetencia(@RequestBody Competencia competencia) throws URISyntaxException {
        log.debug("REST request to save Competencia : {}", competencia);
        if (competencia.getId() != null) {
            throw new BadRequestAlertException("A new competencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Competencia result = competenciaRepository.save(competencia);
        return ResponseEntity.created(new URI("/api/competencias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /competencias} : Updates an existing competencia.
     *
     * @param competencia the competencia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated competencia,
     * or with status {@code 400 (Bad Request)} if the competencia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the competencia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/competencias")
    public ResponseEntity<Competencia> updateCompetencia(@RequestBody Competencia competencia) throws URISyntaxException {
        log.debug("REST request to update Competencia : {}", competencia);
        if (competencia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Competencia result = competenciaRepository.save(competencia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, competencia.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /competencias} : get all the competencias.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of competencias in body.
     */
    @GetMapping("/competencias")
    public List<Competencia> getAllCompetencias(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Competencias");
        List<Competencia> todasCompetencias = competenciaRepository.findAllWithEagerRelationships();
        Collections.sort(todasCompetencias, Comparator.comparing(Competencia::getCodigo));
        return todasCompetencias;
    }

    /**
     * {@code GET  /competencias/:id} : get the "id" competencia.
     *
     * @param id the id of the competencia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the competencia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/competencias/{id}")
    public ResponseEntity<Competencia> getCompetencia(@PathVariable Long id) {
        log.debug("REST request to get Competencia : {}", id);
        Optional<Competencia> competencia = competenciaRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(competencia);
    }

    /**
     * {@code DELETE  /competencias/:id} : delete the "id" competencia.
     *
     * @param id the id of the competencia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/competencias/{id}")
    public ResponseEntity<Void> deleteCompetencia(@PathVariable Long id) {
        log.debug("REST request to delete Competencia : {}", id);
        competenciaRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
