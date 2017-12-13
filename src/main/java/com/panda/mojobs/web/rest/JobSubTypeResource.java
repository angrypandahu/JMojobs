package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.JobSubTypeService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.web.rest.util.PaginationUtil;
import com.panda.mojobs.service.dto.JobSubTypeDTO;
import com.panda.mojobs.service.dto.JobSubTypeCriteria;
import com.panda.mojobs.service.JobSubTypeQueryService;
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
 * REST controller for managing JobSubType.
 */
@RestController
@RequestMapping("/api")
public class JobSubTypeResource {

    private final Logger log = LoggerFactory.getLogger(JobSubTypeResource.class);

    private static final String ENTITY_NAME = "jobSubType";

    private final JobSubTypeService jobSubTypeService;

    private final JobSubTypeQueryService jobSubTypeQueryService;

    public JobSubTypeResource(JobSubTypeService jobSubTypeService, JobSubTypeQueryService jobSubTypeQueryService) {
        this.jobSubTypeService = jobSubTypeService;
        this.jobSubTypeQueryService = jobSubTypeQueryService;
    }

    /**
     * POST  /job-sub-types : Create a new jobSubType.
     *
     * @param jobSubTypeDTO the jobSubTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobSubTypeDTO, or with status 400 (Bad Request) if the jobSubType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-sub-types")
    @Timed
    public ResponseEntity<JobSubTypeDTO> createJobSubType(@Valid @RequestBody JobSubTypeDTO jobSubTypeDTO) throws URISyntaxException {
        log.debug("REST request to save JobSubType : {}", jobSubTypeDTO);
        if (jobSubTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new jobSubType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobSubTypeDTO result = jobSubTypeService.save(jobSubTypeDTO);
        return ResponseEntity.created(new URI("/api/job-sub-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-sub-types : Updates an existing jobSubType.
     *
     * @param jobSubTypeDTO the jobSubTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobSubTypeDTO,
     * or with status 400 (Bad Request) if the jobSubTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the jobSubTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-sub-types")
    @Timed
    public ResponseEntity<JobSubTypeDTO> updateJobSubType(@Valid @RequestBody JobSubTypeDTO jobSubTypeDTO) throws URISyntaxException {
        log.debug("REST request to update JobSubType : {}", jobSubTypeDTO);
        if (jobSubTypeDTO.getId() == null) {
            return createJobSubType(jobSubTypeDTO);
        }
        JobSubTypeDTO result = jobSubTypeService.save(jobSubTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobSubTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-sub-types : get all the jobSubTypes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of jobSubTypes in body
     */
    @GetMapping("/job-sub-types")
    @Timed
    public ResponseEntity<List<JobSubTypeDTO>> getAllJobSubTypes(JobSubTypeCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get JobSubTypes by criteria: {}", criteria);
        Page<JobSubTypeDTO> page = jobSubTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/job-sub-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /job-sub-types/:id : get the "id" jobSubType.
     *
     * @param id the id of the jobSubTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobSubTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/job-sub-types/{id}")
    @Timed
    public ResponseEntity<JobSubTypeDTO> getJobSubType(@PathVariable Long id) {
        log.debug("REST request to get JobSubType : {}", id);
        JobSubTypeDTO jobSubTypeDTO = jobSubTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobSubTypeDTO));
    }

    /**
     * DELETE  /job-sub-types/:id : delete the "id" jobSubType.
     *
     * @param id the id of the jobSubTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-sub-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobSubType(@PathVariable Long id) {
        log.debug("REST request to delete JobSubType : {}", id);
        jobSubTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/job-sub-types?query=:query : search for the jobSubType corresponding
     * to the query.
     *
     * @param query the query of the jobSubType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/job-sub-types")
    @Timed
    public ResponseEntity<List<JobSubTypeDTO>> searchJobSubTypes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of JobSubTypes for query {}", query);
        Page<JobSubTypeDTO> page = jobSubTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/job-sub-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
