package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.SchoolService;
import com.panda.mojobs.domain.School;
import com.panda.mojobs.repository.SchoolRepository;
import com.panda.mojobs.repository.search.SchoolSearchRepository;
import com.panda.mojobs.service.dto.SchoolDTO;
import com.panda.mojobs.service.mapper.SchoolMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing School.
 */
@Service
@Transactional
public class SchoolServiceImpl implements SchoolService{

    private final Logger log = LoggerFactory.getLogger(SchoolServiceImpl.class);

    private final SchoolRepository schoolRepository;

    private final SchoolMapper schoolMapper;

    private final SchoolSearchRepository schoolSearchRepository;

    public SchoolServiceImpl(SchoolRepository schoolRepository, SchoolMapper schoolMapper, SchoolSearchRepository schoolSearchRepository) {
        this.schoolRepository = schoolRepository;
        this.schoolMapper = schoolMapper;
        this.schoolSearchRepository = schoolSearchRepository;
    }

    /**
     * Save a school.
     *
     * @param schoolDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SchoolDTO save(SchoolDTO schoolDTO) {
        log.debug("Request to save School : {}", schoolDTO);
        School school = schoolMapper.toEntity(schoolDTO);
        school = schoolRepository.save(school);
        SchoolDTO result = schoolMapper.toDto(school);
        schoolSearchRepository.save(school);
        return result;
    }

    /**
     *  Get all the schools.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SchoolDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Schools");
        return schoolRepository.findAll(pageable)
            .map(schoolMapper::toDto);
    }

    /**
     *  Get one school by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SchoolDTO findOne(Long id) {
        log.debug("Request to get School : {}", id);
        School school = schoolRepository.findOne(id);
        return schoolMapper.toDto(school);
    }

    /**
     *  Delete the  school by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete School : {}", id);
        schoolRepository.delete(id);
        schoolSearchRepository.delete(id);
    }

    /**
     * Search for the school corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SchoolDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Schools for query {}", query);
        Page<School> result = schoolSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(schoolMapper::toDto);
    }
}
