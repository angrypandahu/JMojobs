package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.TownDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Town.
 */
public interface TownService {

    /**
     * Save a town.
     *
     * @param townDTO the entity to save
     * @return the persisted entity
     */
    TownDTO save(TownDTO townDTO);

    /**
     *  Get all the towns.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TownDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" town.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TownDTO findOne(Long id);

    /**
     *  Delete the "id" town.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the town corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TownDTO> search(String query, Pageable pageable);
}
