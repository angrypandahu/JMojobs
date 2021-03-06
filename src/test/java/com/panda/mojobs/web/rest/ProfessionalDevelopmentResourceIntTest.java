package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.ProfessionalDevelopment;
import com.panda.mojobs.repository.ProfessionalDevelopmentRepository;
import com.panda.mojobs.service.ProfessionalDevelopmentService;
import com.panda.mojobs.repository.search.ProfessionalDevelopmentSearchRepository;
import com.panda.mojobs.service.dto.ProfessionalDevelopmentDTO;
import com.panda.mojobs.service.mapper.ProfessionalDevelopmentMapper;
import com.panda.mojobs.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.panda.mojobs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProfessionalDevelopmentResource REST controller.
 *
 * @see ProfessionalDevelopmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class ProfessionalDevelopmentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TO_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ProfessionalDevelopmentRepository professionalDevelopmentRepository;

    @Autowired
    private ProfessionalDevelopmentMapper professionalDevelopmentMapper;

    @Autowired
    private ProfessionalDevelopmentService professionalDevelopmentService;

    @Autowired
    private ProfessionalDevelopmentSearchRepository professionalDevelopmentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProfessionalDevelopmentMockMvc;

    private ProfessionalDevelopment professionalDevelopment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfessionalDevelopmentResource professionalDevelopmentResource = new ProfessionalDevelopmentResource(professionalDevelopmentService);
        this.restProfessionalDevelopmentMockMvc = MockMvcBuilders.standaloneSetup(professionalDevelopmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalDevelopment createEntity(EntityManager em) {
        ProfessionalDevelopment professionalDevelopment = new ProfessionalDevelopment()
            .name(DEFAULT_NAME)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE);
        return professionalDevelopment;
    }

    @Before
    public void initTest() {
        professionalDevelopmentSearchRepository.deleteAll();
        professionalDevelopment = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfessionalDevelopment() throws Exception {
        int databaseSizeBeforeCreate = professionalDevelopmentRepository.findAll().size();

        // Create the ProfessionalDevelopment
        ProfessionalDevelopmentDTO professionalDevelopmentDTO = professionalDevelopmentMapper.toDto(professionalDevelopment);
        restProfessionalDevelopmentMockMvc.perform(post("/api/professional-developments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalDevelopmentDTO)))
            .andExpect(status().isCreated());

        // Validate the ProfessionalDevelopment in the database
        List<ProfessionalDevelopment> professionalDevelopmentList = professionalDevelopmentRepository.findAll();
        assertThat(professionalDevelopmentList).hasSize(databaseSizeBeforeCreate + 1);
        ProfessionalDevelopment testProfessionalDevelopment = professionalDevelopmentList.get(professionalDevelopmentList.size() - 1);
        assertThat(testProfessionalDevelopment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProfessionalDevelopment.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testProfessionalDevelopment.getToDate()).isEqualTo(DEFAULT_TO_DATE);

        // Validate the ProfessionalDevelopment in Elasticsearch
        ProfessionalDevelopment professionalDevelopmentEs = professionalDevelopmentSearchRepository.findOne(testProfessionalDevelopment.getId());
        assertThat(professionalDevelopmentEs).isEqualToComparingFieldByField(testProfessionalDevelopment);
    }

    @Test
    @Transactional
    public void createProfessionalDevelopmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = professionalDevelopmentRepository.findAll().size();

        // Create the ProfessionalDevelopment with an existing ID
        professionalDevelopment.setId(1L);
        ProfessionalDevelopmentDTO professionalDevelopmentDTO = professionalDevelopmentMapper.toDto(professionalDevelopment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalDevelopmentMockMvc.perform(post("/api/professional-developments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalDevelopmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalDevelopment in the database
        List<ProfessionalDevelopment> professionalDevelopmentList = professionalDevelopmentRepository.findAll();
        assertThat(professionalDevelopmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionalDevelopmentRepository.findAll().size();
        // set the field null
        professionalDevelopment.setName(null);

        // Create the ProfessionalDevelopment, which fails.
        ProfessionalDevelopmentDTO professionalDevelopmentDTO = professionalDevelopmentMapper.toDto(professionalDevelopment);

        restProfessionalDevelopmentMockMvc.perform(post("/api/professional-developments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalDevelopmentDTO)))
            .andExpect(status().isBadRequest());

        List<ProfessionalDevelopment> professionalDevelopmentList = professionalDevelopmentRepository.findAll();
        assertThat(professionalDevelopmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFromDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionalDevelopmentRepository.findAll().size();
        // set the field null
        professionalDevelopment.setFromDate(null);

        // Create the ProfessionalDevelopment, which fails.
        ProfessionalDevelopmentDTO professionalDevelopmentDTO = professionalDevelopmentMapper.toDto(professionalDevelopment);

        restProfessionalDevelopmentMockMvc.perform(post("/api/professional-developments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalDevelopmentDTO)))
            .andExpect(status().isBadRequest());

        List<ProfessionalDevelopment> professionalDevelopmentList = professionalDevelopmentRepository.findAll();
        assertThat(professionalDevelopmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkToDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionalDevelopmentRepository.findAll().size();
        // set the field null
        professionalDevelopment.setToDate(null);

        // Create the ProfessionalDevelopment, which fails.
        ProfessionalDevelopmentDTO professionalDevelopmentDTO = professionalDevelopmentMapper.toDto(professionalDevelopment);

        restProfessionalDevelopmentMockMvc.perform(post("/api/professional-developments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalDevelopmentDTO)))
            .andExpect(status().isBadRequest());

        List<ProfessionalDevelopment> professionalDevelopmentList = professionalDevelopmentRepository.findAll();
        assertThat(professionalDevelopmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfessionalDevelopments() throws Exception {
        // Initialize the database
        professionalDevelopmentRepository.saveAndFlush(professionalDevelopment);

        // Get all the professionalDevelopmentList
        restProfessionalDevelopmentMockMvc.perform(get("/api/professional-developments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalDevelopment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())));
    }

    @Test
    @Transactional
    public void getProfessionalDevelopment() throws Exception {
        // Initialize the database
        professionalDevelopmentRepository.saveAndFlush(professionalDevelopment);

        // Get the professionalDevelopment
        restProfessionalDevelopmentMockMvc.perform(get("/api/professional-developments/{id}", professionalDevelopment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(professionalDevelopment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProfessionalDevelopment() throws Exception {
        // Get the professionalDevelopment
        restProfessionalDevelopmentMockMvc.perform(get("/api/professional-developments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfessionalDevelopment() throws Exception {
        // Initialize the database
        professionalDevelopmentRepository.saveAndFlush(professionalDevelopment);
        professionalDevelopmentSearchRepository.save(professionalDevelopment);
        int databaseSizeBeforeUpdate = professionalDevelopmentRepository.findAll().size();

        // Update the professionalDevelopment
        ProfessionalDevelopment updatedProfessionalDevelopment = professionalDevelopmentRepository.findOne(professionalDevelopment.getId());
        updatedProfessionalDevelopment
            .name(UPDATED_NAME)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE);
        ProfessionalDevelopmentDTO professionalDevelopmentDTO = professionalDevelopmentMapper.toDto(updatedProfessionalDevelopment);

        restProfessionalDevelopmentMockMvc.perform(put("/api/professional-developments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalDevelopmentDTO)))
            .andExpect(status().isOk());

        // Validate the ProfessionalDevelopment in the database
        List<ProfessionalDevelopment> professionalDevelopmentList = professionalDevelopmentRepository.findAll();
        assertThat(professionalDevelopmentList).hasSize(databaseSizeBeforeUpdate);
        ProfessionalDevelopment testProfessionalDevelopment = professionalDevelopmentList.get(professionalDevelopmentList.size() - 1);
        assertThat(testProfessionalDevelopment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProfessionalDevelopment.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testProfessionalDevelopment.getToDate()).isEqualTo(UPDATED_TO_DATE);

        // Validate the ProfessionalDevelopment in Elasticsearch
        ProfessionalDevelopment professionalDevelopmentEs = professionalDevelopmentSearchRepository.findOne(testProfessionalDevelopment.getId());
        assertThat(professionalDevelopmentEs).isEqualToComparingFieldByField(testProfessionalDevelopment);
    }

    @Test
    @Transactional
    public void updateNonExistingProfessionalDevelopment() throws Exception {
        int databaseSizeBeforeUpdate = professionalDevelopmentRepository.findAll().size();

        // Create the ProfessionalDevelopment
        ProfessionalDevelopmentDTO professionalDevelopmentDTO = professionalDevelopmentMapper.toDto(professionalDevelopment);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProfessionalDevelopmentMockMvc.perform(put("/api/professional-developments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalDevelopmentDTO)))
            .andExpect(status().isCreated());

        // Validate the ProfessionalDevelopment in the database
        List<ProfessionalDevelopment> professionalDevelopmentList = professionalDevelopmentRepository.findAll();
        assertThat(professionalDevelopmentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProfessionalDevelopment() throws Exception {
        // Initialize the database
        professionalDevelopmentRepository.saveAndFlush(professionalDevelopment);
        professionalDevelopmentSearchRepository.save(professionalDevelopment);
        int databaseSizeBeforeDelete = professionalDevelopmentRepository.findAll().size();

        // Get the professionalDevelopment
        restProfessionalDevelopmentMockMvc.perform(delete("/api/professional-developments/{id}", professionalDevelopment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean professionalDevelopmentExistsInEs = professionalDevelopmentSearchRepository.exists(professionalDevelopment.getId());
        assertThat(professionalDevelopmentExistsInEs).isFalse();

        // Validate the database is empty
        List<ProfessionalDevelopment> professionalDevelopmentList = professionalDevelopmentRepository.findAll();
        assertThat(professionalDevelopmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProfessionalDevelopment() throws Exception {
        // Initialize the database
        professionalDevelopmentRepository.saveAndFlush(professionalDevelopment);
        professionalDevelopmentSearchRepository.save(professionalDevelopment);

        // Search the professionalDevelopment
        restProfessionalDevelopmentMockMvc.perform(get("/api/_search/professional-developments?query=id:" + professionalDevelopment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalDevelopment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalDevelopment.class);
        ProfessionalDevelopment professionalDevelopment1 = new ProfessionalDevelopment();
        professionalDevelopment1.setId(1L);
        ProfessionalDevelopment professionalDevelopment2 = new ProfessionalDevelopment();
        professionalDevelopment2.setId(professionalDevelopment1.getId());
        assertThat(professionalDevelopment1).isEqualTo(professionalDevelopment2);
        professionalDevelopment2.setId(2L);
        assertThat(professionalDevelopment1).isNotEqualTo(professionalDevelopment2);
        professionalDevelopment1.setId(null);
        assertThat(professionalDevelopment1).isNotEqualTo(professionalDevelopment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalDevelopmentDTO.class);
        ProfessionalDevelopmentDTO professionalDevelopmentDTO1 = new ProfessionalDevelopmentDTO();
        professionalDevelopmentDTO1.setId(1L);
        ProfessionalDevelopmentDTO professionalDevelopmentDTO2 = new ProfessionalDevelopmentDTO();
        assertThat(professionalDevelopmentDTO1).isNotEqualTo(professionalDevelopmentDTO2);
        professionalDevelopmentDTO2.setId(professionalDevelopmentDTO1.getId());
        assertThat(professionalDevelopmentDTO1).isEqualTo(professionalDevelopmentDTO2);
        professionalDevelopmentDTO2.setId(2L);
        assertThat(professionalDevelopmentDTO1).isNotEqualTo(professionalDevelopmentDTO2);
        professionalDevelopmentDTO1.setId(null);
        assertThat(professionalDevelopmentDTO1).isNotEqualTo(professionalDevelopmentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(professionalDevelopmentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(professionalDevelopmentMapper.fromId(null)).isNull();
    }
}
