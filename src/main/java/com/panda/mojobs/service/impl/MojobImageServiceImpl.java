package com.panda.mojobs.service.impl;

import com.panda.mojobs.service.MojobImageService;
import com.panda.mojobs.domain.MojobImage;
import com.panda.mojobs.repository.MojobImageRepository;
import com.panda.mojobs.repository.search.MojobImageSearchRepository;
import com.panda.mojobs.service.dto.MojobImageDTO;
import com.panda.mojobs.service.mapper.MojobImageMapper;
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
 * Service Implementation for managing MojobImage.
 */
@Service
@Transactional
public class MojobImageServiceImpl implements MojobImageService{

    private final Logger log = LoggerFactory.getLogger(MojobImageServiceImpl.class);

    private final MojobImageRepository mojobImageRepository;

    private final MojobImageMapper mojobImageMapper;

    private final MojobImageSearchRepository mojobImageSearchRepository;

    public MojobImageServiceImpl(MojobImageRepository mojobImageRepository, MojobImageMapper mojobImageMapper, MojobImageSearchRepository mojobImageSearchRepository) {
        this.mojobImageRepository = mojobImageRepository;
        this.mojobImageMapper = mojobImageMapper;
        this.mojobImageSearchRepository = mojobImageSearchRepository;
    }

    /**
     * Save a mojobImage.
     *
     * @param mojobImageDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MojobImageDTO save(MojobImageDTO mojobImageDTO) {
        log.debug("Request to save MojobImage : {}", mojobImageDTO);
        MojobImage mojobImage = mojobImageMapper.toEntity(mojobImageDTO);
        mojobImage = mojobImageRepository.save(mojobImage);
        MojobImageDTO result = mojobImageMapper.toDto(mojobImage);
        mojobImageSearchRepository.save(mojobImage);
        return result;
    }

    /**
     *  Get all the mojobImages.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MojobImageDTO> findAll() {
        log.debug("Request to get all MojobImages");
        return mojobImageRepository.findAll().stream()
            .map(mojobImageMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one mojobImage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MojobImageDTO findOne(Long id) {
        log.debug("Request to get MojobImage : {}", id);
        MojobImage mojobImage = mojobImageRepository.findOne(id);
        return mojobImageMapper.toDto(mojobImage);
    }

    /**
     *  Delete the  mojobImage by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MojobImage : {}", id);
        mojobImageRepository.delete(id);
        mojobImageSearchRepository.delete(id);
    }

    /**
     * Search for the mojobImage corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MojobImageDTO> search(String query) {
        log.debug("Request to search MojobImages for query {}", query);
        return StreamSupport
            .stream(mojobImageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(mojobImageMapper::toDto)
            .collect(Collectors.toList());
    }
}
