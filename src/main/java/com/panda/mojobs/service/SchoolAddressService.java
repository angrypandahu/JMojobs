package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.SchoolAddressDTO;
import java.util.List;

/**
 * Service Interface for managing SchoolAddress.
 */
public interface SchoolAddressService {

    /**
     * Save a schoolAddress.
     *
     * @param schoolAddressDTO the entity to save
     * @return the persisted entity
     */
    SchoolAddressDTO save(SchoolAddressDTO schoolAddressDTO);

    /**
     *  Get all the schoolAddresses.
     *
     *  @return the list of entities
     */
    List<SchoolAddressDTO> findAll();

    /**
     *  Get the "id" schoolAddress.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SchoolAddressDTO findOne(Long id);

    /**
     *  Delete the "id" schoolAddress.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the schoolAddress corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<SchoolAddressDTO> search(String query);
}
