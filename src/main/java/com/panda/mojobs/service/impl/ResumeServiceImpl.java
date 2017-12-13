package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.ResumeService;
import com.panda.mojobs.domain.Resume;
import com.panda.mojobs.repository.ResumeRepository;
import com.panda.mojobs.repository.search.ResumeSearchRepository;
import com.panda.mojobs.service.dto.ResumeDTO;
import com.panda.mojobs.service.mapper.ResumeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Resume.
 */
@Service
@Transactional
public class ResumeServiceImpl implements ResumeService{

    private final Logger log = LoggerFactory.getLogger(ResumeServiceImpl.class);

    private final ResumeRepository resumeRepository;

    private final ResumeMapper resumeMapper;

    private final ResumeSearchRepository resumeSearchRepository;

    public ResumeServiceImpl(ResumeRepository resumeRepository, ResumeMapper resumeMapper, ResumeSearchRepository resumeSearchRepository) {
        this.resumeRepository = resumeRepository;
        this.resumeMapper = resumeMapper;
        this.resumeSearchRepository = resumeSearchRepository;
    }

    /**
     * Save a resume.
     *
     * @param resumeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ResumeDTO save(ResumeDTO resumeDTO) {
        log.debug("Request to save Resume : {}", resumeDTO);
        Resume resume = resumeMapper.toEntity(resumeDTO);
        resume = resumeRepository.save(resume);
        ResumeDTO result = resumeMapper.toDto(resume);
        resumeSearchRepository.save(resume);
        return result;
    }

    /**
     *  Get all the resumes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResumeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Resumes");
        return resumeRepository.findAll(pageable)
            .map(resumeMapper::toDto);
    }

    /**
     *  Get one resume by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ResumeDTO findOne(Long id) {
        log.debug("Request to get Resume : {}", id);
        Resume resume = resumeRepository.findOne(id);
        return resumeMapper.toDto(resume);
    }

    /**
     *  Delete the  resume by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Resume : {}", id);
        resumeRepository.delete(id);
        resumeSearchRepository.delete(id);
    }

    /**
     * Search for the resume corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResumeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Resumes for query {}", query);
        Page<Resume> result = resumeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(resumeMapper::toDto);
    }
}
