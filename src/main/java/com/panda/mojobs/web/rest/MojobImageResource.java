package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.MojobImageService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.service.dto.MojobImageDTO;
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
 * REST controller for managing MojobImage.
 */
@RestController
@RequestMapping("/api")
public class MojobImageResource {

    private final Logger log = LoggerFactory.getLogger(MojobImageResource.class);

    private static final String ENTITY_NAME = "mojobImage";

    private final MojobImageService mojobImageService;

    public MojobImageResource(MojobImageService mojobImageService) {
        this.mojobImageService = mojobImageService;
    }

    /**
     * POST  /mojob-images : Create a new mojobImage.
     *
     * @param mojobImageDTO the mojobImageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mojobImageDTO, or with status 400 (Bad Request) if the mojobImage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mojob-images")
    @Timed
    public ResponseEntity<MojobImageDTO> createMojobImage(@Valid @RequestBody MojobImageDTO mojobImageDTO) throws URISyntaxException {
        log.debug("REST request to save MojobImage : {}", mojobImageDTO);
        if (mojobImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new mojobImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MojobImageDTO result = mojobImageService.save(mojobImageDTO);
        return ResponseEntity.created(new URI("/api/mojob-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mojob-images : Updates an existing mojobImage.
     *
     * @param mojobImageDTO the mojobImageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mojobImageDTO,
     * or with status 400 (Bad Request) if the mojobImageDTO is not valid,
     * or with status 500 (Internal Server Error) if the mojobImageDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mojob-images")
    @Timed
    public ResponseEntity<MojobImageDTO> updateMojobImage(@Valid @RequestBody MojobImageDTO mojobImageDTO) throws URISyntaxException {
        log.debug("REST request to update MojobImage : {}", mojobImageDTO);
        if (mojobImageDTO.getId() == null) {
            return createMojobImage(mojobImageDTO);
        }
        MojobImageDTO result = mojobImageService.save(mojobImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mojobImageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mojob-images : get all the mojobImages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of mojobImages in body
     */
    @GetMapping("/mojob-images")
    @Timed
    public List<MojobImageDTO> getAllMojobImages() {
        log.debug("REST request to get all MojobImages");
        return mojobImageService.findAll();
        }

    /**
     * GET  /mojob-images/:id : get the "id" mojobImage.
     *
     * @param id the id of the mojobImageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mojobImageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/mojob-images/{id}")
    @Timed
    public ResponseEntity<MojobImageDTO> getMojobImage(@PathVariable Long id) {
        log.debug("REST request to get MojobImage : {}", id);
        MojobImageDTO mojobImageDTO = mojobImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mojobImageDTO));
    }

    /**
     * DELETE  /mojob-images/:id : delete the "id" mojobImage.
     *
     * @param id the id of the mojobImageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mojob-images/{id}")
    @Timed
    public ResponseEntity<Void> deleteMojobImage(@PathVariable Long id) {
        log.debug("REST request to delete MojobImage : {}", id);
        mojobImageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/mojob-images?query=:query : search for the mojobImage corresponding
     * to the query.
     *
     * @param query the query of the mojobImage search
     * @return the result of the search
     */
    @GetMapping("/_search/mojob-images")
    @Timed
    public List<MojobImageDTO> searchMojobImages(@RequestParam String query) {
        log.debug("REST request to search MojobImages for query {}", query);
        return mojobImageService.search(query);
    }

}
