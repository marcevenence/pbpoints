package com.pbpoints.web.rest;

import com.pbpoints.repository.PlayerDetailPointRepository;
import com.pbpoints.service.PlayerDetailPointQueryService;
import com.pbpoints.service.PlayerDetailPointService;
import com.pbpoints.service.criteria.PlayerDetailPointCriteria;
import com.pbpoints.service.dto.PlayerDetailPointDTO;
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
 * REST controller for managing {@link com.pbpoints.domain.PlayerDetailPoint}.
 */
@RestController
@RequestMapping("/api")
public class PlayerDetailPointResource {

    private final Logger log = LoggerFactory.getLogger(PlayerDetailPointResource.class);

    private static final String ENTITY_NAME = "playerDetailPoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayerDetailPointService playerDetailPointService;

    private final PlayerDetailPointRepository playerDetailPointRepository;

    private final PlayerDetailPointQueryService playerDetailPointQueryService;

    public PlayerDetailPointResource(
        PlayerDetailPointService playerDetailPointService,
        PlayerDetailPointRepository playerDetailPointRepository,
        PlayerDetailPointQueryService playerDetailPointQueryService
    ) {
        this.playerDetailPointService = playerDetailPointService;
        this.playerDetailPointRepository = playerDetailPointRepository;
        this.playerDetailPointQueryService = playerDetailPointQueryService;
    }

    /**
     * {@code POST  /player-detail-points} : Create a new playerDetailPoint.
     *
     * @param playerDetailPointDTO the playerDetailPointDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playerDetailPointDTO, or with status {@code 400 (Bad Request)} if the playerDetailPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/player-detail-points")
    public ResponseEntity<PlayerDetailPointDTO> createPlayerDetailPoint(@Valid @RequestBody PlayerDetailPointDTO playerDetailPointDTO)
        throws URISyntaxException {
        log.debug("REST request to save PlayerDetailPoint : {}", playerDetailPointDTO);
        if (playerDetailPointDTO.getId() != null) {
            throw new BadRequestAlertException("A new playerDetailPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlayerDetailPointDTO result = playerDetailPointService.save(playerDetailPointDTO);
        return ResponseEntity
            .created(new URI("/api/player-detail-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /player-detail-points/:id} : Updates an existing playerDetailPoint.
     *
     * @param id the id of the playerDetailPointDTO to save.
     * @param playerDetailPointDTO the playerDetailPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerDetailPointDTO,
     * or with status {@code 400 (Bad Request)} if the playerDetailPointDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playerDetailPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/player-detail-points")
    public ResponseEntity<PlayerDetailPointDTO> updatePlayerDetailPoint(@Valid @RequestBody PlayerDetailPointDTO playerDetailPointDTO)
        throws URISyntaxException {
        log.debug("REST request to update PlayerDetailPoint : {}, {}", playerDetailPointDTO);
        if (playerDetailPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlayerDetailPointDTO result = playerDetailPointService.save(playerDetailPointDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playerDetailPointDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /player-detail-points/:id} : Partial updates given fields of an existing playerDetailPoint, field will ignore if it is null
     *
     * @param id the id of the playerDetailPointDTO to save.
     * @param playerDetailPointDTO the playerDetailPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerDetailPointDTO,
     * or with status {@code 400 (Bad Request)} if the playerDetailPointDTO is not valid,
     * or with status {@code 404 (Not Found)} if the playerDetailPointDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the playerDetailPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/player-detail-points/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PlayerDetailPointDTO> partialUpdatePlayerDetailPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlayerDetailPointDTO playerDetailPointDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlayerDetailPoint partially : {}, {}", id, playerDetailPointDTO);
        if (playerDetailPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playerDetailPointDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playerDetailPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlayerDetailPointDTO> result = playerDetailPointService.partialUpdate(playerDetailPointDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playerDetailPointDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /player-detail-points} : get all the playerDetailPoints.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playerDetailPoints in body.
     */
    @GetMapping("/player-detail-points")
    public ResponseEntity<List<PlayerDetailPointDTO>> getAllPlayerDetailPoints(PlayerDetailPointCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PlayerDetailPoints by criteria: {}", criteria);
        Page<PlayerDetailPointDTO> page = playerDetailPointQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /player-detail-points/count} : count all the playerDetailPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/player-detail-points/count")
    public ResponseEntity<Long> countPlayerDetailPoints(PlayerDetailPointCriteria criteria) {
        log.debug("REST request to count PlayerDetailPoints by criteria: {}", criteria);
        return ResponseEntity.ok().body(playerDetailPointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /player-detail-points/:id} : get the "id" playerDetailPoint.
     *
     * @param id the id of the playerDetailPointDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playerDetailPointDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/player-detail-points/{id}")
    public ResponseEntity<PlayerDetailPointDTO> getPlayerDetailPoint(@PathVariable Long id) {
        log.debug("REST request to get PlayerDetailPoint : {}", id);
        Optional<PlayerDetailPointDTO> playerDetailPointDTO = playerDetailPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(playerDetailPointDTO);
    }

    /**
     * {@code DELETE  /player-detail-points/:id} : delete the "id" playerDetailPoint.
     *
     * @param id the id of the playerDetailPointDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/player-detail-points/{id}")
    public ResponseEntity<Void> deletePlayerDetailPoint(@PathVariable Long id) {
        log.debug("REST request to delete PlayerDetailPoint : {}", id);
        playerDetailPointService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
