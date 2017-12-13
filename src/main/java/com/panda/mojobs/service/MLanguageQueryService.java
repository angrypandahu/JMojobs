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

import com.panda.mojobs.domain.MLanguage;
import com.panda.mojobs.domain.*; // for static metamodels
import com.panda.mojobs.repository.MLanguageRepository;
import com.panda.mojobs.repository.search.MLanguageSearchRepository;
import com.panda.mojobs.service.dto.MLanguageCriteria;

import com.panda.mojobs.service.dto.MLanguageDTO;
import com.panda.mojobs.service.mapper.MLanguageMapper;
import com.panda.mojobs.domain.enumeration.Language;
import com.panda.mojobs.domain.enumeration.LanguageLevel;

/**
 * Service for executing complex queries for MLanguage entities in the database.
 * The main input is a {@link MLanguageCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link MLanguageDTO} or a {@link Page} of {%link MLanguageDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class MLanguageQueryService extends QueryService<MLanguage> {

    private final Logger log = LoggerFactory.getLogger(MLanguageQueryService.class);


    private final MLanguageRepository mLanguageRepository;

    private final MLanguageMapper mLanguageMapper;

    private final MLanguageSearchRepository mLanguageSearchRepository;

    public MLanguageQueryService(MLanguageRepository mLanguageRepository, MLanguageMapper mLanguageMapper, MLanguageSearchRepository mLanguageSearchRepository) {
        this.mLanguageRepository = mLanguageRepository;
        this.mLanguageMapper = mLanguageMapper;
        this.mLanguageSearchRepository = mLanguageSearchRepository;
    }

    /**
     * Return a {@link List} of {%link MLanguageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MLanguageDTO> findByCriteria(MLanguageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<MLanguage> specification = createSpecification(criteria);
        return mLanguageMapper.toDto(mLanguageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link MLanguageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MLanguageDTO> findByCriteria(MLanguageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<MLanguage> specification = createSpecification(criteria);
        final Page<MLanguage> result = mLanguageRepository.findAll(specification, page);
        return result.map(mLanguageMapper::toDto);
    }

    /**
     * Function to convert MLanguageCriteria to a {@link Specifications}
     */
    private Specifications<MLanguage> createSpecification(MLanguageCriteria criteria) {
        Specifications<MLanguage> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MLanguage_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildSpecification(criteria.getName(), MLanguage_.name));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildSpecification(criteria.getLevel(), MLanguage_.level));
            }
            if (criteria.getResumeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getResumeId(), MLanguage_.resume, Resume_.id));
            }
        }
        return specification;
    }

}
