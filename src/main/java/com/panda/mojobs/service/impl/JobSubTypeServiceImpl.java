package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.JobSubTypeService;
import com.panda.mojobs.domain.JobSubType;
import com.panda.mojobs.repository.JobSubTypeRepository;
import com.panda.mojobs.repository.search.JobSubTypeSearchRepository;
import com.panda.mojobs.service.dto.JobSubTypeDTO;
import com.panda.mojobs.service.mapper.JobSubTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<JobSubTypeDTO> findAll() {
        log.debug("Request to get all JobSubTypes");
        return jobSubTypeRepository.findAll().stream()
            .map(jobSubTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
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
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<JobSubTypeDTO> search(String query) {
        log.debug("Request to search JobSubTypes for query {}", query);
        return StreamSupport
            .stream(jobSubTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(jobSubTypeMapper::toDto)
            .collect(Collectors.toList());
    }
}
