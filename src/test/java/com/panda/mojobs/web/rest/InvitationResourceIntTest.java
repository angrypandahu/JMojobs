package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.Invitation;
import com.panda.mojobs.domain.School;
import com.panda.mojobs.domain.User;
import com.panda.mojobs.repository.InvitationRepository;
import com.panda.mojobs.service.InvitationService;
import com.panda.mojobs.repository.search.InvitationSearchRepository;
import com.panda.mojobs.service.dto.InvitationDTO;
import com.panda.mojobs.service.mapper.InvitationMapper;
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
 * Test class for the InvitationResource REST controller.
 *
 * @see InvitationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class InvitationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private InvitationMapper invitationMapper;

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private InvitationSearchRepository invitationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvitationMockMvc;

    private Invitation invitation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvitationResource invitationResource = new InvitationResource(invitationService);
        this.restInvitationMockMvc = MockMvcBuilders.standaloneSetup(invitationResource)
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
    public static Invitation createEntity(EntityManager em) {
        Invitation invitation = new Invitation()
            .name(DEFAULT_NAME)
            .subject(DEFAULT_SUBJECT)
            .fromDate(DEFAULT_FROM_DATE)
            .content(DEFAULT_CONTENT);
        // Add required entity
        School school = SchoolResourceIntTest.createEntity(em);
        em.persist(school);
        em.flush();
        invitation.setSchool(school);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        invitation.setUser(user);
        return invitation;
    }

    @Before
    public void initTest() {
        invitationSearchRepository.deleteAll();
        invitation = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvitation() throws Exception {
        int databaseSizeBeforeCreate = invitationRepository.findAll().size();

        // Create the Invitation
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);
        restInvitationMockMvc.perform(post("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitationDTO)))
            .andExpect(status().isCreated());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeCreate + 1);
        Invitation testInvitation = invitationList.get(invitationList.size() - 1);
        assertThat(testInvitation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInvitation.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testInvitation.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testInvitation.getContent()).isEqualTo(DEFAULT_CONTENT);

        // Validate the Invitation in Elasticsearch
        Invitation invitationEs = invitationSearchRepository.findOne(testInvitation.getId());
        assertThat(invitationEs).isEqualToComparingFieldByField(testInvitation);
    }

    @Test
    @Transactional
    public void createInvitationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invitationRepository.findAll().size();

        // Create the Invitation with an existing ID
        invitation.setId(1L);
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvitationMockMvc.perform(post("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = invitationRepository.findAll().size();
        // set the field null
        invitation.setName(null);

        // Create the Invitation, which fails.
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);

        restInvitationMockMvc.perform(post("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitationDTO)))
            .andExpect(status().isBadRequest());

        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = invitationRepository.findAll().size();
        // set the field null
        invitation.setSubject(null);

        // Create the Invitation, which fails.
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);

        restInvitationMockMvc.perform(post("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitationDTO)))
            .andExpect(status().isBadRequest());

        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFromDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invitationRepository.findAll().size();
        // set the field null
        invitation.setFromDate(null);

        // Create the Invitation, which fails.
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);

        restInvitationMockMvc.perform(post("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitationDTO)))
            .andExpect(status().isBadRequest());

        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = invitationRepository.findAll().size();
        // set the field null
        invitation.setContent(null);

        // Create the Invitation, which fails.
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);

        restInvitationMockMvc.perform(post("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitationDTO)))
            .andExpect(status().isBadRequest());

        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvitations() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get all the invitationList
        restInvitationMockMvc.perform(get("/api/invitations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get the invitation
        restInvitationMockMvc.perform(get("/api/invitations/{id}", invitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invitation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInvitation() throws Exception {
        // Get the invitation
        restInvitationMockMvc.perform(get("/api/invitations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);
        invitationSearchRepository.save(invitation);
        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();

        // Update the invitation
        Invitation updatedInvitation = invitationRepository.findOne(invitation.getId());
        updatedInvitation
            .name(UPDATED_NAME)
            .subject(UPDATED_SUBJECT)
            .fromDate(UPDATED_FROM_DATE)
            .content(UPDATED_CONTENT);
        InvitationDTO invitationDTO = invitationMapper.toDto(updatedInvitation);

        restInvitationMockMvc.perform(put("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitationDTO)))
            .andExpect(status().isOk());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate);
        Invitation testInvitation = invitationList.get(invitationList.size() - 1);
        assertThat(testInvitation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInvitation.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testInvitation.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testInvitation.getContent()).isEqualTo(UPDATED_CONTENT);

        // Validate the Invitation in Elasticsearch
        Invitation invitationEs = invitationSearchRepository.findOne(testInvitation.getId());
        assertThat(invitationEs).isEqualToComparingFieldByField(testInvitation);
    }

    @Test
    @Transactional
    public void updateNonExistingInvitation() throws Exception {
        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();

        // Create the Invitation
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvitationMockMvc.perform(put("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitationDTO)))
            .andExpect(status().isCreated());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);
        invitationSearchRepository.save(invitation);
        int databaseSizeBeforeDelete = invitationRepository.findAll().size();

        // Get the invitation
        restInvitationMockMvc.perform(delete("/api/invitations/{id}", invitation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean invitationExistsInEs = invitationSearchRepository.exists(invitation.getId());
        assertThat(invitationExistsInEs).isFalse();

        // Validate the database is empty
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);
        invitationSearchRepository.save(invitation);

        // Search the invitation
        restInvitationMockMvc.perform(get("/api/_search/invitations?query=id:" + invitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invitation.class);
        Invitation invitation1 = new Invitation();
        invitation1.setId(1L);
        Invitation invitation2 = new Invitation();
        invitation2.setId(invitation1.getId());
        assertThat(invitation1).isEqualTo(invitation2);
        invitation2.setId(2L);
        assertThat(invitation1).isNotEqualTo(invitation2);
        invitation1.setId(null);
        assertThat(invitation1).isNotEqualTo(invitation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvitationDTO.class);
        InvitationDTO invitationDTO1 = new InvitationDTO();
        invitationDTO1.setId(1L);
        InvitationDTO invitationDTO2 = new InvitationDTO();
        assertThat(invitationDTO1).isNotEqualTo(invitationDTO2);
        invitationDTO2.setId(invitationDTO1.getId());
        assertThat(invitationDTO1).isEqualTo(invitationDTO2);
        invitationDTO2.setId(2L);
        assertThat(invitationDTO1).isNotEqualTo(invitationDTO2);
        invitationDTO1.setId(null);
        assertThat(invitationDTO1).isNotEqualTo(invitationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invitationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invitationMapper.fromId(null)).isNull();
    }
}
