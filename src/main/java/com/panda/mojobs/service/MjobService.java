package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.MjobDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Mjob.
 */
public interface MjobService {

    /**
     * Save a mjob.
     *
     * @param mjobDTO the entity to save
     * @return the persisted entity
     */
    MjobDTO save(MjobDTO mjobDTO);

    /**
     *  Get all the mjobs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MjobDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" mjob.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MjobDTO findOne(Long id);

    /**
     *  Delete the "id" mjob.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the mjob corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MjobDTO> search(String query, Pageable pageable);
}
