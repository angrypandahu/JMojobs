package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.Experience;
import com.panda.mojobs.repository.ExperienceRepository;
import com.panda.mojobs.service.ExperienceService;
import com.panda.mojobs.repository.search.ExperienceSearchRepository;
import com.panda.mojobs.service.dto.ExperienceDTO;
import com.panda.mojobs.service.mapper.ExperienceMapper;
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
        final ExperienceResource experienceResource = new ExperienceResource(experienceService);
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
