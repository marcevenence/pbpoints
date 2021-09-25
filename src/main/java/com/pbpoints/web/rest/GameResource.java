package com.pbpoints.web.rest;

import com.pbpoints.repository.GameRepository;
import com.pbpoints.service.GameQueryService;
import com.pbpoints.service.GameService;
import com.pbpoints.service.criteria.GameCriteria;
import com.pbpoints.service.dto.GameDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pbpoints.domain.Game}.
 */
@RestController
@RequestMapping("/api")
public class GameResource {

    private final Logger log = LoggerFactory.getLogger(GameResource.class);

    private static final String ENTITY_NAME = "game";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameService gameService;

    private final GameRepository gameRepository;

    private final GameQueryService gameQueryService;

    public GameResource(GameService gameService, GameRepository gameRepository, GameQueryService gameQueryService) {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
        this.gameQueryService = gameQueryService;
    }

    /**
     * {@code POST  /games} : Create a new game.
     *
     * @param gameDTO the gameDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameDTO, or with status {@code 400 (Bad Request)} if the game has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/games")
    public ResponseEntity<GameDTO> createGame(@Valid @RequestBody GameDTO gameDTO) throws URISyntaxException {
        log.debug("REST request to save Game : {}", gameDTO);
        if (gameDTO.getId() != null) {
            throw new BadRequestAlertException("A new game cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameDTO result = gameService.save(gameDTO);
        return ResponseEntity
            .created(new URI("/api/games/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /games} : Updates an existing game.
     *
     * @param gameDTO the gameDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameDTO,
     * or with status {@code 400 (Bad Request)} if the gameDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/games")
    public ResponseEntity<GameDTO> updateGame(@Valid @RequestBody GameDTO gameDTO) throws URISyntaxException {
        log.debug("REST request to update Game : {}", gameDTO);
        if (gameDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GameDTO result = gameService.save(gameDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /games} : get all the games.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of games in body.
     */
    @GetMapping("/games")
    public ResponseEntity<List<GameDTO>> getAllGames(GameCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Games by criteria: {}", criteria);
        Page<GameDTO> page = gameQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /games/count} : count all the games.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/games/count")
    public ResponseEntity<Long> countGames(GameCriteria criteria) {
        log.debug("REST request to count Games by criteria: {}", criteria);
        return ResponseEntity.ok().body(gameQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /games/:id} : get the "id" game.
     *
     * @param id the id of the gameDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/games/{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
        log.debug("REST request to get Game : {}", id);
        Optional<GameDTO> gameDTO = gameService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameDTO);
    }

    /**
     * {@code DELETE  /games/:id} : delete the "id" game.
     *
     * @param id the id of the gameDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/games/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        log.debug("REST request to delete Game : {}", id);
        gameService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
