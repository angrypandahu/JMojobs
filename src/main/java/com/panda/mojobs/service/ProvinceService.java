package com.panda.mojobs.service;

import com.panda.mojobs.service.dto.ProvinceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Province.
 */
public interface ProvinceService {

    /**
     * Save a province.
     *
     * @param provinceDTO the entity to save
     * @return the persisted entity
     */
    ProvinceDTO save(ProvinceDTO provinceDTO);

    /**
     *  Get all the provinces.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProvinceDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" province.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProvinceDTO findOne(Long id);

    /**
     *  Delete the "id" province.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the province corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProvinceDTO> search(String query, Pageable pageable);
}
