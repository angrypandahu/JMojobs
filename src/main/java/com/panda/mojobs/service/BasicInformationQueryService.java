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

import com.panda.mojobs.domain.BasicInformation;
import com.panda.mojobs.domain.*; // for static metamodels
import com.panda.mojobs.repository.BasicInformationRepository;
import com.panda.mojobs.repository.search.BasicInformationSearchRepository;
import com.panda.mojobs.service.dto.BasicInformationCriteria;

import com.panda.mojobs.service.dto.BasicInformationDTO;
import com.panda.mojobs.service.mapper.BasicInformationMapper;
import com.panda.mojobs.domain.enumeration.Gender;
import com.panda.mojobs.domain.enumeration.EducationLevel;

/**
 * Service for executing complex queries for BasicInformation entities in the database.
 * The main input is a {@link BasicInformationCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link BasicInformationDTO} or a {@link Page} of {%link BasicInformationDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class BasicInformationQueryService extends QueryService<BasicInformation> {

    private final Logger log = LoggerFactory.getLogger(BasicInformationQueryService.class);


    private final BasicInformationRepository basicInformationRepository;

    private final BasicInformationMapper basicInformationMapper;

    private final BasicInformationSearchRepository basicInformationSearchRepository;

    public BasicInformationQueryService(BasicInformationRepository basicInformationRepository, BasicInformationMapper basicInformationMapper, BasicInformationSearchRepository basicInformationSearchRepository) {
        this.basicInformationRepository = basicInformationRepository;
        this.basicInformationMapper = basicInformationMapper;
        this.basicInformationSearchRepository = basicInformationSearchRepository;
    }

    /**
     * Return a {@link List} of {%link BasicInformationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BasicInformationDTO> findByCriteria(BasicInformationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<BasicInformation> specification = createSpecification(criteria);
        return basicInformationMapper.toDto(basicInformationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link BasicInformationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BasicInformationDTO> findByCriteria(BasicInformationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<BasicInformation> specification = createSpecification(criteria);
        final Page<BasicInformation> result = basicInformationRepository.findAll(specification, page);
        return result.map(basicInformationMapper::toDto);
    }

    /**
     * Function to convert BasicInformationCriteria to a {@link Specifications}
     */
    private Specifications<BasicInformation> createSpecification(BasicInformationCriteria criteria) {
        Specifications<BasicInformation> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), BasicInformation_.id));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), BasicInformation_.lastName));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), BasicInformation_.firstName));
            }
            if (criteria.getNationality() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNationality(), BasicInformation_.nationality));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), BasicInformation_.gender));
            }
            if (criteria.getDateofBrith() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateofBrith(), BasicInformation_.dateofBrith));
            }
            if (criteria.getEducationLevel() != null) {
                specification = specification.and(buildSpecification(criteria.getEducationLevel(), BasicInformation_.educationLevel));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), BasicInformation_.email));
            }
            if (criteria.getSkype() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSkype(), BasicInformation_.skype));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), BasicInformation_.phone));
            }
            if (criteria.getWechat() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWechat(), BasicInformation_.wechat));
            }
            if (criteria.getResumeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getResumeId(), BasicInformation_.resume, Resume_.id));
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getImageId(), BasicInformation_.image, Image_.id));
            }
        }
        return specification;
    }

}
