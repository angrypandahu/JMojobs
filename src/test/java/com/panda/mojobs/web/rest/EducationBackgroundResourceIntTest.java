package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.EducationBackground;
import com.panda.mojobs.repository.EducationBackgroundRepository;
import com.panda.mojobs.service.EducationBackgroundService;
import com.panda.mojobs.repository.search.EducationBackgroundSearchRepository;
import com.panda.mojobs.service.dto.EducationBackgroundDTO;
import com.panda.mojobs.service.mapper.EducationBackgroundMapper;
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

import com.panda.mojobs.domain.enumeration.EducationLevel;
/**
 * Test class for the EducationBackgroundResource REST controller.
 *
 * @see EducationBackgroundResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class EducationBackgroundResourceIntTest {

    private static final String DEFAULT_SCHOOL = "AAAAAAAAAA";
    private static final String UPDATED_SCHOOL = "BBBBBBBBBB";

    private static final String DEFAULT_MAJOR = "AAAAAAAAAA";
    private static final String UPDATED_MAJOR = "BBBBBBBBBB";

    private static final EducationLevel DEFAULT_DEGREE = EducationLevel.ASSOCIATE;
    private static final EducationLevel UPDATED_DEGREE = EducationLevel.BACHELOR;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TO_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private EducationBackgroundRepository educationBackgroundRepository;

    @Autowired
    private EducationBackgroundMapper educationBackgroundMapper;

    @Autowired
    private EducationBackgroundService educationBackgroundService;

    @Autowired
    private EducationBackgroundSearchRepository educationBackgroundSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEducationBackgroundMockMvc;

    private EducationBackground educationBackground;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EducationBackgroundResource educationBackgroundResource = new EducationBackgroundResource(educationBackgroundService);
        this.restEducationBackgroundMockMvc = MockMvcBuilders.standaloneSetup(educationBackgroundResource)
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
    public static EducationBackground createEntity(EntityManager em) {
        EducationBackground educationBackground = new EducationBackground()
            .school(DEFAULT_SCHOOL)
            .major(DEFAULT_MAJOR)
            .degree(DEFAULT_DEGREE)
            .location(DEFAULT_LOCATION)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE);
        return educationBackground;
    }

    @Before
    public void initTest() {
        educationBackgroundSearchRepository.deleteAll();
        educationBackground = createEntity(em);
    }

    @Test
    @Transactional
    public void createEducationBackground() throws Exception {
        int databaseSizeBeforeCreate = educationBackgroundRepository.findAll().size();

        // Create the EducationBackground
        EducationBackgroundDTO educationBackgroundDTO = educationBackgroundMapper.toDto(educationBackground);
        restEducationBackgroundMockMvc.perform(post("/api/education-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationBackgroundDTO)))
            .andExpect(status().isCreated());

        // Validate the EducationBackground in the database
        List<EducationBackground> educationBackgroundList = educationBackgroundRepository.findAll();
        assertThat(educationBackgroundList).hasSize(databaseSizeBeforeCreate + 1);
        EducationBackground testEducationBackground = educationBackgroundList.get(educationBackgroundList.size() - 1);
        assertThat(testEducationBackground.getSchool()).isEqualTo(DEFAULT_SCHOOL);
        assertThat(testEducationBackground.getMajor()).isEqualTo(DEFAULT_MAJOR);
        assertThat(testEducationBackground.getDegree()).isEqualTo(DEFAULT_DEGREE);
        assertThat(testEducationBackground.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testEducationBackground.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testEducationBackground.getToDate()).isEqualTo(DEFAULT_TO_DATE);

        // Validate the EducationBackground in Elasticsearch
        EducationBackground educationBackgroundEs = educationBackgroundSearchRepository.findOne(testEducationBackground.getId());
        assertThat(educationBackgroundEs).isEqualToComparingFieldByField(testEducationBackground);
    }

    @Test
    @Transactional
    public void createEducationBackgroundWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = educationBackgroundRepository.findAll().size();

        // Create the EducationBackground with an existing ID
        educationBackground.setId(1L);
        EducationBackgroundDTO educationBackgroundDTO = educationBackgroundMapper.toDto(educationBackground);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEducationBackgroundMockMvc.perform(post("/api/education-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationBackgroundDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EducationBackground in the database
        List<EducationBackground> educationBackgroundList = educationBackgroundRepository.findAll();
        assertThat(educationBackgroundList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSchoolIsRequired() throws Exception {
        int databaseSizeBeforeTest = educationBackgroundRepository.findAll().size();
        // set the field null
        educationBackground.setSchool(null);

        // Create the EducationBackground, which fails.
        EducationBackgroundDTO educationBackgroundDTO = educationBackgroundMapper.toDto(educationBackground);

        restEducationBackgroundMockMvc.perform(post("/api/education-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationBackgroundDTO)))
            .andExpect(status().isBadRequest());

        List<EducationBackground> educationBackgroundList = educationBackgroundRepository.findAll();
        assertThat(educationBackgroundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMajorIsRequired() throws Exception {
        int databaseSizeBeforeTest = educationBackgroundRepository.findAll().size();
        // set the field null
        educationBackground.setMajor(null);

        // Create the EducationBackground, which fails.
        EducationBackgroundDTO educationBackgroundDTO = educationBackgroundMapper.toDto(educationBackground);

        restEducationBackgroundMockMvc.perform(post("/api/education-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationBackgroundDTO)))
            .andExpect(status().isBadRequest());

        List<EducationBackground> educationBackgroundList = educationBackgroundRepository.findAll();
        assertThat(educationBackgroundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDegreeIsRequired() throws Exception {
        int databaseSizeBeforeTest = educationBackgroundRepository.findAll().size();
        // set the field null
        educationBackground.setDegree(null);

        // Create the EducationBackground, which fails.
        EducationBackgroundDTO educationBackgroundDTO = educationBackgroundMapper.toDto(educationBackground);

        restEducationBackgroundMockMvc.perform(post("/api/education-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationBackgroundDTO)))
            .andExpect(status().isBadRequest());

        List<EducationBackground> educationBackgroundList = educationBackgroundRepository.findAll();
        assertThat(educationBackgroundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = educationBackgroundRepository.findAll().size();
        // set the field null
        educationBackground.setLocation(null);

        // Create the EducationBackground, which fails.
        EducationBackgroundDTO educationBackgroundDTO = educationBackgroundMapper.toDto(educationBackground);

        restEducationBackgroundMockMvc.perform(post("/api/education-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationBackgroundDTO)))
            .andExpect(status().isBadRequest());

        List<EducationBackground> educationBackgroundList = educationBackgroundRepository.findAll();
        assertThat(educationBackgroundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFromDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = educationBackgroundRepository.findAll().size();
        // set the field null
        educationBackground.setFromDate(null);

        // Create the EducationBackground, which fails.
        EducationBackgroundDTO educationBackgroundDTO = educationBackgroundMapper.toDto(educationBackground);

        restEducationBackgroundMockMvc.perform(post("/api/education-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationBackgroundDTO)))
            .andExpect(status().isBadRequest());

        List<EducationBackground> educationBackgroundList = educationBackgroundRepository.findAll();
        assertThat(educationBackgroundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkToDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = educationBackgroundRepository.findAll().size();
        // set the field null
        educationBackground.setToDate(null);

        // Create the EducationBackground, which fails.
        EducationBackgroundDTO educationBackgroundDTO = educationBackgroundMapper.toDto(educationBackground);

        restEducationBackgroundMockMvc.perform(post("/api/education-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationBackgroundDTO)))
            .andExpect(status().isBadRequest());

        List<EducationBackground> educationBackgroundList = educationBackgroundRepository.findAll();
        assertThat(educationBackgroundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEducationBackgrounds() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList
        restEducationBackgroundMockMvc.perform(get("/api/education-backgrounds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(educationBackground.getId().intValue())))
            .andExpect(jsonPath("$.[*].school").value(hasItem(DEFAULT_SCHOOL.toString())))
            .andExpect(jsonPath("$.[*].major").value(hasItem(DEFAULT_MAJOR.toString())))
            .andExpect(jsonPath("$.[*].degree").value(hasItem(DEFAULT_DEGREE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())));
    }

    @Test
    @Transactional
    public void getEducationBackground() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get the educationBackground
        restEducationBackgroundMockMvc.perform(get("/api/education-backgrounds/{id}", educationBackground.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(educationBackground.getId().intValue()))
            .andExpect(jsonPath("$.school").value(DEFAULT_SCHOOL.toString()))
            .andExpect(jsonPath("$.major").value(DEFAULT_MAJOR.toString()))
            .andExpect(jsonPath("$.degree").value(DEFAULT_DEGREE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEducationBackground() throws Exception {
        // Get the educationBackground
        restEducationBackgroundMockMvc.perform(get("/api/education-backgrounds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEducationBackground() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);
        educationBackgroundSearchRepository.save(educationBackground);
        int databaseSizeBeforeUpdate = educationBackgroundRepository.findAll().size();

        // Update the educationBackground
        EducationBackground updatedEducationBackground = educationBackgroundRepository.findOne(educationBackground.getId());
        updatedEducationBackground
            .school(UPDATED_SCHOOL)
            .major(UPDATED_MAJOR)
            .degree(UPDATED_DEGREE)
            .location(UPDATED_LOCATION)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE);
        EducationBackgroundDTO educationBackgroundDTO = educationBackgroundMapper.toDto(updatedEducationBackground);

        restEducationBackgroundMockMvc.perform(put("/api/education-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationBackgroundDTO)))
            .andExpect(status().isOk());

        // Validate the EducationBackground in the database
        List<EducationBackground> educationBackgroundList = educationBackgroundRepository.findAll();
        assertThat(educationBackgroundList).hasSize(databaseSizeBeforeUpdate);
        EducationBackground testEducationBackground = educationBackgroundList.get(educationBackgroundList.size() - 1);
        assertThat(testEducationBackground.getSchool()).isEqualTo(UPDATED_SCHOOL);
        assertThat(testEducationBackground.getMajor()).isEqualTo(UPDATED_MAJOR);
        assertThat(testEducationBackground.getDegree()).isEqualTo(UPDATED_DEGREE);
        assertThat(testEducationBackground.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testEducationBackground.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testEducationBackground.getToDate()).isEqualTo(UPDATED_TO_DATE);

        // Validate the EducationBackground in Elasticsearch
        EducationBackground educationBackgroundEs = educationBackgroundSearchRepository.findOne(testEducationBackground.getId());
        assertThat(educationBackgroundEs).isEqualToComparingFieldByField(testEducationBackground);
    }

    @Test
    @Transactional
    public void updateNonExistingEducationBackground() throws Exception {
        int databaseSizeBeforeUpdate = educationBackgroundRepository.findAll().size();

        // Create the EducationBackground
        EducationBackgroundDTO educationBackgroundDTO = educationBackgroundMapper.toDto(educationBackground);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEducationBackgroundMockMvc.perform(put("/api/education-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationBackgroundDTO)))
            .andExpect(status().isCreated());

        // Validate the EducationBackground in the database
        List<EducationBackground> educationBackgroundList = educationBackgroundRepository.findAll();
        assertThat(educationBackgroundList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEducationBackground() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);
        educationBackgroundSearchRepository.save(educationBackground);
        int databaseSizeBeforeDelete = educationBackgroundRepository.findAll().size();

        // Get the educationBackground
        restEducationBackgroundMockMvc.perform(delete("/api/education-backgrounds/{id}", educationBackground.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean educationBackgroundExistsInEs = educationBackgroundSearchRepository.exists(educationBackground.getId());
        assertThat(educationBackgroundExistsInEs).isFalse();

        // Validate the database is empty
        List<EducationBackground> educationBackgroundList = educationBackgroundRepository.findAll();
        assertThat(educationBackgroundList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEducationBackground() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);
        educationBackgroundSearchRepository.save(educationBackground);

        // Search the educationBackground
        restEducationBackgroundMockMvc.perform(get("/api/_search/education-backgrounds?query=id:" + educationBackground.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(educationBackground.getId().intValue())))
            .andExpect(jsonPath("$.[*].school").value(hasItem(DEFAULT_SCHOOL.toString())))
            .andExpect(jsonPath("$.[*].major").value(hasItem(DEFAULT_MAJOR.toString())))
            .andExpect(jsonPath("$.[*].degree").value(hasItem(DEFAULT_DEGREE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EducationBackground.class);
        EducationBackground educationBackground1 = new EducationBackground();
        educationBackground1.setId(1L);
        EducationBackground educationBackground2 = new EducationBackground();
        educationBackground2.setId(educationBackground1.getId());
        assertThat(educationBackground1).isEqualTo(educationBackground2);
        educationBackground2.setId(2L);
        assertThat(educationBackground1).isNotEqualTo(educationBackground2);
        educationBackground1.setId(null);
        assertThat(educationBackground1).isNotEqualTo(educationBackground2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EducationBackgroundDTO.class);
        EducationBackgroundDTO educationBackgroundDTO1 = new EducationBackgroundDTO();
        educationBackgroundDTO1.setId(1L);
        EducationBackgroundDTO educationBackgroundDTO2 = new EducationBackgroundDTO();
        assertThat(educationBackgroundDTO1).isNotEqualTo(educationBackgroundDTO2);
        educationBackgroundDTO2.setId(educationBackgroundDTO1.getId());
        assertThat(educationBackgroundDTO1).isEqualTo(educationBackgroundDTO2);
        educationBackgroundDTO2.setId(2L);
        assertThat(educationBackgroundDTO1).isNotEqualTo(educationBackgroundDTO2);
        educationBackgroundDTO1.setId(null);
        assertThat(educationBackgroundDTO1).isNotEqualTo(educationBackgroundDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(educationBackgroundMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(educationBackgroundMapper.fromId(null)).isNull();
    }
}
