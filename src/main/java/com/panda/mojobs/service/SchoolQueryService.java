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

import com.panda.mojobs.domain.School;
import com.panda.mojobs.domain.*; // for static metamodels
import com.panda.mojobs.repository.SchoolRepository;
import com.panda.mojobs.repository.search.SchoolSearchRepository;
import com.panda.mojobs.service.dto.SchoolCriteria;

import com.panda.mojobs.service.dto.SchoolDTO;
import com.panda.mojobs.service.mapper.SchoolMapper;
import com.panda.mojobs.domain.enumeration.SchoolLevel;
import com.panda.mojobs.domain.enumeration.SchoolType;

/**
 * Service for executing complex queries for School entities in the database.
 * The main input is a {@link SchoolCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link SchoolDTO} or a {@link Page} of {%link SchoolDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class SchoolQueryService extends QueryService<School> {

    private final Logger log = LoggerFactory.getLogger(SchoolQueryService.class);


    private final SchoolRepository schoolRepository;

    private final SchoolMapper schoolMapper;

    private final SchoolSearchRepository schoolSearchRepository;

    public SchoolQueryService(SchoolRepository schoolRepository, SchoolMapper schoolMapper, SchoolSearchRepository schoolSearchRepository) {
        this.schoolRepository = schoolRepository;
        this.schoolMapper = schoolMapper;
        this.schoolSearchRepository = schoolSearchRepository;
    }

    /**
     * Return a {@link List} of {%link SchoolDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolDTO> findByCriteria(SchoolCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<School> specification = createSpecification(criteria);
        return schoolMapper.toDto(schoolRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link SchoolDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolDTO> findByCriteria(SchoolCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<School> specification = createSpecification(criteria);
        final Page<School> result = schoolRepository.findAll(specification, page);
        return result.map(schoolMapper::toDto);
    }

    /**
     * Function to convert SchoolCriteria to a {@link Specifications}
     */
    private Specifications<School> createSpecification(SchoolCriteria criteria) {
        Specifications<School> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), School_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), School_.name));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildSpecification(criteria.getLevel(), School_.level));
            }
            if (criteria.getSchoolType() != null) {
                specification = specification.and(buildSpecification(criteria.getSchoolType(), School_.schoolType));
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAddressId(), School_.address, Address_.id));
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getImageId(), School_.image, Image_.id));
            }
            if (criteria.getJobsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getJobsId(), School_.jobs, Mjob_.id));
            }
        }
        return specification;
    }

}
