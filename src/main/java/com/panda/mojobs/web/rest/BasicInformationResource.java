package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.BasicInformationService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.service.dto.BasicInformationDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing BasicInformation.
 */
@RestController
@RequestMapping("/api")
public class BasicInformationResource {

    private final Logger log = LoggerFactory.getLogger(BasicInformationResource.class);

    private static final String ENTITY_NAME = "basicInformation";

    private final BasicInformationService basicInformationService;

    public BasicInformationResource(BasicInformationService basicInformationService) {
        this.basicInformationService = basicInformationService;
    }

    /**
     * POST  /basic-informations : Create a new basicInformation.
     *
     * @param basicInformationDTO the basicInformationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new basicInformationDTO, or with status 400 (Bad Request) if the basicInformation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/basic-informations")
    @Timed
    public ResponseEntity<BasicInformationDTO> createBasicInformation(@Valid @RequestBody BasicInformationDTO basicInformationDTO) throws URISyntaxException {
        log.debug("REST request to save BasicInformation : {}", basicInformationDTO);
        if (basicInformationDTO.getId() != null) {
            throw new BadRequestAlertException("A new basicInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BasicInformationDTO result = basicInformationService.save(basicInformationDTO);
        return ResponseEntity.created(new URI("/api/basic-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /basic-informations : Updates an existing basicInformation.
     *
     * @param basicInformationDTO the basicInformationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated basicInformationDTO,
     * or with status 400 (Bad Request) if the basicInformationDTO is not valid,
     * or with status 500 (Internal Server Error) if the basicInformationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/basic-informations")
    @Timed
    public ResponseEntity<BasicInformationDTO> updateBasicInformation(@Valid @RequestBody BasicInformationDTO basicInformationDTO) throws URISyntaxException {
        log.debug("REST request to update BasicInformation : {}", basicInformationDTO);
        if (basicInformationDTO.getId() == null) {
            return createBasicInformation(basicInformationDTO);
        }
        BasicInformationDTO result = basicInformationService.save(basicInformationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, basicInformationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /basic-informations : get all the basicInformations.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of basicInformations in body
     */
    @GetMapping("/basic-informations")
    @Timed
    public List<BasicInformationDTO> getAllBasicInformations(@RequestParam(required = false) String filter) {
        if ("resume-is-null".equals(filter)) {
            log.debug("REST request to get all BasicInformations where resume is null");
            return basicInformationService.findAllWhereResumeIsNull();
        }
        log.debug("REST request to get all BasicInformations");
        return basicInformationService.findAll();
        }

    /**
     * GET  /basic-informations/:id : get the "id" basicInformation.
     *
     * @param id the id of the basicInformationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the basicInformationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/basic-informations/{id}")
    @Timed
    public ResponseEntity<BasicInformationDTO> getBasicInformation(@PathVariable Long id) {
        log.debug("REST request to get BasicInformation : {}", id);
        BasicInformationDTO basicInformationDTO = basicInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(basicInformationDTO));
    }

    /**
     * DELETE  /basic-informations/:id : delete the "id" basicInformation.
     *
     * @param id the id of the basicInformationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/basic-informations/{id}")
    @Timed
    public ResponseEntity<Void> deleteBasicInformation(@PathVariable Long id) {
        log.debug("REST request to delete BasicInformation : {}", id);
        basicInformationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/basic-informations?query=:query : search for the basicInformation corresponding
     * to the query.
     *
     * @param query the query of the basicInformation search
     * @return the result of the search
     */
    @GetMapping("/_search/basic-informations")
    @Timed
    public List<BasicInformationDTO> searchBasicInformations(@RequestParam String query) {
        log.debug("REST request to search BasicInformations for query {}", query);
        return basicInformationService.search(query);
    }

}
