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

import com.panda.mojobs.domain.Invitation;
import com.panda.mojobs.domain.*; // for static metamodels
import com.panda.mojobs.repository.InvitationRepository;
import com.panda.mojobs.repository.search.InvitationSearchRepository;
import com.panda.mojobs.service.dto.InvitationCriteria;

import com.panda.mojobs.service.dto.InvitationDTO;
import com.panda.mojobs.service.mapper.InvitationMapper;

/**
 * Service for executing complex queries for Invitation entities in the database.
 * The main input is a {@link InvitationCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link InvitationDTO} or a {@link Page} of {%link InvitationDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class InvitationQueryService extends QueryService<Invitation> {

    private final Logger log = LoggerFactory.getLogger(InvitationQueryService.class);


    private final InvitationRepository invitationRepository;

    private final InvitationMapper invitationMapper;

    private final InvitationSearchRepository invitationSearchRepository;

    public InvitationQueryService(InvitationRepository invitationRepository, InvitationMapper invitationMapper, InvitationSearchRepository invitationSearchRepository) {
        this.invitationRepository = invitationRepository;
        this.invitationMapper = invitationMapper;
        this.invitationSearchRepository = invitationSearchRepository;
    }

    /**
     * Return a {@link List} of {%link InvitationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvitationDTO> findByCriteria(InvitationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Invitation> specification = createSpecification(criteria);
        return invitationMapper.toDto(invitationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link InvitationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvitationDTO> findByCriteria(InvitationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Invitation> specification = createSpecification(criteria);
        final Page<Invitation> result = invitationRepository.findAll(specification, page);
        return result.map(invitationMapper::toDto);
    }

    /**
     * Function to convert InvitationCriteria to a {@link Specifications}
     */
    private Specifications<Invitation> createSpecification(InvitationCriteria criteria) {
        Specifications<Invitation> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Invitation_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Invitation_.name));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubject(), Invitation_.subject));
            }
            if (criteria.getFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFromDate(), Invitation_.fromDate));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), Invitation_.content));
            }
            if (criteria.getSchoolId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSchoolId(), Invitation_.school, School_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), Invitation_.user, User_.id));
            }
        }
        return specification;
    }

}
