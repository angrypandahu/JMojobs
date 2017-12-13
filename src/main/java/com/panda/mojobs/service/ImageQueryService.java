package com.panda.mojobs.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.panda.mojobs.domain.Image;
import com.panda.mojobs.domain.*; // for static metamodels
import com.panda.mojobs.repository.ImageRepository;
import com.panda.mojobs.repository.search.ImageSearchRepository;
import com.panda.mojobs.service.dto.ImageCriteria;

import com.panda.mojobs.service.dto.ImageDTO;
import com.panda.mojobs.service.mapper.ImageMapper;

/**
 * Service for executing complex queries for Image entities in the database.
 * The main input is a {@link ImageCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link ImageDTO} or a {@link Page} of {%link ImageDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class ImageQueryService extends QueryService<Image> {

    private final Logger log = LoggerFactory.getLogger(ImageQueryService.class);


    private final ImageRepository imageRepository;

    private final ImageMapper imageMapper;

    private final ImageSearchRepository imageSearchRepository;

    public ImageQueryService(ImageRepository imageRepository, ImageMapper imageMapper, ImageSearchRepository imageSearchRepository) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        this.imageSearchRepository = imageSearchRepository;
    }

    /**
     * Return a {@link List} of {%link ImageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ImageDTO> findByCriteria(ImageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Image> specification = createSpecification(criteria);
        return imageMapper.toDto(imageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link ImageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageDTO> findByCriteria(ImageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Image> specification = createSpecification(criteria);
        final Page<Image> result = imageRepository.findAll(specification, page);
        return result.map(imageMapper::toDto);
    }

    /**
     * Function to convert ImageCriteria to a {@link Specifications}
     */
    private Specifications<Image> createSpecification(ImageCriteria criteria) {
        Specifications<Image> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Image_.id));
            }
        }
        return specification;
    }

}
