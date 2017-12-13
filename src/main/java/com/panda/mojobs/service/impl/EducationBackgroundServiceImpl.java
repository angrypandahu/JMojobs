package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.EducationBackgroundService;
import com.panda.mojobs.domain.EducationBackground;
import com.panda.mojobs.repository.EducationBackgroundRepository;
import com.panda.mojobs.repository.search.EducationBackgroundSearchRepository;
import com.panda.mojobs.service.dto.EducationBackgroundDTO;
import com.panda.mojobs.service.mapper.EducationBackgroundMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing EducationBackground.
 */
@Service
@Transactional
public class EducationBackgroundServiceImpl implements EducationBackgroundService{

    private final Logger log = LoggerFactory.getLogger(EducationBackgroundServiceImpl.class);

    private final EducationBackgroundRepository educationBackgroundRepository;

    private final EducationBackgroundMapper educationBackgroundMapper;

    private final EducationBackgroundSearchRepository educationBackgroundSearchRepository;

    public EducationBackgroundServiceImpl(EducationBackgroundRepository educationBackgroundRepository, EducationBackgroundMapper educationBackgroundMapper, EducationBackgroundSearchRepository educationBackgroundSearchRepository) {
        this.educationBackgroundRepository = educationBackgroundRepository;
        this.educationBackgroundMapper = educationBackgroundMapper;
        this.educationBackgroundSearchRepository = educationBackgroundSearchRepository;
    }

    /**
     * Save a educationBackground.
     *
     * @param educationBackgroundDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EducationBackgroundDTO save(EducationBackgroundDTO educationBackgroundDTO) {
        log.debug("Request to save EducationBackground : {}", educationBackgroundDTO);
        EducationBackground educationBackground = educationBackgroundMapper.toEntity(educationBackgroundDTO);
        educationBackground = educationBackgroundRepository.save(educationBackground);
        EducationBackgroundDTO result = educationBackgroundMapper.toDto(educationBackground);
        educationBackgroundSearchRepository.save(educationBackground);
        return result;
    }

    /**
     *  Get all the educationBackgrounds.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EducationBackgroundDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EducationBackgrounds");
        return educationBackgroundRepository.findAll(pageable)
            .map(educationBackgroundMapper::toDto);
    }

    /**
     *  Get one educationBackground by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EducationBackgroundDTO findOne(Long id) {
        log.debug("Request to get EducationBackground : {}", id);
        EducationBackground educationBackground = educationBackgroundRepository.findOne(id);
        return educationBackgroundMapper.toDto(educationBackground);
    }

    /**
     *  Delete the  educationBackground by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EducationBackground : {}", id);
        educationBackgroundRepository.delete(id);
        educationBackgroundSearchRepository.delete(id);
    }

    /**
     * Search for the educationBackground corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EducationBackgroundDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EducationBackgrounds for query {}", query);
        Page<EducationBackground> result = educationBackgroundSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(educationBackgroundMapper::toDto);
    }
}
