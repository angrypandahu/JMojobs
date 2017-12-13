package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.JobSubType;
import com.panda.mojobs.repository.JobSubTypeRepository;
import com.panda.mojobs.service.JobSubTypeService;
import com.panda.mojobs.repository.search.JobSubTypeSearchRepository;
import com.panda.mojobs.service.dto.JobSubTypeDTO;
import com.panda.mojobs.service.mapper.JobSubTypeMapper;
import com.panda.mojobs.web.rest.errors.ExceptionTranslator;
import com.panda.mojobs.service.dto.JobSubTypeCriteria;
import com.panda.mojobs.service.JobSubTypeQueryService;

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
import java.util.List;

import static com.panda.mojobs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.panda.mojobs.domain.enumeration.JobType;
/**
 * Test class for the JobSubTypeResource REST controller.
 *
 * @see JobSubTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class JobSubTypeResourceIntTest {

    private static final JobType DEFAULT_PARENT = JobType.FACULTY;
    private static final JobType UPDATED_PARENT = JobType.PRINCIPAL;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private JobSubTypeRepository jobSubTypeRepository;

    @Autowired
    private JobSubTypeMapper jobSubTypeMapper;

    @Autowired
    private JobSubTypeService jobSubTypeService;

    @Autowired
    private JobSubTypeSearchRepository jobSubTypeSearchRepository;

    @Autowired
    private JobSubTypeQueryService jobSubTypeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobSubTypeMockMvc;

    private JobSubType jobSubType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobSubTypeResource jobSubTypeResource = new JobSubTypeResource(jobSubTypeService, jobSubTypeQueryService);
        this.restJobSubTypeMockMvc = MockMvcBuilders.standaloneSetup(jobSubTypeResource)
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
    public static JobSubType createEntity(EntityManager em) {
        JobSubType jobSubType = new JobSubType()
            .parent(DEFAULT_PARENT)
            .name(DEFAULT_NAME);
        return jobSubType;
    }

    @Before
    public void initTest() {
        jobSubTypeSearchRepository.deleteAll();
        jobSubType = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobSubType() throws Exception {
        int databaseSizeBeforeCreate = jobSubTypeRepository.findAll().size();

        // Create the JobSubType
        JobSubTypeDTO jobSubTypeDTO = jobSubTypeMapper.toDto(jobSubType);
        restJobSubTypeMockMvc.perform(post("/api/job-sub-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobSubTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the JobSubType in the database
        List<JobSubType> jobSubTypeList = jobSubTypeRepository.findAll();
        assertThat(jobSubTypeList).hasSize(databaseSizeBeforeCreate + 1);
        JobSubType testJobSubType = jobSubTypeList.get(jobSubTypeList.size() - 1);
        assertThat(testJobSubType.getParent()).isEqualTo(DEFAULT_PARENT);
        assertThat(testJobSubType.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the JobSubType in Elasticsearch
        JobSubType jobSubTypeEs = jobSubTypeSearchRepository.findOne(testJobSubType.getId());
        assertThat(jobSubTypeEs).isEqualToComparingFieldByField(testJobSubType);
    }

    @Test
    @Transactional
    public void createJobSubTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobSubTypeRepository.findAll().size();

        // Create the JobSubType with an existing ID
        jobSubType.setId(1L);
        JobSubTypeDTO jobSubTypeDTO = jobSubTypeMapper.toDto(jobSubType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobSubTypeMockMvc.perform(post("/api/job-sub-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobSubTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JobSubType in the database
        List<JobSubType> jobSubTypeList = jobSubTypeRepository.findAll();
        assertThat(jobSubTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkParentIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobSubTypeRepository.findAll().size();
        // set the field null
        jobSubType.setParent(null);

        // Create the JobSubType, which fails.
        JobSubTypeDTO jobSubTypeDTO = jobSubTypeMapper.toDto(jobSubType);

        restJobSubTypeMockMvc.perform(post("/api/job-sub-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobSubTypeDTO)))
            .andExpect(status().isBadRequest());

        List<JobSubType> jobSubTypeList = jobSubTypeRepository.findAll();
        assertThat(jobSubTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobSubTypes() throws Exception {
        // Initialize the database
        jobSubTypeRepository.saveAndFlush(jobSubType);

        // Get all the jobSubTypeList
        restJobSubTypeMockMvc.perform(get("/api/job-sub-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobSubType.getId().intValue())))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getJobSubType() throws Exception {
        // Initialize the database
        jobSubTypeRepository.saveAndFlush(jobSubType);

        // Get the jobSubType
        restJobSubTypeMockMvc.perform(get("/api/job-sub-types/{id}", jobSubType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobSubType.getId().intValue()))
            .andExpect(jsonPath("$.parent").value(DEFAULT_PARENT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllJobSubTypesByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        jobSubTypeRepository.saveAndFlush(jobSubType);

        // Get all the jobSubTypeList where parent equals to DEFAULT_PARENT
        defaultJobSubTypeShouldBeFound("parent.equals=" + DEFAULT_PARENT);

        // Get all the jobSubTypeList where parent equals to UPDATED_PARENT
        defaultJobSubTypeShouldNotBeFound("parent.equals=" + UPDATED_PARENT);
    }

    @Test
    @Transactional
    public void getAllJobSubTypesByParentIsInShouldWork() throws Exception {
        // Initialize the database
        jobSubTypeRepository.saveAndFlush(jobSubType);

        // Get all the jobSubTypeList where parent in DEFAULT_PARENT or UPDATED_PARENT
        defaultJobSubTypeShouldBeFound("parent.in=" + DEFAULT_PARENT + "," + UPDATED_PARENT);

        // Get all the jobSubTypeList where parent equals to UPDATED_PARENT
        defaultJobSubTypeShouldNotBeFound("parent.in=" + UPDATED_PARENT);
    }

    @Test
    @Transactional
    public void getAllJobSubTypesByParentIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobSubTypeRepository.saveAndFlush(jobSubType);

        // Get all the jobSubTypeList where parent is not null
        defaultJobSubTypeShouldBeFound("parent.specified=true");

        // Get all the jobSubTypeList where parent is null
        defaultJobSubTypeShouldNotBeFound("parent.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobSubTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        jobSubTypeRepository.saveAndFlush(jobSubType);

        // Get all the jobSubTypeList where name equals to DEFAULT_NAME
        defaultJobSubTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the jobSubTypeList where name equals to UPDATED_NAME
        defaultJobSubTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllJobSubTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        jobSubTypeRepository.saveAndFlush(jobSubType);

        // Get all the jobSubTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultJobSubTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the jobSubTypeList where name equals to UPDATED_NAME
        defaultJobSubTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllJobSubTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobSubTypeRepository.saveAndFlush(jobSubType);

        // Get all the jobSubTypeList where name is not null
        defaultJobSubTypeShouldBeFound("name.specified=true");

        // Get all the jobSubTypeList where name is null
        defaultJobSubTypeShouldNotBeFound("name.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultJobSubTypeShouldBeFound(String filter) throws Exception {
        restJobSubTypeMockMvc.perform(get("/api/job-sub-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobSubType.getId().intValue())))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultJobSubTypeShouldNotBeFound(String filter) throws Exception {
        restJobSubTypeMockMvc.perform(get("/api/job-sub-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingJobSubType() throws Exception {
        // Get the jobSubType
        restJobSubTypeMockMvc.perform(get("/api/job-sub-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobSubType() throws Exception {
        // Initialize the database
        jobSubTypeRepository.saveAndFlush(jobSubType);
        jobSubTypeSearchRepository.save(jobSubType);
        int databaseSizeBeforeUpdate = jobSubTypeRepository.findAll().size();

        // Update the jobSubType
        JobSubType updatedJobSubType = jobSubTypeRepository.findOne(jobSubType.getId());
        updatedJobSubType
            .parent(UPDATED_PARENT)
            .name(UPDATED_NAME);
        JobSubTypeDTO jobSubTypeDTO = jobSubTypeMapper.toDto(updatedJobSubType);

        restJobSubTypeMockMvc.perform(put("/api/job-sub-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobSubTypeDTO)))
            .andExpect(status().isOk());

        // Validate the JobSubType in the database
        List<JobSubType> jobSubTypeList = jobSubTypeRepository.findAll();
        assertThat(jobSubTypeList).hasSize(databaseSizeBeforeUpdate);
        JobSubType testJobSubType = jobSubTypeList.get(jobSubTypeList.size() - 1);
        assertThat(testJobSubType.getParent()).isEqualTo(UPDATED_PARENT);
        assertThat(testJobSubType.getName()).isEqualTo(UPDATED_NAME);

        // Validate the JobSubType in Elasticsearch
        JobSubType jobSubTypeEs = jobSubTypeSearchRepository.findOne(testJobSubType.getId());
        assertThat(jobSubTypeEs).isEqualToComparingFieldByField(testJobSubType);
    }

    @Test
    @Transactional
    public void updateNonExistingJobSubType() throws Exception {
        int databaseSizeBeforeUpdate = jobSubTypeRepository.findAll().size();

        // Create the JobSubType
        JobSubTypeDTO jobSubTypeDTO = jobSubTypeMapper.toDto(jobSubType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobSubTypeMockMvc.perform(put("/api/job-sub-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobSubTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the JobSubType in the database
        List<JobSubType> jobSubTypeList = jobSubTypeRepository.findAll();
        assertThat(jobSubTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobSubType() throws Exception {
        // Initialize the database
        jobSubTypeRepository.saveAndFlush(jobSubType);
        jobSubTypeSearchRepository.save(jobSubType);
        int databaseSizeBeforeDelete = jobSubTypeRepository.findAll().size();

        // Get the jobSubType
        restJobSubTypeMockMvc.perform(delete("/api/job-sub-types/{id}", jobSubType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean jobSubTypeExistsInEs = jobSubTypeSearchRepository.exists(jobSubType.getId());
        assertThat(jobSubTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<JobSubType> jobSubTypeList = jobSubTypeRepository.findAll();
        assertThat(jobSubTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJobSubType() throws Exception {
        // Initialize the database
        jobSubTypeRepository.saveAndFlush(jobSubType);
        jobSubTypeSearchRepository.save(jobSubType);

        // Search the jobSubType
        restJobSubTypeMockMvc.perform(get("/api/_search/job-sub-types?query=id:" + jobSubType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobSubType.getId().intValue())))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobSubType.class);
        JobSubType jobSubType1 = new JobSubType();
        jobSubType1.setId(1L);
        JobSubType jobSubType2 = new JobSubType();
        jobSubType2.setId(jobSubType1.getId());
        assertThat(jobSubType1).isEqualTo(jobSubType2);
        jobSubType2.setId(2L);
        assertThat(jobSubType1).isNotEqualTo(jobSubType2);
        jobSubType1.setId(null);
        assertThat(jobSubType1).isNotEqualTo(jobSubType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobSubTypeDTO.class);
        JobSubTypeDTO jobSubTypeDTO1 = new JobSubTypeDTO();
        jobSubTypeDTO1.setId(1L);
        JobSubTypeDTO jobSubTypeDTO2 = new JobSubTypeDTO();
        assertThat(jobSubTypeDTO1).isNotEqualTo(jobSubTypeDTO2);
        jobSubTypeDTO2.setId(jobSubTypeDTO1.getId());
        assertThat(jobSubTypeDTO1).isEqualTo(jobSubTypeDTO2);
        jobSubTypeDTO2.setId(2L);
        assertThat(jobSubTypeDTO1).isNotEqualTo(jobSubTypeDTO2);
        jobSubTypeDTO1.setId(null);
        assertThat(jobSubTypeDTO1).isNotEqualTo(jobSubTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(jobSubTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(jobSubTypeMapper.fromId(null)).isNull();
    }
}
