package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.SchoolAddressService;
import com.panda.mojobs.domain.SchoolAddress;
import com.panda.mojobs.repository.SchoolAddressRepository;
import com.panda.mojobs.repository.search.SchoolAddressSearchRepository;
import com.panda.mojobs.service.dto.SchoolAddressDTO;
import com.panda.mojobs.service.mapper.SchoolAddressMapper;
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
 * Service Implementation for managing SchoolAddress.
 */
@Service
@Transactional
public class SchoolAddressServiceImpl implements SchoolAddressService{

    private final Logger log = LoggerFactory.getLogger(SchoolAddressServiceImpl.class);

    private final SchoolAddressRepository schoolAddressRepository;

    private final SchoolAddressMapper schoolAddressMapper;

    private final SchoolAddressSearchRepository schoolAddressSearchRepository;

    public SchoolAddressServiceImpl(SchoolAddressRepository schoolAddressRepository, SchoolAddressMapper schoolAddressMapper, SchoolAddressSearchRepository schoolAddressSearchRepository) {
        this.schoolAddressRepository = schoolAddressRepository;
        this.schoolAddressMapper = schoolAddressMapper;
        this.schoolAddressSearchRepository = schoolAddressSearchRepository;
    }

    /**
     * Save a schoolAddress.
     *
     * @param schoolAddressDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SchoolAddressDTO save(SchoolAddressDTO schoolAddressDTO) {
        log.debug("Request to save SchoolAddress : {}", schoolAddressDTO);
        SchoolAddress schoolAddress = schoolAddressMapper.toEntity(schoolAddressDTO);
        schoolAddress = schoolAddressRepository.save(schoolAddress);
        SchoolAddressDTO result = schoolAddressMapper.toDto(schoolAddress);
        schoolAddressSearchRepository.save(schoolAddress);
        return result;
    }

    /**
     *  Get all the schoolAddresses.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SchoolAddressDTO> findAll() {
        log.debug("Request to get all SchoolAddresses");
        return schoolAddressRepository.findAll().stream()
            .map(schoolAddressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one schoolAddress by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SchoolAddressDTO findOne(Long id) {
        log.debug("Request to get SchoolAddress : {}", id);
        SchoolAddress schoolAddress = schoolAddressRepository.findOne(id);
        return schoolAddressMapper.toDto(schoolAddress);
    }

    /**
     *  Delete the  schoolAddress by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SchoolAddress : {}", id);
        schoolAddressRepository.delete(id);
        schoolAddressSearchRepository.delete(id);
    }

    /**
     * Search for the schoolAddress corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SchoolAddressDTO> search(String query) {
        log.debug("Request to search SchoolAddresses for query {}", query);
        return StreamSupport
            .stream(schoolAddressSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(schoolAddressMapper::toDto)
            .collect(Collectors.toList());
    }
}
