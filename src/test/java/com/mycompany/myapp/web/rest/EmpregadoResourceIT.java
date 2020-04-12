package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OGestorApp;
import com.mycompany.myapp.domain.Empregado;
import com.mycompany.myapp.repository.EmpregadoRepository;

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
        EmpregadoResource empregadoResource = new EmpregadoResource(empregadoRepositoryMock);
        when(empregadoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmpregadoMockMvc.perform(get("/api/empregados?eagerload=true"))
            .andExpect(status().isOk());

        verify(empregadoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllEmpregadosWithEagerRelationshipsIsNotEnabled() throws Exception {
        EmpregadoResource empregadoResource = new EmpregadoResource(empregadoRepositoryMock);
        when(empregadoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmpregadoMockMvc.perform(get("/api/empregados?eagerload=true"))
            .andExpect(status().isOk());

        verify(empregadoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
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
    public void getNonExistingEmpregado() throws Exception {
        // Get the empregado
        restEmpregadoMockMvc.perform(get("/api/empregados/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmpregado() throws Exception {
        // Initialize the database
        empregadoRepository.saveAndFlush(empregado);

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
        empregadoRepository.saveAndFlush(empregado);

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
