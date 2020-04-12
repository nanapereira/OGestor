package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OGestorApp;
import com.mycompany.myapp.domain.Lotacao;
import com.mycompany.myapp.repository.LotacaoRepository;

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

/**
 * Integration tests for the {@link LotacaoResource} REST controller.
 */
@SpringBootTest(classes = OGestorApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class LotacaoResourceIT {

    private static final Integer DEFAULT_CODIGO = 1;
    private static final Integer UPDATED_CODIGO = 2;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private LotacaoRepository lotacaoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLotacaoMockMvc;

    private Lotacao lotacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lotacao createEntity(EntityManager em) {
        Lotacao lotacao = new Lotacao()
            .codigo(DEFAULT_CODIGO)
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO);
        return lotacao;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lotacao createUpdatedEntity(EntityManager em) {
        Lotacao lotacao = new Lotacao()
            .codigo(UPDATED_CODIGO)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO);
        return lotacao;
    }

    @BeforeEach
    public void initTest() {
        lotacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createLotacao() throws Exception {
        int databaseSizeBeforeCreate = lotacaoRepository.findAll().size();

        // Create the Lotacao
        restLotacaoMockMvc.perform(post("/api/lotacaos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lotacao)))
            .andExpect(status().isCreated());

        // Validate the Lotacao in the database
        List<Lotacao> lotacaoList = lotacaoRepository.findAll();
        assertThat(lotacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Lotacao testLotacao = lotacaoList.get(lotacaoList.size() - 1);
        assertThat(testLotacao.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testLotacao.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testLotacao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createLotacaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lotacaoRepository.findAll().size();

        // Create the Lotacao with an existing ID
        lotacao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLotacaoMockMvc.perform(post("/api/lotacaos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lotacao)))
            .andExpect(status().isBadRequest());

        // Validate the Lotacao in the database
        List<Lotacao> lotacaoList = lotacaoRepository.findAll();
        assertThat(lotacaoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLotacaos() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList
        restLotacaoMockMvc.perform(get("/api/lotacaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lotacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
    
    @Test
    @Transactional
    public void getLotacao() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get the lotacao
        restLotacaoMockMvc.perform(get("/api/lotacaos/{id}", lotacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lotacao.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    public void getNonExistingLotacao() throws Exception {
        // Get the lotacao
        restLotacaoMockMvc.perform(get("/api/lotacaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLotacao() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        int databaseSizeBeforeUpdate = lotacaoRepository.findAll().size();

        // Update the lotacao
        Lotacao updatedLotacao = lotacaoRepository.findById(lotacao.getId()).get();
        // Disconnect from session so that the updates on updatedLotacao are not directly saved in db
        em.detach(updatedLotacao);
        updatedLotacao
            .codigo(UPDATED_CODIGO)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO);

        restLotacaoMockMvc.perform(put("/api/lotacaos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLotacao)))
            .andExpect(status().isOk());

        // Validate the Lotacao in the database
        List<Lotacao> lotacaoList = lotacaoRepository.findAll();
        assertThat(lotacaoList).hasSize(databaseSizeBeforeUpdate);
        Lotacao testLotacao = lotacaoList.get(lotacaoList.size() - 1);
        assertThat(testLotacao.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testLotacao.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testLotacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void updateNonExistingLotacao() throws Exception {
        int databaseSizeBeforeUpdate = lotacaoRepository.findAll().size();

        // Create the Lotacao

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLotacaoMockMvc.perform(put("/api/lotacaos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lotacao)))
            .andExpect(status().isBadRequest());

        // Validate the Lotacao in the database
        List<Lotacao> lotacaoList = lotacaoRepository.findAll();
        assertThat(lotacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLotacao() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        int databaseSizeBeforeDelete = lotacaoRepository.findAll().size();

        // Delete the lotacao
        restLotacaoMockMvc.perform(delete("/api/lotacaos/{id}", lotacao.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lotacao> lotacaoList = lotacaoRepository.findAll();
        assertThat(lotacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
