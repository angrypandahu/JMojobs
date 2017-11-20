package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.JobAddressDTO;
import java.util.List;

/**
 * Service Interface for managing JobAddress.
 */
public interface JobAddressService {

    /**
     * Save a jobAddress.
     *
     * @param jobAddressDTO the entity to save
     * @return the persisted entity
     */
    JobAddressDTO save(JobAddressDTO jobAddressDTO);

    /**
     *  Get all the jobAddresses.
     *
     *  @return the list of entities
     */
    List<JobAddressDTO> findAll();

    /**
     *  Get the "id" jobAddress.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    JobAddressDTO findOne(Long id);

    /**
     *  Delete the "id" jobAddress.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the jobAddress corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<JobAddressDTO> search(String query);
}
