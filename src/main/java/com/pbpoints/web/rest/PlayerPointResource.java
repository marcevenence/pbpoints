package com.pbpoints.web.rest;

import com.pbpoints.service.PlayerPointQueryService;
import com.pbpoints.service.PlayerPointService;
import com.pbpoints.service.dto.PlayerPointCriteria;
import com.pbpoints.service.dto.PlayerPointDTO;
import com.pbpoints.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pbpoints.domain.PlayerPoint}.
 */
@RestController
@RequestMapping("/api")
public class PlayerPointResource {

    private final Logger log = LoggerFactory.getLogger(PlayerPointResource.class);

    private static final String ENTITY_NAME = "playerPoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayerPointService playerPointService;

    private final PlayerPointQueryService playerPointQueryService;

    public PlayerPointResource(PlayerPointService playerPointService, PlayerPointQueryService playerPointQueryService) {
        this.playerPointService = playerPointService;
        this.playerPointQueryService = playerPointQueryService;
    }

    /**
     * {@code POST  /player-points} : Create a new playerPoint.
     *
     * @param playerPointDTO the playerPointDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playerPointDTO, or with status {@code 400 (Bad Request)} if the playerPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/player-points")
    public ResponseEntity<PlayerPointDTO> createPlayerPoint(@Valid @RequestBody PlayerPointDTO playerPointDTO) throws URISyntaxException {
        log.debug("REST request to save PlayerPoint : {}", playerPointDTO);
        if (playerPointDTO.getId() != null) {
            throw new BadRequestAlertException("A new playerPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlayerPointDTO result = playerPointService.save(playerPointDTO);
        return ResponseEntity
            .created(new URI("/api/player-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /player-points} : Updates an existing playerPoint.
     *
     * @param playerPointDTO the playerPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerPointDTO,
     * or with status {@code 400 (Bad Request)} if the playerPointDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playerPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/player-points")
    public ResponseEntity<PlayerPointDTO> updatePlayerPoint(@Valid @RequestBody PlayerPointDTO playerPointDTO) throws URISyntaxException {
        log.debug("REST request to update PlayerPoint : {}", playerPointDTO);
        if (playerPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlayerPointDTO result = playerPointService.save(playerPointDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playerPointDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /player-points} : get all the playerPoints.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playerPoints in body.
     */
    @GetMapping("/player-points")
    public ResponseEntity<List<PlayerPointDTO>> getAllPlayerPoints(PlayerPointCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PlayerPoints by criteria: {}", criteria);
        Page<PlayerPointDTO> page = playerPointQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /player-points/count} : count all the playerPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/player-points/count")
    public ResponseEntity<Long> countPlayerPoints(PlayerPointCriteria criteria) {
        log.debug("REST request to count PlayerPoints by criteria: {}", criteria);
        return ResponseEntity.ok().body(playerPointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /player-points/:id} : get the "id" playerPoint.
     *
     * @param id the id of the playerPointDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playerPointDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/player-points/{id}")
    public ResponseEntity<PlayerPointDTO> getPlayerPoint(@PathVariable Long id) {
        log.debug("REST request to get PlayerPoint : {}", id);
        Optional<PlayerPointDTO> playerPointDTO = playerPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(playerPointDTO);
    }

    /**
     * {@code DELETE  /player-points/:id} : delete the "id" playerPoint.
     *
     * @param id the id of the playerPointDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/player-points/{id}")
    public ResponseEntity<Void> deletePlayerPoint(@PathVariable Long id) {
        log.debug("REST request to delete PlayerPoint : {}", id);
        playerPointService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
