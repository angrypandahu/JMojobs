package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.TownService;
import com.panda.mojobs.domain.Town;
import com.panda.mojobs.repository.TownRepository;
import com.panda.mojobs.repository.search.TownSearchRepository;
import com.panda.mojobs.service.dto.TownDTO;
import com.panda.mojobs.service.mapper.TownMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Town.
 */
@Service
@Transactional
public class TownServiceImpl implements TownService{

    private final Logger log = LoggerFactory.getLogger(TownServiceImpl.class);

    private final TownRepository townRepository;

    private final TownMapper townMapper;

    private final TownSearchRepository townSearchRepository;

    public TownServiceImpl(TownRepository townRepository, TownMapper townMapper, TownSearchRepository townSearchRepository) {
        this.townRepository = townRepository;
        this.townMapper = townMapper;
        this.townSearchRepository = townSearchRepository;
    }

    /**
     * Save a town.
     *
     * @param townDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TownDTO save(TownDTO townDTO) {
        log.debug("Request to save Town : {}", townDTO);
        Town town = townMapper.toEntity(townDTO);
        town = townRepository.save(town);
        TownDTO result = townMapper.toDto(town);
        townSearchRepository.save(town);
        return result;
    }

    /**
     *  Get all the towns.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TownDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Towns");
        return townRepository.findAll(pageable)
            .map(townMapper::toDto);
    }

    /**
     *  Get one town by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TownDTO findOne(Long id) {
        log.debug("Request to get Town : {}", id);
        Town town = townRepository.findOne(id);
        return townMapper.toDto(town);
    }

    /**
     *  Delete the  town by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Town : {}", id);
        townRepository.delete(id);
        townSearchRepository.delete(id);
    }

    /**
     * Search for the town corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TownDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Towns for query {}", query);
        Page<Town> result = townSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(townMapper::toDto);
    }
}
