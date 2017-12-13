package com.panda.mojobs.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.panda.mojobs.domain.ChatMessage;
import com.panda.mojobs.domain.*; // for static metamodels
import com.panda.mojobs.repository.ChatMessageRepository;
import com.panda.mojobs.repository.search.ChatMessageSearchRepository;
import com.panda.mojobs.service.dto.ChatMessageCriteria;

import com.panda.mojobs.service.dto.ChatMessageDTO;
import com.panda.mojobs.service.mapper.ChatMessageMapper;

/**
 * Service for executing complex queries for ChatMessage entities in the database.
 * The main input is a {@link ChatMessageCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link ChatMessageDTO} or a {@link Page} of {%link ChatMessageDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class ChatMessageQueryService extends QueryService<ChatMessage> {

    private final Logger log = LoggerFactory.getLogger(ChatMessageQueryService.class);


    private final ChatMessageRepository chatMessageRepository;

    private final ChatMessageMapper chatMessageMapper;

    private final ChatMessageSearchRepository chatMessageSearchRepository;

    public ChatMessageQueryService(ChatMessageRepository chatMessageRepository, ChatMessageMapper chatMessageMapper, ChatMessageSearchRepository chatMessageSearchRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageMapper = chatMessageMapper;
        this.chatMessageSearchRepository = chatMessageSearchRepository;
    }

    /**
     * Return a {@link List} of {%link ChatMessageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChatMessageDTO> findByCriteria(ChatMessageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ChatMessage> specification = createSpecification(criteria);
        return chatMessageMapper.toDto(chatMessageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link ChatMessageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatMessageDTO> findByCriteria(ChatMessageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ChatMessage> specification = createSpecification(criteria);
        final Page<ChatMessage> result = chatMessageRepository.findAll(specification, page);
        return result.map(chatMessageMapper::toDto);
    }

    /**
     * Function to convert ChatMessageCriteria to a {@link Specifications}
     */
    private Specifications<ChatMessage> createSpecification(ChatMessageCriteria criteria) {
        Specifications<ChatMessage> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChatMessage_.id));
            }
            if (criteria.getSendTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSendTime(), ChatMessage_.sendTime));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), ChatMessage_.content));
            }
            if (criteria.getFromUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFromUserId(), ChatMessage_.fromUser, User_.id));
            }
            if (criteria.getToUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getToUserId(), ChatMessage_.toUser, User_.id));
            }
        }
        return specification;
    }

}
