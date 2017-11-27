package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.BasicInformationService;
import com.panda.mojobs.domain.BasicInformation;
import com.panda.mojobs.repository.BasicInformationRepository;
import com.panda.mojobs.repository.search.BasicInformationSearchRepository;
import com.panda.mojobs.service.dto.BasicInformationDTO;
import com.panda.mojobs.service.mapper.BasicInformationMapper;
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
 * Service Implementation for managing BasicInformation.
 */
@Service
@Transactional
public class BasicInformationServiceImpl implements BasicInformationService{

    private final Logger log = LoggerFactory.getLogger(BasicInformationServiceImpl.class);

    private final BasicInformationRepository basicInformationRepository;

    private final BasicInformationMapper basicInformationMapper;

    private final BasicInformationSearchRepository basicInformationSearchRepository;

    public BasicInformationServiceImpl(BasicInformationRepository basicInformationRepository, BasicInformationMapper basicInformationMapper, BasicInformationSearchRepository basicInformationSearchRepository) {
        this.basicInformationRepository = basicInformationRepository;
        this.basicInformationMapper = basicInformationMapper;
        this.basicInformationSearchRepository = basicInformationSearchRepository;
    }

    /**
     * Save a basicInformation.
     *
     * @param basicInformationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BasicInformationDTO save(BasicInformationDTO basicInformationDTO) {
        log.debug("Request to save BasicInformation : {}", basicInformationDTO);
        BasicInformation basicInformation = basicInformationMapper.toEntity(basicInformationDTO);
        basicInformation = basicInformationRepository.save(basicInformation);
        BasicInformationDTO result = basicInformationMapper.toDto(basicInformation);
        basicInformationSearchRepository.save(basicInformation);
        return result;
    }

    /**
     *  Get all the basicInformations.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<BasicInformationDTO> findAll() {
        log.debug("Request to get all BasicInformations");
        return basicInformationRepository.findAll().stream()
            .map(basicInformationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     *  get all the basicInformations where Resume is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<BasicInformationDTO> findAllWhereResumeIsNull() {
        log.debug("Request to get all basicInformations where Resume is null");
        return StreamSupport
            .stream(basicInformationRepository.findAll().spliterator(), false)
            .filter(basicInformation -> basicInformation.getResume() == null)
            .map(basicInformationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one basicInformation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BasicInformationDTO findOne(Long id) {
        log.debug("Request to get BasicInformation : {}", id);
        BasicInformation basicInformation = basicInformationRepository.findOne(id);
        return basicInformationMapper.toDto(basicInformation);
    }

    /**
     *  Delete the  basicInformation by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BasicInformation : {}", id);
        basicInformationRepository.delete(id);
        basicInformationSearchRepository.delete(id);
    }

    /**
     * Search for the basicInformation corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<BasicInformationDTO> search(String query) {
        log.debug("Request to search BasicInformations for query {}", query);
        return StreamSupport
            .stream(basicInformationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(basicInformationMapper::toDto)
            .collect(Collectors.toList());
    }
}
