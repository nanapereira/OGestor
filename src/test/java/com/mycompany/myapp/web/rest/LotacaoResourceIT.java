package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OGestorApp;
import com.mycompany.myapp.domain.Lotacao;
import com.mycompany.myapp.repository.LotacaoRepository;
import com.mycompany.myapp.service.LotacaoService;
import com.mycompany.myapp.service.dto.LotacaoCriteria;
import com.mycompany.myapp.service.LotacaoQueryService;

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
    private static final Integer SMALLER_CODIGO = 1 - 1;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private LotacaoRepository lotacaoRepository;

    @Autowired
    private LotacaoService lotacaoService;

    @Autowired
    private LotacaoQueryService lotacaoQueryService;

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
    public void getLotacaosByIdFiltering() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        Long id = lotacao.getId();

        defaultLotacaoShouldBeFound("id.equals=" + id);
        defaultLotacaoShouldNotBeFound("id.notEquals=" + id);

        defaultLotacaoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLotacaoShouldNotBeFound("id.greaterThan=" + id);

        defaultLotacaoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLotacaoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLotacaosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where codigo equals to DEFAULT_CODIGO
        defaultLotacaoShouldBeFound("codigo.equals=" + DEFAULT_CODIGO);

        // Get all the lotacaoList where codigo equals to UPDATED_CODIGO
        defaultLotacaoShouldNotBeFound("codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllLotacaosByCodigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where codigo not equals to DEFAULT_CODIGO
        defaultLotacaoShouldNotBeFound("codigo.notEquals=" + DEFAULT_CODIGO);

        // Get all the lotacaoList where codigo not equals to UPDATED_CODIGO
        defaultLotacaoShouldBeFound("codigo.notEquals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllLotacaosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where codigo in DEFAULT_CODIGO or UPDATED_CODIGO
        defaultLotacaoShouldBeFound("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO);

        // Get all the lotacaoList where codigo equals to UPDATED_CODIGO
        defaultLotacaoShouldNotBeFound("codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllLotacaosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where codigo is not null
        defaultLotacaoShouldBeFound("codigo.specified=true");

        // Get all the lotacaoList where codigo is null
        defaultLotacaoShouldNotBeFound("codigo.specified=false");
    }

    @Test
    @Transactional
    public void getAllLotacaosByCodigoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where codigo is greater than or equal to DEFAULT_CODIGO
        defaultLotacaoShouldBeFound("codigo.greaterThanOrEqual=" + DEFAULT_CODIGO);

        // Get all the lotacaoList where codigo is greater than or equal to UPDATED_CODIGO
        defaultLotacaoShouldNotBeFound("codigo.greaterThanOrEqual=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllLotacaosByCodigoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where codigo is less than or equal to DEFAULT_CODIGO
        defaultLotacaoShouldBeFound("codigo.lessThanOrEqual=" + DEFAULT_CODIGO);

        // Get all the lotacaoList where codigo is less than or equal to SMALLER_CODIGO
        defaultLotacaoShouldNotBeFound("codigo.lessThanOrEqual=" + SMALLER_CODIGO);
    }

    @Test
    @Transactional
    public void getAllLotacaosByCodigoIsLessThanSomething() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where codigo is less than DEFAULT_CODIGO
        defaultLotacaoShouldNotBeFound("codigo.lessThan=" + DEFAULT_CODIGO);

        // Get all the lotacaoList where codigo is less than UPDATED_CODIGO
        defaultLotacaoShouldBeFound("codigo.lessThan=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllLotacaosByCodigoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where codigo is greater than DEFAULT_CODIGO
        defaultLotacaoShouldNotBeFound("codigo.greaterThan=" + DEFAULT_CODIGO);

        // Get all the lotacaoList where codigo is greater than SMALLER_CODIGO
        defaultLotacaoShouldBeFound("codigo.greaterThan=" + SMALLER_CODIGO);
    }


    @Test
    @Transactional
    public void getAllLotacaosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where nome equals to DEFAULT_NOME
        defaultLotacaoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the lotacaoList where nome equals to UPDATED_NOME
        defaultLotacaoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllLotacaosByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where nome not equals to DEFAULT_NOME
        defaultLotacaoShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the lotacaoList where nome not equals to UPDATED_NOME
        defaultLotacaoShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllLotacaosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultLotacaoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the lotacaoList where nome equals to UPDATED_NOME
        defaultLotacaoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllLotacaosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where nome is not null
        defaultLotacaoShouldBeFound("nome.specified=true");

        // Get all the lotacaoList where nome is null
        defaultLotacaoShouldNotBeFound("nome.specified=false");
    }
                @Test
    @Transactional
    public void getAllLotacaosByNomeContainsSomething() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where nome contains DEFAULT_NOME
        defaultLotacaoShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the lotacaoList where nome contains UPDATED_NOME
        defaultLotacaoShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllLotacaosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where nome does not contain DEFAULT_NOME
        defaultLotacaoShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the lotacaoList where nome does not contain UPDATED_NOME
        defaultLotacaoShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }


    @Test
    @Transactional
    public void getAllLotacaosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where descricao equals to DEFAULT_DESCRICAO
        defaultLotacaoShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the lotacaoList where descricao equals to UPDATED_DESCRICAO
        defaultLotacaoShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllLotacaosByDescricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where descricao not equals to DEFAULT_DESCRICAO
        defaultLotacaoShouldNotBeFound("descricao.notEquals=" + DEFAULT_DESCRICAO);

        // Get all the lotacaoList where descricao not equals to UPDATED_DESCRICAO
        defaultLotacaoShouldBeFound("descricao.notEquals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllLotacaosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultLotacaoShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the lotacaoList where descricao equals to UPDATED_DESCRICAO
        defaultLotacaoShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllLotacaosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where descricao is not null
        defaultLotacaoShouldBeFound("descricao.specified=true");

        // Get all the lotacaoList where descricao is null
        defaultLotacaoShouldNotBeFound("descricao.specified=false");
    }
                @Test
    @Transactional
    public void getAllLotacaosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where descricao contains DEFAULT_DESCRICAO
        defaultLotacaoShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the lotacaoList where descricao contains UPDATED_DESCRICAO
        defaultLotacaoShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllLotacaosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        lotacaoRepository.saveAndFlush(lotacao);

        // Get all the lotacaoList where descricao does not contain DEFAULT_DESCRICAO
        defaultLotacaoShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the lotacaoList where descricao does not contain UPDATED_DESCRICAO
        defaultLotacaoShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLotacaoShouldBeFound(String filter) throws Exception {
        restLotacaoMockMvc.perform(get("/api/lotacaos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lotacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));

        // Check, that the count call also returns 1
        restLotacaoMockMvc.perform(get("/api/lotacaos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLotacaoShouldNotBeFound(String filter) throws Exception {
        restLotacaoMockMvc.perform(get("/api/lotacaos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLotacaoMockMvc.perform(get("/api/lotacaos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        lotacaoService.save(lotacao);

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
        lotacaoService.save(lotacao);

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
