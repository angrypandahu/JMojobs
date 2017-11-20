package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.ProfessionalDevelopmentService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.service.dto.ProfessionalDevelopmentDTO;
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
 * REST controller for managing ProfessionalDevelopment.
 */
@RestController
@RequestMapping("/api")
public class ProfessionalDevelopmentResource {

    private final Logger log = LoggerFactory.getLogger(ProfessionalDevelopmentResource.class);

    private static final String ENTITY_NAME = "professionalDevelopment";

    private final ProfessionalDevelopmentService professionalDevelopmentService;

    public ProfessionalDevelopmentResource(ProfessionalDevelopmentService professionalDevelopmentService) {
        this.professionalDevelopmentService = professionalDevelopmentService;
    }

    /**
     * POST  /professional-developments : Create a new professionalDevelopment.
     *
     * @param professionalDevelopmentDTO the professionalDevelopmentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new professionalDevelopmentDTO, or with status 400 (Bad Request) if the professionalDevelopment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/professional-developments")
    @Timed
    public ResponseEntity<ProfessionalDevelopmentDTO> createProfessionalDevelopment(@Valid @RequestBody ProfessionalDevelopmentDTO professionalDevelopmentDTO) throws URISyntaxException {
        log.debug("REST request to save ProfessionalDevelopment : {}", professionalDevelopmentDTO);
        if (professionalDevelopmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalDevelopment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfessionalDevelopmentDTO result = professionalDevelopmentService.save(professionalDevelopmentDTO);
        return ResponseEntity.created(new URI("/api/professional-developments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /professional-developments : Updates an existing professionalDevelopment.
     *
     * @param professionalDevelopmentDTO the professionalDevelopmentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated professionalDevelopmentDTO,
     * or with status 400 (Bad Request) if the professionalDevelopmentDTO is not valid,
     * or with status 500 (Internal Server Error) if the professionalDevelopmentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/professional-developments")
    @Timed
    public ResponseEntity<ProfessionalDevelopmentDTO> updateProfessionalDevelopment(@Valid @RequestBody ProfessionalDevelopmentDTO professionalDevelopmentDTO) throws URISyntaxException {
        log.debug("REST request to update ProfessionalDevelopment : {}", professionalDevelopmentDTO);
        if (professionalDevelopmentDTO.getId() == null) {
            return createProfessionalDevelopment(professionalDevelopmentDTO);
        }
        ProfessionalDevelopmentDTO result = professionalDevelopmentService.save(professionalDevelopmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, professionalDevelopmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /professional-developments : get all the professionalDevelopments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of professionalDevelopments in body
     */
    @GetMapping("/professional-developments")
    @Timed
    public List<ProfessionalDevelopmentDTO> getAllProfessionalDevelopments() {
        log.debug("REST request to get all ProfessionalDevelopments");
        return professionalDevelopmentService.findAll();
        }

    /**
     * GET  /professional-developments/:id : get the "id" professionalDevelopment.
     *
     * @param id the id of the professionalDevelopmentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the professionalDevelopmentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/professional-developments/{id}")
    @Timed
    public ResponseEntity<ProfessionalDevelopmentDTO> getProfessionalDevelopment(@PathVariable Long id) {
        log.debug("REST request to get ProfessionalDevelopment : {}", id);
        ProfessionalDevelopmentDTO professionalDevelopmentDTO = professionalDevelopmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(professionalDevelopmentDTO));
    }

    /**
     * DELETE  /professional-developments/:id : delete the "id" professionalDevelopment.
     *
     * @param id the id of the professionalDevelopmentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/professional-developments/{id}")
    @Timed
    public ResponseEntity<Void> deleteProfessionalDevelopment(@PathVariable Long id) {
        log.debug("REST request to delete ProfessionalDevelopment : {}", id);
        professionalDevelopmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/professional-developments?query=:query : search for the professionalDevelopment corresponding
     * to the query.
     *
     * @param query the query of the professionalDevelopment search
     * @return the result of the search
     */
    @GetMapping("/_search/professional-developments")
    @Timed
    public List<ProfessionalDevelopmentDTO> searchProfessionalDevelopments(@RequestParam String query) {
        log.debug("REST request to search ProfessionalDevelopments for query {}", query);
        return professionalDevelopmentService.search(query);
    }

}
