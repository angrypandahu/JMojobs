package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.JobSubTypeDTO;
import java.util.List;

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
     *  @return the list of entities
     */
    List<JobSubTypeDTO> findAll();

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
     *  @return the list of entities
     */
    List<JobSubTypeDTO> search(String query);
}
