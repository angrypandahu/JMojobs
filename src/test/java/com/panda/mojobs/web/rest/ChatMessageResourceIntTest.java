package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.ChatMessage;
import com.panda.mojobs.domain.User;
import com.panda.mojobs.domain.User;
import com.panda.mojobs.repository.ChatMessageRepository;
import com.panda.mojobs.service.ChatMessageService;
import com.panda.mojobs.repository.search.ChatMessageSearchRepository;
import com.panda.mojobs.service.dto.ChatMessageDTO;
import com.panda.mojobs.service.mapper.ChatMessageMapper;
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
 * Test class for the ChatMessageResource REST controller.
 *
 * @see ChatMessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class ChatMessageResourceIntTest {

    private static final LocalDate DEFAULT_SEND_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SEND_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatMessageSearchRepository chatMessageSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restChatMessageMockMvc;

    private ChatMessage chatMessage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChatMessageResource chatMessageResource = new ChatMessageResource(chatMessageService);
        this.restChatMessageMockMvc = MockMvcBuilders.standaloneSetup(chatMessageResource)
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
    public static ChatMessage createEntity(EntityManager em) {
        ChatMessage chatMessage = new ChatMessage()
            .sendTime(DEFAULT_SEND_TIME)
            .content(DEFAULT_CONTENT);
        // Add required entity
        User fromUser = UserResourceIntTest.createEntity(em);
        em.persist(fromUser);
        em.flush();
        chatMessage.setFromUser(fromUser);
        // Add required entity
        User toUser = UserResourceIntTest.createEntity(em);
        em.persist(toUser);
        em.flush();
        chatMessage.setToUser(toUser);
        return chatMessage;
    }

    @Before
    public void initTest() {
        chatMessageSearchRepository.deleteAll();
        chatMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatMessage() throws Exception {
        int databaseSizeBeforeCreate = chatMessageRepository.findAll().size();

        // Create the ChatMessage
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);
        restChatMessageMockMvc.perform(post("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isCreated());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeCreate + 1);
        ChatMessage testChatMessage = chatMessageList.get(chatMessageList.size() - 1);
        assertThat(testChatMessage.getSendTime()).isEqualTo(DEFAULT_SEND_TIME);
        assertThat(testChatMessage.getContent()).isEqualTo(DEFAULT_CONTENT);

        // Validate the ChatMessage in Elasticsearch
        ChatMessage chatMessageEs = chatMessageSearchRepository.findOne(testChatMessage.getId());
        assertThat(chatMessageEs).isEqualToComparingFieldByField(testChatMessage);
    }

    @Test
    @Transactional
    public void createChatMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatMessageRepository.findAll().size();

        // Create the ChatMessage with an existing ID
        chatMessage.setId(1L);
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatMessageMockMvc.perform(post("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSendTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatMessageRepository.findAll().size();
        // set the field null
        chatMessage.setSendTime(null);

        // Create the ChatMessage, which fails.
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        restChatMessageMockMvc.perform(post("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isBadRequest());

        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatMessageRepository.findAll().size();
        // set the field null
        chatMessage.setContent(null);

        // Create the ChatMessage, which fails.
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        restChatMessageMockMvc.perform(post("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isBadRequest());

        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChatMessages() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList
        restChatMessageMockMvc.perform(get("/api/chat-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].sendTime").value(hasItem(DEFAULT_SEND_TIME.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getChatMessage() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get the chatMessage
        restChatMessageMockMvc.perform(get("/api/chat-messages/{id}", chatMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chatMessage.getId().intValue()))
            .andExpect(jsonPath("$.sendTime").value(DEFAULT_SEND_TIME.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChatMessage() throws Exception {
        // Get the chatMessage
        restChatMessageMockMvc.perform(get("/api/chat-messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatMessage() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);
        chatMessageSearchRepository.save(chatMessage);
        int databaseSizeBeforeUpdate = chatMessageRepository.findAll().size();

        // Update the chatMessage
        ChatMessage updatedChatMessage = chatMessageRepository.findOne(chatMessage.getId());
        updatedChatMessage
            .sendTime(UPDATED_SEND_TIME)
            .content(UPDATED_CONTENT);
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(updatedChatMessage);

        restChatMessageMockMvc.perform(put("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isOk());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeUpdate);
        ChatMessage testChatMessage = chatMessageList.get(chatMessageList.size() - 1);
        assertThat(testChatMessage.getSendTime()).isEqualTo(UPDATED_SEND_TIME);
        assertThat(testChatMessage.getContent()).isEqualTo(UPDATED_CONTENT);

        // Validate the ChatMessage in Elasticsearch
        ChatMessage chatMessageEs = chatMessageSearchRepository.findOne(testChatMessage.getId());
        assertThat(chatMessageEs).isEqualToComparingFieldByField(testChatMessage);
    }

    @Test
    @Transactional
    public void updateNonExistingChatMessage() throws Exception {
        int databaseSizeBeforeUpdate = chatMessageRepository.findAll().size();

        // Create the ChatMessage
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restChatMessageMockMvc.perform(put("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isCreated());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteChatMessage() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);
        chatMessageSearchRepository.save(chatMessage);
        int databaseSizeBeforeDelete = chatMessageRepository.findAll().size();

        // Get the chatMessage
        restChatMessageMockMvc.perform(delete("/api/chat-messages/{id}", chatMessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean chatMessageExistsInEs = chatMessageSearchRepository.exists(chatMessage.getId());
        assertThat(chatMessageExistsInEs).isFalse();

        // Validate the database is empty
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChatMessage() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);
        chatMessageSearchRepository.save(chatMessage);

        // Search the chatMessage
        restChatMessageMockMvc.perform(get("/api/_search/chat-messages?query=id:" + chatMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].sendTime").value(hasItem(DEFAULT_SEND_TIME.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatMessage.class);
        ChatMessage chatMessage1 = new ChatMessage();
        chatMessage1.setId(1L);
        ChatMessage chatMessage2 = new ChatMessage();
        chatMessage2.setId(chatMessage1.getId());
        assertThat(chatMessage1).isEqualTo(chatMessage2);
        chatMessage2.setId(2L);
        assertThat(chatMessage1).isNotEqualTo(chatMessage2);
        chatMessage1.setId(null);
        assertThat(chatMessage1).isNotEqualTo(chatMessage2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatMessageDTO.class);
        ChatMessageDTO chatMessageDTO1 = new ChatMessageDTO();
        chatMessageDTO1.setId(1L);
        ChatMessageDTO chatMessageDTO2 = new ChatMessageDTO();
        assertThat(chatMessageDTO1).isNotEqualTo(chatMessageDTO2);
        chatMessageDTO2.setId(chatMessageDTO1.getId());
        assertThat(chatMessageDTO1).isEqualTo(chatMessageDTO2);
        chatMessageDTO2.setId(2L);
        assertThat(chatMessageDTO1).isNotEqualTo(chatMessageDTO2);
        chatMessageDTO1.setId(null);
        assertThat(chatMessageDTO1).isNotEqualTo(chatMessageDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chatMessageMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chatMessageMapper.fromId(null)).isNull();
    }
}
