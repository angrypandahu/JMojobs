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

import com.panda.mojobs.domain.Resume;
import com.panda.mojobs.domain.*; // for static metamodels
import com.panda.mojobs.repository.ResumeRepository;
import com.panda.mojobs.repository.search.ResumeSearchRepository;
import com.panda.mojobs.service.dto.ResumeCriteria;

import com.panda.mojobs.service.dto.ResumeDTO;
import com.panda.mojobs.service.mapper.ResumeMapper;
import com.panda.mojobs.domain.enumeration.CanBeReadBy;

/**
 * Service for executing complex queries for Resume entities in the database.
 * The main input is a {@link ResumeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link ResumeDTO} or a {@link Page} of {%link ResumeDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class ResumeQueryService extends QueryService<Resume> {

    private final Logger log = LoggerFactory.getLogger(ResumeQueryService.class);


    private final ResumeRepository resumeRepository;

    private final ResumeMapper resumeMapper;

    private final ResumeSearchRepository resumeSearchRepository;

    public ResumeQueryService(ResumeRepository resumeRepository, ResumeMapper resumeMapper, ResumeSearchRepository resumeSearchRepository) {
        this.resumeRepository = resumeRepository;
        this.resumeMapper = resumeMapper;
        this.resumeSearchRepository = resumeSearchRepository;
    }

    /**
     * Return a {@link List} of {%link ResumeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ResumeDTO> findByCriteria(ResumeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Resume> specification = createSpecification(criteria);
        return resumeMapper.toDto(resumeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link ResumeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ResumeDTO> findByCriteria(ResumeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Resume> specification = createSpecification(criteria);
        final Page<Resume> result = resumeRepository.findAll(specification, page);
        return result.map(resumeMapper::toDto);
    }

    /**
     * Function to convert ResumeCriteria to a {@link Specifications}
     */
    private Specifications<Resume> createSpecification(ResumeCriteria criteria) {
        Specifications<Resume> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Resume_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Resume_.name));
            }
            if (criteria.getCanBeReadBy() != null) {
                specification = specification.and(buildSpecification(criteria.getCanBeReadBy(), Resume_.canBeReadBy));
            }
            if (criteria.getOthers() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOthers(), Resume_.others));
            }
            if (criteria.getBasicInformationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBasicInformationId(), Resume_.basicInformation, BasicInformation_.id));
            }
            if (criteria.getExperienciesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getExperienciesId(), Resume_.experiencies, Experience_.id));
            }
            if (criteria.getEducationBackgroundsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getEducationBackgroundsId(), Resume_.educationBackgrounds, EducationBackground_.id));
            }
            if (criteria.getProfessionalDevelopmentsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfessionalDevelopmentsId(), Resume_.professionalDevelopments, ProfessionalDevelopment_.id));
            }
            if (criteria.getLanguagesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLanguagesId(), Resume_.languages, MLanguage_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), Resume_.user, User_.id));
            }
        }
        return specification;
    }

}
