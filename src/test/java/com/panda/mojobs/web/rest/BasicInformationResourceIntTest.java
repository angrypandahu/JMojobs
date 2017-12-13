package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.BasicInformation;
import com.panda.mojobs.domain.Resume;
import com.panda.mojobs.domain.Image;
import com.panda.mojobs.repository.BasicInformationRepository;
import com.panda.mojobs.service.BasicInformationService;
import com.panda.mojobs.repository.search.BasicInformationSearchRepository;
import com.panda.mojobs.service.dto.BasicInformationDTO;
import com.panda.mojobs.service.mapper.BasicInformationMapper;
import com.panda.mojobs.web.rest.errors.ExceptionTranslator;
import com.panda.mojobs.service.dto.BasicInformationCriteria;
import com.panda.mojobs.service.BasicInformationQueryService;

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

import com.panda.mojobs.domain.enumeration.Gender;
import com.panda.mojobs.domain.enumeration.EducationLevel;
/**
 * Test class for the BasicInformationResource REST controller.
 *
 * @see BasicInformationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class BasicInformationResourceIntTest {

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final LocalDate DEFAULT_DATEOF_BRITH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEOF_BRITH = LocalDate.now(ZoneId.systemDefault());

    private static final EducationLevel DEFAULT_EDUCATION_LEVEL = EducationLevel.ASSOCIATE;
    private static final EducationLevel UPDATED_EDUCATION_LEVEL = EducationLevel.BACHELOR;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_SKYPE = "AAAAAAAAAA";
    private static final String UPDATED_SKYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_WECHAT = "AAAAAAAAAA";
    private static final String UPDATED_WECHAT = "BBBBBBBBBB";

    @Autowired
    private BasicInformationRepository basicInformationRepository;

    @Autowired
    private BasicInformationMapper basicInformationMapper;

    @Autowired
    private BasicInformationService basicInformationService;

    @Autowired
    private BasicInformationSearchRepository basicInformationSearchRepository;

    @Autowired
    private BasicInformationQueryService basicInformationQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBasicInformationMockMvc;

    private BasicInformation basicInformation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BasicInformationResource basicInformationResource = new BasicInformationResource(basicInformationService, basicInformationQueryService);
        this.restBasicInformationMockMvc = MockMvcBuilders.standaloneSetup(basicInformationResource)
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
    public static BasicInformation createEntity(EntityManager em) {
        BasicInformation basicInformation = new BasicInformation()
            .lastName(DEFAULT_LAST_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .nationality(DEFAULT_NATIONALITY)
            .gender(DEFAULT_GENDER)
            .dateofBrith(DEFAULT_DATEOF_BRITH)
            .educationLevel(DEFAULT_EDUCATION_LEVEL)
            .email(DEFAULT_EMAIL)
            .skype(DEFAULT_SKYPE)
            .phone(DEFAULT_PHONE)
            .wechat(DEFAULT_WECHAT);
        // Add required entity
        Resume resume = ResumeResourceIntTest.createEntity(em);
        em.persist(resume);
        em.flush();
        basicInformation.setResume(resume);
        return basicInformation;
    }

    @Before
    public void initTest() {
        basicInformationSearchRepository.deleteAll();
        basicInformation = createEntity(em);
    }

    @Test
    @Transactional
    public void createBasicInformation() throws Exception {
        int databaseSizeBeforeCreate = basicInformationRepository.findAll().size();

        // Create the BasicInformation
        BasicInformationDTO basicInformationDTO = basicInformationMapper.toDto(basicInformation);
        restBasicInformationMockMvc.perform(post("/api/basic-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(basicInformationDTO)))
            .andExpect(status().isCreated());

        // Validate the BasicInformation in the database
        List<BasicInformation> basicInformationList = basicInformationRepository.findAll();
        assertThat(basicInformationList).hasSize(databaseSizeBeforeCreate + 1);
        BasicInformation testBasicInformation = basicInformationList.get(basicInformationList.size() - 1);
        assertThat(testBasicInformation.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testBasicInformation.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testBasicInformation.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
        assertThat(testBasicInformation.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testBasicInformation.getDateofBrith()).isEqualTo(DEFAULT_DATEOF_BRITH);
        assertThat(testBasicInformation.getEducationLevel()).isEqualTo(DEFAULT_EDUCATION_LEVEL);
        assertThat(testBasicInformation.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testBasicInformation.getSkype()).isEqualTo(DEFAULT_SKYPE);
        assertThat(testBasicInformation.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testBasicInformation.getWechat()).isEqualTo(DEFAULT_WECHAT);

        // Validate the BasicInformation in Elasticsearch
        BasicInformation basicInformationEs = basicInformationSearchRepository.findOne(testBasicInformation.getId());
        assertThat(basicInformationEs).isEqualToComparingFieldByField(testBasicInformation);
    }

    @Test
    @Transactional
    public void createBasicInformationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = basicInformationRepository.findAll().size();

        // Create the BasicInformation with an existing ID
        basicInformation.setId(1L);
        BasicInformationDTO basicInformationDTO = basicInformationMapper.toDto(basicInformation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBasicInformationMockMvc.perform(post("/api/basic-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(basicInformationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BasicInformation in the database
        List<BasicInformation> basicInformationList = basicInformationRepository.findAll();
        assertThat(basicInformationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicInformationRepository.findAll().size();
        // set the field null
        basicInformation.setLastName(null);

        // Create the BasicInformation, which fails.
        BasicInformationDTO basicInformationDTO = basicInformationMapper.toDto(basicInformation);

        restBasicInformationMockMvc.perform(post("/api/basic-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(basicInformationDTO)))
            .andExpect(status().isBadRequest());

        List<BasicInformation> basicInformationList = basicInformationRepository.findAll();
        assertThat(basicInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicInformationRepository.findAll().size();
        // set the field null
        basicInformation.setFirstName(null);

        // Create the BasicInformation, which fails.
        BasicInformationDTO basicInformationDTO = basicInformationMapper.toDto(basicInformation);

        restBasicInformationMockMvc.perform(post("/api/basic-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(basicInformationDTO)))
            .andExpect(status().isBadRequest());

        List<BasicInformation> basicInformationList = basicInformationRepository.findAll();
        assertThat(basicInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNationalityIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicInformationRepository.findAll().size();
        // set the field null
        basicInformation.setNationality(null);

        // Create the BasicInformation, which fails.
        BasicInformationDTO basicInformationDTO = basicInformationMapper.toDto(basicInformation);

        restBasicInformationMockMvc.perform(post("/api/basic-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(basicInformationDTO)))
            .andExpect(status().isBadRequest());

        List<BasicInformation> basicInformationList = basicInformationRepository.findAll();
        assertThat(basicInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicInformationRepository.findAll().size();
        // set the field null
        basicInformation.setGender(null);

        // Create the BasicInformation, which fails.
        BasicInformationDTO basicInformationDTO = basicInformationMapper.toDto(basicInformation);

        restBasicInformationMockMvc.perform(post("/api/basic-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(basicInformationDTO)))
            .andExpect(status().isBadRequest());

        List<BasicInformation> basicInformationList = basicInformationRepository.findAll();
        assertThat(basicInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateofBrithIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicInformationRepository.findAll().size();
        // set the field null
        basicInformation.setDateofBrith(null);

        // Create the BasicInformation, which fails.
        BasicInformationDTO basicInformationDTO = basicInformationMapper.toDto(basicInformation);

        restBasicInformationMockMvc.perform(post("/api/basic-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(basicInformationDTO)))
            .andExpect(status().isBadRequest());

        List<BasicInformation> basicInformationList = basicInformationRepository.findAll();
        assertThat(basicInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEducationLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicInformationRepository.findAll().size();
        // set the field null
        basicInformation.setEducationLevel(null);

        // Create the BasicInformation, which fails.
        BasicInformationDTO basicInformationDTO = basicInformationMapper.toDto(basicInformation);

        restBasicInformationMockMvc.perform(post("/api/basic-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(basicInformationDTO)))
            .andExpect(status().isBadRequest());

        List<BasicInformation> basicInformationList = basicInformationRepository.findAll();
        assertThat(basicInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicInformationRepository.findAll().size();
        // set the field null
        basicInformation.setEmail(null);

        // Create the BasicInformation, which fails.
        BasicInformationDTO basicInformationDTO = basicInformationMapper.toDto(basicInformation);

        restBasicInformationMockMvc.perform(post("/api/basic-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(basicInformationDTO)))
            .andExpect(status().isBadRequest());

        List<BasicInformation> basicInformationList = basicInformationRepository.findAll();
        assertThat(basicInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBasicInformations() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList
        restBasicInformationMockMvc.perform(get("/api/basic-informations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(basicInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].dateofBrith").value(hasItem(DEFAULT_DATEOF_BRITH.toString())))
            .andExpect(jsonPath("$.[*].educationLevel").value(hasItem(DEFAULT_EDUCATION_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].skype").value(hasItem(DEFAULT_SKYPE.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].wechat").value(hasItem(DEFAULT_WECHAT.toString())));
    }

    @Test
    @Transactional
    public void getBasicInformation() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get the basicInformation
        restBasicInformationMockMvc.perform(get("/api/basic-informations/{id}", basicInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(basicInformation.getId().intValue()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.dateofBrith").value(DEFAULT_DATEOF_BRITH.toString()))
            .andExpect(jsonPath("$.educationLevel").value(DEFAULT_EDUCATION_LEVEL.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.skype").value(DEFAULT_SKYPE.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.wechat").value(DEFAULT_WECHAT.toString()));
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where lastName equals to DEFAULT_LAST_NAME
        defaultBasicInformationShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the basicInformationList where lastName equals to UPDATED_LAST_NAME
        defaultBasicInformationShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultBasicInformationShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the basicInformationList where lastName equals to UPDATED_LAST_NAME
        defaultBasicInformationShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where lastName is not null
        defaultBasicInformationShouldBeFound("lastName.specified=true");

        // Get all the basicInformationList where lastName is null
        defaultBasicInformationShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where firstName equals to DEFAULT_FIRST_NAME
        defaultBasicInformationShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the basicInformationList where firstName equals to UPDATED_FIRST_NAME
        defaultBasicInformationShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultBasicInformationShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the basicInformationList where firstName equals to UPDATED_FIRST_NAME
        defaultBasicInformationShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where firstName is not null
        defaultBasicInformationShouldBeFound("firstName.specified=true");

        // Get all the basicInformationList where firstName is null
        defaultBasicInformationShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByNationalityIsEqualToSomething() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where nationality equals to DEFAULT_NATIONALITY
        defaultBasicInformationShouldBeFound("nationality.equals=" + DEFAULT_NATIONALITY);

        // Get all the basicInformationList where nationality equals to UPDATED_NATIONALITY
        defaultBasicInformationShouldNotBeFound("nationality.equals=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByNationalityIsInShouldWork() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where nationality in DEFAULT_NATIONALITY or UPDATED_NATIONALITY
        defaultBasicInformationShouldBeFound("nationality.in=" + DEFAULT_NATIONALITY + "," + UPDATED_NATIONALITY);

        // Get all the basicInformationList where nationality equals to UPDATED_NATIONALITY
        defaultBasicInformationShouldNotBeFound("nationality.in=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByNationalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where nationality is not null
        defaultBasicInformationShouldBeFound("nationality.specified=true");

        // Get all the basicInformationList where nationality is null
        defaultBasicInformationShouldNotBeFound("nationality.specified=false");
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where gender equals to DEFAULT_GENDER
        defaultBasicInformationShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the basicInformationList where gender equals to UPDATED_GENDER
        defaultBasicInformationShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultBasicInformationShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the basicInformationList where gender equals to UPDATED_GENDER
        defaultBasicInformationShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where gender is not null
        defaultBasicInformationShouldBeFound("gender.specified=true");

        // Get all the basicInformationList where gender is null
        defaultBasicInformationShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByDateofBrithIsEqualToSomething() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where dateofBrith equals to DEFAULT_DATEOF_BRITH
        defaultBasicInformationShouldBeFound("dateofBrith.equals=" + DEFAULT_DATEOF_BRITH);

        // Get all the basicInformationList where dateofBrith equals to UPDATED_DATEOF_BRITH
        defaultBasicInformationShouldNotBeFound("dateofBrith.equals=" + UPDATED_DATEOF_BRITH);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByDateofBrithIsInShouldWork() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where dateofBrith in DEFAULT_DATEOF_BRITH or UPDATED_DATEOF_BRITH
        defaultBasicInformationShouldBeFound("dateofBrith.in=" + DEFAULT_DATEOF_BRITH + "," + UPDATED_DATEOF_BRITH);

        // Get all the basicInformationList where dateofBrith equals to UPDATED_DATEOF_BRITH
        defaultBasicInformationShouldNotBeFound("dateofBrith.in=" + UPDATED_DATEOF_BRITH);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByDateofBrithIsNullOrNotNull() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where dateofBrith is not null
        defaultBasicInformationShouldBeFound("dateofBrith.specified=true");

        // Get all the basicInformationList where dateofBrith is null
        defaultBasicInformationShouldNotBeFound("dateofBrith.specified=false");
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByDateofBrithIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where dateofBrith greater than or equals to DEFAULT_DATEOF_BRITH
        defaultBasicInformationShouldBeFound("dateofBrith.greaterOrEqualThan=" + DEFAULT_DATEOF_BRITH);

        // Get all the basicInformationList where dateofBrith greater than or equals to UPDATED_DATEOF_BRITH
        defaultBasicInformationShouldNotBeFound("dateofBrith.greaterOrEqualThan=" + UPDATED_DATEOF_BRITH);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByDateofBrithIsLessThanSomething() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where dateofBrith less than or equals to DEFAULT_DATEOF_BRITH
        defaultBasicInformationShouldNotBeFound("dateofBrith.lessThan=" + DEFAULT_DATEOF_BRITH);

        // Get all the basicInformationList where dateofBrith less than or equals to UPDATED_DATEOF_BRITH
        defaultBasicInformationShouldBeFound("dateofBrith.lessThan=" + UPDATED_DATEOF_BRITH);
    }


    @Test
    @Transactional
    public void getAllBasicInformationsByEducationLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where educationLevel equals to DEFAULT_EDUCATION_LEVEL
        defaultBasicInformationShouldBeFound("educationLevel.equals=" + DEFAULT_EDUCATION_LEVEL);

        // Get all the basicInformationList where educationLevel equals to UPDATED_EDUCATION_LEVEL
        defaultBasicInformationShouldNotBeFound("educationLevel.equals=" + UPDATED_EDUCATION_LEVEL);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByEducationLevelIsInShouldWork() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where educationLevel in DEFAULT_EDUCATION_LEVEL or UPDATED_EDUCATION_LEVEL
        defaultBasicInformationShouldBeFound("educationLevel.in=" + DEFAULT_EDUCATION_LEVEL + "," + UPDATED_EDUCATION_LEVEL);

        // Get all the basicInformationList where educationLevel equals to UPDATED_EDUCATION_LEVEL
        defaultBasicInformationShouldNotBeFound("educationLevel.in=" + UPDATED_EDUCATION_LEVEL);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByEducationLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where educationLevel is not null
        defaultBasicInformationShouldBeFound("educationLevel.specified=true");

        // Get all the basicInformationList where educationLevel is null
        defaultBasicInformationShouldNotBeFound("educationLevel.specified=false");
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where email equals to DEFAULT_EMAIL
        defaultBasicInformationShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the basicInformationList where email equals to UPDATED_EMAIL
        defaultBasicInformationShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultBasicInformationShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the basicInformationList where email equals to UPDATED_EMAIL
        defaultBasicInformationShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where email is not null
        defaultBasicInformationShouldBeFound("email.specified=true");

        // Get all the basicInformationList where email is null
        defaultBasicInformationShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllBasicInformationsBySkypeIsEqualToSomething() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where skype equals to DEFAULT_SKYPE
        defaultBasicInformationShouldBeFound("skype.equals=" + DEFAULT_SKYPE);

        // Get all the basicInformationList where skype equals to UPDATED_SKYPE
        defaultBasicInformationShouldNotBeFound("skype.equals=" + UPDATED_SKYPE);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsBySkypeIsInShouldWork() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where skype in DEFAULT_SKYPE or UPDATED_SKYPE
        defaultBasicInformationShouldBeFound("skype.in=" + DEFAULT_SKYPE + "," + UPDATED_SKYPE);

        // Get all the basicInformationList where skype equals to UPDATED_SKYPE
        defaultBasicInformationShouldNotBeFound("skype.in=" + UPDATED_SKYPE);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsBySkypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where skype is not null
        defaultBasicInformationShouldBeFound("skype.specified=true");

        // Get all the basicInformationList where skype is null
        defaultBasicInformationShouldNotBeFound("skype.specified=false");
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where phone equals to DEFAULT_PHONE
        defaultBasicInformationShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the basicInformationList where phone equals to UPDATED_PHONE
        defaultBasicInformationShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultBasicInformationShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the basicInformationList where phone equals to UPDATED_PHONE
        defaultBasicInformationShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where phone is not null
        defaultBasicInformationShouldBeFound("phone.specified=true");

        // Get all the basicInformationList where phone is null
        defaultBasicInformationShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByWechatIsEqualToSomething() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where wechat equals to DEFAULT_WECHAT
        defaultBasicInformationShouldBeFound("wechat.equals=" + DEFAULT_WECHAT);

        // Get all the basicInformationList where wechat equals to UPDATED_WECHAT
        defaultBasicInformationShouldNotBeFound("wechat.equals=" + UPDATED_WECHAT);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByWechatIsInShouldWork() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where wechat in DEFAULT_WECHAT or UPDATED_WECHAT
        defaultBasicInformationShouldBeFound("wechat.in=" + DEFAULT_WECHAT + "," + UPDATED_WECHAT);

        // Get all the basicInformationList where wechat equals to UPDATED_WECHAT
        defaultBasicInformationShouldNotBeFound("wechat.in=" + UPDATED_WECHAT);
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByWechatIsNullOrNotNull() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);

        // Get all the basicInformationList where wechat is not null
        defaultBasicInformationShouldBeFound("wechat.specified=true");

        // Get all the basicInformationList where wechat is null
        defaultBasicInformationShouldNotBeFound("wechat.specified=false");
    }

    @Test
    @Transactional
    public void getAllBasicInformationsByResumeIsEqualToSomething() throws Exception {
        // Initialize the database
        Resume resume = ResumeResourceIntTest.createEntity(em);
        em.persist(resume);
        em.flush();
        basicInformation.setResume(resume);
        resume.setBasicInformation(basicInformation);
        basicInformationRepository.saveAndFlush(basicInformation);
        Long resumeId = resume.getId();

        // Get all the basicInformationList where resume equals to resumeId
        defaultBasicInformationShouldBeFound("resumeId.equals=" + resumeId);

        // Get all the basicInformationList where resume equals to resumeId + 1
        defaultBasicInformationShouldNotBeFound("resumeId.equals=" + (resumeId + 1));
    }


    @Test
    @Transactional
    public void getAllBasicInformationsByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        Image image = ImageResourceIntTest.createEntity(em);
        em.persist(image);
        em.flush();
        basicInformation.setImage(image);
        basicInformationRepository.saveAndFlush(basicInformation);
        Long imageId = image.getId();

        // Get all the basicInformationList where image equals to imageId
        defaultBasicInformationShouldBeFound("imageId.equals=" + imageId);

        // Get all the basicInformationList where image equals to imageId + 1
        defaultBasicInformationShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBasicInformationShouldBeFound(String filter) throws Exception {
        restBasicInformationMockMvc.perform(get("/api/basic-informations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(basicInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].dateofBrith").value(hasItem(DEFAULT_DATEOF_BRITH.toString())))
            .andExpect(jsonPath("$.[*].educationLevel").value(hasItem(DEFAULT_EDUCATION_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].skype").value(hasItem(DEFAULT_SKYPE.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].wechat").value(hasItem(DEFAULT_WECHAT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBasicInformationShouldNotBeFound(String filter) throws Exception {
        restBasicInformationMockMvc.perform(get("/api/basic-informations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingBasicInformation() throws Exception {
        // Get the basicInformation
        restBasicInformationMockMvc.perform(get("/api/basic-informations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBasicInformation() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);
        basicInformationSearchRepository.save(basicInformation);
        int databaseSizeBeforeUpdate = basicInformationRepository.findAll().size();

        // Update the basicInformation
        BasicInformation updatedBasicInformation = basicInformationRepository.findOne(basicInformation.getId());
        updatedBasicInformation
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .nationality(UPDATED_NATIONALITY)
            .gender(UPDATED_GENDER)
            .dateofBrith(UPDATED_DATEOF_BRITH)
            .educationLevel(UPDATED_EDUCATION_LEVEL)
            .email(UPDATED_EMAIL)
            .skype(UPDATED_SKYPE)
            .phone(UPDATED_PHONE)
            .wechat(UPDATED_WECHAT);
        BasicInformationDTO basicInformationDTO = basicInformationMapper.toDto(updatedBasicInformation);

        restBasicInformationMockMvc.perform(put("/api/basic-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(basicInformationDTO)))
            .andExpect(status().isOk());

        // Validate the BasicInformation in the database
        List<BasicInformation> basicInformationList = basicInformationRepository.findAll();
        assertThat(basicInformationList).hasSize(databaseSizeBeforeUpdate);
        BasicInformation testBasicInformation = basicInformationList.get(basicInformationList.size() - 1);
        assertThat(testBasicInformation.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testBasicInformation.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testBasicInformation.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testBasicInformation.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testBasicInformation.getDateofBrith()).isEqualTo(UPDATED_DATEOF_BRITH);
        assertThat(testBasicInformation.getEducationLevel()).isEqualTo(UPDATED_EDUCATION_LEVEL);
        assertThat(testBasicInformation.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testBasicInformation.getSkype()).isEqualTo(UPDATED_SKYPE);
        assertThat(testBasicInformation.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testBasicInformation.getWechat()).isEqualTo(UPDATED_WECHAT);

        // Validate the BasicInformation in Elasticsearch
        BasicInformation basicInformationEs = basicInformationSearchRepository.findOne(testBasicInformation.getId());
        assertThat(basicInformationEs).isEqualToComparingFieldByField(testBasicInformation);
    }

    @Test
    @Transactional
    public void updateNonExistingBasicInformation() throws Exception {
        int databaseSizeBeforeUpdate = basicInformationRepository.findAll().size();

        // Create the BasicInformation
        BasicInformationDTO basicInformationDTO = basicInformationMapper.toDto(basicInformation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBasicInformationMockMvc.perform(put("/api/basic-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(basicInformationDTO)))
            .andExpect(status().isCreated());

        // Validate the BasicInformation in the database
        List<BasicInformation> basicInformationList = basicInformationRepository.findAll();
        assertThat(basicInformationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBasicInformation() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);
        basicInformationSearchRepository.save(basicInformation);
        int databaseSizeBeforeDelete = basicInformationRepository.findAll().size();

        // Get the basicInformation
        restBasicInformationMockMvc.perform(delete("/api/basic-informations/{id}", basicInformation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean basicInformationExistsInEs = basicInformationSearchRepository.exists(basicInformation.getId());
        assertThat(basicInformationExistsInEs).isFalse();

        // Validate the database is empty
        List<BasicInformation> basicInformationList = basicInformationRepository.findAll();
        assertThat(basicInformationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBasicInformation() throws Exception {
        // Initialize the database
        basicInformationRepository.saveAndFlush(basicInformation);
        basicInformationSearchRepository.save(basicInformation);

        // Search the basicInformation
        restBasicInformationMockMvc.perform(get("/api/_search/basic-informations?query=id:" + basicInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(basicInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].dateofBrith").value(hasItem(DEFAULT_DATEOF_BRITH.toString())))
            .andExpect(jsonPath("$.[*].educationLevel").value(hasItem(DEFAULT_EDUCATION_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].skype").value(hasItem(DEFAULT_SKYPE.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].wechat").value(hasItem(DEFAULT_WECHAT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BasicInformation.class);
        BasicInformation basicInformation1 = new BasicInformation();
        basicInformation1.setId(1L);
        BasicInformation basicInformation2 = new BasicInformation();
        basicInformation2.setId(basicInformation1.getId());
        assertThat(basicInformation1).isEqualTo(basicInformation2);
        basicInformation2.setId(2L);
        assertThat(basicInformation1).isNotEqualTo(basicInformation2);
        basicInformation1.setId(null);
        assertThat(basicInformation1).isNotEqualTo(basicInformation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BasicInformationDTO.class);
        BasicInformationDTO basicInformationDTO1 = new BasicInformationDTO();
        basicInformationDTO1.setId(1L);
        BasicInformationDTO basicInformationDTO2 = new BasicInformationDTO();
        assertThat(basicInformationDTO1).isNotEqualTo(basicInformationDTO2);
        basicInformationDTO2.setId(basicInformationDTO1.getId());
        assertThat(basicInformationDTO1).isEqualTo(basicInformationDTO2);
        basicInformationDTO2.setId(2L);
        assertThat(basicInformationDTO1).isNotEqualTo(basicInformationDTO2);
        basicInformationDTO1.setId(null);
        assertThat(basicInformationDTO1).isNotEqualTo(basicInformationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(basicInformationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(basicInformationMapper.fromId(null)).isNull();
    }
}
