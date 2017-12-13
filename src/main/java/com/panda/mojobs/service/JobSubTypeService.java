package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.JobSubTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing JobSubType.
 */
public interface JobSubTypeService {

    /**
     * Save a jobSubType.
     *
     * @param jobSubTypeDTO the entity to save
     * @return the persisted entity
     */
    JobSubTypeDTO save(JobSubTypeDTO jobSubTypeDTO);

    /**
     *  Get all the jobSubTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<JobSubTypeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" jobSubType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    JobSubTypeDTO findOne(Long id);

    /**
     *  Delete the "id" jobSubType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the jobSubType corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<JobSubTypeDTO> search(String query, Pageable pageable);
}
