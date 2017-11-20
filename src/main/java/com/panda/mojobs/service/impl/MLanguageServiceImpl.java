package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.MLanguageService;
import com.panda.mojobs.domain.MLanguage;
import com.panda.mojobs.repository.MLanguageRepository;
import com.panda.mojobs.repository.search.MLanguageSearchRepository;
import com.panda.mojobs.service.dto.MLanguageDTO;
import com.panda.mojobs.service.mapper.MLanguageMapper;
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
 * Service Implementation for managing MLanguage.
 */
@Service
@Transactional
public class MLanguageServiceImpl implements MLanguageService{

    private final Logger log = LoggerFactory.getLogger(MLanguageServiceImpl.class);

    private final MLanguageRepository mLanguageRepository;

    private final MLanguageMapper mLanguageMapper;

    private final MLanguageSearchRepository mLanguageSearchRepository;

    public MLanguageServiceImpl(MLanguageRepository mLanguageRepository, MLanguageMapper mLanguageMapper, MLanguageSearchRepository mLanguageSearchRepository) {
        this.mLanguageRepository = mLanguageRepository;
        this.mLanguageMapper = mLanguageMapper;
        this.mLanguageSearchRepository = mLanguageSearchRepository;
    }

    /**
     * Save a mLanguage.
     *
     * @param mLanguageDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MLanguageDTO save(MLanguageDTO mLanguageDTO) {
        log.debug("Request to save MLanguage : {}", mLanguageDTO);
        MLanguage mLanguage = mLanguageMapper.toEntity(mLanguageDTO);
        mLanguage = mLanguageRepository.save(mLanguage);
        MLanguageDTO result = mLanguageMapper.toDto(mLanguage);
        mLanguageSearchRepository.save(mLanguage);
        return result;
    }

    /**
     *  Get all the mLanguages.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MLanguageDTO> findAll() {
        log.debug("Request to get all MLanguages");
        return mLanguageRepository.findAll().stream()
            .map(mLanguageMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one mLanguage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MLanguageDTO findOne(Long id) {
        log.debug("Request to get MLanguage : {}", id);
        MLanguage mLanguage = mLanguageRepository.findOne(id);
        return mLanguageMapper.toDto(mLanguage);
    }

    /**
     *  Delete the  mLanguage by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MLanguage : {}", id);
        mLanguageRepository.delete(id);
        mLanguageSearchRepository.delete(id);
    }

    /**
     * Search for the mLanguage corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MLanguageDTO> search(String query) {
        log.debug("Request to search MLanguages for query {}", query);
        return StreamSupport
            .stream(mLanguageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(mLanguageMapper::toDto)
            .collect(Collectors.toList());
    }
}
