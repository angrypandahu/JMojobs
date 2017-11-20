package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.JobAddress;
import com.panda.mojobs.repository.JobAddressRepository;
import com.panda.mojobs.service.JobAddressService;
import com.panda.mojobs.repository.search.JobAddressSearchRepository;
import com.panda.mojobs.service.dto.JobAddressDTO;
import com.panda.mojobs.service.mapper.JobAddressMapper;
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
 * Test class for the JobAddressResource REST controller.
 *
 * @see JobAddressResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class JobAddressResourceIntTest {

    @Autowired
    private JobAddressRepository jobAddressRepository;

    @Autowired
    private JobAddressMapper jobAddressMapper;

    @Autowired
    private JobAddressService jobAddressService;

    @Autowired
    private JobAddressSearchRepository jobAddressSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobAddressMockMvc;

    private JobAddress jobAddress;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobAddressResource jobAddressResource = new JobAddressResource(jobAddressService);
        this.restJobAddressMockMvc = MockMvcBuilders.standaloneSetup(jobAddressResource)
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
    public static JobAddress createEntity(EntityManager em) {
        JobAddress jobAddress = new JobAddress();
        return jobAddress;
    }

    @Before
    public void initTest() {
        jobAddressSearchRepository.deleteAll();
        jobAddress = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobAddress() throws Exception {
        int databaseSizeBeforeCreate = jobAddressRepository.findAll().size();

        // Create the JobAddress
        JobAddressDTO jobAddressDTO = jobAddressMapper.toDto(jobAddress);
        restJobAddressMockMvc.perform(post("/api/job-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the JobAddress in the database
        List<JobAddress> jobAddressList = jobAddressRepository.findAll();
        assertThat(jobAddressList).hasSize(databaseSizeBeforeCreate + 1);
        JobAddress testJobAddress = jobAddressList.get(jobAddressList.size() - 1);

        // Validate the JobAddress in Elasticsearch
        JobAddress jobAddressEs = jobAddressSearchRepository.findOne(testJobAddress.getId());
        assertThat(jobAddressEs).isEqualToComparingFieldByField(testJobAddress);
    }

    @Test
    @Transactional
    public void createJobAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobAddressRepository.findAll().size();

        // Create the JobAddress with an existing ID
        jobAddress.setId(1L);
        JobAddressDTO jobAddressDTO = jobAddressMapper.toDto(jobAddress);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobAddressMockMvc.perform(post("/api/job-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JobAddress in the database
        List<JobAddress> jobAddressList = jobAddressRepository.findAll();
        assertThat(jobAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobAddresses() throws Exception {
        // Initialize the database
        jobAddressRepository.saveAndFlush(jobAddress);

        // Get all the jobAddressList
        restJobAddressMockMvc.perform(get("/api/job-addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobAddress.getId().intValue())));
    }

    @Test
    @Transactional
    public void getJobAddress() throws Exception {
        // Initialize the database
        jobAddressRepository.saveAndFlush(jobAddress);

        // Get the jobAddress
        restJobAddressMockMvc.perform(get("/api/job-addresses/{id}", jobAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobAddress.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingJobAddress() throws Exception {
        // Get the jobAddress
        restJobAddressMockMvc.perform(get("/api/job-addresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobAddress() throws Exception {
        // Initialize the database
        jobAddressRepository.saveAndFlush(jobAddress);
        jobAddressSearchRepository.save(jobAddress);
        int databaseSizeBeforeUpdate = jobAddressRepository.findAll().size();

        // Update the jobAddress
        JobAddress updatedJobAddress = jobAddressRepository.findOne(jobAddress.getId());
        JobAddressDTO jobAddressDTO = jobAddressMapper.toDto(updatedJobAddress);

        restJobAddressMockMvc.perform(put("/api/job-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobAddressDTO)))
            .andExpect(status().isOk());

        // Validate the JobAddress in the database
        List<JobAddress> jobAddressList = jobAddressRepository.findAll();
        assertThat(jobAddressList).hasSize(databaseSizeBeforeUpdate);
        JobAddress testJobAddress = jobAddressList.get(jobAddressList.size() - 1);

        // Validate the JobAddress in Elasticsearch
        JobAddress jobAddressEs = jobAddressSearchRepository.findOne(testJobAddress.getId());
        assertThat(jobAddressEs).isEqualToComparingFieldByField(testJobAddress);
    }

    @Test
    @Transactional
    public void updateNonExistingJobAddress() throws Exception {
        int databaseSizeBeforeUpdate = jobAddressRepository.findAll().size();

        // Create the JobAddress
        JobAddressDTO jobAddressDTO = jobAddressMapper.toDto(jobAddress);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobAddressMockMvc.perform(put("/api/job-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the JobAddress in the database
        List<JobAddress> jobAddressList = jobAddressRepository.findAll();
        assertThat(jobAddressList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobAddress() throws Exception {
        // Initialize the database
        jobAddressRepository.saveAndFlush(jobAddress);
        jobAddressSearchRepository.save(jobAddress);
        int databaseSizeBeforeDelete = jobAddressRepository.findAll().size();

        // Get the jobAddress
        restJobAddressMockMvc.perform(delete("/api/job-addresses/{id}", jobAddress.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean jobAddressExistsInEs = jobAddressSearchRepository.exists(jobAddress.getId());
        assertThat(jobAddressExistsInEs).isFalse();

        // Validate the database is empty
        List<JobAddress> jobAddressList = jobAddressRepository.findAll();
        assertThat(jobAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJobAddress() throws Exception {
        // Initialize the database
        jobAddressRepository.saveAndFlush(jobAddress);
        jobAddressSearchRepository.save(jobAddress);

        // Search the jobAddress
        restJobAddressMockMvc.perform(get("/api/_search/job-addresses?query=id:" + jobAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobAddress.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobAddress.class);
        JobAddress jobAddress1 = new JobAddress();
        jobAddress1.setId(1L);
        JobAddress jobAddress2 = new JobAddress();
        jobAddress2.setId(jobAddress1.getId());
        assertThat(jobAddress1).isEqualTo(jobAddress2);
        jobAddress2.setId(2L);
        assertThat(jobAddress1).isNotEqualTo(jobAddress2);
        jobAddress1.setId(null);
        assertThat(jobAddress1).isNotEqualTo(jobAddress2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobAddressDTO.class);
        JobAddressDTO jobAddressDTO1 = new JobAddressDTO();
        jobAddressDTO1.setId(1L);
        JobAddressDTO jobAddressDTO2 = new JobAddressDTO();
        assertThat(jobAddressDTO1).isNotEqualTo(jobAddressDTO2);
        jobAddressDTO2.setId(jobAddressDTO1.getId());
        assertThat(jobAddressDTO1).isEqualTo(jobAddressDTO2);
        jobAddressDTO2.setId(2L);
        assertThat(jobAddressDTO1).isNotEqualTo(jobAddressDTO2);
        jobAddressDTO1.setId(null);
        assertThat(jobAddressDTO1).isNotEqualTo(jobAddressDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(jobAddressMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(jobAddressMapper.fromId(null)).isNull();
    }
}
