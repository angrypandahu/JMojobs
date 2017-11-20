package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.ProfessionalDevelopmentDTO;
import java.util.List;

/**
 * Service Interface for managing ProfessionalDevelopment.
 */
public interface ProfessionalDevelopmentService {

    /**
     * Save a professionalDevelopment.
     *
     * @param professionalDevelopmentDTO the entity to save
     * @return the persisted entity
     */
    ProfessionalDevelopmentDTO save(ProfessionalDevelopmentDTO professionalDevelopmentDTO);

    /**
     *  Get all the professionalDevelopments.
     *
     *  @return the list of entities
     */
    List<ProfessionalDevelopmentDTO> findAll();

    /**
     *  Get the "id" professionalDevelopment.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProfessionalDevelopmentDTO findOne(Long id);

    /**
     *  Delete the "id" professionalDevelopment.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the professionalDevelopment corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<ProfessionalDevelopmentDTO> search(String query);
}
