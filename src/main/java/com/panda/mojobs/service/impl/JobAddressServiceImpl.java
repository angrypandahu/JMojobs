package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.JobAddressService;
import com.panda.mojobs.domain.JobAddress;
import com.panda.mojobs.repository.JobAddressRepository;
import com.panda.mojobs.repository.search.JobAddressSearchRepository;
import com.panda.mojobs.service.dto.JobAddressDTO;
import com.panda.mojobs.service.mapper.JobAddressMapper;
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
 * Service Implementation for managing JobAddress.
 */
@Service
@Transactional
public class JobAddressServiceImpl implements JobAddressService{

    private final Logger log = LoggerFactory.getLogger(JobAddressServiceImpl.class);

    private final JobAddressRepository jobAddressRepository;

    private final JobAddressMapper jobAddressMapper;

    private final JobAddressSearchRepository jobAddressSearchRepository;

    public JobAddressServiceImpl(JobAddressRepository jobAddressRepository, JobAddressMapper jobAddressMapper, JobAddressSearchRepository jobAddressSearchRepository) {
        this.jobAddressRepository = jobAddressRepository;
        this.jobAddressMapper = jobAddressMapper;
        this.jobAddressSearchRepository = jobAddressSearchRepository;
    }

    /**
     * Save a jobAddress.
     *
     * @param jobAddressDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public JobAddressDTO save(JobAddressDTO jobAddressDTO) {
        log.debug("Request to save JobAddress : {}", jobAddressDTO);
        JobAddress jobAddress = jobAddressMapper.toEntity(jobAddressDTO);
        jobAddress = jobAddressRepository.save(jobAddress);
        JobAddressDTO result = jobAddressMapper.toDto(jobAddress);
        jobAddressSearchRepository.save(jobAddress);
        return result;
    }

    /**
     *  Get all the jobAddresses.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<JobAddressDTO> findAll() {
        log.debug("Request to get all JobAddresses");
        return jobAddressRepository.findAll().stream()
            .map(jobAddressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one jobAddress by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public JobAddressDTO findOne(Long id) {
        log.debug("Request to get JobAddress : {}", id);
        JobAddress jobAddress = jobAddressRepository.findOne(id);
        return jobAddressMapper.toDto(jobAddress);
    }

    /**
     *  Delete the  jobAddress by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobAddress : {}", id);
        jobAddressRepository.delete(id);
        jobAddressSearchRepository.delete(id);
    }

    /**
     * Search for the jobAddress corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<JobAddressDTO> search(String query) {
        log.debug("Request to search JobAddresses for query {}", query);
        return StreamSupport
            .stream(jobAddressSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(jobAddressMapper::toDto)
            .collect(Collectors.toList());
    }
}
