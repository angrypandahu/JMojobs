package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.MLanguageDTO;
import java.util.List;

/**
 * Service Interface for managing MLanguage.
 */
public interface MLanguageService {

    /**
     * Save a mLanguage.
     *
     * @param mLanguageDTO the entity to save
     * @return the persisted entity
     */
    MLanguageDTO save(MLanguageDTO mLanguageDTO);

    /**
     *  Get all the mLanguages.
     *
     *  @return the list of entities
     */
    List<MLanguageDTO> findAll();

    /**
     *  Get the "id" mLanguage.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MLanguageDTO findOne(Long id);

    /**
     *  Delete the "id" mLanguage.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the mLanguage corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<MLanguageDTO> search(String query);
}
