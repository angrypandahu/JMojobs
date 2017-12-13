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

import com.panda.mojobs.domain.ProfessionalDevelopment;
import com.panda.mojobs.domain.*; // for static metamodels
import com.panda.mojobs.repository.ProfessionalDevelopmentRepository;
import com.panda.mojobs.repository.search.ProfessionalDevelopmentSearchRepository;
import com.panda.mojobs.service.dto.ProfessionalDevelopmentCriteria;

import com.panda.mojobs.service.dto.ProfessionalDevelopmentDTO;
import com.panda.mojobs.service.mapper.ProfessionalDevelopmentMapper;

/**
 * Service for executing complex queries for ProfessionalDevelopment entities in the database.
 * The main input is a {@link ProfessionalDevelopmentCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link ProfessionalDevelopmentDTO} or a {@link Page} of {%link ProfessionalDevelopmentDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class ProfessionalDevelopmentQueryService extends QueryService<ProfessionalDevelopment> {

    private final Logger log = LoggerFactory.getLogger(ProfessionalDevelopmentQueryService.class);


    private final ProfessionalDevelopmentRepository professionalDevelopmentRepository;

    private final ProfessionalDevelopmentMapper professionalDevelopmentMapper;

    private final ProfessionalDevelopmentSearchRepository professionalDevelopmentSearchRepository;

    public ProfessionalDevelopmentQueryService(ProfessionalDevelopmentRepository professionalDevelopmentRepository, ProfessionalDevelopmentMapper professionalDevelopmentMapper, ProfessionalDevelopmentSearchRepository professionalDevelopmentSearchRepository) {
        this.professionalDevelopmentRepository = professionalDevelopmentRepository;
        this.professionalDevelopmentMapper = professionalDevelopmentMapper;
        this.professionalDevelopmentSearchRepository = professionalDevelopmentSearchRepository;
    }

    /**
     * Return a {@link List} of {%link ProfessionalDevelopmentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProfessionalDevelopmentDTO> findByCriteria(ProfessionalDevelopmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ProfessionalDevelopment> specification = createSpecification(criteria);
        return professionalDevelopmentMapper.toDto(professionalDevelopmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link ProfessionalDevelopmentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalDevelopmentDTO> findByCriteria(ProfessionalDevelopmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ProfessionalDevelopment> specification = createSpecification(criteria);
        final Page<ProfessionalDevelopment> result = professionalDevelopmentRepository.findAll(specification, page);
        return result.map(professionalDevelopmentMapper::toDto);
    }

    /**
     * Function to convert ProfessionalDevelopmentCriteria to a {@link Specifications}
     */
    private Specifications<ProfessionalDevelopment> createSpecification(ProfessionalDevelopmentCriteria criteria) {
        Specifications<ProfessionalDevelopment> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProfessionalDevelopment_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProfessionalDevelopment_.name));
            }
            if (criteria.getFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFromDate(), ProfessionalDevelopment_.fromDate));
            }
            if (criteria.getToDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getToDate(), ProfessionalDevelopment_.toDate));
            }
            if (criteria.getResumeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getResumeId(), ProfessionalDevelopment_.resume, Resume_.id));
            }
        }
        return specification;
    }

}
