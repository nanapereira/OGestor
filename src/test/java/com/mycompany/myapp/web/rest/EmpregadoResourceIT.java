package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OGestorApp;
import com.mycompany.myapp.domain.Empregado;
import com.mycompany.myapp.domain.Lotacao;
import com.mycompany.myapp.domain.Competencia;
import com.mycompany.myapp.domain.Projeto;
import com.mycompany.myapp.repository.EmpregadoRepository;
import com.mycompany.myapp.service.EmpregadoService;
import com.mycompany.myapp.service.dto.EmpregadoCriteria;
import com.mycompany.myapp.service.EmpregadoQueryService;

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
 * Integration tests for the {@link EmpregadoResource} REST controller.
 */
@SpringBootTest(classes = OGestorApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class EmpregadoResourceIT {

    private static final String DEFAULT_MATRICULA = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULA = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_RAMAL = "AAAAAAAAAA";
    private static final String UPDATED_RAMAL = "BBBBBBBBBB";

    @Autowired
    private EmpregadoRepository empregadoRepository;

    @Mock
    private EmpregadoRepository empregadoRepositoryMock;

    @Mock
    private EmpregadoService empregadoServiceMock;

    @Autowired
    private EmpregadoService empregadoService;

    @Autowired
    private EmpregadoQueryService empregadoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmpregadoMockMvc;

    private Empregado empregado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empregado createEntity(EntityManager em) {
        Empregado empregado = new Empregado()
            .matricula(DEFAULT_MATRICULA)
            .nome(DEFAULT_NOME)
            .cpf(DEFAULT_CPF)
            .email(DEFAULT_EMAIL)
            .telefone(DEFAULT_TELEFONE)
            .ramal(DEFAULT_RAMAL);
        return empregado;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empregado createUpdatedEntity(EntityManager em) {
        Empregado empregado = new Empregado()
            .matricula(UPDATED_MATRICULA)
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .ramal(UPDATED_RAMAL);
        return empregado;
    }

    @BeforeEach
    public void initTest() {
        empregado = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmpregado() throws Exception {
        int databaseSizeBeforeCreate = empregadoRepository.findAll().size();

        // Create the Empregado
        restEmpregadoMockMvc.perform(post("/api/empregados")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(empregado)))
            .andExpect(status().isCreated());

        // Validate the Empregado in the database
        List<Empregado> empregadoList = empregadoRepository.findAll();
        assertThat(empregadoList).hasSize(databaseSizeBeforeCreate + 1);
        Empregado testEmpregado = empregadoList.get(empregadoList.size() - 1);
        assertThat(testEmpregado.getMatricula()).isEqualTo(DEFAULT_MATRICULA);
        assertThat(testEmpregado.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testEmpregado.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testEmpregado.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmpregado.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testEmpregado.getRamal()).isEqualTo(DEFAULT_RAMAL);
    }

    @Test
    @Transactional
    public void createEmpregadoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = empregadoRepository.findAll().size();

        // Create the Empregado with an existing ID
        empregado.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpregadoMockMvc.perform(post("/api/empregados")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(empregado)))
            .andExpect(status().isBadRequest());

        // Validate the Empregado in the database
        List<Empregado> empregadoList = empregadoRepository.findAll();
        assertThat(empregadoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEmpregados() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList
        restEmpregadoMockMvc.perform(get("/api/empregados?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empregado.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].ramal").value(hasItem(DEFAULT_RAMAL)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllEmpregadosWithEagerRelationshipsIsEnabled() throws Exception {
        when(empregadoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmpregadoMockMvc.perform(get("/api/empregados?eagerload=true"))
            .andExpect(status().isOk());

        verify(empregadoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllEmpregadosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(empregadoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmpregadoMockMvc.perform(get("/api/empregados?eagerload=true"))
            .andExpect(status().isOk());

        verify(empregadoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getEmpregado() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get the empregado
        restEmpregadoMockMvc.perform(get("/api/empregados/{id}", empregado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(empregado.getId().intValue()))
            .andExpect(jsonPath("$.matricula").value(DEFAULT_MATRICULA))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.ramal").value(DEFAULT_RAMAL));
    }


    @Test
    @Transactional
    public void getEmpregadosByIdFiltering() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        Long id = empregado.getId();

        defaultEmpregadoShouldBeFound("id.equals=" + id);
        defaultEmpregadoShouldNotBeFound("id.notEquals=" + id);

        defaultEmpregadoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmpregadoShouldNotBeFound("id.greaterThan=" + id);

        defaultEmpregadoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmpregadoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEmpregadosByMatriculaIsEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where matricula equals to DEFAULT_MATRICULA
        defaultEmpregadoShouldBeFound("matricula.equals=" + DEFAULT_MATRICULA);

        // Get all the empregadoList where matricula equals to UPDATED_MATRICULA
        defaultEmpregadoShouldNotBeFound("matricula.equals=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByMatriculaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where matricula not equals to DEFAULT_MATRICULA
        defaultEmpregadoShouldNotBeFound("matricula.notEquals=" + DEFAULT_MATRICULA);

        // Get all the empregadoList where matricula not equals to UPDATED_MATRICULA
        defaultEmpregadoShouldBeFound("matricula.notEquals=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByMatriculaIsInShouldWork() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where matricula in DEFAULT_MATRICULA or UPDATED_MATRICULA
        defaultEmpregadoShouldBeFound("matricula.in=" + DEFAULT_MATRICULA + "," + UPDATED_MATRICULA);

        // Get all the empregadoList where matricula equals to UPDATED_MATRICULA
        defaultEmpregadoShouldNotBeFound("matricula.in=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByMatriculaIsNullOrNotNull() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where matricula is not null
        defaultEmpregadoShouldBeFound("matricula.specified=true");

        // Get all the empregadoList where matricula is null
        defaultEmpregadoShouldNotBeFound("matricula.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmpregadosByMatriculaContainsSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where matricula contains DEFAULT_MATRICULA
        defaultEmpregadoShouldBeFound("matricula.contains=" + DEFAULT_MATRICULA);

        // Get all the empregadoList where matricula contains UPDATED_MATRICULA
        defaultEmpregadoShouldNotBeFound("matricula.contains=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByMatriculaNotContainsSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where matricula does not contain DEFAULT_MATRICULA
        defaultEmpregadoShouldNotBeFound("matricula.doesNotContain=" + DEFAULT_MATRICULA);

        // Get all the empregadoList where matricula does not contain UPDATED_MATRICULA
        defaultEmpregadoShouldBeFound("matricula.doesNotContain=" + UPDATED_MATRICULA);
    }


    @Test
    @Transactional
    public void getAllEmpregadosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where nome equals to DEFAULT_NOME
        defaultEmpregadoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the empregadoList where nome equals to UPDATED_NOME
        defaultEmpregadoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where nome not equals to DEFAULT_NOME
        defaultEmpregadoShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the empregadoList where nome not equals to UPDATED_NOME
        defaultEmpregadoShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultEmpregadoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the empregadoList where nome equals to UPDATED_NOME
        defaultEmpregadoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where nome is not null
        defaultEmpregadoShouldBeFound("nome.specified=true");

        // Get all the empregadoList where nome is null
        defaultEmpregadoShouldNotBeFound("nome.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmpregadosByNomeContainsSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where nome contains DEFAULT_NOME
        defaultEmpregadoShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the empregadoList where nome contains UPDATED_NOME
        defaultEmpregadoShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where nome does not contain DEFAULT_NOME
        defaultEmpregadoShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the empregadoList where nome does not contain UPDATED_NOME
        defaultEmpregadoShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }


    @Test
    @Transactional
    public void getAllEmpregadosByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where cpf equals to DEFAULT_CPF
        defaultEmpregadoShouldBeFound("cpf.equals=" + DEFAULT_CPF);

        // Get all the empregadoList where cpf equals to UPDATED_CPF
        defaultEmpregadoShouldNotBeFound("cpf.equals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByCpfIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where cpf not equals to DEFAULT_CPF
        defaultEmpregadoShouldNotBeFound("cpf.notEquals=" + DEFAULT_CPF);

        // Get all the empregadoList where cpf not equals to UPDATED_CPF
        defaultEmpregadoShouldBeFound("cpf.notEquals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByCpfIsInShouldWork() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where cpf in DEFAULT_CPF or UPDATED_CPF
        defaultEmpregadoShouldBeFound("cpf.in=" + DEFAULT_CPF + "," + UPDATED_CPF);

        // Get all the empregadoList where cpf equals to UPDATED_CPF
        defaultEmpregadoShouldNotBeFound("cpf.in=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByCpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where cpf is not null
        defaultEmpregadoShouldBeFound("cpf.specified=true");

        // Get all the empregadoList where cpf is null
        defaultEmpregadoShouldNotBeFound("cpf.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmpregadosByCpfContainsSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where cpf contains DEFAULT_CPF
        defaultEmpregadoShouldBeFound("cpf.contains=" + DEFAULT_CPF);

        // Get all the empregadoList where cpf contains UPDATED_CPF
        defaultEmpregadoShouldNotBeFound("cpf.contains=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByCpfNotContainsSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where cpf does not contain DEFAULT_CPF
        defaultEmpregadoShouldNotBeFound("cpf.doesNotContain=" + DEFAULT_CPF);

        // Get all the empregadoList where cpf does not contain UPDATED_CPF
        defaultEmpregadoShouldBeFound("cpf.doesNotContain=" + UPDATED_CPF);
    }


    @Test
    @Transactional
    public void getAllEmpregadosByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where email equals to DEFAULT_EMAIL
        defaultEmpregadoShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the empregadoList where email equals to UPDATED_EMAIL
        defaultEmpregadoShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where email not equals to DEFAULT_EMAIL
        defaultEmpregadoShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the empregadoList where email not equals to UPDATED_EMAIL
        defaultEmpregadoShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultEmpregadoShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the empregadoList where email equals to UPDATED_EMAIL
        defaultEmpregadoShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where email is not null
        defaultEmpregadoShouldBeFound("email.specified=true");

        // Get all the empregadoList where email is null
        defaultEmpregadoShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmpregadosByEmailContainsSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where email contains DEFAULT_EMAIL
        defaultEmpregadoShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the empregadoList where email contains UPDATED_EMAIL
        defaultEmpregadoShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where email does not contain DEFAULT_EMAIL
        defaultEmpregadoShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the empregadoList where email does not contain UPDATED_EMAIL
        defaultEmpregadoShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllEmpregadosByTelefoneIsEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where telefone equals to DEFAULT_TELEFONE
        defaultEmpregadoShouldBeFound("telefone.equals=" + DEFAULT_TELEFONE);

        // Get all the empregadoList where telefone equals to UPDATED_TELEFONE
        defaultEmpregadoShouldNotBeFound("telefone.equals=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByTelefoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where telefone not equals to DEFAULT_TELEFONE
        defaultEmpregadoShouldNotBeFound("telefone.notEquals=" + DEFAULT_TELEFONE);

        // Get all the empregadoList where telefone not equals to UPDATED_TELEFONE
        defaultEmpregadoShouldBeFound("telefone.notEquals=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByTelefoneIsInShouldWork() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where telefone in DEFAULT_TELEFONE or UPDATED_TELEFONE
        defaultEmpregadoShouldBeFound("telefone.in=" + DEFAULT_TELEFONE + "," + UPDATED_TELEFONE);

        // Get all the empregadoList where telefone equals to UPDATED_TELEFONE
        defaultEmpregadoShouldNotBeFound("telefone.in=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByTelefoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where telefone is not null
        defaultEmpregadoShouldBeFound("telefone.specified=true");

        // Get all the empregadoList where telefone is null
        defaultEmpregadoShouldNotBeFound("telefone.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmpregadosByTelefoneContainsSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where telefone contains DEFAULT_TELEFONE
        defaultEmpregadoShouldBeFound("telefone.contains=" + DEFAULT_TELEFONE);

        // Get all the empregadoList where telefone contains UPDATED_TELEFONE
        defaultEmpregadoShouldNotBeFound("telefone.contains=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByTelefoneNotContainsSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where telefone does not contain DEFAULT_TELEFONE
        defaultEmpregadoShouldNotBeFound("telefone.doesNotContain=" + DEFAULT_TELEFONE);

        // Get all the empregadoList where telefone does not contain UPDATED_TELEFONE
        defaultEmpregadoShouldBeFound("telefone.doesNotContain=" + UPDATED_TELEFONE);
    }


    @Test
    @Transactional
    public void getAllEmpregadosByRamalIsEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where ramal equals to DEFAULT_RAMAL
        defaultEmpregadoShouldBeFound("ramal.equals=" + DEFAULT_RAMAL);

        // Get all the empregadoList where ramal equals to UPDATED_RAMAL
        defaultEmpregadoShouldNotBeFound("ramal.equals=" + UPDATED_RAMAL);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByRamalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where ramal not equals to DEFAULT_RAMAL
        defaultEmpregadoShouldNotBeFound("ramal.notEquals=" + DEFAULT_RAMAL);

        // Get all the empregadoList where ramal not equals to UPDATED_RAMAL
        defaultEmpregadoShouldBeFound("ramal.notEquals=" + UPDATED_RAMAL);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByRamalIsInShouldWork() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where ramal in DEFAULT_RAMAL or UPDATED_RAMAL
        defaultEmpregadoShouldBeFound("ramal.in=" + DEFAULT_RAMAL + "," + UPDATED_RAMAL);

        // Get all the empregadoList where ramal equals to UPDATED_RAMAL
        defaultEmpregadoShouldNotBeFound("ramal.in=" + UPDATED_RAMAL);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByRamalIsNullOrNotNull() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where ramal is not null
        defaultEmpregadoShouldBeFound("ramal.specified=true");

        // Get all the empregadoList where ramal is null
        defaultEmpregadoShouldNotBeFound("ramal.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmpregadosByRamalContainsSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where ramal contains DEFAULT_RAMAL
        defaultEmpregadoShouldBeFound("ramal.contains=" + DEFAULT_RAMAL);

        // Get all the empregadoList where ramal contains UPDATED_RAMAL
        defaultEmpregadoShouldNotBeFound("ramal.contains=" + UPDATED_RAMAL);
    }

    @Test
    @Transactional
    public void getAllEmpregadosByRamalNotContainsSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

        // Get all the empregadoList where ramal does not contain DEFAULT_RAMAL
        defaultEmpregadoShouldNotBeFound("ramal.doesNotContain=" + DEFAULT_RAMAL);

        // Get all the empregadoList where ramal does not contain UPDATED_RAMAL
        defaultEmpregadoShouldBeFound("ramal.doesNotContain=" + UPDATED_RAMAL);
    }


    @Test
    @Transactional
    public void getAllEmpregadosByLotacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);
        Lotacao lotacao = LotacaoResourceIT.createEntity(em);
        em.persist(lotacao);
        em.flush();
        empregado.setLotacao(lotacao);
        empregadoRepository.saveAndFlush(empregado);
        Long lotacaoId = lotacao.getId();

        // Get all the empregadoList where lotacao equals to lotacaoId
        defaultEmpregadoShouldBeFound("lotacaoId.equals=" + lotacaoId);

        // Get all the empregadoList where lotacao equals to lotacaoId + 1
        defaultEmpregadoShouldNotBeFound("lotacaoId.equals=" + (lotacaoId + 1));
    }


    @Test
    @Transactional
    public void getAllEmpregadosByCompetenciasIsEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);
        Competencia competencias = CompetenciaResourceIT.createEntity(em);
        em.persist(competencias);
        em.flush();
        empregado.addCompetencias(competencias);
        empregadoRepository.saveAndFlush(empregado);
        Long competenciasId = competencias.getId();

        // Get all the empregadoList where competencias equals to competenciasId
        defaultEmpregadoShouldBeFound("competenciasId.equals=" + competenciasId);

        // Get all the empregadoList where competencias equals to competenciasId + 1
        defaultEmpregadoShouldNotBeFound("competenciasId.equals=" + (competenciasId + 1));
    }


    @Test
    @Transactional
    public void getAllEmpregadosByProjetosIsEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);
        Projeto projetos = ProjetoResourceIT.createEntity(em);
        em.persist(projetos);
        em.flush();
        empregado.addProjetos(projetos);
        empregadoRepository.saveAndFlush(empregado);
        Long projetosId = projetos.getId();

        // Get all the empregadoList where projetos equals to projetosId
        defaultEmpregadoShouldBeFound("projetosId.equals=" + projetosId);

        // Get all the empregadoList where projetos equals to projetosId + 1
        defaultEmpregadoShouldNotBeFound("projetosId.equals=" + (projetosId + 1));
    }


    @Test
    @Transactional
    public void getAllEmpregadosByListaCompetenciasIsEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);
        Competencia listaCompetencias = CompetenciaResourceIT.createEntity(em);
        em.persist(listaCompetencias);
        em.flush();
        empregado.addListaCompetencias(listaCompetencias);
        empregadoRepository.saveAndFlush(empregado);
        Long listaCompetenciasId = listaCompetencias.getId();

        // Get all the empregadoList where listaCompetencias equals to listaCompetenciasId
        defaultEmpregadoShouldBeFound("listaCompetenciasId.equals=" + listaCompetenciasId);

        // Get all the empregadoList where listaCompetencias equals to listaCompetenciasId + 1
        defaultEmpregadoShouldNotBeFound("listaCompetenciasId.equals=" + (listaCompetenciasId + 1));
    }


    @Test
    @Transactional
    public void getAllEmpregadosByListaProjetosIsEqualToSomething() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);
        Projeto listaProjetos = ProjetoResourceIT.createEntity(em);
        em.persist(listaProjetos);
        em.flush();
        empregado.addListaProjetos(listaProjetos);
        empregadoRepository.saveAndFlush(empregado);
        Long listaProjetosId = listaProjetos.getId();

        // Get all the empregadoList where listaProjetos equals to listaProjetosId
        defaultEmpregadoShouldBeFound("listaProjetosId.equals=" + listaProjetosId);

        // Get all the empregadoList where listaProjetos equals to listaProjetosId + 1
        defaultEmpregadoShouldNotBeFound("listaProjetosId.equals=" + (listaProjetosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmpregadoShouldBeFound(String filter) throws Exception {
        restEmpregadoMockMvc.perform(get("/api/empregados?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empregado.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].ramal").value(hasItem(DEFAULT_RAMAL)));

        // Check, that the count call also returns 1
        restEmpregadoMockMvc.perform(get("/api/empregados/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmpregadoShouldNotBeFound(String filter) throws Exception {
        restEmpregadoMockMvc.perform(get("/api/empregados?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmpregadoMockMvc.perform(get("/api/empregados/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEmpregado() throws Exception {
        // Get the empregado
        restEmpregadoMockMvc.perform(get("/api/empregados/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmpregado() throws Exception {
        // Initialize the database
        empregadoService.save(empregado);

        int databaseSizeBeforeUpdate = empregadoRepository.findAll().size();

        // Update the empregado
        Empregado updatedEmpregado = empregadoRepository.findById(empregado.getId()).get();
        // Disconnect from session so that the updates on updatedEmpregado are not directly saved in db
        em.detach(updatedEmpregado);
        updatedEmpregado
            .matricula(UPDATED_MATRICULA)
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .ramal(UPDATED_RAMAL);

        restEmpregadoMockMvc.perform(put("/api/empregados")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmpregado)))
            .andExpect(status().isOk());

        // Validate the Empregado in the database
        List<Empregado> empregadoList = empregadoRepository.findAll();
        assertThat(empregadoList).hasSize(databaseSizeBeforeUpdate);
        Empregado testEmpregado = empregadoList.get(empregadoList.size() - 1);
        assertThat(testEmpregado.getMatricula()).isEqualTo(UPDATED_MATRICULA);
        assertThat(testEmpregado.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testEmpregado.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testEmpregado.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmpregado.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testEmpregado.getRamal()).isEqualTo(UPDATED_RAMAL);
    }

    @Test
    @Transactional
    public void updateNonExistingEmpregado() throws Exception {
        int databaseSizeBeforeUpdate = empregadoRepository.findAll().size();

        // Create the Empregado

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpregadoMockMvc.perform(put("/api/empregados")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(empregado)))
            .andExpect(status().isBadRequest());

        // Validate the Empregado in the database
        List<Empregado> empregadoList = empregadoRepository.findAll();
        assertThat(empregadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmpregado() throws Exception {
        // Initialize the database
        empregadoService.save(empregado);

        int databaseSizeBeforeDelete = empregadoRepository.findAll().size();

        // Delete the empregado
        restEmpregadoMockMvc.perform(delete("/api/empregados/{id}", empregado.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Empregado> empregadoList = empregadoRepository.findAll();
        assertThat(empregadoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
