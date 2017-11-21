package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.Mjob;
import com.panda.mojobs.domain.School;
import com.panda.mojobs.domain.JobSubType;
import com.panda.mojobs.repository.MjobRepository;
import com.panda.mojobs.service.MjobService;
import com.panda.mojobs.repository.search.MjobSearchRepository;
import com.panda.mojobs.service.dto.MjobDTO;
import com.panda.mojobs.service.mapper.MjobMapper;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.panda.mojobs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.panda.mojobs.domain.enumeration.JobType;
/**
 * Test class for the MjobResource REST controller.
 *
 * @see MjobResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class MjobResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_JOB_DESCRIPTION = "BBBBBBBBBB";

    private static final JobType DEFAULT_TYPE = JobType.FACULTY;
    private static final JobType UPDATED_TYPE = JobType.PRINCIPAL;

    @Autowired
    private MjobRepository mjobRepository;

    @Autowired
    private MjobMapper mjobMapper;

    @Autowired
    private MjobService mjobService;

    @Autowired
    private MjobSearchRepository mjobSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMjobMockMvc;

    private Mjob mjob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MjobResource mjobResource = new MjobResource(mjobService);
        this.restMjobMockMvc = MockMvcBuilders.standaloneSetup(mjobResource)
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
    public static Mjob createEntity(EntityManager em) {
        Mjob mjob = new Mjob()
            .name(DEFAULT_NAME)
            .jobDescription(DEFAULT_JOB_DESCRIPTION)
            .type(DEFAULT_TYPE);
        // Add required entity
        School school = SchoolResourceIntTest.createEntity(em);
        em.persist(school);
        em.flush();
        mjob.setSchool(school);
        // Add required entity
        JobSubType subType = JobSubTypeResourceIntTest.createEntity(em);
        em.persist(subType);
        em.flush();
        mjob.setSubType(subType);
        return mjob;
    }

    @Before
    public void initTest() {
        mjobSearchRepository.deleteAll();
        mjob = createEntity(em);
    }

    @Test
    @Transactional
    public void createMjob() throws Exception {
        int databaseSizeBeforeCreate = mjobRepository.findAll().size();

        // Create the Mjob
        MjobDTO mjobDTO = mjobMapper.toDto(mjob);
        restMjobMockMvc.perform(post("/api/mjobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mjobDTO)))
            .andExpect(status().isCreated());

        // Validate the Mjob in the database
        List<Mjob> mjobList = mjobRepository.findAll();
        assertThat(mjobList).hasSize(databaseSizeBeforeCreate + 1);
        Mjob testMjob = mjobList.get(mjobList.size() - 1);
        assertThat(testMjob.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMjob.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
        assertThat(testMjob.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the Mjob in Elasticsearch
        Mjob mjobEs = mjobSearchRepository.findOne(testMjob.getId());
        assertThat(mjobEs).isEqualToComparingFieldByField(testMjob);
    }

    @Test
    @Transactional
    public void createMjobWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mjobRepository.findAll().size();

        // Create the Mjob with an existing ID
        mjob.setId(1L);
        MjobDTO mjobDTO = mjobMapper.toDto(mjob);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMjobMockMvc.perform(post("/api/mjobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mjobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mjob in the database
        List<Mjob> mjobList = mjobRepository.findAll();
        assertThat(mjobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mjobRepository.findAll().size();
        // set the field null
        mjob.setName(null);

        // Create the Mjob, which fails.
        MjobDTO mjobDTO = mjobMapper.toDto(mjob);

        restMjobMockMvc.perform(post("/api/mjobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mjobDTO)))
            .andExpect(status().isBadRequest());

        List<Mjob> mjobList = mjobRepository.findAll();
        assertThat(mjobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJobDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = mjobRepository.findAll().size();
        // set the field null
        mjob.setJobDescription(null);

        // Create the Mjob, which fails.
        MjobDTO mjobDTO = mjobMapper.toDto(mjob);

        restMjobMockMvc.perform(post("/api/mjobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mjobDTO)))
            .andExpect(status().isBadRequest());

        List<Mjob> mjobList = mjobRepository.findAll();
        assertThat(mjobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = mjobRepository.findAll().size();
        // set the field null
        mjob.setType(null);

        // Create the Mjob, which fails.
        MjobDTO mjobDTO = mjobMapper.toDto(mjob);

        restMjobMockMvc.perform(post("/api/mjobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mjobDTO)))
            .andExpect(status().isBadRequest());

        List<Mjob> mjobList = mjobRepository.findAll();
        assertThat(mjobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMjobs() throws Exception {
        // Initialize the database
        mjobRepository.saveAndFlush(mjob);

        // Get all the mjobList
        restMjobMockMvc.perform(get("/api/mjobs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mjob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getMjob() throws Exception {
        // Initialize the database
        mjobRepository.saveAndFlush(mjob);

        // Get the mjob
        restMjobMockMvc.perform(get("/api/mjobs/{id}", mjob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mjob.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.jobDescription").value(DEFAULT_JOB_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMjob() throws Exception {
        // Get the mjob
        restMjobMockMvc.perform(get("/api/mjobs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMjob() throws Exception {
        // Initialize the database
        mjobRepository.saveAndFlush(mjob);
        mjobSearchRepository.save(mjob);
        int databaseSizeBeforeUpdate = mjobRepository.findAll().size();

        // Update the mjob
        Mjob updatedMjob = mjobRepository.findOne(mjob.getId());
        updatedMjob
            .name(UPDATED_NAME)
            .jobDescription(UPDATED_JOB_DESCRIPTION)
            .type(UPDATED_TYPE);
        MjobDTO mjobDTO = mjobMapper.toDto(updatedMjob);

        restMjobMockMvc.perform(put("/api/mjobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mjobDTO)))
            .andExpect(status().isOk());

        // Validate the Mjob in the database
        List<Mjob> mjobList = mjobRepository.findAll();
        assertThat(mjobList).hasSize(databaseSizeBeforeUpdate);
        Mjob testMjob = mjobList.get(mjobList.size() - 1);
        assertThat(testMjob.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMjob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
        assertThat(testMjob.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the Mjob in Elasticsearch
        Mjob mjobEs = mjobSearchRepository.findOne(testMjob.getId());
        assertThat(mjobEs).isEqualToComparingFieldByField(testMjob);
    }

    @Test
    @Transactional
    public void updateNonExistingMjob() throws Exception {
        int databaseSizeBeforeUpdate = mjobRepository.findAll().size();

        // Create the Mjob
        MjobDTO mjobDTO = mjobMapper.toDto(mjob);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMjobMockMvc.perform(put("/api/mjobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mjobDTO)))
            .andExpect(status().isCreated());

        // Validate the Mjob in the database
        List<Mjob> mjobList = mjobRepository.findAll();
        assertThat(mjobList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMjob() throws Exception {
        // Initialize the database
        mjobRepository.saveAndFlush(mjob);
        mjobSearchRepository.save(mjob);
        int databaseSizeBeforeDelete = mjobRepository.findAll().size();

        // Get the mjob
        restMjobMockMvc.perform(delete("/api/mjobs/{id}", mjob.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean mjobExistsInEs = mjobSearchRepository.exists(mjob.getId());
        assertThat(mjobExistsInEs).isFalse();

        // Validate the database is empty
        List<Mjob> mjobList = mjobRepository.findAll();
        assertThat(mjobList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMjob() throws Exception {
        // Initialize the database
        mjobRepository.saveAndFlush(mjob);
        mjobSearchRepository.save(mjob);

        // Search the mjob
        restMjobMockMvc.perform(get("/api/_search/mjobs?query=id:" + mjob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mjob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mjob.class);
        Mjob mjob1 = new Mjob();
        mjob1.setId(1L);
        Mjob mjob2 = new Mjob();
        mjob2.setId(mjob1.getId());
        assertThat(mjob1).isEqualTo(mjob2);
        mjob2.setId(2L);
        assertThat(mjob1).isNotEqualTo(mjob2);
        mjob1.setId(null);
        assertThat(mjob1).isNotEqualTo(mjob2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MjobDTO.class);
        MjobDTO mjobDTO1 = new MjobDTO();
        mjobDTO1.setId(1L);
        MjobDTO mjobDTO2 = new MjobDTO();
        assertThat(mjobDTO1).isNotEqualTo(mjobDTO2);
        mjobDTO2.setId(mjobDTO1.getId());
        assertThat(mjobDTO1).isEqualTo(mjobDTO2);
        mjobDTO2.setId(2L);
        assertThat(mjobDTO1).isNotEqualTo(mjobDTO2);
        mjobDTO1.setId(null);
        assertThat(mjobDTO1).isNotEqualTo(mjobDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(mjobMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(mjobMapper.fromId(null)).isNull();
    }
}
