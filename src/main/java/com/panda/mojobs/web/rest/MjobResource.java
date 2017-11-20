package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.MjobService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.web.rest.util.PaginationUtil;
import com.panda.mojobs.service.dto.MjobDTO;
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
 * REST controller for managing Mjob.
 */
@RestController
@RequestMapping("/api")
public class MjobResource {

    private final Logger log = LoggerFactory.getLogger(MjobResource.class);

    private static final String ENTITY_NAME = "mjob";

    private final MjobService mjobService;

    public MjobResource(MjobService mjobService) {
        this.mjobService = mjobService;
    }

    /**
     * POST  /mjobs : Create a new mjob.
     *
     * @param mjobDTO the mjobDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mjobDTO, or with status 400 (Bad Request) if the mjob has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mjobs")
    @Timed
    public ResponseEntity<MjobDTO> createMjob(@Valid @RequestBody MjobDTO mjobDTO) throws URISyntaxException {
        log.debug("REST request to save Mjob : {}", mjobDTO);
        if (mjobDTO.getId() != null) {
            throw new BadRequestAlertException("A new mjob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MjobDTO result = mjobService.save(mjobDTO);
        return ResponseEntity.created(new URI("/api/mjobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mjobs : Updates an existing mjob.
     *
     * @param mjobDTO the mjobDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mjobDTO,
     * or with status 400 (Bad Request) if the mjobDTO is not valid,
     * or with status 500 (Internal Server Error) if the mjobDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mjobs")
    @Timed
    public ResponseEntity<MjobDTO> updateMjob(@Valid @RequestBody MjobDTO mjobDTO) throws URISyntaxException {
        log.debug("REST request to update Mjob : {}", mjobDTO);
        if (mjobDTO.getId() == null) {
            return createMjob(mjobDTO);
        }
        MjobDTO result = mjobService.save(mjobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mjobDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mjobs : get all the mjobs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mjobs in body
     */
    @GetMapping("/mjobs")
    @Timed
    public ResponseEntity<List<MjobDTO>> getAllMjobs(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Mjobs");
        Page<MjobDTO> page = mjobService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mjobs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mjobs/:id : get the "id" mjob.
     *
     * @param id the id of the mjobDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mjobDTO, or with status 404 (Not Found)
     */
    @GetMapping("/mjobs/{id}")
    @Timed
    public ResponseEntity<MjobDTO> getMjob(@PathVariable Long id) {
        log.debug("REST request to get Mjob : {}", id);
        MjobDTO mjobDTO = mjobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mjobDTO));
    }

    /**
     * DELETE  /mjobs/:id : delete the "id" mjob.
     *
     * @param id the id of the mjobDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mjobs/{id}")
    @Timed
    public ResponseEntity<Void> deleteMjob(@PathVariable Long id) {
        log.debug("REST request to delete Mjob : {}", id);
        mjobService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/mjobs?query=:query : search for the mjob corresponding
     * to the query.
     *
     * @param query the query of the mjob search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/mjobs")
    @Timed
    public ResponseEntity<List<MjobDTO>> searchMjobs(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Mjobs for query {}", query);
        Page<MjobDTO> page = mjobService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/mjobs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
