package com.panda.mojobs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.panda.mojobs.service.ChatMessageService;
import com.panda.mojobs.web.rest.errors.BadRequestAlertException;
import com.panda.mojobs.web.rest.util.HeaderUtil;
import com.panda.mojobs.web.rest.util.PaginationUtil;
import com.panda.mojobs.service.dto.ChatMessageDTO;
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
 * REST controller for managing ChatMessage.
 */
@RestController
@RequestMapping("/api")
public class ChatMessageResource {

    private final Logger log = LoggerFactory.getLogger(ChatMessageResource.class);

    private static final String ENTITY_NAME = "chatMessage";

    private final ChatMessageService chatMessageService;

    public ChatMessageResource(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    /**
     * POST  /chat-messages : Create a new chatMessage.
     *
     * @param chatMessageDTO the chatMessageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chatMessageDTO, or with status 400 (Bad Request) if the chatMessage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chat-messages")
    @Timed
    public ResponseEntity<ChatMessageDTO> createChatMessage(@Valid @RequestBody ChatMessageDTO chatMessageDTO) throws URISyntaxException {
        log.debug("REST request to save ChatMessage : {}", chatMessageDTO);
        if (chatMessageDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatMessageDTO result = chatMessageService.save(chatMessageDTO);
        return ResponseEntity.created(new URI("/api/chat-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chat-messages : Updates an existing chatMessage.
     *
     * @param chatMessageDTO the chatMessageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chatMessageDTO,
     * or with status 400 (Bad Request) if the chatMessageDTO is not valid,
     * or with status 500 (Internal Server Error) if the chatMessageDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/chat-messages")
    @Timed
    public ResponseEntity<ChatMessageDTO> updateChatMessage(@Valid @RequestBody ChatMessageDTO chatMessageDTO) throws URISyntaxException {
        log.debug("REST request to update ChatMessage : {}", chatMessageDTO);
        if (chatMessageDTO.getId() == null) {
            return createChatMessage(chatMessageDTO);
        }
        ChatMessageDTO result = chatMessageService.save(chatMessageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, chatMessageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chat-messages : get all the chatMessages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of chatMessages in body
     */
    @GetMapping("/chat-messages")
    @Timed
    public ResponseEntity<List<ChatMessageDTO>> getAllChatMessages(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ChatMessages");
        Page<ChatMessageDTO> page = chatMessageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/chat-messages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /chat-messages/:id : get the "id" chatMessage.
     *
     * @param id the id of the chatMessageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chatMessageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/chat-messages/{id}")
    @Timed
    public ResponseEntity<ChatMessageDTO> getChatMessage(@PathVariable Long id) {
        log.debug("REST request to get ChatMessage : {}", id);
        ChatMessageDTO chatMessageDTO = chatMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(chatMessageDTO));
    }

    /**
     * DELETE  /chat-messages/:id : delete the "id" chatMessage.
     *
     * @param id the id of the chatMessageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/chat-messages/{id}")
    @Timed
    public ResponseEntity<Void> deleteChatMessage(@PathVariable Long id) {
        log.debug("REST request to delete ChatMessage : {}", id);
        chatMessageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/chat-messages?query=:query : search for the chatMessage corresponding
     * to the query.
     *
     * @param query the query of the chatMessage search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/chat-messages")
    @Timed
    public ResponseEntity<List<ChatMessageDTO>> searchChatMessages(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ChatMessages for query {}", query);
        Page<ChatMessageDTO> page = chatMessageService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/chat-messages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
