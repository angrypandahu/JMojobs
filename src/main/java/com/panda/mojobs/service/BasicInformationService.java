package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.BasicInformationDTO;
import java.util.List;

/**
 * Service Interface for managing BasicInformation.
 */
public interface BasicInformationService {

    /**
     * Save a basicInformation.
     *
     * @param basicInformationDTO the entity to save
     * @return the persisted entity
     */
    BasicInformationDTO save(BasicInformationDTO basicInformationDTO);

    /**
     *  Get all the basicInformations.
     *
     *  @return the list of entities
     */
    List<BasicInformationDTO> findAll();
    /**
     *  Get all the BasicInformationDTO where Resume is null.
     *
     *  @return the list of entities
     */
    List<BasicInformationDTO> findAllWhereResumeIsNull();

    /**
     *  Get the "id" basicInformation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BasicInformationDTO findOne(Long id);

    /**
     *  Delete the "id" basicInformation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the basicInformation corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<BasicInformationDTO> search(String query);
}
