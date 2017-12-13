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

import com.panda.mojobs.domain.Mjob;
import com.panda.mojobs.domain.*; // for static metamodels
import com.panda.mojobs.repository.MjobRepository;
import com.panda.mojobs.repository.search.MjobSearchRepository;
import com.panda.mojobs.service.dto.MjobCriteria;

import com.panda.mojobs.service.dto.MjobDTO;
import com.panda.mojobs.service.mapper.MjobMapper;
import com.panda.mojobs.domain.enumeration.JobType;

/**
 * Service for executing complex queries for Mjob entities in the database.
 * The main input is a {@link MjobCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link MjobDTO} or a {@link Page} of {%link MjobDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class MjobQueryService extends QueryService<Mjob> {

    private final Logger log = LoggerFactory.getLogger(MjobQueryService.class);


    private final MjobRepository mjobRepository;

    private final MjobMapper mjobMapper;

    private final MjobSearchRepository mjobSearchRepository;

    public MjobQueryService(MjobRepository mjobRepository, MjobMapper mjobMapper, MjobSearchRepository mjobSearchRepository) {
        this.mjobRepository = mjobRepository;
        this.mjobMapper = mjobMapper;
        this.mjobSearchRepository = mjobSearchRepository;
    }

    /**
     * Return a {@link List} of {%link MjobDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MjobDTO> findByCriteria(MjobCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Mjob> specification = createSpecification(criteria);
        return mjobMapper.toDto(mjobRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link MjobDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MjobDTO> findByCriteria(MjobCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Mjob> specification = createSpecification(criteria);
        final Page<Mjob> result = mjobRepository.findAll(specification, page);
        return result.map(mjobMapper::toDto);
    }

    /**
     * Function to convert MjobCriteria to a {@link Specifications}
     */
    private Specifications<Mjob> createSpecification(MjobCriteria criteria) {
        Specifications<Mjob> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Mjob_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Mjob_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Mjob_.type));
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAddressId(), Mjob_.address, Address_.id));
            }
            if (criteria.getSchoolId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSchoolId(), Mjob_.school, School_.id));
            }
            if (criteria.getSubTypeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSubTypeId(), Mjob_.subType, JobSubType_.id));
            }
        }
        return specification;
    }

}
