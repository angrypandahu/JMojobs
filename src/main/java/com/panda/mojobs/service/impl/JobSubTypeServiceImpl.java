package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.JobSubTypeService;
import com.panda.mojobs.domain.JobSubType;
import com.panda.mojobs.repository.JobSubTypeRepository;
import com.panda.mojobs.repository.search.JobSubTypeSearchRepository;
import com.panda.mojobs.service.dto.JobSubTypeDTO;
import com.panda.mojobs.service.mapper.JobSubTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing JobSubType.
 */
@Service
@Transactional
public class JobSubTypeServiceImpl implements JobSubTypeService{

    private final Logger log = LoggerFactory.getLogger(JobSubTypeServiceImpl.class);

    private final JobSubTypeRepository jobSubTypeRepository;

    private final JobSubTypeMapper jobSubTypeMapper;

    private final JobSubTypeSearchRepository jobSubTypeSearchRepository;

    public JobSubTypeServiceImpl(JobSubTypeRepository jobSubTypeRepository, JobSubTypeMapper jobSubTypeMapper, JobSubTypeSearchRepository jobSubTypeSearchRepository) {
        this.jobSubTypeRepository = jobSubTypeRepository;
        this.jobSubTypeMapper = jobSubTypeMapper;
        this.jobSubTypeSearchRepository = jobSubTypeSearchRepository;
    }

    /**
     * Save a jobSubType.
     *
     * @param jobSubTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public JobSubTypeDTO save(JobSubTypeDTO jobSubTypeDTO) {
        log.debug("Request to save JobSubType : {}", jobSubTypeDTO);
        JobSubType jobSubType = jobSubTypeMapper.toEntity(jobSubTypeDTO);
        jobSubType = jobSubTypeRepository.save(jobSubType);
        JobSubTypeDTO result = jobSubTypeMapper.toDto(jobSubType);
        jobSubTypeSearchRepository.save(jobSubType);
        return result;
    }

    /**
     *  Get all the jobSubTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JobSubTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobSubTypes");
        return jobSubTypeRepository.findAll(pageable)
            .map(jobSubTypeMapper::toDto);
    }

    /**
     *  Get one jobSubType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public JobSubTypeDTO findOne(Long id) {
        log.debug("Request to get JobSubType : {}", id);
        JobSubType jobSubType = jobSubTypeRepository.findOne(id);
        return jobSubTypeMapper.toDto(jobSubType);
    }

    /**
     *  Delete the  jobSubType by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobSubType : {}", id);
        jobSubTypeRepository.delete(id);
        jobSubTypeSearchRepository.delete(id);
    }

    /**
     * Search for the jobSubType corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JobSubTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of JobSubTypes for query {}", query);
        Page<JobSubType> result = jobSubTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(jobSubTypeMapper::toDto);
    }
}
