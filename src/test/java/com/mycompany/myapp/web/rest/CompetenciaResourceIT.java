package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OGestorApp;
import com.mycompany.myapp.domain.Competencia;
import com.mycompany.myapp.domain.Empregado;
import com.mycompany.myapp.repository.CompetenciaRepository;
import com.mycompany.myapp.service.CompetenciaService;
import com.mycompany.myapp.service.dto.CompetenciaCriteria;
import com.mycompany.myapp.service.CompetenciaQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CompetenciaResource} REST controller.
 */
@SpringBootTest(classes = OGestorApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CompetenciaResourceIT {

    private static final Integer DEFAULT_CODIGO = 1;
    private static final Integer UPDATED_CODIGO = 2;
    private static final Integer SMALLER_CODIGO = 1 - 1;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private CompetenciaRepository competenciaRepository;

    @Mock
    private CompetenciaRepository competenciaRepositoryMock;

    @Mock
    private CompetenciaService competenciaServiceMock;

    @Autowired
    private CompetenciaService competenciaService;

    @Autowired
    private CompetenciaQueryService competenciaQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompetenciaMockMvc;

    private Competencia competencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Competencia createEntity(EntityManager em) {
        Competencia competencia = new Competencia()
            .codigo(DEFAULT_CODIGO)
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO);
        return competencia;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Competencia createUpdatedEntity(EntityManager em) {
        Competencia competencia = new Competencia()
            .codigo(UPDATED_CODIGO)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO);
        return competencia;
    }

    @BeforeEach
    public void initTest() {
        competencia = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompetencia() throws Exception {
        int databaseSizeBeforeCreate = competenciaRepository.findAll().size();

        // Create the Competencia
        restCompetenciaMockMvc.perform(post("/api/competencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(competencia)))
            .andExpect(status().isCreated());

        // Validate the Competencia in the database
        List<Competencia> competenciaList = competenciaRepository.findAll();
        assertThat(competenciaList).hasSize(databaseSizeBeforeCreate + 1);
        Competencia testCompetencia = competenciaList.get(competenciaList.size() - 1);
        assertThat(testCompetencia.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testCompetencia.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCompetencia.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createCompetenciaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = competenciaRepository.findAll().size();

        // Create the Competencia with an existing ID
        competencia.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompetenciaMockMvc.perform(post("/api/competencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(competencia)))
            .andExpect(status().isBadRequest());

        // Validate the Competencia in the database
        List<Competencia> competenciaList = competenciaRepository.findAll();
        assertThat(competenciaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCompetencias() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList
        restCompetenciaMockMvc.perform(get("/api/competencias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCompetenciasWithEagerRelationshipsIsEnabled() throws Exception {
        when(competenciaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompetenciaMockMvc.perform(get("/api/competencias?eagerload=true"))
            .andExpect(status().isOk());

        verify(competenciaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCompetenciasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(competenciaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompetenciaMockMvc.perform(get("/api/competencias?eagerload=true"))
            .andExpect(status().isOk());

        verify(competenciaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCompetencia() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get the competencia
        restCompetenciaMockMvc.perform(get("/api/competencias/{id}", competencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(competencia.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }


    @Test
    @Transactional
    public void getCompetenciasByIdFiltering() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        Long id = competencia.getId();

        defaultCompetenciaShouldBeFound("id.equals=" + id);
        defaultCompetenciaShouldNotBeFound("id.notEquals=" + id);

        defaultCompetenciaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompetenciaShouldNotBeFound("id.greaterThan=" + id);

        defaultCompetenciaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompetenciaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCompetenciasByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where codigo equals to DEFAULT_CODIGO
        defaultCompetenciaShouldBeFound("codigo.equals=" + DEFAULT_CODIGO);

        // Get all the competenciaList where codigo equals to UPDATED_CODIGO
        defaultCompetenciaShouldNotBeFound("codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllCompetenciasByCodigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where codigo not equals to DEFAULT_CODIGO
        defaultCompetenciaShouldNotBeFound("codigo.notEquals=" + DEFAULT_CODIGO);

        // Get all the competenciaList where codigo not equals to UPDATED_CODIGO
        defaultCompetenciaShouldBeFound("codigo.notEquals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllCompetenciasByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where codigo in DEFAULT_CODIGO or UPDATED_CODIGO
        defaultCompetenciaShouldBeFound("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO);

        // Get all the competenciaList where codigo equals to UPDATED_CODIGO
        defaultCompetenciaShouldNotBeFound("codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllCompetenciasByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where codigo is not null
        defaultCompetenciaShouldBeFound("codigo.specified=true");

        // Get all the competenciaList where codigo is null
        defaultCompetenciaShouldNotBeFound("codigo.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompetenciasByCodigoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where codigo is greater than or equal to DEFAULT_CODIGO
        defaultCompetenciaShouldBeFound("codigo.greaterThanOrEqual=" + DEFAULT_CODIGO);

        // Get all the competenciaList where codigo is greater than or equal to UPDATED_CODIGO
        defaultCompetenciaShouldNotBeFound("codigo.greaterThanOrEqual=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllCompetenciasByCodigoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where codigo is less than or equal to DEFAULT_CODIGO
        defaultCompetenciaShouldBeFound("codigo.lessThanOrEqual=" + DEFAULT_CODIGO);

        // Get all the competenciaList where codigo is less than or equal to SMALLER_CODIGO
        defaultCompetenciaShouldNotBeFound("codigo.lessThanOrEqual=" + SMALLER_CODIGO);
    }

    @Test
    @Transactional
    public void getAllCompetenciasByCodigoIsLessThanSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where codigo is less than DEFAULT_CODIGO
        defaultCompetenciaShouldNotBeFound("codigo.lessThan=" + DEFAULT_CODIGO);

        // Get all the competenciaList where codigo is less than UPDATED_CODIGO
        defaultCompetenciaShouldBeFound("codigo.lessThan=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllCompetenciasByCodigoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where codigo is greater than DEFAULT_CODIGO
        defaultCompetenciaShouldNotBeFound("codigo.greaterThan=" + DEFAULT_CODIGO);

        // Get all the competenciaList where codigo is greater than SMALLER_CODIGO
        defaultCompetenciaShouldBeFound("codigo.greaterThan=" + SMALLER_CODIGO);
    }


    @Test
    @Transactional
    public void getAllCompetenciasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where nome equals to DEFAULT_NOME
        defaultCompetenciaShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the competenciaList where nome equals to UPDATED_NOME
        defaultCompetenciaShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllCompetenciasByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where nome not equals to DEFAULT_NOME
        defaultCompetenciaShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the competenciaList where nome not equals to UPDATED_NOME
        defaultCompetenciaShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllCompetenciasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultCompetenciaShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the competenciaList where nome equals to UPDATED_NOME
        defaultCompetenciaShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllCompetenciasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where nome is not null
        defaultCompetenciaShouldBeFound("nome.specified=true");

        // Get all the competenciaList where nome is null
        defaultCompetenciaShouldNotBeFound("nome.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompetenciasByNomeContainsSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where nome contains DEFAULT_NOME
        defaultCompetenciaShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the competenciaList where nome contains UPDATED_NOME
        defaultCompetenciaShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllCompetenciasByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where nome does not contain DEFAULT_NOME
        defaultCompetenciaShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the competenciaList where nome does not contain UPDATED_NOME
        defaultCompetenciaShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }


    @Test
    @Transactional
    public void getAllCompetenciasByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where descricao equals to DEFAULT_DESCRICAO
        defaultCompetenciaShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the competenciaList where descricao equals to UPDATED_DESCRICAO
        defaultCompetenciaShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllCompetenciasByDescricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where descricao not equals to DEFAULT_DESCRICAO
        defaultCompetenciaShouldNotBeFound("descricao.notEquals=" + DEFAULT_DESCRICAO);

        // Get all the competenciaList where descricao not equals to UPDATED_DESCRICAO
        defaultCompetenciaShouldBeFound("descricao.notEquals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllCompetenciasByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultCompetenciaShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the competenciaList where descricao equals to UPDATED_DESCRICAO
        defaultCompetenciaShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllCompetenciasByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where descricao is not null
        defaultCompetenciaShouldBeFound("descricao.specified=true");

        // Get all the competenciaList where descricao is null
        defaultCompetenciaShouldNotBeFound("descricao.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompetenciasByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where descricao contains DEFAULT_DESCRICAO
        defaultCompetenciaShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the competenciaList where descricao contains UPDATED_DESCRICAO
        defaultCompetenciaShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllCompetenciasByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);

        // Get all the competenciaList where descricao does not contain DEFAULT_DESCRICAO
        defaultCompetenciaShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the competenciaList where descricao does not contain UPDATED_DESCRICAO
        defaultCompetenciaShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }


    @Test
    @Transactional
    public void getAllCompetenciasByEmpregadosIsEqualToSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);
        Empregado empregados = EmpregadoResourceIT.createEntity(em);
        em.persist(empregados);
        em.flush();
        competencia.addEmpregados(empregados);
        competenciaRepository.saveAndFlush(competencia);
        Long empregadosId = empregados.getId();

        // Get all the competenciaList where empregados equals to empregadosId
        defaultCompetenciaShouldBeFound("empregadosId.equals=" + empregadosId);

        // Get all the competenciaList where empregados equals to empregadosId + 1
        defaultCompetenciaShouldNotBeFound("empregadosId.equals=" + (empregadosId + 1));
    }


    @Test
    @Transactional
    public void getAllCompetenciasByListaEmpregadosIsEqualToSomething() throws Exception {
        // Initialize the database
        competenciaRepository.saveAndFlush(competencia);
        Empregado listaEmpregados = EmpregadoResourceIT.createEntity(em);
        em.persist(listaEmpregados);
        em.flush();
        competencia.addListaEmpregados(listaEmpregados);
        competenciaRepository.saveAndFlush(competencia);
        Long listaEmpregadosId = listaEmpregados.getId();

        // Get all the competenciaList where listaEmpregados equals to listaEmpregadosId
        defaultCompetenciaShouldBeFound("listaEmpregadosId.equals=" + listaEmpregadosId);

        // Get all the competenciaList where listaEmpregados equals to listaEmpregadosId + 1
        defaultCompetenciaShouldNotBeFound("listaEmpregadosId.equals=" + (listaEmpregadosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompetenciaShouldBeFound(String filter) throws Exception {
        restCompetenciaMockMvc.perform(get("/api/competencias?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));

        // Check, that the count call also returns 1
        restCompetenciaMockMvc.perform(get("/api/competencias/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompetenciaShouldNotBeFound(String filter) throws Exception {
        restCompetenciaMockMvc.perform(get("/api/competencias?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompetenciaMockMvc.perform(get("/api/competencias/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCompetencia() throws Exception {
        // Get the competencia
        restCompetenciaMockMvc.perform(get("/api/competencias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompetencia() throws Exception {
        // Initialize the database
        competenciaService.save(competencia);

        int databaseSizeBeforeUpdate = competenciaRepository.findAll().size();

        // Update the competencia
        Competencia updatedCompetencia = competenciaRepository.findById(competencia.getId()).get();
        // Disconnect from session so that the updates on updatedCompetencia are not directly saved in db
        em.detach(updatedCompetencia);
        updatedCompetencia
            .codigo(UPDATED_CODIGO)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO);

        restCompetenciaMockMvc.perform(put("/api/competencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompetencia)))
            .andExpect(status().isOk());

        // Validate the Competencia in the database
        List<Competencia> competenciaList = competenciaRepository.findAll();
        assertThat(competenciaList).hasSize(databaseSizeBeforeUpdate);
        Competencia testCompetencia = competenciaList.get(competenciaList.size() - 1);
        assertThat(testCompetencia.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCompetencia.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCompetencia.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void updateNonExistingCompetencia() throws Exception {
        int databaseSizeBeforeUpdate = competenciaRepository.findAll().size();

        // Create the Competencia

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompetenciaMockMvc.perform(put("/api/competencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(competencia)))
            .andExpect(status().isBadRequest());

        // Validate the Competencia in the database
        List<Competencia> competenciaList = competenciaRepository.findAll();
        assertThat(competenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCompetencia() throws Exception {
        // Initialize the database
        competenciaService.save(competencia);

        int databaseSizeBeforeDelete = competenciaRepository.findAll().size();

        // Delete the competencia
        restCompetenciaMockMvc.perform(delete("/api/competencias/{id}", competencia.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Competencia> competenciaList = competenciaRepository.findAll();
        assertThat(competenciaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
