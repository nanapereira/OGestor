package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Empregado;
import com.mycompany.myapp.service.EmpregadoService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.EmpregadoCriteria;
import com.mycompany.myapp.service.EmpregadoQueryService;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Empregado}.
 */
@RestController
@RequestMapping("/api")
public class EmpregadoResource {

    private final Logger log = LoggerFactory.getLogger(EmpregadoResource.class);

    private static final String ENTITY_NAME = "empregado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmpregadoService empregadoService;

    private final EmpregadoQueryService empregadoQueryService;

    public EmpregadoResource(EmpregadoService empregadoService, EmpregadoQueryService empregadoQueryService) {
        this.empregadoService = empregadoService;
        this.empregadoQueryService = empregadoQueryService;
    }

    /**
     * {@code POST  /empregados} : Create a new empregado.
     *
     * @param empregado the empregado to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new empregado, or with status {@code 400 (Bad Request)} if the empregado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/empregados")
    public ResponseEntity<Empregado> createEmpregado(@RequestBody Empregado empregado) throws URISyntaxException {
        log.debug("REST request to save Empregado : {}", empregado);
        if (empregado.getId() != null) {
            throw new BadRequestAlertException("A new empregado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Empregado result = empregadoService.save(empregado);
        return ResponseEntity.created(new URI("/api/empregados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /empregados} : Updates an existing empregado.
     *
     * @param empregado the empregado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empregado,
     * or with status {@code 400 (Bad Request)} if the empregado is not valid,
     * or with status {@code 500 (Internal Server Error)} if the empregado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/empregados")
    public ResponseEntity<Empregado> updateEmpregado(@RequestBody Empregado empregado) throws URISyntaxException {
        log.debug("REST request to update Empregado : {}", empregado);
        if (empregado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Empregado result = empregadoService.save(empregado);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, empregado.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /empregados} : get all the empregados.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of empregados in body.
     */
    @GetMapping("/empregados")
    public ResponseEntity<List<Empregado>> getAllEmpregados(EmpregadoCriteria criteria) {
        log.debug("REST request to get Empregados by criteria: {}", criteria);
        List<Empregado> entityList = empregadoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /empregados/count} : count all the empregados.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/empregados/count")
    public ResponseEntity<Long> countEmpregados(EmpregadoCriteria criteria) {
        log.debug("REST request to count Empregados by criteria: {}", criteria);
        return ResponseEntity.ok().body(empregadoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /empregados/:id} : get the "id" empregado.
     *
     * @param id the id of the empregado to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the empregado, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/empregados/{id}")
    public ResponseEntity<Empregado> getEmpregado(@PathVariable Long id) {
        log.debug("REST request to get Empregado : {}", id);
        Optional<Empregado> empregado = empregadoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(empregado);
    }

    /**
     * {@code DELETE  /empregados/:id} : delete the "id" empregado.
     *
     * @param id the id of the empregado to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/empregados/{id}")
    public ResponseEntity<Void> deleteEmpregado(@PathVariable Long id) {
        log.debug("REST request to delete Empregado : {}", id);
        empregadoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
