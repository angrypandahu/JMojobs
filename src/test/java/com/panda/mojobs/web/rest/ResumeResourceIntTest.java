package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.Resume;
import com.panda.mojobs.domain.BasicInformation;
import com.panda.mojobs.domain.Experience;
import com.panda.mojobs.domain.EducationBackground;
import com.panda.mojobs.domain.ProfessionalDevelopment;
import com.panda.mojobs.domain.MLanguage;
import com.panda.mojobs.domain.User;
import com.panda.mojobs.repository.ResumeRepository;
import com.panda.mojobs.service.ResumeService;
import com.panda.mojobs.repository.search.ResumeSearchRepository;
import com.panda.mojobs.service.dto.ResumeDTO;
import com.panda.mojobs.service.mapper.ResumeMapper;
import com.panda.mojobs.web.rest.errors.ExceptionTranslator;
import com.panda.mojobs.service.dto.ResumeCriteria;
import com.panda.mojobs.service.ResumeQueryService;

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

import com.panda.mojobs.domain.enumeration.CanBeReadBy;
/**
 * Test class for the ResumeResource REST controller.
 *
 * @see ResumeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class ResumeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final CanBeReadBy DEFAULT_CAN_BE_READ_BY = CanBeReadBy.ALL_SCHOOLS;
    private static final CanBeReadBy UPDATED_CAN_BE_READ_BY = CanBeReadBy.UNBLOCKED_SCHOOLS;

    private static final String DEFAULT_OTHERS = "AAAAAAAAAA";
    private static final String UPDATED_OTHERS = "BBBBBBBBBB";

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private ResumeSearchRepository resumeSearchRepository;

    @Autowired
    private ResumeQueryService resumeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResumeMockMvc;

    private Resume resume;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResumeResource resumeResource = new ResumeResource(resumeService, resumeQueryService);
        this.restResumeMockMvc = MockMvcBuilders.standaloneSetup(resumeResource)
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
    public static Resume createEntity(EntityManager em) {
        Resume resume = new Resume()
            .name(DEFAULT_NAME)
            .canBeReadBy(DEFAULT_CAN_BE_READ_BY)
            .others(DEFAULT_OTHERS);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        resume.setUser(user);
        return resume;
    }

    @Before
    public void initTest() {
        resumeSearchRepository.deleteAll();
        resume = createEntity(em);
    }

    @Test
    @Transactional
    public void createResume() throws Exception {
        int databaseSizeBeforeCreate = resumeRepository.findAll().size();

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);
        restResumeMockMvc.perform(post("/api/resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resumeDTO)))
            .andExpect(status().isCreated());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeCreate + 1);
        Resume testResume = resumeList.get(resumeList.size() - 1);
        assertThat(testResume.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testResume.getCanBeReadBy()).isEqualTo(DEFAULT_CAN_BE_READ_BY);
        assertThat(testResume.getOthers()).isEqualTo(DEFAULT_OTHERS);

        // Validate the Resume in Elasticsearch
        Resume resumeEs = resumeSearchRepository.findOne(testResume.getId());
        assertThat(resumeEs).isEqualToComparingFieldByField(testResume);
    }

    @Test
    @Transactional
    public void createResumeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resumeRepository.findAll().size();

        // Create the Resume with an existing ID
        resume.setId(1L);
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResumeMockMvc.perform(post("/api/resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resumeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = resumeRepository.findAll().size();
        // set the field null
        resume.setName(null);

        // Create the Resume, which fails.
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        restResumeMockMvc.perform(post("/api/resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resumeDTO)))
            .andExpect(status().isBadRequest());

        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResumes() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumeList
        restResumeMockMvc.perform(get("/api/resumes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resume.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].canBeReadBy").value(hasItem(DEFAULT_CAN_BE_READ_BY.toString())))
            .andExpect(jsonPath("$.[*].others").value(hasItem(DEFAULT_OTHERS.toString())));
    }

    @Test
    @Transactional
    public void getResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get the resume
        restResumeMockMvc.perform(get("/api/resumes/{id}", resume.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resume.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.canBeReadBy").value(DEFAULT_CAN_BE_READ_BY.toString()))
            .andExpect(jsonPath("$.others").value(DEFAULT_OTHERS.toString()));
    }

    @Test
    @Transactional
    public void getAllResumesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumeList where name equals to DEFAULT_NAME
        defaultResumeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the resumeList where name equals to UPDATED_NAME
        defaultResumeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllResumesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultResumeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the resumeList where name equals to UPDATED_NAME
        defaultResumeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllResumesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumeList where name is not null
        defaultResumeShouldBeFound("name.specified=true");

        // Get all the resumeList where name is null
        defaultResumeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllResumesByCanBeReadByIsEqualToSomething() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumeList where canBeReadBy equals to DEFAULT_CAN_BE_READ_BY
        defaultResumeShouldBeFound("canBeReadBy.equals=" + DEFAULT_CAN_BE_READ_BY);

        // Get all the resumeList where canBeReadBy equals to UPDATED_CAN_BE_READ_BY
        defaultResumeShouldNotBeFound("canBeReadBy.equals=" + UPDATED_CAN_BE_READ_BY);
    }

    @Test
    @Transactional
    public void getAllResumesByCanBeReadByIsInShouldWork() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumeList where canBeReadBy in DEFAULT_CAN_BE_READ_BY or UPDATED_CAN_BE_READ_BY
        defaultResumeShouldBeFound("canBeReadBy.in=" + DEFAULT_CAN_BE_READ_BY + "," + UPDATED_CAN_BE_READ_BY);

        // Get all the resumeList where canBeReadBy equals to UPDATED_CAN_BE_READ_BY
        defaultResumeShouldNotBeFound("canBeReadBy.in=" + UPDATED_CAN_BE_READ_BY);
    }

    @Test
    @Transactional
    public void getAllResumesByCanBeReadByIsNullOrNotNull() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumeList where canBeReadBy is not null
        defaultResumeShouldBeFound("canBeReadBy.specified=true");

        // Get all the resumeList where canBeReadBy is null
        defaultResumeShouldNotBeFound("canBeReadBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllResumesByOthersIsEqualToSomething() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumeList where others equals to DEFAULT_OTHERS
        defaultResumeShouldBeFound("others.equals=" + DEFAULT_OTHERS);

        // Get all the resumeList where others equals to UPDATED_OTHERS
        defaultResumeShouldNotBeFound("others.equals=" + UPDATED_OTHERS);
    }

    @Test
    @Transactional
    public void getAllResumesByOthersIsInShouldWork() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumeList where others in DEFAULT_OTHERS or UPDATED_OTHERS
        defaultResumeShouldBeFound("others.in=" + DEFAULT_OTHERS + "," + UPDATED_OTHERS);

        // Get all the resumeList where others equals to UPDATED_OTHERS
        defaultResumeShouldNotBeFound("others.in=" + UPDATED_OTHERS);
    }

    @Test
    @Transactional
    public void getAllResumesByOthersIsNullOrNotNull() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumeList where others is not null
        defaultResumeShouldBeFound("others.specified=true");

        // Get all the resumeList where others is null
        defaultResumeShouldNotBeFound("others.specified=false");
    }

    @Test
    @Transactional
    public void getAllResumesByBasicInformationIsEqualToSomething() throws Exception {
        // Initialize the database
        BasicInformation basicInformation = BasicInformationResourceIntTest.createEntity(em);
        em.persist(basicInformation);
        em.flush();
        resume.setBasicInformation(basicInformation);
        resumeRepository.saveAndFlush(resume);
        Long basicInformationId = basicInformation.getId();

        // Get all the resumeList where basicInformation equals to basicInformationId
        defaultResumeShouldBeFound("basicInformationId.equals=" + basicInformationId);

        // Get all the resumeList where basicInformation equals to basicInformationId + 1
        defaultResumeShouldNotBeFound("basicInformationId.equals=" + (basicInformationId + 1));
    }


    @Test
    @Transactional
    public void getAllResumesByExperienciesIsEqualToSomething() throws Exception {
        // Initialize the database
        Experience experiencies = ExperienceResourceIntTest.createEntity(em);
        em.persist(experiencies);
        em.flush();
        resume.addExperiencies(experiencies);
        resumeRepository.saveAndFlush(resume);
        Long experienciesId = experiencies.getId();

        // Get all the resumeList where experiencies equals to experienciesId
        defaultResumeShouldBeFound("experienciesId.equals=" + experienciesId);

        // Get all the resumeList where experiencies equals to experienciesId + 1
        defaultResumeShouldNotBeFound("experienciesId.equals=" + (experienciesId + 1));
    }


    @Test
    @Transactional
    public void getAllResumesByEducationBackgroundsIsEqualToSomething() throws Exception {
        // Initialize the database
        EducationBackground educationBackgrounds = EducationBackgroundResourceIntTest.createEntity(em);
        em.persist(educationBackgrounds);
        em.flush();
        resume.addEducationBackgrounds(educationBackgrounds);
        resumeRepository.saveAndFlush(resume);
        Long educationBackgroundsId = educationBackgrounds.getId();

        // Get all the resumeList where educationBackgrounds equals to educationBackgroundsId
        defaultResumeShouldBeFound("educationBackgroundsId.equals=" + educationBackgroundsId);

        // Get all the resumeList where educationBackgrounds equals to educationBackgroundsId + 1
        defaultResumeShouldNotBeFound("educationBackgroundsId.equals=" + (educationBackgroundsId + 1));
    }


    @Test
    @Transactional
    public void getAllResumesByProfessionalDevelopmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        ProfessionalDevelopment professionalDevelopments = ProfessionalDevelopmentResourceIntTest.createEntity(em);
        em.persist(professionalDevelopments);
        em.flush();
        resume.addProfessionalDevelopments(professionalDevelopments);
        resumeRepository.saveAndFlush(resume);
        Long professionalDevelopmentsId = professionalDevelopments.getId();

        // Get all the resumeList where professionalDevelopments equals to professionalDevelopmentsId
        defaultResumeShouldBeFound("professionalDevelopmentsId.equals=" + professionalDevelopmentsId);

        // Get all the resumeList where professionalDevelopments equals to professionalDevelopmentsId + 1
        defaultResumeShouldNotBeFound("professionalDevelopmentsId.equals=" + (professionalDevelopmentsId + 1));
    }


    @Test
    @Transactional
    public void getAllResumesByLanguagesIsEqualToSomething() throws Exception {
        // Initialize the database
        MLanguage languages = MLanguageResourceIntTest.createEntity(em);
        em.persist(languages);
        em.flush();
        resume.addLanguages(languages);
        resumeRepository.saveAndFlush(resume);
        Long languagesId = languages.getId();

        // Get all the resumeList where languages equals to languagesId
        defaultResumeShouldBeFound("languagesId.equals=" + languagesId);

        // Get all the resumeList where languages equals to languagesId + 1
        defaultResumeShouldNotBeFound("languagesId.equals=" + (languagesId + 1));
    }


    @Test
    @Transactional
    public void getAllResumesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        resume.setUser(user);
        resumeRepository.saveAndFlush(resume);
        Long userId = user.getId();

        // Get all the resumeList where user equals to userId
        defaultResumeShouldBeFound("userId.equals=" + userId);

        // Get all the resumeList where user equals to userId + 1
        defaultResumeShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultResumeShouldBeFound(String filter) throws Exception {
        restResumeMockMvc.perform(get("/api/resumes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resume.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].canBeReadBy").value(hasItem(DEFAULT_CAN_BE_READ_BY.toString())))
            .andExpect(jsonPath("$.[*].others").value(hasItem(DEFAULT_OTHERS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultResumeShouldNotBeFound(String filter) throws Exception {
        restResumeMockMvc.perform(get("/api/resumes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingResume() throws Exception {
        // Get the resume
        restResumeMockMvc.perform(get("/api/resumes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);
        resumeSearchRepository.save(resume);
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();

        // Update the resume
        Resume updatedResume = resumeRepository.findOne(resume.getId());
        updatedResume
            .name(UPDATED_NAME)
            .canBeReadBy(UPDATED_CAN_BE_READ_BY)
            .others(UPDATED_OTHERS);
        ResumeDTO resumeDTO = resumeMapper.toDto(updatedResume);

        restResumeMockMvc.perform(put("/api/resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resumeDTO)))
            .andExpect(status().isOk());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);
        Resume testResume = resumeList.get(resumeList.size() - 1);
        assertThat(testResume.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResume.getCanBeReadBy()).isEqualTo(UPDATED_CAN_BE_READ_BY);
        assertThat(testResume.getOthers()).isEqualTo(UPDATED_OTHERS);

        // Validate the Resume in Elasticsearch
        Resume resumeEs = resumeSearchRepository.findOne(testResume.getId());
        assertThat(resumeEs).isEqualToComparingFieldByField(testResume);
    }

    @Test
    @Transactional
    public void updateNonExistingResume() throws Exception {
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restResumeMockMvc.perform(put("/api/resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resumeDTO)))
            .andExpect(status().isCreated());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);
        resumeSearchRepository.save(resume);
        int databaseSizeBeforeDelete = resumeRepository.findAll().size();

        // Get the resume
        restResumeMockMvc.perform(delete("/api/resumes/{id}", resume.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean resumeExistsInEs = resumeSearchRepository.exists(resume.getId());
        assertThat(resumeExistsInEs).isFalse();

        // Validate the database is empty
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);
        resumeSearchRepository.save(resume);

        // Search the resume
        restResumeMockMvc.perform(get("/api/_search/resumes?query=id:" + resume.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resume.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].canBeReadBy").value(hasItem(DEFAULT_CAN_BE_READ_BY.toString())))
            .andExpect(jsonPath("$.[*].others").value(hasItem(DEFAULT_OTHERS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resume.class);
        Resume resume1 = new Resume();
        resume1.setId(1L);
        Resume resume2 = new Resume();
        resume2.setId(resume1.getId());
        assertThat(resume1).isEqualTo(resume2);
        resume2.setId(2L);
        assertThat(resume1).isNotEqualTo(resume2);
        resume1.setId(null);
        assertThat(resume1).isNotEqualTo(resume2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResumeDTO.class);
        ResumeDTO resumeDTO1 = new ResumeDTO();
        resumeDTO1.setId(1L);
        ResumeDTO resumeDTO2 = new ResumeDTO();
        assertThat(resumeDTO1).isNotEqualTo(resumeDTO2);
        resumeDTO2.setId(resumeDTO1.getId());
        assertThat(resumeDTO1).isEqualTo(resumeDTO2);
        resumeDTO2.setId(2L);
        assertThat(resumeDTO1).isNotEqualTo(resumeDTO2);
        resumeDTO1.setId(null);
        assertThat(resumeDTO1).isNotEqualTo(resumeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(resumeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(resumeMapper.fromId(null)).isNull();
    }
}
