package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.SchoolAddressService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.service.dto.SchoolAddressDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SchoolAddress.
 */
@RestController
@RequestMapping("/api")
public class SchoolAddressResource {

    private final Logger log = LoggerFactory.getLogger(SchoolAddressResource.class);

    private static final String ENTITY_NAME = "schoolAddress";

    private final SchoolAddressService schoolAddressService;

    public SchoolAddressResource(SchoolAddressService schoolAddressService) {
        this.schoolAddressService = schoolAddressService;
    }

    /**
     * POST  /school-addresses : Create a new schoolAddress.
     *
     * @param schoolAddressDTO the schoolAddressDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new schoolAddressDTO, or with status 400 (Bad Request) if the schoolAddress has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/school-addresses")
    @Timed
    public ResponseEntity<SchoolAddressDTO> createSchoolAddress(@RequestBody SchoolAddressDTO schoolAddressDTO) throws URISyntaxException {
        log.debug("REST request to save SchoolAddress : {}", schoolAddressDTO);
        if (schoolAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new schoolAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchoolAddressDTO result = schoolAddressService.save(schoolAddressDTO);
        return ResponseEntity.created(new URI("/api/school-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /school-addresses : Updates an existing schoolAddress.
     *
     * @param schoolAddressDTO the schoolAddressDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated schoolAddressDTO,
     * or with status 400 (Bad Request) if the schoolAddressDTO is not valid,
     * or with status 500 (Internal Server Error) if the schoolAddressDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/school-addresses")
    @Timed
    public ResponseEntity<SchoolAddressDTO> updateSchoolAddress(@RequestBody SchoolAddressDTO schoolAddressDTO) throws URISyntaxException {
        log.debug("REST request to update SchoolAddress : {}", schoolAddressDTO);
        if (schoolAddressDTO.getId() == null) {
            return createSchoolAddress(schoolAddressDTO);
        }
        SchoolAddressDTO result = schoolAddressService.save(schoolAddressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, schoolAddressDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /school-addresses : get all the schoolAddresses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of schoolAddresses in body
     */
    @GetMapping("/school-addresses")
    @Timed
    public List<SchoolAddressDTO> getAllSchoolAddresses() {
        log.debug("REST request to get all SchoolAddresses");
        return schoolAddressService.findAll();
        }

    /**
     * GET  /school-addresses/:id : get the "id" schoolAddress.
     *
     * @param id the id of the schoolAddressDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the schoolAddressDTO, or with status 404 (Not Found)
     */
    @GetMapping("/school-addresses/{id}")
    @Timed
    public ResponseEntity<SchoolAddressDTO> getSchoolAddress(@PathVariable Long id) {
        log.debug("REST request to get SchoolAddress : {}", id);
        SchoolAddressDTO schoolAddressDTO = schoolAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(schoolAddressDTO));
    }

    /**
     * DELETE  /school-addresses/:id : delete the "id" schoolAddress.
     *
     * @param id the id of the schoolAddressDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/school-addresses/{id}")
    @Timed
    public ResponseEntity<Void> deleteSchoolAddress(@PathVariable Long id) {
        log.debug("REST request to delete SchoolAddress : {}", id);
        schoolAddressService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/school-addresses?query=:query : search for the schoolAddress corresponding
     * to the query.
     *
     * @param query the query of the schoolAddress search
     * @return the result of the search
     */
    @GetMapping("/_search/school-addresses")
    @Timed
    public List<SchoolAddressDTO> searchSchoolAddresses(@RequestParam String query) {
        log.debug("REST request to search SchoolAddresses for query {}", query);
        return schoolAddressService.search(query);
    }

}
