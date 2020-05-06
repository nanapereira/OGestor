package com.mycompany.myapp.web.rest;
import com.mycompany.myapp.domain.Empregado;
import com.mycompany.myapp.repository.EmpregadoRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Empregado}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EmpregadoResource {

    private final Logger log = LoggerFactory.getLogger(EmpregadoResource.class);

    private static final String ENTITY_NAME = "empregado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmpregadoRepository empregadoRepository;

    public EmpregadoResource(EmpregadoRepository empregadoRepository) {
        this.empregadoRepository = empregadoRepository;
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
        Empregado result = empregadoRepository.save(empregado);
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
        Empregado result = empregadoRepository.save(empregado);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, empregado.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /empregados} : get all the empregados.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of empregados in body.
     */
    @GetMapping("/empregados")
    public List<Empregado> getAllEmpregados(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Empregados");
        List<Empregado> todasEmpregados = empregadoRepository.findAllWithEagerRelationships();
        Collections.sort(todasEmpregados, Comparator.comparing(Empregado::getMatricula));
        return todasEmpregados;
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
        Optional<Empregado> empregado = empregadoRepository.findOneWithEagerRelationships(id);
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
        empregadoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
