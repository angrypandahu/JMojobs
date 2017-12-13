package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.ResumeService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.web.rest.util.PaginationUtil;
import com.panda.mojobs.service.dto.ResumeDTO;
import com.panda.mojobs.service.dto.ResumeCriteria;
import com.panda.mojobs.service.ResumeQueryService;
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
 * REST controller for managing Resume.
 */
@RestController
@RequestMapping("/api")
public class ResumeResource {

    private final Logger log = LoggerFactory.getLogger(ResumeResource.class);

    private static final String ENTITY_NAME = "resume";

    private final ResumeService resumeService;

    private final ResumeQueryService resumeQueryService;

    public ResumeResource(ResumeService resumeService, ResumeQueryService resumeQueryService) {
        this.resumeService = resumeService;
        this.resumeQueryService = resumeQueryService;
    }

    /**
     * POST  /resumes : Create a new resume.
     *
     * @param resumeDTO the resumeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new resumeDTO, or with status 400 (Bad Request) if the resume has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/resumes")
    @Timed
    public ResponseEntity<ResumeDTO> createResume(@Valid @RequestBody ResumeDTO resumeDTO) throws URISyntaxException {
        log.debug("REST request to save Resume : {}", resumeDTO);
        if (resumeDTO.getId() != null) {
            throw new BadRequestAlertException("A new resume cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResumeDTO result = resumeService.save(resumeDTO);
        return ResponseEntity.created(new URI("/api/resumes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /resumes : Updates an existing resume.
     *
     * @param resumeDTO the resumeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated resumeDTO,
     * or with status 400 (Bad Request) if the resumeDTO is not valid,
     * or with status 500 (Internal Server Error) if the resumeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/resumes")
    @Timed
    public ResponseEntity<ResumeDTO> updateResume(@Valid @RequestBody ResumeDTO resumeDTO) throws URISyntaxException {
        log.debug("REST request to update Resume : {}", resumeDTO);
        if (resumeDTO.getId() == null) {
            return createResume(resumeDTO);
        }
        ResumeDTO result = resumeService.save(resumeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, resumeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /resumes : get all the resumes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of resumes in body
     */
    @GetMapping("/resumes")
    @Timed
    public ResponseEntity<List<ResumeDTO>> getAllResumes(ResumeCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get Resumes by criteria: {}", criteria);
        Page<ResumeDTO> page = resumeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/resumes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /resumes/:id : get the "id" resume.
     *
     * @param id the id of the resumeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the resumeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/resumes/{id}")
    @Timed
    public ResponseEntity<ResumeDTO> getResume(@PathVariable Long id) {
        log.debug("REST request to get Resume : {}", id);
        ResumeDTO resumeDTO = resumeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(resumeDTO));
    }

    /**
     * DELETE  /resumes/:id : delete the "id" resume.
     *
     * @param id the id of the resumeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/resumes/{id}")
    @Timed
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        log.debug("REST request to delete Resume : {}", id);
        resumeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/resumes?query=:query : search for the resume corresponding
     * to the query.
     *
     * @param query the query of the resume search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/resumes")
    @Timed
    public ResponseEntity<List<ResumeDTO>> searchResumes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Resumes for query {}", query);
        Page<ResumeDTO> page = resumeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/resumes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
