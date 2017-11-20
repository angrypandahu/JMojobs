package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.SchoolAddress;
import com.panda.mojobs.repository.SchoolAddressRepository;
import com.panda.mojobs.service.SchoolAddressService;
import com.panda.mojobs.repository.search.SchoolAddressSearchRepository;
import com.panda.mojobs.service.dto.SchoolAddressDTO;
import com.panda.mojobs.service.mapper.SchoolAddressMapper;
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

/**
 * Test class for the SchoolAddressResource REST controller.
 *
 * @see SchoolAddressResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class SchoolAddressResourceIntTest {

    @Autowired
    private SchoolAddressRepository schoolAddressRepository;

    @Autowired
    private SchoolAddressMapper schoolAddressMapper;

    @Autowired
    private SchoolAddressService schoolAddressService;

    @Autowired
    private SchoolAddressSearchRepository schoolAddressSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSchoolAddressMockMvc;

    private SchoolAddress schoolAddress;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SchoolAddressResource schoolAddressResource = new SchoolAddressResource(schoolAddressService);
        this.restSchoolAddressMockMvc = MockMvcBuilders.standaloneSetup(schoolAddressResource)
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
    public static SchoolAddress createEntity(EntityManager em) {
        SchoolAddress schoolAddress = new SchoolAddress();
        return schoolAddress;
    }

    @Before
    public void initTest() {
        schoolAddressSearchRepository.deleteAll();
        schoolAddress = createEntity(em);
    }

    @Test
    @Transactional
    public void createSchoolAddress() throws Exception {
        int databaseSizeBeforeCreate = schoolAddressRepository.findAll().size();

        // Create the SchoolAddress
        SchoolAddressDTO schoolAddressDTO = schoolAddressMapper.toDto(schoolAddress);
        restSchoolAddressMockMvc.perform(post("/api/school-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the SchoolAddress in the database
        List<SchoolAddress> schoolAddressList = schoolAddressRepository.findAll();
        assertThat(schoolAddressList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolAddress testSchoolAddress = schoolAddressList.get(schoolAddressList.size() - 1);

        // Validate the SchoolAddress in Elasticsearch
        SchoolAddress schoolAddressEs = schoolAddressSearchRepository.findOne(testSchoolAddress.getId());
        assertThat(schoolAddressEs).isEqualToComparingFieldByField(testSchoolAddress);
    }

    @Test
    @Transactional
    public void createSchoolAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = schoolAddressRepository.findAll().size();

        // Create the SchoolAddress with an existing ID
        schoolAddress.setId(1L);
        SchoolAddressDTO schoolAddressDTO = schoolAddressMapper.toDto(schoolAddress);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolAddressMockMvc.perform(post("/api/school-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SchoolAddress in the database
        List<SchoolAddress> schoolAddressList = schoolAddressRepository.findAll();
        assertThat(schoolAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSchoolAddresses() throws Exception {
        // Initialize the database
        schoolAddressRepository.saveAndFlush(schoolAddress);

        // Get all the schoolAddressList
        restSchoolAddressMockMvc.perform(get("/api/school-addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolAddress.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSchoolAddress() throws Exception {
        // Initialize the database
        schoolAddressRepository.saveAndFlush(schoolAddress);

        // Get the schoolAddress
        restSchoolAddressMockMvc.perform(get("/api/school-addresses/{id}", schoolAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(schoolAddress.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSchoolAddress() throws Exception {
        // Get the schoolAddress
        restSchoolAddressMockMvc.perform(get("/api/school-addresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSchoolAddress() throws Exception {
        // Initialize the database
        schoolAddressRepository.saveAndFlush(schoolAddress);
        schoolAddressSearchRepository.save(schoolAddress);
        int databaseSizeBeforeUpdate = schoolAddressRepository.findAll().size();

        // Update the schoolAddress
        SchoolAddress updatedSchoolAddress = schoolAddressRepository.findOne(schoolAddress.getId());
        SchoolAddressDTO schoolAddressDTO = schoolAddressMapper.toDto(updatedSchoolAddress);

        restSchoolAddressMockMvc.perform(put("/api/school-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolAddressDTO)))
            .andExpect(status().isOk());

        // Validate the SchoolAddress in the database
        List<SchoolAddress> schoolAddressList = schoolAddressRepository.findAll();
        assertThat(schoolAddressList).hasSize(databaseSizeBeforeUpdate);
        SchoolAddress testSchoolAddress = schoolAddressList.get(schoolAddressList.size() - 1);

        // Validate the SchoolAddress in Elasticsearch
        SchoolAddress schoolAddressEs = schoolAddressSearchRepository.findOne(testSchoolAddress.getId());
        assertThat(schoolAddressEs).isEqualToComparingFieldByField(testSchoolAddress);
    }

    @Test
    @Transactional
    public void updateNonExistingSchoolAddress() throws Exception {
        int databaseSizeBeforeUpdate = schoolAddressRepository.findAll().size();

        // Create the SchoolAddress
        SchoolAddressDTO schoolAddressDTO = schoolAddressMapper.toDto(schoolAddress);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSchoolAddressMockMvc.perform(put("/api/school-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the SchoolAddress in the database
        List<SchoolAddress> schoolAddressList = schoolAddressRepository.findAll();
        assertThat(schoolAddressList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSchoolAddress() throws Exception {
        // Initialize the database
        schoolAddressRepository.saveAndFlush(schoolAddress);
        schoolAddressSearchRepository.save(schoolAddress);
        int databaseSizeBeforeDelete = schoolAddressRepository.findAll().size();

        // Get the schoolAddress
        restSchoolAddressMockMvc.perform(delete("/api/school-addresses/{id}", schoolAddress.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean schoolAddressExistsInEs = schoolAddressSearchRepository.exists(schoolAddress.getId());
        assertThat(schoolAddressExistsInEs).isFalse();

        // Validate the database is empty
        List<SchoolAddress> schoolAddressList = schoolAddressRepository.findAll();
        assertThat(schoolAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSchoolAddress() throws Exception {
        // Initialize the database
        schoolAddressRepository.saveAndFlush(schoolAddress);
        schoolAddressSearchRepository.save(schoolAddress);

        // Search the schoolAddress
        restSchoolAddressMockMvc.perform(get("/api/_search/school-addresses?query=id:" + schoolAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolAddress.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolAddress.class);
        SchoolAddress schoolAddress1 = new SchoolAddress();
        schoolAddress1.setId(1L);
        SchoolAddress schoolAddress2 = new SchoolAddress();
        schoolAddress2.setId(schoolAddress1.getId());
        assertThat(schoolAddress1).isEqualTo(schoolAddress2);
        schoolAddress2.setId(2L);
        assertThat(schoolAddress1).isNotEqualTo(schoolAddress2);
        schoolAddress1.setId(null);
        assertThat(schoolAddress1).isNotEqualTo(schoolAddress2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolAddressDTO.class);
        SchoolAddressDTO schoolAddressDTO1 = new SchoolAddressDTO();
        schoolAddressDTO1.setId(1L);
        SchoolAddressDTO schoolAddressDTO2 = new SchoolAddressDTO();
        assertThat(schoolAddressDTO1).isNotEqualTo(schoolAddressDTO2);
        schoolAddressDTO2.setId(schoolAddressDTO1.getId());
        assertThat(schoolAddressDTO1).isEqualTo(schoolAddressDTO2);
        schoolAddressDTO2.setId(2L);
        assertThat(schoolAddressDTO1).isNotEqualTo(schoolAddressDTO2);
        schoolAddressDTO1.setId(null);
        assertThat(schoolAddressDTO1).isNotEqualTo(schoolAddressDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(schoolAddressMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(schoolAddressMapper.fromId(null)).isNull();
    }
}
