package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.MLanguageService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.service.dto.MLanguageDTO;
import com.panda.mojobs.service.dto.MLanguageCriteria;
import com.panda.mojobs.service.MLanguageQueryService;
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
 * REST controller for managing MLanguage.
 */
@RestController
@RequestMapping("/api")
public class MLanguageResource {

    private final Logger log = LoggerFactory.getLogger(MLanguageResource.class);

    private static final String ENTITY_NAME = "mLanguage";

    private final MLanguageService mLanguageService;

    private final MLanguageQueryService mLanguageQueryService;

    public MLanguageResource(MLanguageService mLanguageService, MLanguageQueryService mLanguageQueryService) {
        this.mLanguageService = mLanguageService;
        this.mLanguageQueryService = mLanguageQueryService;
    }

    /**
     * POST  /m-languages : Create a new mLanguage.
     *
     * @param mLanguageDTO the mLanguageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mLanguageDTO, or with status 400 (Bad Request) if the mLanguage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-languages")
    @Timed
    public ResponseEntity<MLanguageDTO> createMLanguage(@Valid @RequestBody MLanguageDTO mLanguageDTO) throws URISyntaxException {
        log.debug("REST request to save MLanguage : {}", mLanguageDTO);
        if (mLanguageDTO.getId() != null) {
            throw new BadRequestAlertException("A new mLanguage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MLanguageDTO result = mLanguageService.save(mLanguageDTO);
        return ResponseEntity.created(new URI("/api/m-languages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /m-languages : Updates an existing mLanguage.
     *
     * @param mLanguageDTO the mLanguageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mLanguageDTO,
     * or with status 400 (Bad Request) if the mLanguageDTO is not valid,
     * or with status 500 (Internal Server Error) if the mLanguageDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-languages")
    @Timed
    public ResponseEntity<MLanguageDTO> updateMLanguage(@Valid @RequestBody MLanguageDTO mLanguageDTO) throws URISyntaxException {
        log.debug("REST request to update MLanguage : {}", mLanguageDTO);
        if (mLanguageDTO.getId() == null) {
            return createMLanguage(mLanguageDTO);
        }
        MLanguageDTO result = mLanguageService.save(mLanguageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mLanguageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /m-languages : get all the mLanguages.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of mLanguages in body
     */
    @GetMapping("/m-languages")
    @Timed
    public ResponseEntity<List<MLanguageDTO>> getAllMLanguages(MLanguageCriteria criteria) {
        log.debug("REST request to get MLanguages by criteria: {}", criteria);
        List<MLanguageDTO> entityList = mLanguageQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /m-languages/:id : get the "id" mLanguage.
     *
     * @param id the id of the mLanguageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mLanguageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/m-languages/{id}")
    @Timed
    public ResponseEntity<MLanguageDTO> getMLanguage(@PathVariable Long id) {
        log.debug("REST request to get MLanguage : {}", id);
        MLanguageDTO mLanguageDTO = mLanguageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mLanguageDTO));
    }

    /**
     * DELETE  /m-languages/:id : delete the "id" mLanguage.
     *
     * @param id the id of the mLanguageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-languages/{id}")
    @Timed
    public ResponseEntity<Void> deleteMLanguage(@PathVariable Long id) {
        log.debug("REST request to delete MLanguage : {}", id);
        mLanguageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/m-languages?query=:query : search for the mLanguage corresponding
     * to the query.
     *
     * @param query the query of the mLanguage search
     * @return the result of the search
     */
    @GetMapping("/_search/m-languages")
    @Timed
    public List<MLanguageDTO> searchMLanguages(@RequestParam String query) {
        log.debug("REST request to search MLanguages for query {}", query);
        return mLanguageService.search(query);
    }

}
