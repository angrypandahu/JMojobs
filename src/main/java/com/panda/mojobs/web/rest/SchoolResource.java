package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.SchoolService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.web.rest.util.PaginationUtil;
import com.panda.mojobs.service.dto.SchoolDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing School.
 */
@RestController
@RequestMapping("/api")
public class SchoolResource {

    private final Logger log = LoggerFactory.getLogger(SchoolResource.class);

    private static final String ENTITY_NAME = "school";

    private final SchoolService schoolService;

    public SchoolResource(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    /**
     * POST  /schools : Create a new school.
     *
     * @param schoolDTO the schoolDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new schoolDTO, or with status 400 (Bad Request) if the school has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/schools")
    @Timed
    public ResponseEntity<SchoolDTO> createSchool(@Valid @RequestBody SchoolDTO schoolDTO) throws URISyntaxException {
        log.debug("REST request to save School : {}", schoolDTO);
        if (schoolDTO.getId() != null) {
            throw new BadRequestAlertException("A new school cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchoolDTO result = schoolService.save(schoolDTO);
        return ResponseEntity.created(new URI("/api/schools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /schools : Updates an existing school.
     *
     * @param schoolDTO the schoolDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated schoolDTO,
     * or with status 400 (Bad Request) if the schoolDTO is not valid,
     * or with status 500 (Internal Server Error) if the schoolDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/schools")
    @Timed
    public ResponseEntity<SchoolDTO> updateSchool(@Valid @RequestBody SchoolDTO schoolDTO) throws URISyntaxException {
        log.debug("REST request to update School : {}", schoolDTO);
        if (schoolDTO.getId() == null) {
            return createSchool(schoolDTO);
        }
        SchoolDTO result = schoolService.save(schoolDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, schoolDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /schools : get all the schools.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of schools in body
     */
    @GetMapping("/schools")
    @Timed
    public ResponseEntity<List<SchoolDTO>> getAllSchools(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Schools");
        Page<SchoolDTO> page = schoolService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/schools");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /schools/:id : get the "id" school.
     *
     * @param id the id of the schoolDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the schoolDTO, or with status 404 (Not Found)
     */
    @GetMapping("/schools/{id}")
    @Timed
    public ResponseEntity<SchoolDTO> getSchool(@PathVariable Long id) {
        log.debug("REST request to get School : {}", id);
        SchoolDTO schoolDTO = schoolService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(schoolDTO));
    }

    /**
     * DELETE  /schools/:id : delete the "id" school.
     *
     * @param id the id of the schoolDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/schools/{id}")
    @Timed
    public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
        log.debug("REST request to delete School : {}", id);
        schoolService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/schools?query=:query : search for the school corresponding
     * to the query.
     *
     * @param query the query of the school search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/schools")
    @Timed
    public ResponseEntity<List<SchoolDTO>> searchSchools(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Schools for query {}", query);
        Page<SchoolDTO> page = schoolService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/schools");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
