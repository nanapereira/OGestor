package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OGestorApp;
import com.mycompany.myapp.domain.Projeto;
import com.mycompany.myapp.domain.Empregado;
import com.mycompany.myapp.repository.ProjetoRepository;
import com.mycompany.myapp.service.ProjetoService;
import com.mycompany.myapp.service.dto.ProjetoCriteria;
import com.mycompany.myapp.service.ProjetoQueryService;

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
 * Integration tests for the {@link ProjetoResource} REST controller.
 */
@SpringBootTest(classes = OGestorApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProjetoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_INICIO = "AAAAAAAAAA";
    private static final String UPDATED_DATA_INICIO = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_FIM = "AAAAAAAAAA";
    private static final String UPDATED_DATA_FIM = "BBBBBBBBBB";

    @Autowired
    private ProjetoRepository projetoRepository;

    @Mock
    private ProjetoRepository projetoRepositoryMock;

    @Mock
    private ProjetoService projetoServiceMock;

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private ProjetoQueryService projetoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjetoMockMvc;

    private Projeto projeto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projeto createEntity(EntityManager em) {
        Projeto projeto = new Projeto()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .dataInicio(DEFAULT_DATA_INICIO)
            .dataFim(DEFAULT_DATA_FIM);
        return projeto;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projeto createUpdatedEntity(EntityManager em) {
        Projeto projeto = new Projeto()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM);
        return projeto;
    }

    @BeforeEach
    public void initTest() {
        projeto = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjeto() throws Exception {
        int databaseSizeBeforeCreate = projetoRepository.findAll().size();

        // Create the Projeto
        restProjetoMockMvc.perform(post("/api/projetos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isCreated());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeCreate + 1);
        Projeto testProjeto = projetoList.get(projetoList.size() - 1);
        assertThat(testProjeto.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testProjeto.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testProjeto.getDataInicio()).isEqualTo(DEFAULT_DATA_INICIO);
        assertThat(testProjeto.getDataFim()).isEqualTo(DEFAULT_DATA_FIM);
    }

    @Test
    @Transactional
    public void createProjetoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projetoRepository.findAll().size();

        // Create the Projeto with an existing ID
        projeto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjetoMockMvc.perform(post("/api/projetos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isBadRequest());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProjetos() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList
        restProjetoMockMvc.perform(get("/api/projetos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projeto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO)))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllProjetosWithEagerRelationshipsIsEnabled() throws Exception {
        when(projetoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjetoMockMvc.perform(get("/api/projetos?eagerload=true"))
            .andExpect(status().isOk());

        verify(projetoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllProjetosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(projetoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjetoMockMvc.perform(get("/api/projetos?eagerload=true"))
            .andExpect(status().isOk());

        verify(projetoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getProjeto() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get the projeto
        restProjetoMockMvc.perform(get("/api/projetos/{id}", projeto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projeto.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.dataInicio").value(DEFAULT_DATA_INICIO))
            .andExpect(jsonPath("$.dataFim").value(DEFAULT_DATA_FIM));
    }


    @Test
    @Transactional
    public void getProjetosByIdFiltering() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        Long id = projeto.getId();

        defaultProjetoShouldBeFound("id.equals=" + id);
        defaultProjetoShouldNotBeFound("id.notEquals=" + id);

        defaultProjetoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProjetoShouldNotBeFound("id.greaterThan=" + id);

        defaultProjetoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProjetoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProjetosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where nome equals to DEFAULT_NOME
        defaultProjetoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the projetoList where nome equals to UPDATED_NOME
        defaultProjetoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllProjetosByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where nome not equals to DEFAULT_NOME
        defaultProjetoShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the projetoList where nome not equals to UPDATED_NOME
        defaultProjetoShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllProjetosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultProjetoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the projetoList where nome equals to UPDATED_NOME
        defaultProjetoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllProjetosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where nome is not null
        defaultProjetoShouldBeFound("nome.specified=true");

        // Get all the projetoList where nome is null
        defaultProjetoShouldNotBeFound("nome.specified=false");
    }
                @Test
    @Transactional
    public void getAllProjetosByNomeContainsSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where nome contains DEFAULT_NOME
        defaultProjetoShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the projetoList where nome contains UPDATED_NOME
        defaultProjetoShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllProjetosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where nome does not contain DEFAULT_NOME
        defaultProjetoShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the projetoList where nome does not contain UPDATED_NOME
        defaultProjetoShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }


    @Test
    @Transactional
    public void getAllProjetosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where descricao equals to DEFAULT_DESCRICAO
        defaultProjetoShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the projetoList where descricao equals to UPDATED_DESCRICAO
        defaultProjetoShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllProjetosByDescricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where descricao not equals to DEFAULT_DESCRICAO
        defaultProjetoShouldNotBeFound("descricao.notEquals=" + DEFAULT_DESCRICAO);

        // Get all the projetoList where descricao not equals to UPDATED_DESCRICAO
        defaultProjetoShouldBeFound("descricao.notEquals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllProjetosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultProjetoShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the projetoList where descricao equals to UPDATED_DESCRICAO
        defaultProjetoShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllProjetosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where descricao is not null
        defaultProjetoShouldBeFound("descricao.specified=true");

        // Get all the projetoList where descricao is null
        defaultProjetoShouldNotBeFound("descricao.specified=false");
    }
                @Test
    @Transactional
    public void getAllProjetosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where descricao contains DEFAULT_DESCRICAO
        defaultProjetoShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the projetoList where descricao contains UPDATED_DESCRICAO
        defaultProjetoShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllProjetosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where descricao does not contain DEFAULT_DESCRICAO
        defaultProjetoShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the projetoList where descricao does not contain UPDATED_DESCRICAO
        defaultProjetoShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }


    @Test
    @Transactional
    public void getAllProjetosByDataInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where dataInicio equals to DEFAULT_DATA_INICIO
        defaultProjetoShouldBeFound("dataInicio.equals=" + DEFAULT_DATA_INICIO);

        // Get all the projetoList where dataInicio equals to UPDATED_DATA_INICIO
        defaultProjetoShouldNotBeFound("dataInicio.equals=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    public void getAllProjetosByDataInicioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where dataInicio not equals to DEFAULT_DATA_INICIO
        defaultProjetoShouldNotBeFound("dataInicio.notEquals=" + DEFAULT_DATA_INICIO);

        // Get all the projetoList where dataInicio not equals to UPDATED_DATA_INICIO
        defaultProjetoShouldBeFound("dataInicio.notEquals=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    public void getAllProjetosByDataInicioIsInShouldWork() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where dataInicio in DEFAULT_DATA_INICIO or UPDATED_DATA_INICIO
        defaultProjetoShouldBeFound("dataInicio.in=" + DEFAULT_DATA_INICIO + "," + UPDATED_DATA_INICIO);

        // Get all the projetoList where dataInicio equals to UPDATED_DATA_INICIO
        defaultProjetoShouldNotBeFound("dataInicio.in=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    public void getAllProjetosByDataInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where dataInicio is not null
        defaultProjetoShouldBeFound("dataInicio.specified=true");

        // Get all the projetoList where dataInicio is null
        defaultProjetoShouldNotBeFound("dataInicio.specified=false");
    }
                @Test
    @Transactional
    public void getAllProjetosByDataInicioContainsSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where dataInicio contains DEFAULT_DATA_INICIO
        defaultProjetoShouldBeFound("dataInicio.contains=" + DEFAULT_DATA_INICIO);

        // Get all the projetoList where dataInicio contains UPDATED_DATA_INICIO
        defaultProjetoShouldNotBeFound("dataInicio.contains=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    public void getAllProjetosByDataInicioNotContainsSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where dataInicio does not contain DEFAULT_DATA_INICIO
        defaultProjetoShouldNotBeFound("dataInicio.doesNotContain=" + DEFAULT_DATA_INICIO);

        // Get all the projetoList where dataInicio does not contain UPDATED_DATA_INICIO
        defaultProjetoShouldBeFound("dataInicio.doesNotContain=" + UPDATED_DATA_INICIO);
    }


    @Test
    @Transactional
    public void getAllProjetosByDataFimIsEqualToSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where dataFim equals to DEFAULT_DATA_FIM
        defaultProjetoShouldBeFound("dataFim.equals=" + DEFAULT_DATA_FIM);

        // Get all the projetoList where dataFim equals to UPDATED_DATA_FIM
        defaultProjetoShouldNotBeFound("dataFim.equals=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    public void getAllProjetosByDataFimIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where dataFim not equals to DEFAULT_DATA_FIM
        defaultProjetoShouldNotBeFound("dataFim.notEquals=" + DEFAULT_DATA_FIM);

        // Get all the projetoList where dataFim not equals to UPDATED_DATA_FIM
        defaultProjetoShouldBeFound("dataFim.notEquals=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    public void getAllProjetosByDataFimIsInShouldWork() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where dataFim in DEFAULT_DATA_FIM or UPDATED_DATA_FIM
        defaultProjetoShouldBeFound("dataFim.in=" + DEFAULT_DATA_FIM + "," + UPDATED_DATA_FIM);

        // Get all the projetoList where dataFim equals to UPDATED_DATA_FIM
        defaultProjetoShouldNotBeFound("dataFim.in=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    public void getAllProjetosByDataFimIsNullOrNotNull() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where dataFim is not null
        defaultProjetoShouldBeFound("dataFim.specified=true");

        // Get all the projetoList where dataFim is null
        defaultProjetoShouldNotBeFound("dataFim.specified=false");
    }
                @Test
    @Transactional
    public void getAllProjetosByDataFimContainsSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where dataFim contains DEFAULT_DATA_FIM
        defaultProjetoShouldBeFound("dataFim.contains=" + DEFAULT_DATA_FIM);

        // Get all the projetoList where dataFim contains UPDATED_DATA_FIM
        defaultProjetoShouldNotBeFound("dataFim.contains=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    public void getAllProjetosByDataFimNotContainsSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList where dataFim does not contain DEFAULT_DATA_FIM
        defaultProjetoShouldNotBeFound("dataFim.doesNotContain=" + DEFAULT_DATA_FIM);

        // Get all the projetoList where dataFim does not contain UPDATED_DATA_FIM
        defaultProjetoShouldBeFound("dataFim.doesNotContain=" + UPDATED_DATA_FIM);
    }


    @Test
    @Transactional
    public void getAllProjetosByGestorIsEqualToSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);
        Empregado gestor = EmpregadoResourceIT.createEntity(em);
        em.persist(gestor);
        em.flush();
        projeto.setGestor(gestor);
        projetoRepository.saveAndFlush(projeto);
        Long gestorId = gestor.getId();

        // Get all the projetoList where gestor equals to gestorId
        defaultProjetoShouldBeFound("gestorId.equals=" + gestorId);

        // Get all the projetoList where gestor equals to gestorId + 1
        defaultProjetoShouldNotBeFound("gestorId.equals=" + (gestorId + 1));
    }


    @Test
    @Transactional
    public void getAllProjetosByEmpregadosIsEqualToSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);
        Empregado empregados = EmpregadoResourceIT.createEntity(em);
        em.persist(empregados);
        em.flush();
        projeto.addEmpregados(empregados);
        projetoRepository.saveAndFlush(projeto);
        Long empregadosId = empregados.getId();

        // Get all the projetoList where empregados equals to empregadosId
        defaultProjetoShouldBeFound("empregadosId.equals=" + empregadosId);

        // Get all the projetoList where empregados equals to empregadosId + 1
        defaultProjetoShouldNotBeFound("empregadosId.equals=" + (empregadosId + 1));
    }


    @Test
    @Transactional
    public void getAllProjetosByListaempregadoIsEqualToSomething() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);
        Empregado listaempregado = EmpregadoResourceIT.createEntity(em);
        em.persist(listaempregado);
        em.flush();
        projeto.addListaempregado(listaempregado);
        projetoRepository.saveAndFlush(projeto);
        Long listaempregadoId = listaempregado.getId();

        // Get all the projetoList where listaempregado equals to listaempregadoId
        defaultProjetoShouldBeFound("listaempregadoId.equals=" + listaempregadoId);

        // Get all the projetoList where listaempregado equals to listaempregadoId + 1
        defaultProjetoShouldNotBeFound("listaempregadoId.equals=" + (listaempregadoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProjetoShouldBeFound(String filter) throws Exception {
        restProjetoMockMvc.perform(get("/api/projetos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projeto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO)))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM)));

        // Check, that the count call also returns 1
        restProjetoMockMvc.perform(get("/api/projetos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProjetoShouldNotBeFound(String filter) throws Exception {
        restProjetoMockMvc.perform(get("/api/projetos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProjetoMockMvc.perform(get("/api/projetos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProjeto() throws Exception {
        // Get the projeto
        restProjetoMockMvc.perform(get("/api/projetos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjeto() throws Exception {
        // Initialize the database
        projetoService.save(projeto);

        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();

        // Update the projeto
        Projeto updatedProjeto = projetoRepository.findById(projeto.getId()).get();
        // Disconnect from session so that the updates on updatedProjeto are not directly saved in db
        em.detach(updatedProjeto);
        updatedProjeto
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM);

        restProjetoMockMvc.perform(put("/api/projetos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProjeto)))
            .andExpect(status().isOk());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate);
        Projeto testProjeto = projetoList.get(projetoList.size() - 1);
        assertThat(testProjeto.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testProjeto.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testProjeto.getDataInicio()).isEqualTo(UPDATED_DATA_INICIO);
        assertThat(testProjeto.getDataFim()).isEqualTo(UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    public void updateNonExistingProjeto() throws Exception {
        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();

        // Create the Projeto

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjetoMockMvc.perform(put("/api/projetos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isBadRequest());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProjeto() throws Exception {
        // Initialize the database
        projetoService.save(projeto);

        int databaseSizeBeforeDelete = projetoRepository.findAll().size();

        // Delete the projeto
        restProjetoMockMvc.perform(delete("/api/projetos/{id}", projeto.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
