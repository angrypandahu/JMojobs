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

import com.panda.mojobs.domain.Address;
import com.panda.mojobs.domain.*; // for static metamodels
import com.panda.mojobs.repository.AddressRepository;
import com.panda.mojobs.repository.search.AddressSearchRepository;
import com.panda.mojobs.service.dto.AddressCriteria;

import com.panda.mojobs.service.dto.AddressDTO;
import com.panda.mojobs.service.mapper.AddressMapper;
import com.panda.mojobs.domain.enumeration.Location;

/**
 * Service for executing complex queries for Address entities in the database.
 * The main input is a {@link AddressCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link AddressDTO} or a {@link Page} of {%link AddressDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class AddressQueryService extends QueryService<Address> {

    private final Logger log = LoggerFactory.getLogger(AddressQueryService.class);


    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    private final AddressSearchRepository addressSearchRepository;

    public AddressQueryService(AddressRepository addressRepository, AddressMapper addressMapper, AddressSearchRepository addressSearchRepository) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.addressSearchRepository = addressSearchRepository;
    }

    /**
     * Return a {@link List} of {%link AddressDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AddressDTO> findByCriteria(AddressCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Address> specification = createSpecification(criteria);
        return addressMapper.toDto(addressRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link AddressDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AddressDTO> findByCriteria(AddressCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Address> specification = createSpecification(criteria);
        final Page<Address> result = addressRepository.findAll(specification, page);
        return result.map(addressMapper::toDto);
    }

    /**
     * Function to convert AddressCriteria to a {@link Specifications}
     */
    private Specifications<Address> createSpecification(AddressCriteria criteria) {
        Specifications<Address> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Address_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildSpecification(criteria.getName(), Address_.name));
            }
            if (criteria.getMobile() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobile(), Address_.mobile));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Address_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Address_.phone));
            }
            if (criteria.getLine() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLine(), Address_.line));
            }
        }
        return specification;
    }

}
