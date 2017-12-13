package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.ResumeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Resume.
 */
public interface ResumeService {

    /**
     * Save a resume.
     *
     * @param resumeDTO the entity to save
     * @return the persisted entity
     */
    ResumeDTO save(ResumeDTO resumeDTO);

    /**
     *  Get all the resumes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ResumeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" resume.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ResumeDTO findOne(Long id);

    /**
     *  Delete the "id" resume.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the resume corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ResumeDTO> search(String query, Pageable pageable);
}
