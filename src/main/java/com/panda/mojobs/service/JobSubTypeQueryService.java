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

import com.panda.mojobs.domain.JobSubType;
import com.panda.mojobs.domain.*; // for static metamodels
import com.panda.mojobs.repository.JobSubTypeRepository;
import com.panda.mojobs.repository.search.JobSubTypeSearchRepository;
import com.panda.mojobs.service.dto.JobSubTypeCriteria;

import com.panda.mojobs.service.dto.JobSubTypeDTO;
import com.panda.mojobs.service.mapper.JobSubTypeMapper;
import com.panda.mojobs.domain.enumeration.JobType;

/**
 * Service for executing complex queries for JobSubType entities in the database.
 * The main input is a {@link JobSubTypeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link JobSubTypeDTO} or a {@link Page} of {%link JobSubTypeDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class JobSubTypeQueryService extends QueryService<JobSubType> {

    private final Logger log = LoggerFactory.getLogger(JobSubTypeQueryService.class);


    private final JobSubTypeRepository jobSubTypeRepository;

    private final JobSubTypeMapper jobSubTypeMapper;

    private final JobSubTypeSearchRepository jobSubTypeSearchRepository;

    public JobSubTypeQueryService(JobSubTypeRepository jobSubTypeRepository, JobSubTypeMapper jobSubTypeMapper, JobSubTypeSearchRepository jobSubTypeSearchRepository) {
        this.jobSubTypeRepository = jobSubTypeRepository;
        this.jobSubTypeMapper = jobSubTypeMapper;
        this.jobSubTypeSearchRepository = jobSubTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {%link JobSubTypeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JobSubTypeDTO> findByCriteria(JobSubTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<JobSubType> specification = createSpecification(criteria);
        return jobSubTypeMapper.toDto(jobSubTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link JobSubTypeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JobSubTypeDTO> findByCriteria(JobSubTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<JobSubType> specification = createSpecification(criteria);
        final Page<JobSubType> result = jobSubTypeRepository.findAll(specification, page);
        return result.map(jobSubTypeMapper::toDto);
    }

    /**
     * Function to convert JobSubTypeCriteria to a {@link Specifications}
     */
    private Specifications<JobSubType> createSpecification(JobSubTypeCriteria criteria) {
        Specifications<JobSubType> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), JobSubType_.id));
            }
            if (criteria.getParent() != null) {
                specification = specification.and(buildSpecification(criteria.getParent(), JobSubType_.parent));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), JobSubType_.name));
            }
        }
        return specification;
    }

}
