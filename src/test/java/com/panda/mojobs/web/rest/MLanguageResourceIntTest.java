package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.MLanguage;
import com.panda.mojobs.repository.MLanguageRepository;
import com.panda.mojobs.service.MLanguageService;
import com.panda.mojobs.repository.search.MLanguageSearchRepository;
import com.panda.mojobs.service.dto.MLanguageDTO;
import com.panda.mojobs.service.mapper.MLanguageMapper;
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
import java.util.List;

import static com.panda.mojobs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.panda.mojobs.domain.enumeration.Language;
import com.panda.mojobs.domain.enumeration.LanguageLevel;
/**
 * Test class for the MLanguageResource REST controller.
 *
 * @see MLanguageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class MLanguageResourceIntTest {

    private static final Language DEFAULT_NAME = Language.ENGLISH;
    private static final Language UPDATED_NAME = Language.CHINESE;

    private static final LanguageLevel DEFAULT_LEVEL = LanguageLevel.NATIVE_SPEAKER;
    private static final LanguageLevel UPDATED_LEVEL = LanguageLevel.ADVANCED;

    @Autowired
    private MLanguageRepository mLanguageRepository;

    @Autowired
    private MLanguageMapper mLanguageMapper;

    @Autowired
    private MLanguageService mLanguageService;

    @Autowired
    private MLanguageSearchRepository mLanguageSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMLanguageMockMvc;

    private MLanguage mLanguage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MLanguageResource mLanguageResource = new MLanguageResource(mLanguageService);
        this.restMLanguageMockMvc = MockMvcBuilders.standaloneSetup(mLanguageResource)
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
    public static MLanguage createEntity(EntityManager em) {
        MLanguage mLanguage = new MLanguage()
            .name(DEFAULT_NAME)
            .level(DEFAULT_LEVEL);
        return mLanguage;
    }

    @Before
    public void initTest() {
        mLanguageSearchRepository.deleteAll();
        mLanguage = createEntity(em);
    }

    @Test
    @Transactional
    public void createMLanguage() throws Exception {
        int databaseSizeBeforeCreate = mLanguageRepository.findAll().size();

        // Create the MLanguage
        MLanguageDTO mLanguageDTO = mLanguageMapper.toDto(mLanguage);
        restMLanguageMockMvc.perform(post("/api/m-languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mLanguageDTO)))
            .andExpect(status().isCreated());

        // Validate the MLanguage in the database
        List<MLanguage> mLanguageList = mLanguageRepository.findAll();
        assertThat(mLanguageList).hasSize(databaseSizeBeforeCreate + 1);
        MLanguage testMLanguage = mLanguageList.get(mLanguageList.size() - 1);
        assertThat(testMLanguage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMLanguage.getLevel()).isEqualTo(DEFAULT_LEVEL);

        // Validate the MLanguage in Elasticsearch
        MLanguage mLanguageEs = mLanguageSearchRepository.findOne(testMLanguage.getId());
        assertThat(mLanguageEs).isEqualToComparingFieldByField(testMLanguage);
    }

    @Test
    @Transactional
    public void createMLanguageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mLanguageRepository.findAll().size();

        // Create the MLanguage with an existing ID
        mLanguage.setId(1L);
        MLanguageDTO mLanguageDTO = mLanguageMapper.toDto(mLanguage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMLanguageMockMvc.perform(post("/api/m-languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mLanguageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MLanguage in the database
        List<MLanguage> mLanguageList = mLanguageRepository.findAll();
        assertThat(mLanguageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mLanguageRepository.findAll().size();
        // set the field null
        mLanguage.setName(null);

        // Create the MLanguage, which fails.
        MLanguageDTO mLanguageDTO = mLanguageMapper.toDto(mLanguage);

        restMLanguageMockMvc.perform(post("/api/m-languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mLanguageDTO)))
            .andExpect(status().isBadRequest());

        List<MLanguage> mLanguageList = mLanguageRepository.findAll();
        assertThat(mLanguageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = mLanguageRepository.findAll().size();
        // set the field null
        mLanguage.setLevel(null);

        // Create the MLanguage, which fails.
        MLanguageDTO mLanguageDTO = mLanguageMapper.toDto(mLanguage);

        restMLanguageMockMvc.perform(post("/api/m-languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mLanguageDTO)))
            .andExpect(status().isBadRequest());

        List<MLanguage> mLanguageList = mLanguageRepository.findAll();
        assertThat(mLanguageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMLanguages() throws Exception {
        // Initialize the database
        mLanguageRepository.saveAndFlush(mLanguage);

        // Get all the mLanguageList
        restMLanguageMockMvc.perform(get("/api/m-languages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mLanguage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())));
    }

    @Test
    @Transactional
    public void getMLanguage() throws Exception {
        // Initialize the database
        mLanguageRepository.saveAndFlush(mLanguage);

        // Get the mLanguage
        restMLanguageMockMvc.perform(get("/api/m-languages/{id}", mLanguage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mLanguage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMLanguage() throws Exception {
        // Get the mLanguage
        restMLanguageMockMvc.perform(get("/api/m-languages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMLanguage() throws Exception {
        // Initialize the database
        mLanguageRepository.saveAndFlush(mLanguage);
        mLanguageSearchRepository.save(mLanguage);
        int databaseSizeBeforeUpdate = mLanguageRepository.findAll().size();

        // Update the mLanguage
        MLanguage updatedMLanguage = mLanguageRepository.findOne(mLanguage.getId());
        updatedMLanguage
            .name(UPDATED_NAME)
            .level(UPDATED_LEVEL);
        MLanguageDTO mLanguageDTO = mLanguageMapper.toDto(updatedMLanguage);

        restMLanguageMockMvc.perform(put("/api/m-languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mLanguageDTO)))
            .andExpect(status().isOk());

        // Validate the MLanguage in the database
        List<MLanguage> mLanguageList = mLanguageRepository.findAll();
        assertThat(mLanguageList).hasSize(databaseSizeBeforeUpdate);
        MLanguage testMLanguage = mLanguageList.get(mLanguageList.size() - 1);
        assertThat(testMLanguage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMLanguage.getLevel()).isEqualTo(UPDATED_LEVEL);

        // Validate the MLanguage in Elasticsearch
        MLanguage mLanguageEs = mLanguageSearchRepository.findOne(testMLanguage.getId());
        assertThat(mLanguageEs).isEqualToComparingFieldByField(testMLanguage);
    }

    @Test
    @Transactional
    public void updateNonExistingMLanguage() throws Exception {
        int databaseSizeBeforeUpdate = mLanguageRepository.findAll().size();

        // Create the MLanguage
        MLanguageDTO mLanguageDTO = mLanguageMapper.toDto(mLanguage);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMLanguageMockMvc.perform(put("/api/m-languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mLanguageDTO)))
            .andExpect(status().isCreated());

        // Validate the MLanguage in the database
        List<MLanguage> mLanguageList = mLanguageRepository.findAll();
        assertThat(mLanguageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMLanguage() throws Exception {
        // Initialize the database
        mLanguageRepository.saveAndFlush(mLanguage);
        mLanguageSearchRepository.save(mLanguage);
        int databaseSizeBeforeDelete = mLanguageRepository.findAll().size();

        // Get the mLanguage
        restMLanguageMockMvc.perform(delete("/api/m-languages/{id}", mLanguage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean mLanguageExistsInEs = mLanguageSearchRepository.exists(mLanguage.getId());
        assertThat(mLanguageExistsInEs).isFalse();

        // Validate the database is empty
        List<MLanguage> mLanguageList = mLanguageRepository.findAll();
        assertThat(mLanguageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMLanguage() throws Exception {
        // Initialize the database
        mLanguageRepository.saveAndFlush(mLanguage);
        mLanguageSearchRepository.save(mLanguage);

        // Search the mLanguage
        restMLanguageMockMvc.perform(get("/api/_search/m-languages?query=id:" + mLanguage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mLanguage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MLanguage.class);
        MLanguage mLanguage1 = new MLanguage();
        mLanguage1.setId(1L);
        MLanguage mLanguage2 = new MLanguage();
        mLanguage2.setId(mLanguage1.getId());
        assertThat(mLanguage1).isEqualTo(mLanguage2);
        mLanguage2.setId(2L);
        assertThat(mLanguage1).isNotEqualTo(mLanguage2);
        mLanguage1.setId(null);
        assertThat(mLanguage1).isNotEqualTo(mLanguage2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MLanguageDTO.class);
        MLanguageDTO mLanguageDTO1 = new MLanguageDTO();
        mLanguageDTO1.setId(1L);
        MLanguageDTO mLanguageDTO2 = new MLanguageDTO();
        assertThat(mLanguageDTO1).isNotEqualTo(mLanguageDTO2);
        mLanguageDTO2.setId(mLanguageDTO1.getId());
        assertThat(mLanguageDTO1).isEqualTo(mLanguageDTO2);
        mLanguageDTO2.setId(2L);
        assertThat(mLanguageDTO1).isNotEqualTo(mLanguageDTO2);
        mLanguageDTO1.setId(null);
        assertThat(mLanguageDTO1).isNotEqualTo(mLanguageDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(mLanguageMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(mLanguageMapper.fromId(null)).isNull();
    }
}
