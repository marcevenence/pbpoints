package com.pbpoints.web.rest;

import com.pbpoints.repository.PlayerPointHistoryRepository;
import com.pbpoints.service.PlayerPointHistoryQueryService;
import com.pbpoints.service.PlayerPointHistoryService;
import com.pbpoints.service.criteria.PlayerPointHistoryCriteria;
import com.pbpoints.service.dto.PlayerPointHistoryDTO;
import com.pbpoints.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pbpoints.domain.PlayerPointHistory}.
 */
@RestController
@RequestMapping("/api")
public class PlayerPointHistoryResource {

    private final Logger log = LoggerFactory.getLogger(PlayerPointHistoryResource.class);

    private static final String ENTITY_NAME = "playerPointHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayerPointHistoryService playerPointHistoryService;

    private final PlayerPointHistoryRepository playerPointHistoryRepository;

    private final PlayerPointHistoryQueryService playerPointHistoryQueryService;

    public PlayerPointHistoryResource(
        PlayerPointHistoryService playerPointHistoryService,
        PlayerPointHistoryRepository playerPointHistoryRepository,
        PlayerPointHistoryQueryService playerPointHistoryQueryService
    ) {
        this.playerPointHistoryService = playerPointHistoryService;
        this.playerPointHistoryRepository = playerPointHistoryRepository;
        this.playerPointHistoryQueryService = playerPointHistoryQueryService;
    }

    /**
     * {@code POST  /player-point-histories} : Create a new playerPointHistory.
     *
     * @param playerPointHistoryDTO the playerPointHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playerPointHistoryDTO, or with status {@code 400 (Bad Request)} if the playerPointHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/player-point-histories")
    public ResponseEntity<PlayerPointHistoryDTO> createPlayerPointHistory(@Valid @RequestBody PlayerPointHistoryDTO playerPointHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save PlayerPointHistory : {}", playerPointHistoryDTO);
        if (playerPointHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new playerPointHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlayerPointHistoryDTO result = playerPointHistoryService.save(playerPointHistoryDTO);
        return ResponseEntity
            .created(new URI("/api/player-point-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /player-point-histories/:id} : Updates an existing playerPointHistory.
     *
     * @param id the id of the playerPointHistoryDTO to save.
     * @param playerPointHistoryDTO the playerPointHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerPointHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the playerPointHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playerPointHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/player-point-histories/{id}")
    public ResponseEntity<PlayerPointHistoryDTO> updatePlayerPointHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlayerPointHistoryDTO playerPointHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PlayerPointHistory : {}, {}", id, playerPointHistoryDTO);
        if (playerPointHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playerPointHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playerPointHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlayerPointHistoryDTO result = playerPointHistoryService.save(playerPointHistoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playerPointHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /player-point-histories/:id} : Partial updates given fields of an existing playerPointHistory, field will ignore if it is null
     *
     * @param id the id of the playerPointHistoryDTO to save.
     * @param playerPointHistoryDTO the playerPointHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerPointHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the playerPointHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the playerPointHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the playerPointHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/player-point-histories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlayerPointHistoryDTO> partialUpdatePlayerPointHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlayerPointHistoryDTO playerPointHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlayerPointHistory partially : {}, {}", id, playerPointHistoryDTO);
        if (playerPointHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playerPointHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playerPointHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlayerPointHistoryDTO> result = playerPointHistoryService.partialUpdate(playerPointHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playerPointHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /player-point-histories} : get all the playerPointHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playerPointHistories in body.
     */
    @GetMapping("/player-point-histories")
    public ResponseEntity<List<PlayerPointHistoryDTO>> getAllPlayerPointHistories(PlayerPointHistoryCriteria criteria) {
        log.debug("REST request to get PlayerPointHistories by criteria: {}", criteria);
        List<PlayerPointHistoryDTO> entityList = playerPointHistoryQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /player-point-histories/count} : count all the playerPointHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/player-point-histories/count")
    public ResponseEntity<Long> countPlayerPointHistories(PlayerPointHistoryCriteria criteria) {
        log.debug("REST request to count PlayerPointHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(playerPointHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /player-point-histories/:id} : get the "id" playerPointHistory.
     *
     * @param id the id of the playerPointHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playerPointHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/player-point-histories/{id}")
    public ResponseEntity<PlayerPointHistoryDTO> getPlayerPointHistory(@PathVariable Long id) {
        log.debug("REST request to get PlayerPointHistory : {}", id);
        Optional<PlayerPointHistoryDTO> playerPointHistoryDTO = playerPointHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(playerPointHistoryDTO);
    }

    /**
     * {@code DELETE  /player-point-histories/:id} : delete the "id" playerPointHistory.
     *
     * @param id the id of the playerPointHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/player-point-histories/{id}")
    public ResponseEntity<Void> deletePlayerPointHistory(@PathVariable Long id) {
        log.debug("REST request to delete PlayerPointHistory : {}", id);
        playerPointHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
