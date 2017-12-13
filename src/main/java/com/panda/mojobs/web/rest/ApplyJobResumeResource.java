package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.ApplyJobResumeService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.web.rest.util.PaginationUtil;
import com.panda.mojobs.service.dto.ApplyJobResumeDTO;
import com.panda.mojobs.service.dto.ApplyJobResumeCriteria;
import com.panda.mojobs.service.ApplyJobResumeQueryService;
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
 * REST controller for managing ApplyJobResume.
 */
@RestController
@RequestMapping("/api")
public class ApplyJobResumeResource {

    private final Logger log = LoggerFactory.getLogger(ApplyJobResumeResource.class);

    private static final String ENTITY_NAME = "applyJobResume";

    private final ApplyJobResumeService applyJobResumeService;

    private final ApplyJobResumeQueryService applyJobResumeQueryService;

    public ApplyJobResumeResource(ApplyJobResumeService applyJobResumeService, ApplyJobResumeQueryService applyJobResumeQueryService) {
        this.applyJobResumeService = applyJobResumeService;
        this.applyJobResumeQueryService = applyJobResumeQueryService;
    }

    /**
     * POST  /apply-job-resumes : Create a new applyJobResume.
     *
     * @param applyJobResumeDTO the applyJobResumeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new applyJobResumeDTO, or with status 400 (Bad Request) if the applyJobResume has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/apply-job-resumes")
    @Timed
    public ResponseEntity<ApplyJobResumeDTO> createApplyJobResume(@Valid @RequestBody ApplyJobResumeDTO applyJobResumeDTO) throws URISyntaxException {
        log.debug("REST request to save ApplyJobResume : {}", applyJobResumeDTO);
        if (applyJobResumeDTO.getId() != null) {
            throw new BadRequestAlertException("A new applyJobResume cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplyJobResumeDTO result = applyJobResumeService.save(applyJobResumeDTO);
        return ResponseEntity.created(new URI("/api/apply-job-resumes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /apply-job-resumes : Updates an existing applyJobResume.
     *
     * @param applyJobResumeDTO the applyJobResumeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated applyJobResumeDTO,
     * or with status 400 (Bad Request) if the applyJobResumeDTO is not valid,
     * or with status 500 (Internal Server Error) if the applyJobResumeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/apply-job-resumes")
    @Timed
    public ResponseEntity<ApplyJobResumeDTO> updateApplyJobResume(@Valid @RequestBody ApplyJobResumeDTO applyJobResumeDTO) throws URISyntaxException {
        log.debug("REST request to update ApplyJobResume : {}", applyJobResumeDTO);
        if (applyJobResumeDTO.getId() == null) {
            return createApplyJobResume(applyJobResumeDTO);
        }
        ApplyJobResumeDTO result = applyJobResumeService.save(applyJobResumeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, applyJobResumeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /apply-job-resumes : get all the applyJobResumes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of applyJobResumes in body
     */
    @GetMapping("/apply-job-resumes")
    @Timed
    public ResponseEntity<List<ApplyJobResumeDTO>> getAllApplyJobResumes(ApplyJobResumeCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get ApplyJobResumes by criteria: {}", criteria);
        Page<ApplyJobResumeDTO> page = applyJobResumeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/apply-job-resumes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /apply-job-resumes/:id : get the "id" applyJobResume.
     *
     * @param id the id of the applyJobResumeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the applyJobResumeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/apply-job-resumes/{id}")
    @Timed
    public ResponseEntity<ApplyJobResumeDTO> getApplyJobResume(@PathVariable Long id) {
        log.debug("REST request to get ApplyJobResume : {}", id);
        ApplyJobResumeDTO applyJobResumeDTO = applyJobResumeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(applyJobResumeDTO));
    }

    /**
     * DELETE  /apply-job-resumes/:id : delete the "id" applyJobResume.
     *
     * @param id the id of the applyJobResumeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/apply-job-resumes/{id}")
    @Timed
    public ResponseEntity<Void> deleteApplyJobResume(@PathVariable Long id) {
        log.debug("REST request to delete ApplyJobResume : {}", id);
        applyJobResumeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/apply-job-resumes?query=:query : search for the applyJobResume corresponding
     * to the query.
     *
     * @param query the query of the applyJobResume search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/apply-job-resumes")
    @Timed
    public ResponseEntity<List<ApplyJobResumeDTO>> searchApplyJobResumes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ApplyJobResumes for query {}", query);
        Page<ApplyJobResumeDTO> page = applyJobResumeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/apply-job-resumes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
