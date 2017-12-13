package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.BasicInformationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BasicInformationDTO> findAll(Pageable pageable);
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
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BasicInformationDTO> search(String query, Pageable pageable);
}
