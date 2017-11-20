package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.JobAddressService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.service.dto.JobAddressDTO;
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
 * REST controller for managing JobAddress.
 */
@RestController
@RequestMapping("/api")
public class JobAddressResource {

    private final Logger log = LoggerFactory.getLogger(JobAddressResource.class);

    private static final String ENTITY_NAME = "jobAddress";

    private final JobAddressService jobAddressService;

    public JobAddressResource(JobAddressService jobAddressService) {
        this.jobAddressService = jobAddressService;
    }

    /**
     * POST  /job-addresses : Create a new jobAddress.
     *
     * @param jobAddressDTO the jobAddressDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobAddressDTO, or with status 400 (Bad Request) if the jobAddress has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-addresses")
    @Timed
    public ResponseEntity<JobAddressDTO> createJobAddress(@RequestBody JobAddressDTO jobAddressDTO) throws URISyntaxException {
        log.debug("REST request to save JobAddress : {}", jobAddressDTO);
        if (jobAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new jobAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobAddressDTO result = jobAddressService.save(jobAddressDTO);
        return ResponseEntity.created(new URI("/api/job-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-addresses : Updates an existing jobAddress.
     *
     * @param jobAddressDTO the jobAddressDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobAddressDTO,
     * or with status 400 (Bad Request) if the jobAddressDTO is not valid,
     * or with status 500 (Internal Server Error) if the jobAddressDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-addresses")
    @Timed
    public ResponseEntity<JobAddressDTO> updateJobAddress(@RequestBody JobAddressDTO jobAddressDTO) throws URISyntaxException {
        log.debug("REST request to update JobAddress : {}", jobAddressDTO);
        if (jobAddressDTO.getId() == null) {
            return createJobAddress(jobAddressDTO);
        }
        JobAddressDTO result = jobAddressService.save(jobAddressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobAddressDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-addresses : get all the jobAddresses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobAddresses in body
     */
    @GetMapping("/job-addresses")
    @Timed
    public List<JobAddressDTO> getAllJobAddresses() {
        log.debug("REST request to get all JobAddresses");
        return jobAddressService.findAll();
        }

    /**
     * GET  /job-addresses/:id : get the "id" jobAddress.
     *
     * @param id the id of the jobAddressDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobAddressDTO, or with status 404 (Not Found)
     */
    @GetMapping("/job-addresses/{id}")
    @Timed
    public ResponseEntity<JobAddressDTO> getJobAddress(@PathVariable Long id) {
        log.debug("REST request to get JobAddress : {}", id);
        JobAddressDTO jobAddressDTO = jobAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobAddressDTO));
    }

    /**
     * DELETE  /job-addresses/:id : delete the "id" jobAddress.
     *
     * @param id the id of the jobAddressDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-addresses/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobAddress(@PathVariable Long id) {
        log.debug("REST request to delete JobAddress : {}", id);
        jobAddressService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/job-addresses?query=:query : search for the jobAddress corresponding
     * to the query.
     *
     * @param query the query of the jobAddress search
     * @return the result of the search
     */
    @GetMapping("/_search/job-addresses")
    @Timed
    public List<JobAddressDTO> searchJobAddresses(@RequestParam String query) {
        log.debug("REST request to search JobAddresses for query {}", query);
        return jobAddressService.search(query);
    }

}
