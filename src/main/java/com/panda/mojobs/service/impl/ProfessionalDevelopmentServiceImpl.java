package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.ProfessionalDevelopmentService;
import com.panda.mojobs.domain.ProfessionalDevelopment;
import com.panda.mojobs.repository.ProfessionalDevelopmentRepository;
import com.panda.mojobs.repository.search.ProfessionalDevelopmentSearchRepository;
import com.panda.mojobs.service.dto.ProfessionalDevelopmentDTO;
import com.panda.mojobs.service.mapper.ProfessionalDevelopmentMapper;
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
 * Service Implementation for managing ProfessionalDevelopment.
 */
@Service
@Transactional
public class ProfessionalDevelopmentServiceImpl implements ProfessionalDevelopmentService{

    private final Logger log = LoggerFactory.getLogger(ProfessionalDevelopmentServiceImpl.class);

    private final ProfessionalDevelopmentRepository professionalDevelopmentRepository;

    private final ProfessionalDevelopmentMapper professionalDevelopmentMapper;

    private final ProfessionalDevelopmentSearchRepository professionalDevelopmentSearchRepository;

    public ProfessionalDevelopmentServiceImpl(ProfessionalDevelopmentRepository professionalDevelopmentRepository, ProfessionalDevelopmentMapper professionalDevelopmentMapper, ProfessionalDevelopmentSearchRepository professionalDevelopmentSearchRepository) {
        this.professionalDevelopmentRepository = professionalDevelopmentRepository;
        this.professionalDevelopmentMapper = professionalDevelopmentMapper;
        this.professionalDevelopmentSearchRepository = professionalDevelopmentSearchRepository;
    }

    /**
     * Save a professionalDevelopment.
     *
     * @param professionalDevelopmentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProfessionalDevelopmentDTO save(ProfessionalDevelopmentDTO professionalDevelopmentDTO) {
        log.debug("Request to save ProfessionalDevelopment : {}", professionalDevelopmentDTO);
        ProfessionalDevelopment professionalDevelopment = professionalDevelopmentMapper.toEntity(professionalDevelopmentDTO);
        professionalDevelopment = professionalDevelopmentRepository.save(professionalDevelopment);
        ProfessionalDevelopmentDTO result = professionalDevelopmentMapper.toDto(professionalDevelopment);
        professionalDevelopmentSearchRepository.save(professionalDevelopment);
        return result;
    }

    /**
     *  Get all the professionalDevelopments.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalDevelopmentDTO> findAll() {
        log.debug("Request to get all ProfessionalDevelopments");
        return professionalDevelopmentRepository.findAll().stream()
            .map(professionalDevelopmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one professionalDevelopment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProfessionalDevelopmentDTO findOne(Long id) {
        log.debug("Request to get ProfessionalDevelopment : {}", id);
        ProfessionalDevelopment professionalDevelopment = professionalDevelopmentRepository.findOne(id);
        return professionalDevelopmentMapper.toDto(professionalDevelopment);
    }

    /**
     *  Delete the  professionalDevelopment by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProfessionalDevelopment : {}", id);
        professionalDevelopmentRepository.delete(id);
        professionalDevelopmentSearchRepository.delete(id);
    }

    /**
     * Search for the professionalDevelopment corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalDevelopmentDTO> search(String query) {
        log.debug("Request to search ProfessionalDevelopments for query {}", query);
        return StreamSupport
            .stream(professionalDevelopmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(professionalDevelopmentMapper::toDto)
            .collect(Collectors.toList());
    }
}
