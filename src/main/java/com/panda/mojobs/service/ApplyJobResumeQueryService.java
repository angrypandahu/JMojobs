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

import com.panda.mojobs.domain.ApplyJobResume;
import com.panda.mojobs.domain.*; // for static metamodels
import com.panda.mojobs.repository.ApplyJobResumeRepository;
import com.panda.mojobs.repository.search.ApplyJobResumeSearchRepository;
import com.panda.mojobs.service.dto.ApplyJobResumeCriteria;

import com.panda.mojobs.service.dto.ApplyJobResumeDTO;
import com.panda.mojobs.service.mapper.ApplyJobResumeMapper;

/**
 * Service for executing complex queries for ApplyJobResume entities in the database.
 * The main input is a {@link ApplyJobResumeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link ApplyJobResumeDTO} or a {@link Page} of {%link ApplyJobResumeDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class ApplyJobResumeQueryService extends QueryService<ApplyJobResume> {

    private final Logger log = LoggerFactory.getLogger(ApplyJobResumeQueryService.class);


    private final ApplyJobResumeRepository applyJobResumeRepository;

    private final ApplyJobResumeMapper applyJobResumeMapper;

    private final ApplyJobResumeSearchRepository applyJobResumeSearchRepository;

    public ApplyJobResumeQueryService(ApplyJobResumeRepository applyJobResumeRepository, ApplyJobResumeMapper applyJobResumeMapper, ApplyJobResumeSearchRepository applyJobResumeSearchRepository) {
        this.applyJobResumeRepository = applyJobResumeRepository;
        this.applyJobResumeMapper = applyJobResumeMapper;
        this.applyJobResumeSearchRepository = applyJobResumeSearchRepository;
    }

    /**
     * Return a {@link List} of {%link ApplyJobResumeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ApplyJobResumeDTO> findByCriteria(ApplyJobResumeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ApplyJobResume> specification = createSpecification(criteria);
        return applyJobResumeMapper.toDto(applyJobResumeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link ApplyJobResumeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ApplyJobResumeDTO> findByCriteria(ApplyJobResumeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ApplyJobResume> specification = createSpecification(criteria);
        final Page<ApplyJobResume> result = applyJobResumeRepository.findAll(specification, page);
        return result.map(applyJobResumeMapper::toDto);
    }

    /**
     * Function to convert ApplyJobResumeCriteria to a {@link Specifications}
     */
    private Specifications<ApplyJobResume> createSpecification(ApplyJobResumeCriteria criteria) {
        Specifications<ApplyJobResume> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ApplyJobResume_.id));
            }
            if (criteria.getApplyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApplyDate(), ApplyJobResume_.applyDate));
            }
            if (criteria.getResumeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getResumeId(), ApplyJobResume_.resume, Resume_.id));
            }
            if (criteria.getMjobId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMjobId(), ApplyJobResume_.mjob, Mjob_.id));
            }
        }
        return specification;
    }

}
