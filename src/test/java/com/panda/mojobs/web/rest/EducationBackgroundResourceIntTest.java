package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.EducationBackground;
import com.panda.mojobs.domain.Resume;
import com.panda.mojobs.repository.EducationBackgroundRepository;
import com.panda.mojobs.service.EducationBackgroundService;
import com.panda.mojobs.repository.search.EducationBackgroundSearchRepository;
import com.panda.mojobs.service.dto.EducationBackgroundDTO;
import com.panda.mojobs.service.mapper.EducationBackgroundMapper;
import com.panda.mojobs.web.rest.errors.ExceptionTranslator;
import com.panda.mojobs.service.dto.EducationBackgroundCriteria;
import com.panda.mojobs.service.EducationBackgroundQueryService;

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
    private EducationBackgroundQueryService educationBackgroundQueryService;

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
        final EducationBackgroundResource educationBackgroundResource = new EducationBackgroundResource(educationBackgroundService, educationBackgroundQueryService);
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
    public void getAllEducationBackgroundsBySchoolIsEqualToSomething() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where school equals to DEFAULT_SCHOOL
        defaultEducationBackgroundShouldBeFound("school.equals=" + DEFAULT_SCHOOL);

        // Get all the educationBackgroundList where school equals to UPDATED_SCHOOL
        defaultEducationBackgroundShouldNotBeFound("school.equals=" + UPDATED_SCHOOL);
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsBySchoolIsInShouldWork() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where school in DEFAULT_SCHOOL or UPDATED_SCHOOL
        defaultEducationBackgroundShouldBeFound("school.in=" + DEFAULT_SCHOOL + "," + UPDATED_SCHOOL);

        // Get all the educationBackgroundList where school equals to UPDATED_SCHOOL
        defaultEducationBackgroundShouldNotBeFound("school.in=" + UPDATED_SCHOOL);
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsBySchoolIsNullOrNotNull() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where school is not null
        defaultEducationBackgroundShouldBeFound("school.specified=true");

        // Get all the educationBackgroundList where school is null
        defaultEducationBackgroundShouldNotBeFound("school.specified=false");
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByMajorIsEqualToSomething() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where major equals to DEFAULT_MAJOR
        defaultEducationBackgroundShouldBeFound("major.equals=" + DEFAULT_MAJOR);

        // Get all the educationBackgroundList where major equals to UPDATED_MAJOR
        defaultEducationBackgroundShouldNotBeFound("major.equals=" + UPDATED_MAJOR);
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByMajorIsInShouldWork() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where major in DEFAULT_MAJOR or UPDATED_MAJOR
        defaultEducationBackgroundShouldBeFound("major.in=" + DEFAULT_MAJOR + "," + UPDATED_MAJOR);

        // Get all the educationBackgroundList where major equals to UPDATED_MAJOR
        defaultEducationBackgroundShouldNotBeFound("major.in=" + UPDATED_MAJOR);
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByMajorIsNullOrNotNull() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where major is not null
        defaultEducationBackgroundShouldBeFound("major.specified=true");

        // Get all the educationBackgroundList where major is null
        defaultEducationBackgroundShouldNotBeFound("major.specified=false");
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByDegreeIsEqualToSomething() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where degree equals to DEFAULT_DEGREE
        defaultEducationBackgroundShouldBeFound("degree.equals=" + DEFAULT_DEGREE);

        // Get all the educationBackgroundList where degree equals to UPDATED_DEGREE
        defaultEducationBackgroundShouldNotBeFound("degree.equals=" + UPDATED_DEGREE);
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByDegreeIsInShouldWork() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where degree in DEFAULT_DEGREE or UPDATED_DEGREE
        defaultEducationBackgroundShouldBeFound("degree.in=" + DEFAULT_DEGREE + "," + UPDATED_DEGREE);

        // Get all the educationBackgroundList where degree equals to UPDATED_DEGREE
        defaultEducationBackgroundShouldNotBeFound("degree.in=" + UPDATED_DEGREE);
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByDegreeIsNullOrNotNull() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where degree is not null
        defaultEducationBackgroundShouldBeFound("degree.specified=true");

        // Get all the educationBackgroundList where degree is null
        defaultEducationBackgroundShouldNotBeFound("degree.specified=false");
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where location equals to DEFAULT_LOCATION
        defaultEducationBackgroundShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the educationBackgroundList where location equals to UPDATED_LOCATION
        defaultEducationBackgroundShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultEducationBackgroundShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the educationBackgroundList where location equals to UPDATED_LOCATION
        defaultEducationBackgroundShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where location is not null
        defaultEducationBackgroundShouldBeFound("location.specified=true");

        // Get all the educationBackgroundList where location is null
        defaultEducationBackgroundShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where fromDate equals to DEFAULT_FROM_DATE
        defaultEducationBackgroundShouldBeFound("fromDate.equals=" + DEFAULT_FROM_DATE);

        // Get all the educationBackgroundList where fromDate equals to UPDATED_FROM_DATE
        defaultEducationBackgroundShouldNotBeFound("fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where fromDate in DEFAULT_FROM_DATE or UPDATED_FROM_DATE
        defaultEducationBackgroundShouldBeFound("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE);

        // Get all the educationBackgroundList where fromDate equals to UPDATED_FROM_DATE
        defaultEducationBackgroundShouldNotBeFound("fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where fromDate is not null
        defaultEducationBackgroundShouldBeFound("fromDate.specified=true");

        // Get all the educationBackgroundList where fromDate is null
        defaultEducationBackgroundShouldNotBeFound("fromDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByFromDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where fromDate greater than or equals to DEFAULT_FROM_DATE
        defaultEducationBackgroundShouldBeFound("fromDate.greaterOrEqualThan=" + DEFAULT_FROM_DATE);

        // Get all the educationBackgroundList where fromDate greater than or equals to UPDATED_FROM_DATE
        defaultEducationBackgroundShouldNotBeFound("fromDate.greaterOrEqualThan=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByFromDateIsLessThanSomething() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where fromDate less than or equals to DEFAULT_FROM_DATE
        defaultEducationBackgroundShouldNotBeFound("fromDate.lessThan=" + DEFAULT_FROM_DATE);

        // Get all the educationBackgroundList where fromDate less than or equals to UPDATED_FROM_DATE
        defaultEducationBackgroundShouldBeFound("fromDate.lessThan=" + UPDATED_FROM_DATE);
    }


    @Test
    @Transactional
    public void getAllEducationBackgroundsByToDateIsEqualToSomething() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where toDate equals to DEFAULT_TO_DATE
        defaultEducationBackgroundShouldBeFound("toDate.equals=" + DEFAULT_TO_DATE);

        // Get all the educationBackgroundList where toDate equals to UPDATED_TO_DATE
        defaultEducationBackgroundShouldNotBeFound("toDate.equals=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByToDateIsInShouldWork() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where toDate in DEFAULT_TO_DATE or UPDATED_TO_DATE
        defaultEducationBackgroundShouldBeFound("toDate.in=" + DEFAULT_TO_DATE + "," + UPDATED_TO_DATE);

        // Get all the educationBackgroundList where toDate equals to UPDATED_TO_DATE
        defaultEducationBackgroundShouldNotBeFound("toDate.in=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByToDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where toDate is not null
        defaultEducationBackgroundShouldBeFound("toDate.specified=true");

        // Get all the educationBackgroundList where toDate is null
        defaultEducationBackgroundShouldNotBeFound("toDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByToDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where toDate greater than or equals to DEFAULT_TO_DATE
        defaultEducationBackgroundShouldBeFound("toDate.greaterOrEqualThan=" + DEFAULT_TO_DATE);

        // Get all the educationBackgroundList where toDate greater than or equals to UPDATED_TO_DATE
        defaultEducationBackgroundShouldNotBeFound("toDate.greaterOrEqualThan=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    public void getAllEducationBackgroundsByToDateIsLessThanSomething() throws Exception {
        // Initialize the database
        educationBackgroundRepository.saveAndFlush(educationBackground);

        // Get all the educationBackgroundList where toDate less than or equals to DEFAULT_TO_DATE
        defaultEducationBackgroundShouldNotBeFound("toDate.lessThan=" + DEFAULT_TO_DATE);

        // Get all the educationBackgroundList where toDate less than or equals to UPDATED_TO_DATE
        defaultEducationBackgroundShouldBeFound("toDate.lessThan=" + UPDATED_TO_DATE);
    }


    @Test
    @Transactional
    public void getAllEducationBackgroundsByResumeIsEqualToSomething() throws Exception {
        // Initialize the database
        Resume resume = ResumeResourceIntTest.createEntity(em);
        em.persist(resume);
        em.flush();
        educationBackground.setResume(resume);
        educationBackgroundRepository.saveAndFlush(educationBackground);
        Long resumeId = resume.getId();

        // Get all the educationBackgroundList where resume equals to resumeId
        defaultEducationBackgroundShouldBeFound("resumeId.equals=" + resumeId);

        // Get all the educationBackgroundList where resume equals to resumeId + 1
        defaultEducationBackgroundShouldNotBeFound("resumeId.equals=" + (resumeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultEducationBackgroundShouldBeFound(String filter) throws Exception {
        restEducationBackgroundMockMvc.perform(get("/api/education-backgrounds?sort=id,desc&" + filter))
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

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultEducationBackgroundShouldNotBeFound(String filter) throws Exception {
        restEducationBackgroundMockMvc.perform(get("/api/education-backgrounds?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
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
