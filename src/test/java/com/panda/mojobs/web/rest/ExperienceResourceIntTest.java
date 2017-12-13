package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.Experience;
import com.panda.mojobs.domain.Resume;
import com.panda.mojobs.repository.ExperienceRepository;
import com.panda.mojobs.service.ExperienceService;
import com.panda.mojobs.repository.search.ExperienceSearchRepository;
import com.panda.mojobs.service.dto.ExperienceDTO;
import com.panda.mojobs.service.mapper.ExperienceMapper;
import com.panda.mojobs.web.rest.errors.ExceptionTranslator;
import com.panda.mojobs.service.dto.ExperienceCriteria;
import com.panda.mojobs.service.ExperienceQueryService;

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
 * Test class for the ExperienceResource REST controller.
 *
 * @see ExperienceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class ExperienceResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SCHOOL = "AAAAAAAAAA";
    private static final String UPDATED_SCHOOL = "BBBBBBBBBB";

    private static final String DEFAULT_GRADE = "AAAAAAAAAA";
    private static final String UPDATED_GRADE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TO_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_CURRENTLY_WORK_HERE = false;
    private static final Boolean UPDATED_CURRENTLY_WORK_HERE = true;

    private static final String DEFAULT_RESPONSIBILITY = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSIBILITY = "BBBBBBBBBB";

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private ExperienceMapper experienceMapper;

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private ExperienceSearchRepository experienceSearchRepository;

    @Autowired
    private ExperienceQueryService experienceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExperienceMockMvc;

    private Experience experience;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExperienceResource experienceResource = new ExperienceResource(experienceService, experienceQueryService);
        this.restExperienceMockMvc = MockMvcBuilders.standaloneSetup(experienceResource)
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
    public static Experience createEntity(EntityManager em) {
        Experience experience = new Experience()
            .title(DEFAULT_TITLE)
            .school(DEFAULT_SCHOOL)
            .grade(DEFAULT_GRADE)
            .location(DEFAULT_LOCATION)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .currentlyWorkHere(DEFAULT_CURRENTLY_WORK_HERE)
            .responsibility(DEFAULT_RESPONSIBILITY);
        return experience;
    }

    @Before
    public void initTest() {
        experienceSearchRepository.deleteAll();
        experience = createEntity(em);
    }

    @Test
    @Transactional
    public void createExperience() throws Exception {
        int databaseSizeBeforeCreate = experienceRepository.findAll().size();

        // Create the Experience
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);
        restExperienceMockMvc.perform(post("/api/experiences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experienceDTO)))
            .andExpect(status().isCreated());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeCreate + 1);
        Experience testExperience = experienceList.get(experienceList.size() - 1);
        assertThat(testExperience.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testExperience.getSchool()).isEqualTo(DEFAULT_SCHOOL);
        assertThat(testExperience.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testExperience.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testExperience.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testExperience.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testExperience.isCurrentlyWorkHere()).isEqualTo(DEFAULT_CURRENTLY_WORK_HERE);
        assertThat(testExperience.getResponsibility()).isEqualTo(DEFAULT_RESPONSIBILITY);

        // Validate the Experience in Elasticsearch
        Experience experienceEs = experienceSearchRepository.findOne(testExperience.getId());
        assertThat(experienceEs).isEqualToComparingFieldByField(testExperience);
    }

    @Test
    @Transactional
    public void createExperienceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = experienceRepository.findAll().size();

        // Create the Experience with an existing ID
        experience.setId(1L);
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExperienceMockMvc.perform(post("/api/experiences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experienceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = experienceRepository.findAll().size();
        // set the field null
        experience.setTitle(null);

        // Create the Experience, which fails.
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        restExperienceMockMvc.perform(post("/api/experiences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experienceDTO)))
            .andExpect(status().isBadRequest());

        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSchoolIsRequired() throws Exception {
        int databaseSizeBeforeTest = experienceRepository.findAll().size();
        // set the field null
        experience.setSchool(null);

        // Create the Experience, which fails.
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        restExperienceMockMvc.perform(post("/api/experiences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experienceDTO)))
            .andExpect(status().isBadRequest());

        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGradeIsRequired() throws Exception {
        int databaseSizeBeforeTest = experienceRepository.findAll().size();
        // set the field null
        experience.setGrade(null);

        // Create the Experience, which fails.
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        restExperienceMockMvc.perform(post("/api/experiences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experienceDTO)))
            .andExpect(status().isBadRequest());

        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = experienceRepository.findAll().size();
        // set the field null
        experience.setLocation(null);

        // Create the Experience, which fails.
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        restExperienceMockMvc.perform(post("/api/experiences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experienceDTO)))
            .andExpect(status().isBadRequest());

        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResponsibilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = experienceRepository.findAll().size();
        // set the field null
        experience.setResponsibility(null);

        // Create the Experience, which fails.
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        restExperienceMockMvc.perform(post("/api/experiences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experienceDTO)))
            .andExpect(status().isBadRequest());

        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExperiences() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList
        restExperienceMockMvc.perform(get("/api/experiences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experience.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].school").value(hasItem(DEFAULT_SCHOOL.toString())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].currentlyWorkHere").value(hasItem(DEFAULT_CURRENTLY_WORK_HERE.booleanValue())))
            .andExpect(jsonPath("$.[*].responsibility").value(hasItem(DEFAULT_RESPONSIBILITY.toString())));
    }

    @Test
    @Transactional
    public void getExperience() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get the experience
        restExperienceMockMvc.perform(get("/api/experiences/{id}", experience.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(experience.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.school").value(DEFAULT_SCHOOL.toString()))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()))
            .andExpect(jsonPath("$.currentlyWorkHere").value(DEFAULT_CURRENTLY_WORK_HERE.booleanValue()))
            .andExpect(jsonPath("$.responsibility").value(DEFAULT_RESPONSIBILITY.toString()));
    }

    @Test
    @Transactional
    public void getAllExperiencesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where title equals to DEFAULT_TITLE
        defaultExperienceShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the experienceList where title equals to UPDATED_TITLE
        defaultExperienceShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultExperienceShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the experienceList where title equals to UPDATED_TITLE
        defaultExperienceShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where title is not null
        defaultExperienceShouldBeFound("title.specified=true");

        // Get all the experienceList where title is null
        defaultExperienceShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllExperiencesBySchoolIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where school equals to DEFAULT_SCHOOL
        defaultExperienceShouldBeFound("school.equals=" + DEFAULT_SCHOOL);

        // Get all the experienceList where school equals to UPDATED_SCHOOL
        defaultExperienceShouldNotBeFound("school.equals=" + UPDATED_SCHOOL);
    }

    @Test
    @Transactional
    public void getAllExperiencesBySchoolIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where school in DEFAULT_SCHOOL or UPDATED_SCHOOL
        defaultExperienceShouldBeFound("school.in=" + DEFAULT_SCHOOL + "," + UPDATED_SCHOOL);

        // Get all the experienceList where school equals to UPDATED_SCHOOL
        defaultExperienceShouldNotBeFound("school.in=" + UPDATED_SCHOOL);
    }

    @Test
    @Transactional
    public void getAllExperiencesBySchoolIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where school is not null
        defaultExperienceShouldBeFound("school.specified=true");

        // Get all the experienceList where school is null
        defaultExperienceShouldNotBeFound("school.specified=false");
    }

    @Test
    @Transactional
    public void getAllExperiencesByGradeIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where grade equals to DEFAULT_GRADE
        defaultExperienceShouldBeFound("grade.equals=" + DEFAULT_GRADE);

        // Get all the experienceList where grade equals to UPDATED_GRADE
        defaultExperienceShouldNotBeFound("grade.equals=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByGradeIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where grade in DEFAULT_GRADE or UPDATED_GRADE
        defaultExperienceShouldBeFound("grade.in=" + DEFAULT_GRADE + "," + UPDATED_GRADE);

        // Get all the experienceList where grade equals to UPDATED_GRADE
        defaultExperienceShouldNotBeFound("grade.in=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByGradeIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where grade is not null
        defaultExperienceShouldBeFound("grade.specified=true");

        // Get all the experienceList where grade is null
        defaultExperienceShouldNotBeFound("grade.specified=false");
    }

    @Test
    @Transactional
    public void getAllExperiencesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where location equals to DEFAULT_LOCATION
        defaultExperienceShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the experienceList where location equals to UPDATED_LOCATION
        defaultExperienceShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllExperiencesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultExperienceShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the experienceList where location equals to UPDATED_LOCATION
        defaultExperienceShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllExperiencesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where location is not null
        defaultExperienceShouldBeFound("location.specified=true");

        // Get all the experienceList where location is null
        defaultExperienceShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    public void getAllExperiencesByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where fromDate equals to DEFAULT_FROM_DATE
        defaultExperienceShouldBeFound("fromDate.equals=" + DEFAULT_FROM_DATE);

        // Get all the experienceList where fromDate equals to UPDATED_FROM_DATE
        defaultExperienceShouldNotBeFound("fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where fromDate in DEFAULT_FROM_DATE or UPDATED_FROM_DATE
        defaultExperienceShouldBeFound("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE);

        // Get all the experienceList where fromDate equals to UPDATED_FROM_DATE
        defaultExperienceShouldNotBeFound("fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where fromDate is not null
        defaultExperienceShouldBeFound("fromDate.specified=true");

        // Get all the experienceList where fromDate is null
        defaultExperienceShouldNotBeFound("fromDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllExperiencesByFromDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where fromDate greater than or equals to DEFAULT_FROM_DATE
        defaultExperienceShouldBeFound("fromDate.greaterOrEqualThan=" + DEFAULT_FROM_DATE);

        // Get all the experienceList where fromDate greater than or equals to UPDATED_FROM_DATE
        defaultExperienceShouldNotBeFound("fromDate.greaterOrEqualThan=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByFromDateIsLessThanSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where fromDate less than or equals to DEFAULT_FROM_DATE
        defaultExperienceShouldNotBeFound("fromDate.lessThan=" + DEFAULT_FROM_DATE);

        // Get all the experienceList where fromDate less than or equals to UPDATED_FROM_DATE
        defaultExperienceShouldBeFound("fromDate.lessThan=" + UPDATED_FROM_DATE);
    }


    @Test
    @Transactional
    public void getAllExperiencesByToDateIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where toDate equals to DEFAULT_TO_DATE
        defaultExperienceShouldBeFound("toDate.equals=" + DEFAULT_TO_DATE);

        // Get all the experienceList where toDate equals to UPDATED_TO_DATE
        defaultExperienceShouldNotBeFound("toDate.equals=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByToDateIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where toDate in DEFAULT_TO_DATE or UPDATED_TO_DATE
        defaultExperienceShouldBeFound("toDate.in=" + DEFAULT_TO_DATE + "," + UPDATED_TO_DATE);

        // Get all the experienceList where toDate equals to UPDATED_TO_DATE
        defaultExperienceShouldNotBeFound("toDate.in=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByToDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where toDate is not null
        defaultExperienceShouldBeFound("toDate.specified=true");

        // Get all the experienceList where toDate is null
        defaultExperienceShouldNotBeFound("toDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllExperiencesByToDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where toDate greater than or equals to DEFAULT_TO_DATE
        defaultExperienceShouldBeFound("toDate.greaterOrEqualThan=" + DEFAULT_TO_DATE);

        // Get all the experienceList where toDate greater than or equals to UPDATED_TO_DATE
        defaultExperienceShouldNotBeFound("toDate.greaterOrEqualThan=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByToDateIsLessThanSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where toDate less than or equals to DEFAULT_TO_DATE
        defaultExperienceShouldNotBeFound("toDate.lessThan=" + DEFAULT_TO_DATE);

        // Get all the experienceList where toDate less than or equals to UPDATED_TO_DATE
        defaultExperienceShouldBeFound("toDate.lessThan=" + UPDATED_TO_DATE);
    }


    @Test
    @Transactional
    public void getAllExperiencesByCurrentlyWorkHereIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where currentlyWorkHere equals to DEFAULT_CURRENTLY_WORK_HERE
        defaultExperienceShouldBeFound("currentlyWorkHere.equals=" + DEFAULT_CURRENTLY_WORK_HERE);

        // Get all the experienceList where currentlyWorkHere equals to UPDATED_CURRENTLY_WORK_HERE
        defaultExperienceShouldNotBeFound("currentlyWorkHere.equals=" + UPDATED_CURRENTLY_WORK_HERE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByCurrentlyWorkHereIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where currentlyWorkHere in DEFAULT_CURRENTLY_WORK_HERE or UPDATED_CURRENTLY_WORK_HERE
        defaultExperienceShouldBeFound("currentlyWorkHere.in=" + DEFAULT_CURRENTLY_WORK_HERE + "," + UPDATED_CURRENTLY_WORK_HERE);

        // Get all the experienceList where currentlyWorkHere equals to UPDATED_CURRENTLY_WORK_HERE
        defaultExperienceShouldNotBeFound("currentlyWorkHere.in=" + UPDATED_CURRENTLY_WORK_HERE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByCurrentlyWorkHereIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where currentlyWorkHere is not null
        defaultExperienceShouldBeFound("currentlyWorkHere.specified=true");

        // Get all the experienceList where currentlyWorkHere is null
        defaultExperienceShouldNotBeFound("currentlyWorkHere.specified=false");
    }

    @Test
    @Transactional
    public void getAllExperiencesByResponsibilityIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where responsibility equals to DEFAULT_RESPONSIBILITY
        defaultExperienceShouldBeFound("responsibility.equals=" + DEFAULT_RESPONSIBILITY);

        // Get all the experienceList where responsibility equals to UPDATED_RESPONSIBILITY
        defaultExperienceShouldNotBeFound("responsibility.equals=" + UPDATED_RESPONSIBILITY);
    }

    @Test
    @Transactional
    public void getAllExperiencesByResponsibilityIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where responsibility in DEFAULT_RESPONSIBILITY or UPDATED_RESPONSIBILITY
        defaultExperienceShouldBeFound("responsibility.in=" + DEFAULT_RESPONSIBILITY + "," + UPDATED_RESPONSIBILITY);

        // Get all the experienceList where responsibility equals to UPDATED_RESPONSIBILITY
        defaultExperienceShouldNotBeFound("responsibility.in=" + UPDATED_RESPONSIBILITY);
    }

    @Test
    @Transactional
    public void getAllExperiencesByResponsibilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where responsibility is not null
        defaultExperienceShouldBeFound("responsibility.specified=true");

        // Get all the experienceList where responsibility is null
        defaultExperienceShouldNotBeFound("responsibility.specified=false");
    }

    @Test
    @Transactional
    public void getAllExperiencesByResumeIsEqualToSomething() throws Exception {
        // Initialize the database
        Resume resume = ResumeResourceIntTest.createEntity(em);
        em.persist(resume);
        em.flush();
        experience.setResume(resume);
        experienceRepository.saveAndFlush(experience);
        Long resumeId = resume.getId();

        // Get all the experienceList where resume equals to resumeId
        defaultExperienceShouldBeFound("resumeId.equals=" + resumeId);

        // Get all the experienceList where resume equals to resumeId + 1
        defaultExperienceShouldNotBeFound("resumeId.equals=" + (resumeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultExperienceShouldBeFound(String filter) throws Exception {
        restExperienceMockMvc.perform(get("/api/experiences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experience.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].school").value(hasItem(DEFAULT_SCHOOL.toString())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].currentlyWorkHere").value(hasItem(DEFAULT_CURRENTLY_WORK_HERE.booleanValue())))
            .andExpect(jsonPath("$.[*].responsibility").value(hasItem(DEFAULT_RESPONSIBILITY.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultExperienceShouldNotBeFound(String filter) throws Exception {
        restExperienceMockMvc.perform(get("/api/experiences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingExperience() throws Exception {
        // Get the experience
        restExperienceMockMvc.perform(get("/api/experiences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExperience() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);
        experienceSearchRepository.save(experience);
        int databaseSizeBeforeUpdate = experienceRepository.findAll().size();

        // Update the experience
        Experience updatedExperience = experienceRepository.findOne(experience.getId());
        updatedExperience
            .title(UPDATED_TITLE)
            .school(UPDATED_SCHOOL)
            .grade(UPDATED_GRADE)
            .location(UPDATED_LOCATION)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .currentlyWorkHere(UPDATED_CURRENTLY_WORK_HERE)
            .responsibility(UPDATED_RESPONSIBILITY);
        ExperienceDTO experienceDTO = experienceMapper.toDto(updatedExperience);

        restExperienceMockMvc.perform(put("/api/experiences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experienceDTO)))
            .andExpect(status().isOk());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeUpdate);
        Experience testExperience = experienceList.get(experienceList.size() - 1);
        assertThat(testExperience.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testExperience.getSchool()).isEqualTo(UPDATED_SCHOOL);
        assertThat(testExperience.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testExperience.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testExperience.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testExperience.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testExperience.isCurrentlyWorkHere()).isEqualTo(UPDATED_CURRENTLY_WORK_HERE);
        assertThat(testExperience.getResponsibility()).isEqualTo(UPDATED_RESPONSIBILITY);

        // Validate the Experience in Elasticsearch
        Experience experienceEs = experienceSearchRepository.findOne(testExperience.getId());
        assertThat(experienceEs).isEqualToComparingFieldByField(testExperience);
    }

    @Test
    @Transactional
    public void updateNonExistingExperience() throws Exception {
        int databaseSizeBeforeUpdate = experienceRepository.findAll().size();

        // Create the Experience
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExperienceMockMvc.perform(put("/api/experiences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experienceDTO)))
            .andExpect(status().isCreated());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExperience() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);
        experienceSearchRepository.save(experience);
        int databaseSizeBeforeDelete = experienceRepository.findAll().size();

        // Get the experience
        restExperienceMockMvc.perform(delete("/api/experiences/{id}", experience.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean experienceExistsInEs = experienceSearchRepository.exists(experience.getId());
        assertThat(experienceExistsInEs).isFalse();

        // Validate the database is empty
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchExperience() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);
        experienceSearchRepository.save(experience);

        // Search the experience
        restExperienceMockMvc.perform(get("/api/_search/experiences?query=id:" + experience.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experience.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].school").value(hasItem(DEFAULT_SCHOOL.toString())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].currentlyWorkHere").value(hasItem(DEFAULT_CURRENTLY_WORK_HERE.booleanValue())))
            .andExpect(jsonPath("$.[*].responsibility").value(hasItem(DEFAULT_RESPONSIBILITY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Experience.class);
        Experience experience1 = new Experience();
        experience1.setId(1L);
        Experience experience2 = new Experience();
        experience2.setId(experience1.getId());
        assertThat(experience1).isEqualTo(experience2);
        experience2.setId(2L);
        assertThat(experience1).isNotEqualTo(experience2);
        experience1.setId(null);
        assertThat(experience1).isNotEqualTo(experience2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExperienceDTO.class);
        ExperienceDTO experienceDTO1 = new ExperienceDTO();
        experienceDTO1.setId(1L);
        ExperienceDTO experienceDTO2 = new ExperienceDTO();
        assertThat(experienceDTO1).isNotEqualTo(experienceDTO2);
        experienceDTO2.setId(experienceDTO1.getId());
        assertThat(experienceDTO1).isEqualTo(experienceDTO2);
        experienceDTO2.setId(2L);
        assertThat(experienceDTO1).isNotEqualTo(experienceDTO2);
        experienceDTO1.setId(null);
        assertThat(experienceDTO1).isNotEqualTo(experienceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(experienceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(experienceMapper.fromId(null)).isNull();
    }
}
