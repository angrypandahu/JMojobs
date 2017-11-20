package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.ProvinceService;
import com.panda.mojobs.domain.Province;
import com.panda.mojobs.repository.ProvinceRepository;
import com.panda.mojobs.repository.search.ProvinceSearchRepository;
import com.panda.mojobs.service.dto.ProvinceDTO;
import com.panda.mojobs.service.mapper.ProvinceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Province.
 */
@Service
@Transactional
public class ProvinceServiceImpl implements ProvinceService{

    private final Logger log = LoggerFactory.getLogger(ProvinceServiceImpl.class);

    private final ProvinceRepository provinceRepository;

    private final ProvinceMapper provinceMapper;

    private final ProvinceSearchRepository provinceSearchRepository;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository, ProvinceMapper provinceMapper, ProvinceSearchRepository provinceSearchRepository) {
        this.provinceRepository = provinceRepository;
        this.provinceMapper = provinceMapper;
        this.provinceSearchRepository = provinceSearchRepository;
    }

    /**
     * Save a province.
     *
     * @param provinceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProvinceDTO save(ProvinceDTO provinceDTO) {
        log.debug("Request to save Province : {}", provinceDTO);
        Province province = provinceMapper.toEntity(provinceDTO);
        province = provinceRepository.save(province);
        ProvinceDTO result = provinceMapper.toDto(province);
        provinceSearchRepository.save(province);
        return result;
    }

    /**
     *  Get all the provinces.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProvinceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Provinces");
        return provinceRepository.findAll(pageable)
            .map(provinceMapper::toDto);
    }

    /**
     *  Get one province by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProvinceDTO findOne(Long id) {
        log.debug("Request to get Province : {}", id);
        Province province = provinceRepository.findOne(id);
        return provinceMapper.toDto(province);
    }

    /**
     *  Delete the  province by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Province : {}", id);
        provinceRepository.delete(id);
        provinceSearchRepository.delete(id);
    }

    /**
     * Search for the province corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProvinceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Provinces for query {}", query);
        Page<Province> result = provinceSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(provinceMapper::toDto);
    }
}
