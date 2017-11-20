package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.ApplyJobResumeService;
import com.panda.mojobs.domain.ApplyJobResume;
import com.panda.mojobs.repository.ApplyJobResumeRepository;
import com.panda.mojobs.repository.search.ApplyJobResumeSearchRepository;
import com.panda.mojobs.service.dto.ApplyJobResumeDTO;
import com.panda.mojobs.service.mapper.ApplyJobResumeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ApplyJobResume.
 */
@Service
@Transactional
public class ApplyJobResumeServiceImpl implements ApplyJobResumeService{

    private final Logger log = LoggerFactory.getLogger(ApplyJobResumeServiceImpl.class);

    private final ApplyJobResumeRepository applyJobResumeRepository;

    private final ApplyJobResumeMapper applyJobResumeMapper;

    private final ApplyJobResumeSearchRepository applyJobResumeSearchRepository;

    public ApplyJobResumeServiceImpl(ApplyJobResumeRepository applyJobResumeRepository, ApplyJobResumeMapper applyJobResumeMapper, ApplyJobResumeSearchRepository applyJobResumeSearchRepository) {
        this.applyJobResumeRepository = applyJobResumeRepository;
        this.applyJobResumeMapper = applyJobResumeMapper;
        this.applyJobResumeSearchRepository = applyJobResumeSearchRepository;
    }

    /**
     * Save a applyJobResume.
     *
     * @param applyJobResumeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ApplyJobResumeDTO save(ApplyJobResumeDTO applyJobResumeDTO) {
        log.debug("Request to save ApplyJobResume : {}", applyJobResumeDTO);
        ApplyJobResume applyJobResume = applyJobResumeMapper.toEntity(applyJobResumeDTO);
        applyJobResume = applyJobResumeRepository.save(applyJobResume);
        ApplyJobResumeDTO result = applyJobResumeMapper.toDto(applyJobResume);
        applyJobResumeSearchRepository.save(applyJobResume);
        return result;
    }

    /**
     *  Get all the applyJobResumes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ApplyJobResumeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ApplyJobResumes");
        return applyJobResumeRepository.findAll(pageable)
            .map(applyJobResumeMapper::toDto);
    }

    /**
     *  Get one applyJobResume by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ApplyJobResumeDTO findOne(Long id) {
        log.debug("Request to get ApplyJobResume : {}", id);
        ApplyJobResume applyJobResume = applyJobResumeRepository.findOne(id);
        return applyJobResumeMapper.toDto(applyJobResume);
    }

    /**
     *  Delete the  applyJobResume by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplyJobResume : {}", id);
        applyJobResumeRepository.delete(id);
        applyJobResumeSearchRepository.delete(id);
    }

    /**
     * Search for the applyJobResume corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ApplyJobResumeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ApplyJobResumes for query {}", query);
        Page<ApplyJobResume> result = applyJobResumeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(applyJobResumeMapper::toDto);
    }
}
