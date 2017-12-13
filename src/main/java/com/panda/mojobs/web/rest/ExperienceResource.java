package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.ExperienceService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.web.rest.util.PaginationUtil;
import com.panda.mojobs.service.dto.ExperienceDTO;
import com.panda.mojobs.service.dto.ExperienceCriteria;
import com.panda.mojobs.service.ExperienceQueryService;
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
 * REST controller for managing Experience.
 */
@RestController
@RequestMapping("/api")
public class ExperienceResource {

    private final Logger log = LoggerFactory.getLogger(ExperienceResource.class);

    private static final String ENTITY_NAME = "experience";

    private final ExperienceService experienceService;

    private final ExperienceQueryService experienceQueryService;

    public ExperienceResource(ExperienceService experienceService, ExperienceQueryService experienceQueryService) {
        this.experienceService = experienceService;
        this.experienceQueryService = experienceQueryService;
    }

    /**
     * POST  /experiences : Create a new experience.
     *
     * @param experienceDTO the experienceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new experienceDTO, or with status 400 (Bad Request) if the experience has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/experiences")
    @Timed
    public ResponseEntity<ExperienceDTO> createExperience(@Valid @RequestBody ExperienceDTO experienceDTO) throws URISyntaxException {
        log.debug("REST request to save Experience : {}", experienceDTO);
        if (experienceDTO.getId() != null) {
            throw new BadRequestAlertException("A new experience cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExperienceDTO result = experienceService.save(experienceDTO);
        return ResponseEntity.created(new URI("/api/experiences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /experiences : Updates an existing experience.
     *
     * @param experienceDTO the experienceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated experienceDTO,
     * or with status 400 (Bad Request) if the experienceDTO is not valid,
     * or with status 500 (Internal Server Error) if the experienceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/experiences")
    @Timed
    public ResponseEntity<ExperienceDTO> updateExperience(@Valid @RequestBody ExperienceDTO experienceDTO) throws URISyntaxException {
        log.debug("REST request to update Experience : {}", experienceDTO);
        if (experienceDTO.getId() == null) {
            return createExperience(experienceDTO);
        }
        ExperienceDTO result = experienceService.save(experienceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, experienceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /experiences : get all the experiences.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of experiences in body
     */
    @GetMapping("/experiences")
    @Timed
    public ResponseEntity<List<ExperienceDTO>> getAllExperiences(ExperienceCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get Experiences by criteria: {}", criteria);
        Page<ExperienceDTO> page = experienceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/experiences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /experiences/:id : get the "id" experience.
     *
     * @param id the id of the experienceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the experienceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/experiences/{id}")
    @Timed
    public ResponseEntity<ExperienceDTO> getExperience(@PathVariable Long id) {
        log.debug("REST request to get Experience : {}", id);
        ExperienceDTO experienceDTO = experienceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(experienceDTO));
    }

    /**
     * DELETE  /experiences/:id : delete the "id" experience.
     *
     * @param id the id of the experienceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/experiences/{id}")
    @Timed
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        log.debug("REST request to delete Experience : {}", id);
        experienceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/experiences?query=:query : search for the experience corresponding
     * to the query.
     *
     * @param query the query of the experience search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/experiences")
    @Timed
    public ResponseEntity<List<ExperienceDTO>> searchExperiences(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Experiences for query {}", query);
        Page<ExperienceDTO> page = experienceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/experiences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
