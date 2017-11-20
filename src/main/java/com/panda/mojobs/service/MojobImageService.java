package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.MojobImageDTO;
import java.util.List;

/**
 * Service Interface for managing MojobImage.
 */
public interface MojobImageService {

    /**
     * Save a mojobImage.
     *
     * @param mojobImageDTO the entity to save
     * @return the persisted entity
     */
    MojobImageDTO save(MojobImageDTO mojobImageDTO);

    /**
     *  Get all the mojobImages.
     *
     *  @return the list of entities
     */
    List<MojobImageDTO> findAll();

    /**
     *  Get the "id" mojobImage.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MojobImageDTO findOne(Long id);

    /**
     *  Delete the "id" mojobImage.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the mojobImage corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<MojobImageDTO> search(String query);
}
