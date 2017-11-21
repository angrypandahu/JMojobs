package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.TownService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.web.rest.util.PaginationUtil;
import com.panda.mojobs.service.dto.TownDTO;
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
 * REST controller for managing Town.
 */
@RestController
@RequestMapping("/api")
public class TownResource {

    private final Logger log = LoggerFactory.getLogger(TownResource.class);

    private static final String ENTITY_NAME = "town";

    private final TownService townService;

    public TownResource(TownService townService) {
        this.townService = townService;
    }

    /**
     * POST  /towns : Create a new town.
     *
     * @param townDTO the townDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new townDTO, or with status 400 (Bad Request) if the town has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/towns")
    @Timed
    public ResponseEntity<TownDTO> createTown(@Valid @RequestBody TownDTO townDTO) throws URISyntaxException {
        log.debug("REST request to save Town : {}", townDTO);
        if (townDTO.getId() != null) {
            throw new BadRequestAlertException("A new town cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TownDTO result = townService.save(townDTO);
        return ResponseEntity.created(new URI("/api/towns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /towns : Updates an existing town.
     *
     * @param townDTO the townDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated townDTO,
     * or with status 400 (Bad Request) if the townDTO is not valid,
     * or with status 500 (Internal Server Error) if the townDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/towns")
    @Timed
    public ResponseEntity<TownDTO> updateTown(@Valid @RequestBody TownDTO townDTO) throws URISyntaxException {
        log.debug("REST request to update Town : {}", townDTO);
        if (townDTO.getId() == null) {
            return createTown(townDTO);
        }
        TownDTO result = townService.save(townDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, townDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /towns : get all the towns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of towns in body
     */
    @GetMapping("/towns")
    @Timed
    public ResponseEntity<List<TownDTO>> getAllTowns(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Towns");
        Page<TownDTO> page = townService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/towns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /towns/:id : get the "id" town.
     *
     * @param id the id of the townDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the townDTO, or with status 404 (Not Found)
     */
    @GetMapping("/towns/{id}")
    @Timed
    public ResponseEntity<TownDTO> getTown(@PathVariable Long id) {
        log.debug("REST request to get Town : {}", id);
        TownDTO townDTO = townService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(townDTO));
    }

    /**
     * DELETE  /towns/:id : delete the "id" town.
     *
     * @param id the id of the townDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/towns/{id}")
    @Timed
    public ResponseEntity<Void> deleteTown(@PathVariable Long id) {
        log.debug("REST request to delete Town : {}", id);
        townService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/towns?query=:query : search for the town corresponding
     * to the query.
     *
     * @param query the query of the town search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/towns")
    @Timed
    public ResponseEntity<List<TownDTO>> searchTowns(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Towns for query {}", query);
        Page<TownDTO> page = townService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/towns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
