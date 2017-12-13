package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.EducationBackgroundDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing EducationBackground.
 */
public interface EducationBackgroundService {

    /**
     * Save a educationBackground.
     *
     * @param educationBackgroundDTO the entity to save
     * @return the persisted entity
     */
    EducationBackgroundDTO save(EducationBackgroundDTO educationBackgroundDTO);

    /**
     *  Get all the educationBackgrounds.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EducationBackgroundDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" educationBackground.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EducationBackgroundDTO findOne(Long id);

    /**
     *  Delete the "id" educationBackground.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the educationBackground corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EducationBackgroundDTO> search(String query, Pageable pageable);
}
