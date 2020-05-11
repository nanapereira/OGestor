package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Competencia;
import com.mycompany.myapp.service.CompetenciaService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.CompetenciaCriteria;
import com.mycompany.myapp.service.CompetenciaQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Competencia}.
 */
@RestController
@RequestMapping("/api")
public class CompetenciaResource {

    private final Logger log = LoggerFactory.getLogger(CompetenciaResource.class);

    private static final String ENTITY_NAME = "competencia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompetenciaService competenciaService;

    private final CompetenciaQueryService competenciaQueryService;

    public CompetenciaResource(CompetenciaService competenciaService, CompetenciaQueryService competenciaQueryService) {
        this.competenciaService = competenciaService;
        this.competenciaQueryService = competenciaQueryService;
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
        Competencia result = competenciaService.save(competencia);
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
        Competencia result = competenciaService.save(competencia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, competencia.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /competencias} : get all the competencias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of competencias in body.
     */
    @GetMapping("/competencias")
    public ResponseEntity<List<Competencia>> getAllCompetencias(CompetenciaCriteria criteria) {
        log.debug("REST request to get Competencias by criteria: {}", criteria);
        List<Competencia> entityList = competenciaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /competencias/count} : count all the competencias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/competencias/count")
    public ResponseEntity<Long> countCompetencias(CompetenciaCriteria criteria) {
        log.debug("REST request to count Competencias by criteria: {}", criteria);
        return ResponseEntity.ok().body(competenciaQueryService.countByCriteria(criteria));
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
        Optional<Competencia> competencia = competenciaService.findOne(id);
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
        competenciaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
