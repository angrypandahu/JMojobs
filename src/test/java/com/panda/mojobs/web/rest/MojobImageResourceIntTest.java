package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.MojobImage;
import com.panda.mojobs.repository.MojobImageRepository;
import com.panda.mojobs.service.MojobImageService;
import com.panda.mojobs.repository.search.MojobImageSearchRepository;
import com.panda.mojobs.service.dto.MojobImageDTO;
import com.panda.mojobs.service.mapper.MojobImageMapper;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.panda.mojobs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MojobImageResource REST controller.
 *
 * @see MojobImageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class MojobImageResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    @Autowired
    private MojobImageRepository mojobImageRepository;

    @Autowired
    private MojobImageMapper mojobImageMapper;

    @Autowired
    private MojobImageService mojobImageService;

    @Autowired
    private MojobImageSearchRepository mojobImageSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMojobImageMockMvc;

    private MojobImage mojobImage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MojobImageResource mojobImageResource = new MojobImageResource(mojobImageService);
        this.restMojobImageMockMvc = MockMvcBuilders.standaloneSetup(mojobImageResource)
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
    public static MojobImage createEntity(EntityManager em) {
        MojobImage mojobImage = new MojobImage()
            .name(DEFAULT_NAME)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE);
        return mojobImage;
    }

    @Before
    public void initTest() {
        mojobImageSearchRepository.deleteAll();
        mojobImage = createEntity(em);
    }

    @Test
    @Transactional
    public void createMojobImage() throws Exception {
        int databaseSizeBeforeCreate = mojobImageRepository.findAll().size();

        // Create the MojobImage
        MojobImageDTO mojobImageDTO = mojobImageMapper.toDto(mojobImage);
        restMojobImageMockMvc.perform(post("/api/mojob-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mojobImageDTO)))
            .andExpect(status().isCreated());

        // Validate the MojobImage in the database
        List<MojobImage> mojobImageList = mojobImageRepository.findAll();
        assertThat(mojobImageList).hasSize(databaseSizeBeforeCreate + 1);
        MojobImage testMojobImage = mojobImageList.get(mojobImageList.size() - 1);
        assertThat(testMojobImage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMojobImage.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testMojobImage.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);

        // Validate the MojobImage in Elasticsearch
        MojobImage mojobImageEs = mojobImageSearchRepository.findOne(testMojobImage.getId());
        assertThat(mojobImageEs).isEqualToComparingFieldByField(testMojobImage);
    }

    @Test
    @Transactional
    public void createMojobImageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mojobImageRepository.findAll().size();

        // Create the MojobImage with an existing ID
        mojobImage.setId(1L);
        MojobImageDTO mojobImageDTO = mojobImageMapper.toDto(mojobImage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMojobImageMockMvc.perform(post("/api/mojob-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mojobImageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MojobImage in the database
        List<MojobImage> mojobImageList = mojobImageRepository.findAll();
        assertThat(mojobImageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = mojobImageRepository.findAll().size();
        // set the field null
        mojobImage.setContent(null);

        // Create the MojobImage, which fails.
        MojobImageDTO mojobImageDTO = mojobImageMapper.toDto(mojobImage);

        restMojobImageMockMvc.perform(post("/api/mojob-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mojobImageDTO)))
            .andExpect(status().isBadRequest());

        List<MojobImage> mojobImageList = mojobImageRepository.findAll();
        assertThat(mojobImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMojobImages() throws Exception {
        // Initialize the database
        mojobImageRepository.saveAndFlush(mojobImage);

        // Get all the mojobImageList
        restMojobImageMockMvc.perform(get("/api/mojob-images?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mojobImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }

    @Test
    @Transactional
    public void getMojobImage() throws Exception {
        // Initialize the database
        mojobImageRepository.saveAndFlush(mojobImage);

        // Get the mojobImage
        restMojobImageMockMvc.perform(get("/api/mojob-images/{id}", mojobImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mojobImage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    public void getNonExistingMojobImage() throws Exception {
        // Get the mojobImage
        restMojobImageMockMvc.perform(get("/api/mojob-images/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMojobImage() throws Exception {
        // Initialize the database
        mojobImageRepository.saveAndFlush(mojobImage);
        mojobImageSearchRepository.save(mojobImage);
        int databaseSizeBeforeUpdate = mojobImageRepository.findAll().size();

        // Update the mojobImage
        MojobImage updatedMojobImage = mojobImageRepository.findOne(mojobImage.getId());
        updatedMojobImage
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE);
        MojobImageDTO mojobImageDTO = mojobImageMapper.toDto(updatedMojobImage);

        restMojobImageMockMvc.perform(put("/api/mojob-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mojobImageDTO)))
            .andExpect(status().isOk());

        // Validate the MojobImage in the database
        List<MojobImage> mojobImageList = mojobImageRepository.findAll();
        assertThat(mojobImageList).hasSize(databaseSizeBeforeUpdate);
        MojobImage testMojobImage = mojobImageList.get(mojobImageList.size() - 1);
        assertThat(testMojobImage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMojobImage.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testMojobImage.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);

        // Validate the MojobImage in Elasticsearch
        MojobImage mojobImageEs = mojobImageSearchRepository.findOne(testMojobImage.getId());
        assertThat(mojobImageEs).isEqualToComparingFieldByField(testMojobImage);
    }

    @Test
    @Transactional
    public void updateNonExistingMojobImage() throws Exception {
        int databaseSizeBeforeUpdate = mojobImageRepository.findAll().size();

        // Create the MojobImage
        MojobImageDTO mojobImageDTO = mojobImageMapper.toDto(mojobImage);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMojobImageMockMvc.perform(put("/api/mojob-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mojobImageDTO)))
            .andExpect(status().isCreated());

        // Validate the MojobImage in the database
        List<MojobImage> mojobImageList = mojobImageRepository.findAll();
        assertThat(mojobImageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMojobImage() throws Exception {
        // Initialize the database
        mojobImageRepository.saveAndFlush(mojobImage);
        mojobImageSearchRepository.save(mojobImage);
        int databaseSizeBeforeDelete = mojobImageRepository.findAll().size();

        // Get the mojobImage
        restMojobImageMockMvc.perform(delete("/api/mojob-images/{id}", mojobImage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean mojobImageExistsInEs = mojobImageSearchRepository.exists(mojobImage.getId());
        assertThat(mojobImageExistsInEs).isFalse();

        // Validate the database is empty
        List<MojobImage> mojobImageList = mojobImageRepository.findAll();
        assertThat(mojobImageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMojobImage() throws Exception {
        // Initialize the database
        mojobImageRepository.saveAndFlush(mojobImage);
        mojobImageSearchRepository.save(mojobImage);

        // Search the mojobImage
        restMojobImageMockMvc.perform(get("/api/_search/mojob-images?query=id:" + mojobImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mojobImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MojobImage.class);
        MojobImage mojobImage1 = new MojobImage();
        mojobImage1.setId(1L);
        MojobImage mojobImage2 = new MojobImage();
        mojobImage2.setId(mojobImage1.getId());
        assertThat(mojobImage1).isEqualTo(mojobImage2);
        mojobImage2.setId(2L);
        assertThat(mojobImage1).isNotEqualTo(mojobImage2);
        mojobImage1.setId(null);
        assertThat(mojobImage1).isNotEqualTo(mojobImage2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MojobImageDTO.class);
        MojobImageDTO mojobImageDTO1 = new MojobImageDTO();
        mojobImageDTO1.setId(1L);
        MojobImageDTO mojobImageDTO2 = new MojobImageDTO();
        assertThat(mojobImageDTO1).isNotEqualTo(mojobImageDTO2);
        mojobImageDTO2.setId(mojobImageDTO1.getId());
        assertThat(mojobImageDTO1).isEqualTo(mojobImageDTO2);
        mojobImageDTO2.setId(2L);
        assertThat(mojobImageDTO1).isNotEqualTo(mojobImageDTO2);
        mojobImageDTO1.setId(null);
        assertThat(mojobImageDTO1).isNotEqualTo(mojobImageDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(mojobImageMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(mojobImageMapper.fromId(null)).isNull();
    }
}
