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

import com.panda.mojobs.domain.Experience;
import com.panda.mojobs.domain.*; // for static metamodels
import com.panda.mojobs.repository.ExperienceRepository;
import com.panda.mojobs.repository.search.ExperienceSearchRepository;
import com.panda.mojobs.service.dto.ExperienceCriteria;

import com.panda.mojobs.service.dto.ExperienceDTO;
import com.panda.mojobs.service.mapper.ExperienceMapper;

/**
 * Service for executing complex queries for Experience entities in the database.
 * The main input is a {@link ExperienceCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link ExperienceDTO} or a {@link Page} of {%link ExperienceDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class ExperienceQueryService extends QueryService<Experience> {

    private final Logger log = LoggerFactory.getLogger(ExperienceQueryService.class);


    private final ExperienceRepository experienceRepository;

    private final ExperienceMapper experienceMapper;

    private final ExperienceSearchRepository experienceSearchRepository;

    public ExperienceQueryService(ExperienceRepository experienceRepository, ExperienceMapper experienceMapper, ExperienceSearchRepository experienceSearchRepository) {
        this.experienceRepository = experienceRepository;
        this.experienceMapper = experienceMapper;
        this.experienceSearchRepository = experienceSearchRepository;
    }

    /**
     * Return a {@link List} of {%link ExperienceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExperienceDTO> findByCriteria(ExperienceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Experience> specification = createSpecification(criteria);
        return experienceMapper.toDto(experienceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link ExperienceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExperienceDTO> findByCriteria(ExperienceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Experience> specification = createSpecification(criteria);
        final Page<Experience> result = experienceRepository.findAll(specification, page);
        return result.map(experienceMapper::toDto);
    }

    /**
     * Function to convert ExperienceCriteria to a {@link Specifications}
     */
    private Specifications<Experience> createSpecification(ExperienceCriteria criteria) {
        Specifications<Experience> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Experience_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Experience_.title));
            }
            if (criteria.getSchool() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSchool(), Experience_.school));
            }
            if (criteria.getGrade() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGrade(), Experience_.grade));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Experience_.location));
            }
            if (criteria.getFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFromDate(), Experience_.fromDate));
            }
            if (criteria.getToDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getToDate(), Experience_.toDate));
            }
            if (criteria.getCurrentlyWorkHere() != null) {
                specification = specification.and(buildSpecification(criteria.getCurrentlyWorkHere(), Experience_.currentlyWorkHere));
            }
            if (criteria.getResponsibility() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResponsibility(), Experience_.responsibility));
            }
            if (criteria.getResumeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getResumeId(), Experience_.resume, Resume_.id));
            }
        }
        return specification;
    }

}
