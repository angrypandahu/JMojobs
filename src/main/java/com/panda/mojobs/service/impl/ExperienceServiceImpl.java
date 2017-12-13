package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.ExperienceService;
import com.panda.mojobs.domain.Experience;
import com.panda.mojobs.repository.ExperienceRepository;
import com.panda.mojobs.repository.search.ExperienceSearchRepository;
import com.panda.mojobs.service.dto.ExperienceDTO;
import com.panda.mojobs.service.mapper.ExperienceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Experience.
 */
@Service
@Transactional
public class ExperienceServiceImpl implements ExperienceService{

    private final Logger log = LoggerFactory.getLogger(ExperienceServiceImpl.class);

    private final ExperienceRepository experienceRepository;

    private final ExperienceMapper experienceMapper;

    private final ExperienceSearchRepository experienceSearchRepository;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository, ExperienceMapper experienceMapper, ExperienceSearchRepository experienceSearchRepository) {
        this.experienceRepository = experienceRepository;
        this.experienceMapper = experienceMapper;
        this.experienceSearchRepository = experienceSearchRepository;
    }

    /**
     * Save a experience.
     *
     * @param experienceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ExperienceDTO save(ExperienceDTO experienceDTO) {
        log.debug("Request to save Experience : {}", experienceDTO);
        Experience experience = experienceMapper.toEntity(experienceDTO);
        experience = experienceRepository.save(experience);
        ExperienceDTO result = experienceMapper.toDto(experience);
        experienceSearchRepository.save(experience);
        return result;
    }

    /**
     *  Get all the experiences.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExperienceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Experiences");
        return experienceRepository.findAll(pageable)
            .map(experienceMapper::toDto);
    }

    /**
     *  Get one experience by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ExperienceDTO findOne(Long id) {
        log.debug("Request to get Experience : {}", id);
        Experience experience = experienceRepository.findOne(id);
        return experienceMapper.toDto(experience);
    }

    /**
     *  Delete the  experience by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Experience : {}", id);
        experienceRepository.delete(id);
        experienceSearchRepository.delete(id);
    }

    /**
     * Search for the experience corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExperienceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Experiences for query {}", query);
        Page<Experience> result = experienceSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(experienceMapper::toDto);
    }
}
