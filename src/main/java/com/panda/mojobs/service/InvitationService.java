package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.InvitationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Invitation.
 */
public interface InvitationService {

    /**
     * Save a invitation.
     *
     * @param invitationDTO the entity to save
     * @return the persisted entity
     */
    InvitationDTO save(InvitationDTO invitationDTO);

    /**
     *  Get all the invitations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<InvitationDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" invitation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    InvitationDTO findOne(Long id);

    /**
     *  Delete the "id" invitation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the invitation corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<InvitationDTO> search(String query, Pageable pageable);
}
