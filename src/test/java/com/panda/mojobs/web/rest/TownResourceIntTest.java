package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.Town;
import com.panda.mojobs.domain.City;
import com.panda.mojobs.repository.TownRepository;
import com.panda.mojobs.service.TownService;
import com.panda.mojobs.repository.search.TownSearchRepository;
import com.panda.mojobs.service.dto.TownDTO;
import com.panda.mojobs.service.mapper.TownMapper;
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
 * Test class for the TownResource REST controller.
 *
 * @see TownResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class TownResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP = "AAAAAAAAAA";
    private static final String UPDATED_ZIP = "BBBBBBBBBB";

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private TownMapper townMapper;

    @Autowired
    private TownService townService;

    @Autowired
    private TownSearchRepository townSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTownMockMvc;

    private Town town;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TownResource townResource = new TownResource(townService);
        this.restTownMockMvc = MockMvcBuilders.standaloneSetup(townResource)
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
    public static Town createEntity(EntityManager em) {
        Town town = new Town()
            .name(DEFAULT_NAME)
            .zip(DEFAULT_ZIP);
        // Add required entity
        City city = CityResourceIntTest.createEntity(em);
        em.persist(city);
        em.flush();
        town.setCity(city);
        return town;
    }

    @Before
    public void initTest() {
        townSearchRepository.deleteAll();
        town = createEntity(em);
    }

    @Test
    @Transactional
    public void createTown() throws Exception {
        int databaseSizeBeforeCreate = townRepository.findAll().size();

        // Create the Town
        TownDTO townDTO = townMapper.toDto(town);
        restTownMockMvc.perform(post("/api/towns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(townDTO)))
            .andExpect(status().isCreated());

        // Validate the Town in the database
        List<Town> townList = townRepository.findAll();
        assertThat(townList).hasSize(databaseSizeBeforeCreate + 1);
        Town testTown = townList.get(townList.size() - 1);
        assertThat(testTown.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTown.getZip()).isEqualTo(DEFAULT_ZIP);

        // Validate the Town in Elasticsearch
        Town townEs = townSearchRepository.findOne(testTown.getId());
        assertThat(townEs).isEqualToComparingFieldByField(testTown);
    }

    @Test
    @Transactional
    public void createTownWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = townRepository.findAll().size();

        // Create the Town with an existing ID
        town.setId(1L);
        TownDTO townDTO = townMapper.toDto(town);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTownMockMvc.perform(post("/api/towns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(townDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Town in the database
        List<Town> townList = townRepository.findAll();
        assertThat(townList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTowns() throws Exception {
        // Initialize the database
        townRepository.saveAndFlush(town);

        // Get all the townList
        restTownMockMvc.perform(get("/api/towns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(town.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP.toString())));
    }

    @Test
    @Transactional
    public void getTown() throws Exception {
        // Initialize the database
        townRepository.saveAndFlush(town);

        // Get the town
        restTownMockMvc.perform(get("/api/towns/{id}", town.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(town.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.zip").value(DEFAULT_ZIP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTown() throws Exception {
        // Get the town
        restTownMockMvc.perform(get("/api/towns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTown() throws Exception {
        // Initialize the database
        townRepository.saveAndFlush(town);
        townSearchRepository.save(town);
        int databaseSizeBeforeUpdate = townRepository.findAll().size();

        // Update the town
        Town updatedTown = townRepository.findOne(town.getId());
        updatedTown
            .name(UPDATED_NAME)
            .zip(UPDATED_ZIP);
        TownDTO townDTO = townMapper.toDto(updatedTown);

        restTownMockMvc.perform(put("/api/towns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(townDTO)))
            .andExpect(status().isOk());

        // Validate the Town in the database
        List<Town> townList = townRepository.findAll();
        assertThat(townList).hasSize(databaseSizeBeforeUpdate);
        Town testTown = townList.get(townList.size() - 1);
        assertThat(testTown.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTown.getZip()).isEqualTo(UPDATED_ZIP);

        // Validate the Town in Elasticsearch
        Town townEs = townSearchRepository.findOne(testTown.getId());
        assertThat(townEs).isEqualToComparingFieldByField(testTown);
    }

    @Test
    @Transactional
    public void updateNonExistingTown() throws Exception {
        int databaseSizeBeforeUpdate = townRepository.findAll().size();

        // Create the Town
        TownDTO townDTO = townMapper.toDto(town);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTownMockMvc.perform(put("/api/towns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(townDTO)))
            .andExpect(status().isCreated());

        // Validate the Town in the database
        List<Town> townList = townRepository.findAll();
        assertThat(townList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTown() throws Exception {
        // Initialize the database
        townRepository.saveAndFlush(town);
        townSearchRepository.save(town);
        int databaseSizeBeforeDelete = townRepository.findAll().size();

        // Get the town
        restTownMockMvc.perform(delete("/api/towns/{id}", town.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean townExistsInEs = townSearchRepository.exists(town.getId());
        assertThat(townExistsInEs).isFalse();

        // Validate the database is empty
        List<Town> townList = townRepository.findAll();
        assertThat(townList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTown() throws Exception {
        // Initialize the database
        townRepository.saveAndFlush(town);
        townSearchRepository.save(town);

        // Search the town
        restTownMockMvc.perform(get("/api/_search/towns?query=id:" + town.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(town.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Town.class);
        Town town1 = new Town();
        town1.setId(1L);
        Town town2 = new Town();
        town2.setId(town1.getId());
        assertThat(town1).isEqualTo(town2);
        town2.setId(2L);
        assertThat(town1).isNotEqualTo(town2);
        town1.setId(null);
        assertThat(town1).isNotEqualTo(town2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TownDTO.class);
        TownDTO townDTO1 = new TownDTO();
        townDTO1.setId(1L);
        TownDTO townDTO2 = new TownDTO();
        assertThat(townDTO1).isNotEqualTo(townDTO2);
        townDTO2.setId(townDTO1.getId());
        assertThat(townDTO1).isEqualTo(townDTO2);
        townDTO2.setId(2L);
        assertThat(townDTO1).isNotEqualTo(townDTO2);
        townDTO1.setId(null);
        assertThat(townDTO1).isNotEqualTo(townDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(townMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(townMapper.fromId(null)).isNull();
    }
}
