package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.ExperienceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Experience.
 */
public interface ExperienceService {

    /**
     * Save a experience.
     *
     * @param experienceDTO the entity to save
     * @return the persisted entity
     */
    ExperienceDTO save(ExperienceDTO experienceDTO);

    /**
     *  Get all the experiences.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ExperienceDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" experience.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ExperienceDTO findOne(Long id);

    /**
     *  Delete the "id" experience.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the experience corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ExperienceDTO> search(String query, Pageable pageable);
}
