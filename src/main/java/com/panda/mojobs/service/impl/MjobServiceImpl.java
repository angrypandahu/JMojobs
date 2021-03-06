package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.MjobService;
import com.panda.mojobs.domain.Mjob;
import com.panda.mojobs.repository.MjobRepository;
import com.panda.mojobs.repository.search.MjobSearchRepository;
import com.panda.mojobs.service.dto.MjobDTO;
import com.panda.mojobs.service.mapper.MjobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Mjob.
 */
@Service
@Transactional
public class MjobServiceImpl implements MjobService{

    private final Logger log = LoggerFactory.getLogger(MjobServiceImpl.class);

    private final MjobRepository mjobRepository;

    private final MjobMapper mjobMapper;

    private final MjobSearchRepository mjobSearchRepository;

    public MjobServiceImpl(MjobRepository mjobRepository, MjobMapper mjobMapper, MjobSearchRepository mjobSearchRepository) {
        this.mjobRepository = mjobRepository;
        this.mjobMapper = mjobMapper;
        this.mjobSearchRepository = mjobSearchRepository;
    }

    /**
     * Save a mjob.
     *
     * @param mjobDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MjobDTO save(MjobDTO mjobDTO) {
        log.debug("Request to save Mjob : {}", mjobDTO);
        Mjob mjob = mjobMapper.toEntity(mjobDTO);
        mjob = mjobRepository.save(mjob);
        MjobDTO result = mjobMapper.toDto(mjob);
        mjobSearchRepository.save(mjob);
        return result;
    }

    /**
     *  Get all the mjobs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MjobDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mjobs");
        return mjobRepository.findAll(pageable)
            .map(mjobMapper::toDto);
    }

    /**
     *  Get one mjob by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MjobDTO findOne(Long id) {
        log.debug("Request to get Mjob : {}", id);
        Mjob mjob = mjobRepository.findOne(id);
        return mjobMapper.toDto(mjob);
    }

    /**
     *  Delete the  mjob by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Mjob : {}", id);
        mjobRepository.delete(id);
        mjobSearchRepository.delete(id);
    }

    /**
     * Search for the mjob corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MjobDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Mjobs for query {}", query);
        Page<Mjob> result = mjobSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(mjobMapper::toDto);
    }
}
