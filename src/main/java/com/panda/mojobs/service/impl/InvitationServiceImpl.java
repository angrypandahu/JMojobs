package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.InvitationService;
import com.panda.mojobs.domain.Invitation;
import com.panda.mojobs.repository.InvitationRepository;
import com.panda.mojobs.repository.search.InvitationSearchRepository;
import com.panda.mojobs.service.dto.InvitationDTO;
import com.panda.mojobs.service.mapper.InvitationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Invitation.
 */
@Service
@Transactional
public class InvitationServiceImpl implements InvitationService{

    private final Logger log = LoggerFactory.getLogger(InvitationServiceImpl.class);

    private final InvitationRepository invitationRepository;

    private final InvitationMapper invitationMapper;

    private final InvitationSearchRepository invitationSearchRepository;

    public InvitationServiceImpl(InvitationRepository invitationRepository, InvitationMapper invitationMapper, InvitationSearchRepository invitationSearchRepository) {
        this.invitationRepository = invitationRepository;
        this.invitationMapper = invitationMapper;
        this.invitationSearchRepository = invitationSearchRepository;
    }

    /**
     * Save a invitation.
     *
     * @param invitationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InvitationDTO save(InvitationDTO invitationDTO) {
        log.debug("Request to save Invitation : {}", invitationDTO);
        Invitation invitation = invitationMapper.toEntity(invitationDTO);
        invitation = invitationRepository.save(invitation);
        InvitationDTO result = invitationMapper.toDto(invitation);
        invitationSearchRepository.save(invitation);
        return result;
    }

    /**
     *  Get all the invitations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InvitationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Invitations");
        return invitationRepository.findAll(pageable)
            .map(invitationMapper::toDto);
    }

    /**
     *  Get one invitation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public InvitationDTO findOne(Long id) {
        log.debug("Request to get Invitation : {}", id);
        Invitation invitation = invitationRepository.findOne(id);
        return invitationMapper.toDto(invitation);
    }

    /**
     *  Delete the  invitation by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Invitation : {}", id);
        invitationRepository.delete(id);
        invitationSearchRepository.delete(id);
    }

    /**
     * Search for the invitation corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InvitationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Invitations for query {}", query);
        Page<Invitation> result = invitationSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(invitationMapper::toDto);
    }
}
