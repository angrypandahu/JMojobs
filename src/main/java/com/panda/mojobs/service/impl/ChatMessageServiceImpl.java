package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.ChatMessageService;
import com.panda.mojobs.domain.ChatMessage;
import com.panda.mojobs.repository.ChatMessageRepository;
import com.panda.mojobs.repository.search.ChatMessageSearchRepository;
import com.panda.mojobs.service.dto.ChatMessageDTO;
import com.panda.mojobs.service.mapper.ChatMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ChatMessage.
 */
@Service
@Transactional
public class ChatMessageServiceImpl implements ChatMessageService{

    private final Logger log = LoggerFactory.getLogger(ChatMessageServiceImpl.class);

    private final ChatMessageRepository chatMessageRepository;

    private final ChatMessageMapper chatMessageMapper;

    private final ChatMessageSearchRepository chatMessageSearchRepository;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository, ChatMessageMapper chatMessageMapper, ChatMessageSearchRepository chatMessageSearchRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageMapper = chatMessageMapper;
        this.chatMessageSearchRepository = chatMessageSearchRepository;
    }

    /**
     * Save a chatMessage.
     *
     * @param chatMessageDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ChatMessageDTO save(ChatMessageDTO chatMessageDTO) {
        log.debug("Request to save ChatMessage : {}", chatMessageDTO);
        ChatMessage chatMessage = chatMessageMapper.toEntity(chatMessageDTO);
        chatMessage = chatMessageRepository.save(chatMessage);
        ChatMessageDTO result = chatMessageMapper.toDto(chatMessage);
        chatMessageSearchRepository.save(chatMessage);
        return result;
    }

    /**
     *  Get all the chatMessages.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ChatMessageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChatMessages");
        return chatMessageRepository.findAll(pageable)
            .map(chatMessageMapper::toDto);
    }

    /**
     *  Get one chatMessage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ChatMessageDTO findOne(Long id) {
        log.debug("Request to get ChatMessage : {}", id);
        ChatMessage chatMessage = chatMessageRepository.findOne(id);
        return chatMessageMapper.toDto(chatMessage);
    }

    /**
     *  Delete the  chatMessage by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChatMessage : {}", id);
        chatMessageRepository.delete(id);
        chatMessageSearchRepository.delete(id);
    }

    /**
     * Search for the chatMessage corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ChatMessageDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ChatMessages for query {}", query);
        Page<ChatMessage> result = chatMessageSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(chatMessageMapper::toDto);
    }
}
