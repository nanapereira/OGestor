package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OGestorApp;
import com.mycompany.myapp.domain.Ausencia;
import com.mycompany.myapp.domain.Empregado;
import com.mycompany.myapp.repository.AusenciaRepository;
import com.mycompany.myapp.service.AusenciaService;
import com.mycompany.myapp.service.dto.AusenciaCriteria;
import com.mycompany.myapp.service.AusenciaQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.TipoAusencia;
/**
 * Integration tests for the {@link AusenciaResource} REST controller.
 */
@SpringBootTest(classes = OGestorApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class AusenciaResourceIT {

    private static final TipoAusencia DEFAULT_TIPO = TipoAusencia.ABONO;
    private static final TipoAusencia UPDATED_TIPO = TipoAusencia.FERIAS;

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_INICIO = "AAAAAAAAAA";
    private static final String UPDATED_DATA_INICIO = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_FIM = "AAAAAAAAAA";
    private static final String UPDATED_DATA_FIM = "BBBBBBBBBB";

    @Autowired
    private AusenciaRepository ausenciaRepository;

    @Autowired
    private AusenciaService ausenciaService;

    @Autowired
    private AusenciaQueryService ausenciaQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAusenciaMockMvc;

    private Ausencia ausencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ausencia createEntity(EntityManager em) {
        Ausencia ausencia = new Ausencia()
            .tipo(DEFAULT_TIPO)
            .descricao(DEFAULT_DESCRICAO)
            .dataInicio(DEFAULT_DATA_INICIO)
            .dataFim(DEFAULT_DATA_FIM);
        return ausencia;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ausencia createUpdatedEntity(EntityManager em) {
        Ausencia ausencia = new Ausencia()
            .tipo(UPDATED_TIPO)
            .descricao(UPDATED_DESCRICAO)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM);
        return ausencia;
    }

    @BeforeEach
    public void initTest() {
        ausencia = createEntity(em);
    }

    @Test
    @Transactional
    public void createAusencia() throws Exception {
        int databaseSizeBeforeCreate = ausenciaRepository.findAll().size();

        // Create the Ausencia
        restAusenciaMockMvc.perform(post("/api/ausencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ausencia)))
            .andExpect(status().isCreated());

        // Validate the Ausencia in the database
        List<Ausencia> ausenciaList = ausenciaRepository.findAll();
        assertThat(ausenciaList).hasSize(databaseSizeBeforeCreate + 1);
        Ausencia testAusencia = ausenciaList.get(ausenciaList.size() - 1);
        assertThat(testAusencia.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testAusencia.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testAusencia.getDataInicio()).isEqualTo(DEFAULT_DATA_INICIO);
        assertThat(testAusencia.getDataFim()).isEqualTo(DEFAULT_DATA_FIM);
    }

    @Test
    @Transactional
    public void createAusenciaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ausenciaRepository.findAll().size();

        // Create the Ausencia with an existing ID
        ausencia.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAusenciaMockMvc.perform(post("/api/ausencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ausencia)))
            .andExpect(status().isBadRequest());

        // Validate the Ausencia in the database
        List<Ausencia> ausenciaList = ausenciaRepository.findAll();
        assertThat(ausenciaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAusencias() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList
        restAusenciaMockMvc.perform(get("/api/ausencias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ausencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO)))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM)));
    }
    
    @Test
    @Transactional
    public void getAusencia() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get the ausencia
        restAusenciaMockMvc.perform(get("/api/ausencias/{id}", ausencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ausencia.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.dataInicio").value(DEFAULT_DATA_INICIO))
            .andExpect(jsonPath("$.dataFim").value(DEFAULT_DATA_FIM));
    }


    @Test
    @Transactional
    public void getAusenciasByIdFiltering() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        Long id = ausencia.getId();

        defaultAusenciaShouldBeFound("id.equals=" + id);
        defaultAusenciaShouldNotBeFound("id.notEquals=" + id);

        defaultAusenciaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAusenciaShouldNotBeFound("id.greaterThan=" + id);

        defaultAusenciaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAusenciaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAusenciasByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where tipo equals to DEFAULT_TIPO
        defaultAusenciaShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the ausenciaList where tipo equals to UPDATED_TIPO
        defaultAusenciaShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllAusenciasByTipoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where tipo not equals to DEFAULT_TIPO
        defaultAusenciaShouldNotBeFound("tipo.notEquals=" + DEFAULT_TIPO);

        // Get all the ausenciaList where tipo not equals to UPDATED_TIPO
        defaultAusenciaShouldBeFound("tipo.notEquals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllAusenciasByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultAusenciaShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the ausenciaList where tipo equals to UPDATED_TIPO
        defaultAusenciaShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllAusenciasByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where tipo is not null
        defaultAusenciaShouldBeFound("tipo.specified=true");

        // Get all the ausenciaList where tipo is null
        defaultAusenciaShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    public void getAllAusenciasByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where descricao equals to DEFAULT_DESCRICAO
        defaultAusenciaShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the ausenciaList where descricao equals to UPDATED_DESCRICAO
        defaultAusenciaShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllAusenciasByDescricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where descricao not equals to DEFAULT_DESCRICAO
        defaultAusenciaShouldNotBeFound("descricao.notEquals=" + DEFAULT_DESCRICAO);

        // Get all the ausenciaList where descricao not equals to UPDATED_DESCRICAO
        defaultAusenciaShouldBeFound("descricao.notEquals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllAusenciasByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultAusenciaShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the ausenciaList where descricao equals to UPDATED_DESCRICAO
        defaultAusenciaShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllAusenciasByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where descricao is not null
        defaultAusenciaShouldBeFound("descricao.specified=true");

        // Get all the ausenciaList where descricao is null
        defaultAusenciaShouldNotBeFound("descricao.specified=false");
    }
                @Test
    @Transactional
    public void getAllAusenciasByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where descricao contains DEFAULT_DESCRICAO
        defaultAusenciaShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the ausenciaList where descricao contains UPDATED_DESCRICAO
        defaultAusenciaShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllAusenciasByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where descricao does not contain DEFAULT_DESCRICAO
        defaultAusenciaShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the ausenciaList where descricao does not contain UPDATED_DESCRICAO
        defaultAusenciaShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }


    @Test
    @Transactional
    public void getAllAusenciasByDataInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where dataInicio equals to DEFAULT_DATA_INICIO
        defaultAusenciaShouldBeFound("dataInicio.equals=" + DEFAULT_DATA_INICIO);

        // Get all the ausenciaList where dataInicio equals to UPDATED_DATA_INICIO
        defaultAusenciaShouldNotBeFound("dataInicio.equals=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    public void getAllAusenciasByDataInicioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where dataInicio not equals to DEFAULT_DATA_INICIO
        defaultAusenciaShouldNotBeFound("dataInicio.notEquals=" + DEFAULT_DATA_INICIO);

        // Get all the ausenciaList where dataInicio not equals to UPDATED_DATA_INICIO
        defaultAusenciaShouldBeFound("dataInicio.notEquals=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    public void getAllAusenciasByDataInicioIsInShouldWork() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where dataInicio in DEFAULT_DATA_INICIO or UPDATED_DATA_INICIO
        defaultAusenciaShouldBeFound("dataInicio.in=" + DEFAULT_DATA_INICIO + "," + UPDATED_DATA_INICIO);

        // Get all the ausenciaList where dataInicio equals to UPDATED_DATA_INICIO
        defaultAusenciaShouldNotBeFound("dataInicio.in=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    public void getAllAusenciasByDataInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where dataInicio is not null
        defaultAusenciaShouldBeFound("dataInicio.specified=true");

        // Get all the ausenciaList where dataInicio is null
        defaultAusenciaShouldNotBeFound("dataInicio.specified=false");
    }
                @Test
    @Transactional
    public void getAllAusenciasByDataInicioContainsSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where dataInicio contains DEFAULT_DATA_INICIO
        defaultAusenciaShouldBeFound("dataInicio.contains=" + DEFAULT_DATA_INICIO);

        // Get all the ausenciaList where dataInicio contains UPDATED_DATA_INICIO
        defaultAusenciaShouldNotBeFound("dataInicio.contains=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    public void getAllAusenciasByDataInicioNotContainsSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where dataInicio does not contain DEFAULT_DATA_INICIO
        defaultAusenciaShouldNotBeFound("dataInicio.doesNotContain=" + DEFAULT_DATA_INICIO);

        // Get all the ausenciaList where dataInicio does not contain UPDATED_DATA_INICIO
        defaultAusenciaShouldBeFound("dataInicio.doesNotContain=" + UPDATED_DATA_INICIO);
    }


    @Test
    @Transactional
    public void getAllAusenciasByDataFimIsEqualToSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where dataFim equals to DEFAULT_DATA_FIM
        defaultAusenciaShouldBeFound("dataFim.equals=" + DEFAULT_DATA_FIM);

        // Get all the ausenciaList where dataFim equals to UPDATED_DATA_FIM
        defaultAusenciaShouldNotBeFound("dataFim.equals=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    public void getAllAusenciasByDataFimIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where dataFim not equals to DEFAULT_DATA_FIM
        defaultAusenciaShouldNotBeFound("dataFim.notEquals=" + DEFAULT_DATA_FIM);

        // Get all the ausenciaList where dataFim not equals to UPDATED_DATA_FIM
        defaultAusenciaShouldBeFound("dataFim.notEquals=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    public void getAllAusenciasByDataFimIsInShouldWork() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where dataFim in DEFAULT_DATA_FIM or UPDATED_DATA_FIM
        defaultAusenciaShouldBeFound("dataFim.in=" + DEFAULT_DATA_FIM + "," + UPDATED_DATA_FIM);

        // Get all the ausenciaList where dataFim equals to UPDATED_DATA_FIM
        defaultAusenciaShouldNotBeFound("dataFim.in=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    public void getAllAusenciasByDataFimIsNullOrNotNull() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where dataFim is not null
        defaultAusenciaShouldBeFound("dataFim.specified=true");

        // Get all the ausenciaList where dataFim is null
        defaultAusenciaShouldNotBeFound("dataFim.specified=false");
    }
                @Test
    @Transactional
    public void getAllAusenciasByDataFimContainsSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where dataFim contains DEFAULT_DATA_FIM
        defaultAusenciaShouldBeFound("dataFim.contains=" + DEFAULT_DATA_FIM);

        // Get all the ausenciaList where dataFim contains UPDATED_DATA_FIM
        defaultAusenciaShouldNotBeFound("dataFim.contains=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    public void getAllAusenciasByDataFimNotContainsSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

        // Get all the ausenciaList where dataFim does not contain DEFAULT_DATA_FIM
        defaultAusenciaShouldNotBeFound("dataFim.doesNotContain=" + DEFAULT_DATA_FIM);

        // Get all the ausenciaList where dataFim does not contain UPDATED_DATA_FIM
        defaultAusenciaShouldBeFound("dataFim.doesNotContain=" + UPDATED_DATA_FIM);
    }


    @Test
    @Transactional
    public void getAllAusenciasByEmpregadoIsEqualToSomething() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);
        Empregado empregado = EmpregadoResourceIT.createEntity(em);
        em.persist(empregado);
        em.flush();
        ausencia.setEmpregado(empregado);
        ausenciaRepository.saveAndFlush(ausencia);
        Long empregadoId = empregado.getId();

        // Get all the ausenciaList where empregado equals to empregadoId
        defaultAusenciaShouldBeFound("empregadoId.equals=" + empregadoId);

        // Get all the ausenciaList where empregado equals to empregadoId + 1
        defaultAusenciaShouldNotBeFound("empregadoId.equals=" + (empregadoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAusenciaShouldBeFound(String filter) throws Exception {
        restAusenciaMockMvc.perform(get("/api/ausencias?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ausencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO)))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM)));

        // Check, that the count call also returns 1
        restAusenciaMockMvc.perform(get("/api/ausencias/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAusenciaShouldNotBeFound(String filter) throws Exception {
        restAusenciaMockMvc.perform(get("/api/ausencias?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAusenciaMockMvc.perform(get("/api/ausencias/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAusencia() throws Exception {
        // Get the ausencia
        restAusenciaMockMvc.perform(get("/api/ausencias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAusencia() throws Exception {
        // Initialize the database
        ausenciaService.save(ausencia);

        int databaseSizeBeforeUpdate = ausenciaRepository.findAll().size();

        // Update the ausencia
        Ausencia updatedAusencia = ausenciaRepository.findById(ausencia.getId()).get();
        // Disconnect from session so that the updates on updatedAusencia are not directly saved in db
        em.detach(updatedAusencia);
        updatedAusencia
            .tipo(UPDATED_TIPO)
            .descricao(UPDATED_DESCRICAO)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM);

        restAusenciaMockMvc.perform(put("/api/ausencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAusencia)))
            .andExpect(status().isOk());

        // Validate the Ausencia in the database
        List<Ausencia> ausenciaList = ausenciaRepository.findAll();
        assertThat(ausenciaList).hasSize(databaseSizeBeforeUpdate);
        Ausencia testAusencia = ausenciaList.get(ausenciaList.size() - 1);
        assertThat(testAusencia.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testAusencia.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testAusencia.getDataInicio()).isEqualTo(UPDATED_DATA_INICIO);
        assertThat(testAusencia.getDataFim()).isEqualTo(UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    public void updateNonExistingAusencia() throws Exception {
        int databaseSizeBeforeUpdate = ausenciaRepository.findAll().size();

        // Create the Ausencia

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAusenciaMockMvc.perform(put("/api/ausencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ausencia)))
            .andExpect(status().isBadRequest());

        // Validate the Ausencia in the database
        List<Ausencia> ausenciaList = ausenciaRepository.findAll();
        assertThat(ausenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAusencia() throws Exception {
        // Initialize the database
        ausenciaService.save(ausencia);

        int databaseSizeBeforeDelete = ausenciaRepository.findAll().size();

        // Delete the ausencia
        restAusenciaMockMvc.perform(delete("/api/ausencias/{id}", ausencia.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ausencia> ausenciaList = ausenciaRepository.findAll();
        assertThat(ausenciaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
