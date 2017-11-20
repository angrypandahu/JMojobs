package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.EducationBackgroundService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.service.dto.EducationBackgroundDTO;
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
 * REST controller for managing EducationBackground.
 */
@RestController
@RequestMapping("/api")
public class EducationBackgroundResource {

    private final Logger log = LoggerFactory.getLogger(EducationBackgroundResource.class);

    private static final String ENTITY_NAME = "educationBackground";

    private final EducationBackgroundService educationBackgroundService;

    public EducationBackgroundResource(EducationBackgroundService educationBackgroundService) {
        this.educationBackgroundService = educationBackgroundService;
    }

    /**
     * POST  /education-backgrounds : Create a new educationBackground.
     *
     * @param educationBackgroundDTO the educationBackgroundDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new educationBackgroundDTO, or with status 400 (Bad Request) if the educationBackground has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/education-backgrounds")
    @Timed
    public ResponseEntity<EducationBackgroundDTO> createEducationBackground(@Valid @RequestBody EducationBackgroundDTO educationBackgroundDTO) throws URISyntaxException {
        log.debug("REST request to save EducationBackground : {}", educationBackgroundDTO);
        if (educationBackgroundDTO.getId() != null) {
            throw new BadRequestAlertException("A new educationBackground cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EducationBackgroundDTO result = educationBackgroundService.save(educationBackgroundDTO);
        return ResponseEntity.created(new URI("/api/education-backgrounds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /education-backgrounds : Updates an existing educationBackground.
     *
     * @param educationBackgroundDTO the educationBackgroundDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated educationBackgroundDTO,
     * or with status 400 (Bad Request) if the educationBackgroundDTO is not valid,
     * or with status 500 (Internal Server Error) if the educationBackgroundDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/education-backgrounds")
    @Timed
    public ResponseEntity<EducationBackgroundDTO> updateEducationBackground(@Valid @RequestBody EducationBackgroundDTO educationBackgroundDTO) throws URISyntaxException {
        log.debug("REST request to update EducationBackground : {}", educationBackgroundDTO);
        if (educationBackgroundDTO.getId() == null) {
            return createEducationBackground(educationBackgroundDTO);
        }
        EducationBackgroundDTO result = educationBackgroundService.save(educationBackgroundDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, educationBackgroundDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /education-backgrounds : get all the educationBackgrounds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of educationBackgrounds in body
     */
    @GetMapping("/education-backgrounds")
    @Timed
    public List<EducationBackgroundDTO> getAllEducationBackgrounds() {
        log.debug("REST request to get all EducationBackgrounds");
        return educationBackgroundService.findAll();
        }

    /**
     * GET  /education-backgrounds/:id : get the "id" educationBackground.
     *
     * @param id the id of the educationBackgroundDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the educationBackgroundDTO, or with status 404 (Not Found)
     */
    @GetMapping("/education-backgrounds/{id}")
    @Timed
    public ResponseEntity<EducationBackgroundDTO> getEducationBackground(@PathVariable Long id) {
        log.debug("REST request to get EducationBackground : {}", id);
        EducationBackgroundDTO educationBackgroundDTO = educationBackgroundService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(educationBackgroundDTO));
    }

    /**
     * DELETE  /education-backgrounds/:id : delete the "id" educationBackground.
     *
     * @param id the id of the educationBackgroundDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/education-backgrounds/{id}")
    @Timed
    public ResponseEntity<Void> deleteEducationBackground(@PathVariable Long id) {
        log.debug("REST request to delete EducationBackground : {}", id);
        educationBackgroundService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/education-backgrounds?query=:query : search for the educationBackground corresponding
     * to the query.
     *
     * @param query the query of the educationBackground search
     * @return the result of the search
     */
    @GetMapping("/_search/education-backgrounds")
    @Timed
    public List<EducationBackgroundDTO> searchEducationBackgrounds(@RequestParam String query) {
        log.debug("REST request to search EducationBackgrounds for query {}", query);
        return educationBackgroundService.search(query);
    }

}
