package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.School;
import com.panda.mojobs.domain.Address;
import com.panda.mojobs.domain.Image;
import com.panda.mojobs.domain.Mjob;
import com.panda.mojobs.repository.SchoolRepository;
import com.panda.mojobs.service.SchoolService;
import com.panda.mojobs.repository.search.SchoolSearchRepository;
import com.panda.mojobs.service.dto.SchoolDTO;
import com.panda.mojobs.service.mapper.SchoolMapper;
import com.panda.mojobs.web.rest.errors.ExceptionTranslator;
import com.panda.mojobs.service.dto.SchoolCriteria;
import com.panda.mojobs.service.SchoolQueryService;

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

import com.panda.mojobs.domain.enumeration.SchoolLevel;
import com.panda.mojobs.domain.enumeration.SchoolType;
/**
 * Test class for the SchoolResource REST controller.
 *
 * @see SchoolResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class SchoolResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final SchoolLevel DEFAULT_LEVEL = SchoolLevel.KINDERGARTEN;
    private static final SchoolLevel UPDATED_LEVEL = SchoolLevel.ELEMENTARY;

    private static final SchoolType DEFAULT_SCHOOL_TYPE = SchoolType.INTERNATIONAL_BILINGUAL_SCHOOL;
    private static final SchoolType UPDATED_SCHOOL_TYPE = SchoolType.LANGUAGE_TRAINING;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private SchoolSearchRepository schoolSearchRepository;

    @Autowired
    private SchoolQueryService schoolQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSchoolMockMvc;

    private School school;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SchoolResource schoolResource = new SchoolResource(schoolService, schoolQueryService);
        this.restSchoolMockMvc = MockMvcBuilders.standaloneSetup(schoolResource)
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
    public static School createEntity(EntityManager em) {
        School school = new School()
            .name(DEFAULT_NAME)
            .level(DEFAULT_LEVEL)
            .schoolType(DEFAULT_SCHOOL_TYPE);
        return school;
    }

    @Before
    public void initTest() {
        schoolSearchRepository.deleteAll();
        school = createEntity(em);
    }

    @Test
    @Transactional
    public void createSchool() throws Exception {
        int databaseSizeBeforeCreate = schoolRepository.findAll().size();

        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);
        restSchoolMockMvc.perform(post("/api/schools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolDTO)))
            .andExpect(status().isCreated());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeCreate + 1);
        School testSchool = schoolList.get(schoolList.size() - 1);
        assertThat(testSchool.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSchool.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testSchool.getSchoolType()).isEqualTo(DEFAULT_SCHOOL_TYPE);

        // Validate the School in Elasticsearch
        School schoolEs = schoolSearchRepository.findOne(testSchool.getId());
        assertThat(schoolEs).isEqualToComparingFieldByField(testSchool);
    }

    @Test
    @Transactional
    public void createSchoolWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = schoolRepository.findAll().size();

        // Create the School with an existing ID
        school.setId(1L);
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolMockMvc.perform(post("/api/schools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolRepository.findAll().size();
        // set the field null
        school.setName(null);

        // Create the School, which fails.
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        restSchoolMockMvc.perform(post("/api/schools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolDTO)))
            .andExpect(status().isBadRequest());

        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolRepository.findAll().size();
        // set the field null
        school.setLevel(null);

        // Create the School, which fails.
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        restSchoolMockMvc.perform(post("/api/schools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolDTO)))
            .andExpect(status().isBadRequest());

        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSchoolTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolRepository.findAll().size();
        // set the field null
        school.setSchoolType(null);

        // Create the School, which fails.
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        restSchoolMockMvc.perform(post("/api/schools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolDTO)))
            .andExpect(status().isBadRequest());

        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSchools() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList
        restSchoolMockMvc.perform(get("/api/schools?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(school.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].schoolType").value(hasItem(DEFAULT_SCHOOL_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getSchool() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get the school
        restSchoolMockMvc.perform(get("/api/schools/{id}", school.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(school.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.schoolType").value(DEFAULT_SCHOOL_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllSchoolsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where name equals to DEFAULT_NAME
        defaultSchoolShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the schoolList where name equals to UPDATED_NAME
        defaultSchoolShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSchoolsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSchoolShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the schoolList where name equals to UPDATED_NAME
        defaultSchoolShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSchoolsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where name is not null
        defaultSchoolShouldBeFound("name.specified=true");

        // Get all the schoolList where name is null
        defaultSchoolShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllSchoolsByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where level equals to DEFAULT_LEVEL
        defaultSchoolShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the schoolList where level equals to UPDATED_LEVEL
        defaultSchoolShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllSchoolsByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultSchoolShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the schoolList where level equals to UPDATED_LEVEL
        defaultSchoolShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllSchoolsByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where level is not null
        defaultSchoolShouldBeFound("level.specified=true");

        // Get all the schoolList where level is null
        defaultSchoolShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    public void getAllSchoolsBySchoolTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where schoolType equals to DEFAULT_SCHOOL_TYPE
        defaultSchoolShouldBeFound("schoolType.equals=" + DEFAULT_SCHOOL_TYPE);

        // Get all the schoolList where schoolType equals to UPDATED_SCHOOL_TYPE
        defaultSchoolShouldNotBeFound("schoolType.equals=" + UPDATED_SCHOOL_TYPE);
    }

    @Test
    @Transactional
    public void getAllSchoolsBySchoolTypeIsInShouldWork() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where schoolType in DEFAULT_SCHOOL_TYPE or UPDATED_SCHOOL_TYPE
        defaultSchoolShouldBeFound("schoolType.in=" + DEFAULT_SCHOOL_TYPE + "," + UPDATED_SCHOOL_TYPE);

        // Get all the schoolList where schoolType equals to UPDATED_SCHOOL_TYPE
        defaultSchoolShouldNotBeFound("schoolType.in=" + UPDATED_SCHOOL_TYPE);
    }

    @Test
    @Transactional
    public void getAllSchoolsBySchoolTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where schoolType is not null
        defaultSchoolShouldBeFound("schoolType.specified=true");

        // Get all the schoolList where schoolType is null
        defaultSchoolShouldNotBeFound("schoolType.specified=false");
    }

    @Test
    @Transactional
    public void getAllSchoolsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        Address address = AddressResourceIntTest.createEntity(em);
        em.persist(address);
        em.flush();
        school.setAddress(address);
        schoolRepository.saveAndFlush(school);
        Long addressId = address.getId();

        // Get all the schoolList where address equals to addressId
        defaultSchoolShouldBeFound("addressId.equals=" + addressId);

        // Get all the schoolList where address equals to addressId + 1
        defaultSchoolShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }


    @Test
    @Transactional
    public void getAllSchoolsByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        Image image = ImageResourceIntTest.createEntity(em);
        em.persist(image);
        em.flush();
        school.setImage(image);
        schoolRepository.saveAndFlush(school);
        Long imageId = image.getId();

        // Get all the schoolList where image equals to imageId
        defaultSchoolShouldBeFound("imageId.equals=" + imageId);

        // Get all the schoolList where image equals to imageId + 1
        defaultSchoolShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }


    @Test
    @Transactional
    public void getAllSchoolsByJobsIsEqualToSomething() throws Exception {
        // Initialize the database
        Mjob jobs = MjobResourceIntTest.createEntity(em);
        em.persist(jobs);
        em.flush();
        school.addJobs(jobs);
        schoolRepository.saveAndFlush(school);
        Long jobsId = jobs.getId();

        // Get all the schoolList where jobs equals to jobsId
        defaultSchoolShouldBeFound("jobsId.equals=" + jobsId);

        // Get all the schoolList where jobs equals to jobsId + 1
        defaultSchoolShouldNotBeFound("jobsId.equals=" + (jobsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSchoolShouldBeFound(String filter) throws Exception {
        restSchoolMockMvc.perform(get("/api/schools?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(school.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].schoolType").value(hasItem(DEFAULT_SCHOOL_TYPE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSchoolShouldNotBeFound(String filter) throws Exception {
        restSchoolMockMvc.perform(get("/api/schools?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSchool() throws Exception {
        // Get the school
        restSchoolMockMvc.perform(get("/api/schools/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSchool() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);
        schoolSearchRepository.save(school);
        int databaseSizeBeforeUpdate = schoolRepository.findAll().size();

        // Update the school
        School updatedSchool = schoolRepository.findOne(school.getId());
        updatedSchool
            .name(UPDATED_NAME)
            .level(UPDATED_LEVEL)
            .schoolType(UPDATED_SCHOOL_TYPE);
        SchoolDTO schoolDTO = schoolMapper.toDto(updatedSchool);

        restSchoolMockMvc.perform(put("/api/schools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolDTO)))
            .andExpect(status().isOk());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeUpdate);
        School testSchool = schoolList.get(schoolList.size() - 1);
        assertThat(testSchool.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSchool.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testSchool.getSchoolType()).isEqualTo(UPDATED_SCHOOL_TYPE);

        // Validate the School in Elasticsearch
        School schoolEs = schoolSearchRepository.findOne(testSchool.getId());
        assertThat(schoolEs).isEqualToComparingFieldByField(testSchool);
    }

    @Test
    @Transactional
    public void updateNonExistingSchool() throws Exception {
        int databaseSizeBeforeUpdate = schoolRepository.findAll().size();

        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSchoolMockMvc.perform(put("/api/schools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolDTO)))
            .andExpect(status().isCreated());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSchool() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);
        schoolSearchRepository.save(school);
        int databaseSizeBeforeDelete = schoolRepository.findAll().size();

        // Get the school
        restSchoolMockMvc.perform(delete("/api/schools/{id}", school.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean schoolExistsInEs = schoolSearchRepository.exists(school.getId());
        assertThat(schoolExistsInEs).isFalse();

        // Validate the database is empty
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSchool() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);
        schoolSearchRepository.save(school);

        // Search the school
        restSchoolMockMvc.perform(get("/api/_search/schools?query=id:" + school.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(school.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].schoolType").value(hasItem(DEFAULT_SCHOOL_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(School.class);
        School school1 = new School();
        school1.setId(1L);
        School school2 = new School();
        school2.setId(school1.getId());
        assertThat(school1).isEqualTo(school2);
        school2.setId(2L);
        assertThat(school1).isNotEqualTo(school2);
        school1.setId(null);
        assertThat(school1).isNotEqualTo(school2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolDTO.class);
        SchoolDTO schoolDTO1 = new SchoolDTO();
        schoolDTO1.setId(1L);
        SchoolDTO schoolDTO2 = new SchoolDTO();
        assertThat(schoolDTO1).isNotEqualTo(schoolDTO2);
        schoolDTO2.setId(schoolDTO1.getId());
        assertThat(schoolDTO1).isEqualTo(schoolDTO2);
        schoolDTO2.setId(2L);
        assertThat(schoolDTO1).isNotEqualTo(schoolDTO2);
        schoolDTO1.setId(null);
        assertThat(schoolDTO1).isNotEqualTo(schoolDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(schoolMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(schoolMapper.fromId(null)).isNull();
    }
}
