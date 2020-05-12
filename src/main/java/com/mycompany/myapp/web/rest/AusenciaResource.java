package com.mycompany.myapp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.myapp.domain.Ausencia;
import com.mycompany.myapp.repository.AusenciaRepository;
import com.mycompany.myapp.service.AusenciaQueryService;
import com.mycompany.myapp.service.AusenciaService;
import com.mycompany.myapp.service.dto.AusenciaCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Ausencia}.
 */
@RestController
@RequestMapping("/api")
public class AusenciaResource {

	private final Logger log = LoggerFactory.getLogger(AusenciaResource.class);

	private static final String ENTITY_NAME = "ausencia";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final AusenciaRepository ausenciaRepository;

	@Autowired
	public AusenciaService ausenciaService;

	private final AusenciaQueryService ausenciaQueryService;

	public AusenciaResource(AusenciaService ausenciaService, AusenciaQueryService ausenciaQueryService,
			AusenciaRepository ausenciaRepository) {
		this.ausenciaService = ausenciaService;
		this.ausenciaQueryService = ausenciaQueryService;
		this.ausenciaRepository = ausenciaRepository;
	}

	/**
	 * {@code POST  /ausencias} : Create a new ausencia.
	 *
	 * @param ausencia the ausencia to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new ausencia, or with status {@code 400 (Bad Request)} if
	 *         the ausencia has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/ausencias")
	public ResponseEntity<Ausencia> createAusencia(@RequestBody Ausencia ausencia) throws URISyntaxException {
		log.debug("REST request to save Ausencia : {}", ausencia);
		if (ausencia.getId() != null) {
			throw new BadRequestAlertException("A new ausencia cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Ausencia result = ausenciaService.save(ausencia);
		return ResponseEntity
				.created(new URI("/api/ausencias/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /ausencias} : Updates an existing ausencia.
	 *
	 * @param ausencia the ausencia to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated ausencia, or with status {@code 400 (Bad Request)} if the
	 *         ausencia is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the ausencia couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/ausencias")
	public ResponseEntity<Ausencia> updateAusencia(@RequestBody Ausencia ausencia) throws URISyntaxException {
		log.debug("REST request to update Ausencia : {}", ausencia);
		if (ausencia.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		Ausencia result = ausenciaService.save(ausencia);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ausencia.getId().toString()))
				.body(result);
	}

	/**
	 * {@code GET  /ausencias} : get all the ausencias.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of ausencias in body.
	 */
	/*
	 * @GetMapping("/ausencias") public ResponseEntity<List<Ausencia>>
	 * getAllAusencias(AusenciaCriteria criteria) {
	 * log.debug("REST request to get Ausencias by criteria: {}", criteria);
	 * List<Ausencia> entityList = ausenciaQueryService.findByCriteria(criteria);
	 * return ResponseEntity.ok().body(entityList); }
	 */

	@GetMapping("/ausencias")
	public List<Ausencia> getAllAusencias() {
		log.debug("REST request to get all Ausencias");
		log.debug("REST request to get all Ausencias");
		List<Ausencia> todasAusencias = ausenciaService.findAllAusenciaWithEmpregadoProjetos();
		Collections.sort(todasAusencias, Comparator.comparing(Ausencia::getDataInicio));
		return todasAusencias;
	}

	/**
	 * {@code GET  /ausencias/projeto/:idProjeto} : get all the ausencias filtered
	 * by projeto.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of ausencias in body.
	 */
	@GetMapping("/ausencias/projeto/{idProjeto}")
	public List<Ausencia> getAllAusenciasByProjeto(@PathVariable Long idProjeto) {
		log.debug("REST request to get all Ausencias by projeto");
		List<Ausencia> todasAusencias = ausenciaService.findAllAusenciaWithEmpregadoProjetosByProjeto(idProjeto);
		Collections.sort(todasAusencias, Comparator.comparing(Ausencia::getDataInicio));
		return todasAusencias;
	}

	/**
	 * {@code GET  /ausencias/count} : count all the ausencias.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/ausencias/count")
	public ResponseEntity<Long> countAusencias(AusenciaCriteria criteria) {
		log.debug("REST request to count Ausencias by criteria: {}", criteria);
		return ResponseEntity.ok().body(ausenciaQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /ausencias/:id} : get the "id" ausencia.
	 *
	 * @param id the id of the ausencia to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the ausencia, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/ausencias/{id}")
	public ResponseEntity<Ausencia> getAusencia(@PathVariable Long id) {
		log.debug("REST request to get Ausencia : {}", id);
		Optional<Ausencia> ausencia = ausenciaService.findOne(id);
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
		ausenciaService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
