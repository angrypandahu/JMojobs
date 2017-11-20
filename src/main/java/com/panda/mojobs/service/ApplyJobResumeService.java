package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.ApplyJobResumeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ApplyJobResume.
 */
public interface ApplyJobResumeService {

    /**
     * Save a applyJobResume.
     *
     * @param applyJobResumeDTO the entity to save
     * @return the persisted entity
     */
    ApplyJobResumeDTO save(ApplyJobResumeDTO applyJobResumeDTO);

    /**
     *  Get all the applyJobResumes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ApplyJobResumeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" applyJobResume.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ApplyJobResumeDTO findOne(Long id);

    /**
     *  Delete the "id" applyJobResume.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the applyJobResume corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ApplyJobResumeDTO> search(String query, Pageable pageable);
}
