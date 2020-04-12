package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OGestorApp;
import com.mycompany.myapp.domain.Ausencia;
import com.mycompany.myapp.repository.AusenciaRepository;

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
    public void getNonExistingAusencia() throws Exception {
        // Get the ausencia
        restAusenciaMockMvc.perform(get("/api/ausencias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAusencia() throws Exception {
        // Initialize the database
        ausenciaRepository.saveAndFlush(ausencia);

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
        ausenciaRepository.saveAndFlush(ausencia);

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
