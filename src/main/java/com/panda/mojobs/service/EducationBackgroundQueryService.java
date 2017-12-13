package com.panda.mojobs.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.panda.mojobs.domain.EducationBackground;
import com.panda.mojobs.domain.*; // for static metamodels
import com.panda.mojobs.repository.EducationBackgroundRepository;
import com.panda.mojobs.repository.search.EducationBackgroundSearchRepository;
import com.panda.mojobs.service.dto.EducationBackgroundCriteria;

import com.panda.mojobs.service.dto.EducationBackgroundDTO;
import com.panda.mojobs.service.mapper.EducationBackgroundMapper;
import com.panda.mojobs.domain.enumeration.EducationLevel;

/**
 * Service for executing complex queries for EducationBackground entities in the database.
 * The main input is a {@link EducationBackgroundCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link EducationBackgroundDTO} or a {@link Page} of {%link EducationBackgroundDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class EducationBackgroundQueryService extends QueryService<EducationBackground> {

    private final Logger log = LoggerFactory.getLogger(EducationBackgroundQueryService.class);


    private final EducationBackgroundRepository educationBackgroundRepository;

    private final EducationBackgroundMapper educationBackgroundMapper;

    private final EducationBackgroundSearchRepository educationBackgroundSearchRepository;

    public EducationBackgroundQueryService(EducationBackgroundRepository educationBackgroundRepository, EducationBackgroundMapper educationBackgroundMapper, EducationBackgroundSearchRepository educationBackgroundSearchRepository) {
        this.educationBackgroundRepository = educationBackgroundRepository;
        this.educationBackgroundMapper = educationBackgroundMapper;
        this.educationBackgroundSearchRepository = educationBackgroundSearchRepository;
    }

    /**
     * Return a {@link List} of {%link EducationBackgroundDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EducationBackgroundDTO> findByCriteria(EducationBackgroundCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<EducationBackground> specification = createSpecification(criteria);
        return educationBackgroundMapper.toDto(educationBackgroundRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link EducationBackgroundDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EducationBackgroundDTO> findByCriteria(EducationBackgroundCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<EducationBackground> specification = createSpecification(criteria);
        final Page<EducationBackground> result = educationBackgroundRepository.findAll(specification, page);
        return result.map(educationBackgroundMapper::toDto);
    }

    /**
     * Function to convert EducationBackgroundCriteria to a {@link Specifications}
     */
    private Specifications<EducationBackground> createSpecification(EducationBackgroundCriteria criteria) {
        Specifications<EducationBackground> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), EducationBackground_.id));
            }
            if (criteria.getSchool() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSchool(), EducationBackground_.school));
            }
            if (criteria.getMajor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMajor(), EducationBackground_.major));
            }
            if (criteria.getDegree() != null) {
                specification = specification.and(buildSpecification(criteria.getDegree(), EducationBackground_.degree));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), EducationBackground_.location));
            }
            if (criteria.getFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFromDate(), EducationBackground_.fromDate));
            }
            if (criteria.getToDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getToDate(), EducationBackground_.toDate));
            }
            if (criteria.getResumeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getResumeId(), EducationBackground_.resume, Resume_.id));
            }
        }
        return specification;
    }

}
