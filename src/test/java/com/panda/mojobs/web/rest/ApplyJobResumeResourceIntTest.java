package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.ApplyJobResume;
import com.panda.mojobs.domain.Resume;
import com.panda.mojobs.domain.Mjob;
import com.panda.mojobs.repository.ApplyJobResumeRepository;
import com.panda.mojobs.service.ApplyJobResumeService;
import com.panda.mojobs.repository.search.ApplyJobResumeSearchRepository;
import com.panda.mojobs.service.dto.ApplyJobResumeDTO;
import com.panda.mojobs.service.mapper.ApplyJobResumeMapper;
import com.panda.mojobs.web.rest.errors.ExceptionTranslator;
import com.panda.mojobs.service.dto.ApplyJobResumeCriteria;
import com.panda.mojobs.service.ApplyJobResumeQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.panda.mojobs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ApplyJobResumeResource REST controller.
 *
 * @see ApplyJobResumeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class ApplyJobResumeResourceIntTest {

    private static final LocalDate DEFAULT_APPLY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPLY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private ApplyJobResumeRepository applyJobResumeRepository;

    @Autowired
    private ApplyJobResumeMapper applyJobResumeMapper;

    @Autowired
    private ApplyJobResumeService applyJobResumeService;

    @Autowired
    private ApplyJobResumeSearchRepository applyJobResumeSearchRepository;

    @Autowired
    private ApplyJobResumeQueryService applyJobResumeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApplyJobResumeMockMvc;

    private ApplyJobResume applyJobResume;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApplyJobResumeResource applyJobResumeResource = new ApplyJobResumeResource(applyJobResumeService, applyJobResumeQueryService);
        this.restApplyJobResumeMockMvc = MockMvcBuilders.standaloneSetup(applyJobResumeResource)
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
    public static ApplyJobResume createEntity(EntityManager em) {
        ApplyJobResume applyJobResume = new ApplyJobResume()
            .applyDate(DEFAULT_APPLY_DATE)
            .content(DEFAULT_CONTENT);
        // Add required entity
        Resume resume = ResumeResourceIntTest.createEntity(em);
        em.persist(resume);
        em.flush();
        applyJobResume.setResume(resume);
        // Add required entity
        Mjob mjob = MjobResourceIntTest.createEntity(em);
        em.persist(mjob);
        em.flush();
        applyJobResume.setMjob(mjob);
        return applyJobResume;
    }

    @Before
    public void initTest() {
        applyJobResumeSearchRepository.deleteAll();
        applyJobResume = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplyJobResume() throws Exception {
        int databaseSizeBeforeCreate = applyJobResumeRepository.findAll().size();

        // Create the ApplyJobResume
        ApplyJobResumeDTO applyJobResumeDTO = applyJobResumeMapper.toDto(applyJobResume);
        restApplyJobResumeMockMvc.perform(post("/api/apply-job-resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applyJobResumeDTO)))
            .andExpect(status().isCreated());

        // Validate the ApplyJobResume in the database
        List<ApplyJobResume> applyJobResumeList = applyJobResumeRepository.findAll();
        assertThat(applyJobResumeList).hasSize(databaseSizeBeforeCreate + 1);
        ApplyJobResume testApplyJobResume = applyJobResumeList.get(applyJobResumeList.size() - 1);
        assertThat(testApplyJobResume.getApplyDate()).isEqualTo(DEFAULT_APPLY_DATE);
        assertThat(testApplyJobResume.getContent()).isEqualTo(DEFAULT_CONTENT);

        // Validate the ApplyJobResume in Elasticsearch
        ApplyJobResume applyJobResumeEs = applyJobResumeSearchRepository.findOne(testApplyJobResume.getId());
        assertThat(applyJobResumeEs).isEqualToComparingFieldByField(testApplyJobResume);
    }

    @Test
    @Transactional
    public void createApplyJobResumeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applyJobResumeRepository.findAll().size();

        // Create the ApplyJobResume with an existing ID
        applyJobResume.setId(1L);
        ApplyJobResumeDTO applyJobResumeDTO = applyJobResumeMapper.toDto(applyJobResume);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplyJobResumeMockMvc.perform(post("/api/apply-job-resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applyJobResumeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplyJobResume in the database
        List<ApplyJobResume> applyJobResumeList = applyJobResumeRepository.findAll();
        assertThat(applyJobResumeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkApplyDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = applyJobResumeRepository.findAll().size();
        // set the field null
        applyJobResume.setApplyDate(null);

        // Create the ApplyJobResume, which fails.
        ApplyJobResumeDTO applyJobResumeDTO = applyJobResumeMapper.toDto(applyJobResume);

        restApplyJobResumeMockMvc.perform(post("/api/apply-job-resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applyJobResumeDTO)))
            .andExpect(status().isBadRequest());

        List<ApplyJobResume> applyJobResumeList = applyJobResumeRepository.findAll();
        assertThat(applyJobResumeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = applyJobResumeRepository.findAll().size();
        // set the field null
        applyJobResume.setContent(null);

        // Create the ApplyJobResume, which fails.
        ApplyJobResumeDTO applyJobResumeDTO = applyJobResumeMapper.toDto(applyJobResume);

        restApplyJobResumeMockMvc.perform(post("/api/apply-job-resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applyJobResumeDTO)))
            .andExpect(status().isBadRequest());

        List<ApplyJobResume> applyJobResumeList = applyJobResumeRepository.findAll();
        assertThat(applyJobResumeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplyJobResumes() throws Exception {
        // Initialize the database
        applyJobResumeRepository.saveAndFlush(applyJobResume);

        // Get all the applyJobResumeList
        restApplyJobResumeMockMvc.perform(get("/api/apply-job-resumes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applyJobResume.getId().intValue())))
            .andExpect(jsonPath("$.[*].applyDate").value(hasItem(DEFAULT_APPLY_DATE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getApplyJobResume() throws Exception {
        // Initialize the database
        applyJobResumeRepository.saveAndFlush(applyJobResume);

        // Get the applyJobResume
        restApplyJobResumeMockMvc.perform(get("/api/apply-job-resumes/{id}", applyJobResume.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(applyJobResume.getId().intValue()))
            .andExpect(jsonPath("$.applyDate").value(DEFAULT_APPLY_DATE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getAllApplyJobResumesByApplyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        applyJobResumeRepository.saveAndFlush(applyJobResume);

        // Get all the applyJobResumeList where applyDate equals to DEFAULT_APPLY_DATE
        defaultApplyJobResumeShouldBeFound("applyDate.equals=" + DEFAULT_APPLY_DATE);

        // Get all the applyJobResumeList where applyDate equals to UPDATED_APPLY_DATE
        defaultApplyJobResumeShouldNotBeFound("applyDate.equals=" + UPDATED_APPLY_DATE);
    }

    @Test
    @Transactional
    public void getAllApplyJobResumesByApplyDateIsInShouldWork() throws Exception {
        // Initialize the database
        applyJobResumeRepository.saveAndFlush(applyJobResume);

        // Get all the applyJobResumeList where applyDate in DEFAULT_APPLY_DATE or UPDATED_APPLY_DATE
        defaultApplyJobResumeShouldBeFound("applyDate.in=" + DEFAULT_APPLY_DATE + "," + UPDATED_APPLY_DATE);

        // Get all the applyJobResumeList where applyDate equals to UPDATED_APPLY_DATE
        defaultApplyJobResumeShouldNotBeFound("applyDate.in=" + UPDATED_APPLY_DATE);
    }

    @Test
    @Transactional
    public void getAllApplyJobResumesByApplyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        applyJobResumeRepository.saveAndFlush(applyJobResume);

        // Get all the applyJobResumeList where applyDate is not null
        defaultApplyJobResumeShouldBeFound("applyDate.specified=true");

        // Get all the applyJobResumeList where applyDate is null
        defaultApplyJobResumeShouldNotBeFound("applyDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllApplyJobResumesByApplyDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        applyJobResumeRepository.saveAndFlush(applyJobResume);

        // Get all the applyJobResumeList where applyDate greater than or equals to DEFAULT_APPLY_DATE
        defaultApplyJobResumeShouldBeFound("applyDate.greaterOrEqualThan=" + DEFAULT_APPLY_DATE);

        // Get all the applyJobResumeList where applyDate greater than or equals to UPDATED_APPLY_DATE
        defaultApplyJobResumeShouldNotBeFound("applyDate.greaterOrEqualThan=" + UPDATED_APPLY_DATE);
    }

    @Test
    @Transactional
    public void getAllApplyJobResumesByApplyDateIsLessThanSomething() throws Exception {
        // Initialize the database
        applyJobResumeRepository.saveAndFlush(applyJobResume);

        // Get all the applyJobResumeList where applyDate less than or equals to DEFAULT_APPLY_DATE
        defaultApplyJobResumeShouldNotBeFound("applyDate.lessThan=" + DEFAULT_APPLY_DATE);

        // Get all the applyJobResumeList where applyDate less than or equals to UPDATED_APPLY_DATE
        defaultApplyJobResumeShouldBeFound("applyDate.lessThan=" + UPDATED_APPLY_DATE);
    }


    @Test
    @Transactional
    public void getAllApplyJobResumesByResumeIsEqualToSomething() throws Exception {
        // Initialize the database
        Resume resume = ResumeResourceIntTest.createEntity(em);
        em.persist(resume);
        em.flush();
        applyJobResume.setResume(resume);
        applyJobResumeRepository.saveAndFlush(applyJobResume);
        Long resumeId = resume.getId();

        // Get all the applyJobResumeList where resume equals to resumeId
        defaultApplyJobResumeShouldBeFound("resumeId.equals=" + resumeId);

        // Get all the applyJobResumeList where resume equals to resumeId + 1
        defaultApplyJobResumeShouldNotBeFound("resumeId.equals=" + (resumeId + 1));
    }


    @Test
    @Transactional
    public void getAllApplyJobResumesByMjobIsEqualToSomething() throws Exception {
        // Initialize the database
        Mjob mjob = MjobResourceIntTest.createEntity(em);
        em.persist(mjob);
        em.flush();
        applyJobResume.setMjob(mjob);
        applyJobResumeRepository.saveAndFlush(applyJobResume);
        Long mjobId = mjob.getId();

        // Get all the applyJobResumeList where mjob equals to mjobId
        defaultApplyJobResumeShouldBeFound("mjobId.equals=" + mjobId);

        // Get all the applyJobResumeList where mjob equals to mjobId + 1
        defaultApplyJobResumeShouldNotBeFound("mjobId.equals=" + (mjobId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultApplyJobResumeShouldBeFound(String filter) throws Exception {
        restApplyJobResumeMockMvc.perform(get("/api/apply-job-resumes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applyJobResume.getId().intValue())))
            .andExpect(jsonPath("$.[*].applyDate").value(hasItem(DEFAULT_APPLY_DATE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultApplyJobResumeShouldNotBeFound(String filter) throws Exception {
        restApplyJobResumeMockMvc.perform(get("/api/apply-job-resumes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingApplyJobResume() throws Exception {
        // Get the applyJobResume
        restApplyJobResumeMockMvc.perform(get("/api/apply-job-resumes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplyJobResume() throws Exception {
        // Initialize the database
        applyJobResumeRepository.saveAndFlush(applyJobResume);
        applyJobResumeSearchRepository.save(applyJobResume);
        int databaseSizeBeforeUpdate = applyJobResumeRepository.findAll().size();

        // Update the applyJobResume
        ApplyJobResume updatedApplyJobResume = applyJobResumeRepository.findOne(applyJobResume.getId());
        updatedApplyJobResume
            .applyDate(UPDATED_APPLY_DATE)
            .content(UPDATED_CONTENT);
        ApplyJobResumeDTO applyJobResumeDTO = applyJobResumeMapper.toDto(updatedApplyJobResume);

        restApplyJobResumeMockMvc.perform(put("/api/apply-job-resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applyJobResumeDTO)))
            .andExpect(status().isOk());

        // Validate the ApplyJobResume in the database
        List<ApplyJobResume> applyJobResumeList = applyJobResumeRepository.findAll();
        assertThat(applyJobResumeList).hasSize(databaseSizeBeforeUpdate);
        ApplyJobResume testApplyJobResume = applyJobResumeList.get(applyJobResumeList.size() - 1);
        assertThat(testApplyJobResume.getApplyDate()).isEqualTo(UPDATED_APPLY_DATE);
        assertThat(testApplyJobResume.getContent()).isEqualTo(UPDATED_CONTENT);

        // Validate the ApplyJobResume in Elasticsearch
        ApplyJobResume applyJobResumeEs = applyJobResumeSearchRepository.findOne(testApplyJobResume.getId());
        assertThat(applyJobResumeEs).isEqualToComparingFieldByField(testApplyJobResume);
    }

    @Test
    @Transactional
    public void updateNonExistingApplyJobResume() throws Exception {
        int databaseSizeBeforeUpdate = applyJobResumeRepository.findAll().size();

        // Create the ApplyJobResume
        ApplyJobResumeDTO applyJobResumeDTO = applyJobResumeMapper.toDto(applyJobResume);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restApplyJobResumeMockMvc.perform(put("/api/apply-job-resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applyJobResumeDTO)))
            .andExpect(status().isCreated());

        // Validate the ApplyJobResume in the database
        List<ApplyJobResume> applyJobResumeList = applyJobResumeRepository.findAll();
        assertThat(applyJobResumeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteApplyJobResume() throws Exception {
        // Initialize the database
        applyJobResumeRepository.saveAndFlush(applyJobResume);
        applyJobResumeSearchRepository.save(applyJobResume);
        int databaseSizeBeforeDelete = applyJobResumeRepository.findAll().size();

        // Get the applyJobResume
        restApplyJobResumeMockMvc.perform(delete("/api/apply-job-resumes/{id}", applyJobResume.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean applyJobResumeExistsInEs = applyJobResumeSearchRepository.exists(applyJobResume.getId());
        assertThat(applyJobResumeExistsInEs).isFalse();

        // Validate the database is empty
        List<ApplyJobResume> applyJobResumeList = applyJobResumeRepository.findAll();
        assertThat(applyJobResumeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchApplyJobResume() throws Exception {
        // Initialize the database
        applyJobResumeRepository.saveAndFlush(applyJobResume);
        applyJobResumeSearchRepository.save(applyJobResume);

        // Search the applyJobResume
        restApplyJobResumeMockMvc.perform(get("/api/_search/apply-job-resumes?query=id:" + applyJobResume.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applyJobResume.getId().intValue())))
            .andExpect(jsonPath("$.[*].applyDate").value(hasItem(DEFAULT_APPLY_DATE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplyJobResume.class);
        ApplyJobResume applyJobResume1 = new ApplyJobResume();
        applyJobResume1.setId(1L);
        ApplyJobResume applyJobResume2 = new ApplyJobResume();
        applyJobResume2.setId(applyJobResume1.getId());
        assertThat(applyJobResume1).isEqualTo(applyJobResume2);
        applyJobResume2.setId(2L);
        assertThat(applyJobResume1).isNotEqualTo(applyJobResume2);
        applyJobResume1.setId(null);
        assertThat(applyJobResume1).isNotEqualTo(applyJobResume2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplyJobResumeDTO.class);
        ApplyJobResumeDTO applyJobResumeDTO1 = new ApplyJobResumeDTO();
        applyJobResumeDTO1.setId(1L);
        ApplyJobResumeDTO applyJobResumeDTO2 = new ApplyJobResumeDTO();
        assertThat(applyJobResumeDTO1).isNotEqualTo(applyJobResumeDTO2);
        applyJobResumeDTO2.setId(applyJobResumeDTO1.getId());
        assertThat(applyJobResumeDTO1).isEqualTo(applyJobResumeDTO2);
        applyJobResumeDTO2.setId(2L);
        assertThat(applyJobResumeDTO1).isNotEqualTo(applyJobResumeDTO2);
        applyJobResumeDTO1.setId(null);
        assertThat(applyJobResumeDTO1).isNotEqualTo(applyJobResumeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(applyJobResumeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(applyJobResumeMapper.fromId(null)).isNull();
    }
}
