package com.panda.mojobs.service;

import com.panda.mojobs.domain.Resume;
import com.panda.mojobs.service.data.ResumeData;
import com.panda.mojobs.service.dto.ResumeDTO;

import java.util.List;

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
     * Get all the resumes.
     *
     * @return the list of entities
     */
    List<ResumeDTO> findAll();

    /**
     * Get the "id" resume.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ResumeDTO findOne(Long id);

    /**
     * Delete the "id" resume.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the resume corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    List<ResumeDTO> search(String query);


    List<ResumeDTO> findByCurrentUser();

    List<ResumeData> findDataByCurrentUser();

    ResumeData toResumeData(ResumeDTO resumeDTO);

    ResumeData toResumeData(Resume resume);

    ResumeDTO createByLoginUser();

}
