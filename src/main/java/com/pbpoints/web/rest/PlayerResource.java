package com.pbpoints.web.rest;

import com.pbpoints.domain.Category;
import com.pbpoints.domain.Player;
import com.pbpoints.domain.PlayerPoint;
import com.pbpoints.repository.PlayerRepository;
import com.pbpoints.service.*;
import com.pbpoints.service.criteria.PlayerCriteria;
import com.pbpoints.service.dto.*;
import com.pbpoints.service.mapper.TournamentMapper;
import com.pbpoints.service.mapper.UserMapper;
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
 * REST controller for managing {@link com.pbpoints.domain.Player}.
 */
@RestController
@RequestMapping("/api")
public class PlayerResource {

    private final Logger log = LoggerFactory.getLogger(PlayerResource.class);

    private static final String ENTITY_NAME = "player";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayerService playerService;

    private final PlayerPointService playerPointService;

    private final PlayerRepository playerRepository;

    private final PlayerQueryService playerQueryService;

    private final UserService userService;

    private final RosterService rosterService;

    public PlayerResource(
        PlayerService playerService,
        PlayerRepository playerRepository,
        PlayerQueryService playerQueryService,
        PlayerPointService playerPointService,
        UserService userService,
        CategoryService categoryService,
        TournamentService tournamentService,
        RosterService rosterService,
        TournamentMapper tournamentMapper
    ) {
        this.playerService = playerService;
        this.playerRepository = playerRepository;
        this.playerQueryService = playerQueryService;
        this.playerPointService = playerPointService;
        this.userService = userService;
        this.rosterService = rosterService;
    }

    /**
     * {@code POST  /players} : Create a new player.
     *
     * @param playerDTO the playerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playerDTO, or with status {@code 400 (Bad Request)} if the player has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/players")
    public ResponseEntity<PlayerDTO> createPlayer(@Valid @RequestBody PlayerDTO playerDTO) throws URISyntaxException {
        log.debug("REST request to save Player : {}", playerDTO);
        if (playerDTO.getId() != null) {
            throw new BadRequestAlertException("A new player cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (playerDTO.getProfile() == null) {
            throw new BadRequestAlertException("Profile cant be Null", ENTITY_NAME, "nullProfile");
        }
        if (playerService.validExists(playerDTO)) {
            throw new BadRequestAlertException("Already Exists", ENTITY_NAME, "alreadyInRoster");
        }
        if (playerService.validExistsOtherRoster(playerDTO)) {
            throw new BadRequestAlertException("Already Exists", ENTITY_NAME, "alreadyInOtherRoster");
        }
        PlayerDTO result = playerService.save(playerDTO);
        return ResponseEntity
            .created(new URI("/api/players/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /players/:id} : Updates an existing player.
     *
     * @param id        the id of the playerDTO to save.
     * @param playerDTO the playerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerDTO,
     * or with status {@code 400 (Bad Request)} if the playerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/players/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlayerDTO playerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Player : {}, {}", id, playerDTO);
        if (playerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlayerDTO result = playerService.save(playerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /players/:id} : Partial updates given fields of an existing player, field will ignore if it is null
     *
     * @param id        the id of the playerDTO to save.
     * @param playerDTO the playerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerDTO,
     * or with status {@code 400 (Bad Request)} if the playerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the playerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the playerDTO couldn't be updated.
     */
    @PatchMapping(value = "/players/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PlayerDTO> partialUpdatePlayer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlayerDTO playerDTO
    ) {
        log.debug("REST request to partial update Player partially : {}, {}", id, playerDTO);
        if (playerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlayerDTO> result = playerService.partialUpdate(playerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /players} : get all the players.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of players in body.
     */
    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers(PlayerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Players by criteria: {}", criteria);
        Page<PlayerDTO> page = playerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /players/count} : count all the players.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/players/count")
    public ResponseEntity<Long> countPlayers(PlayerCriteria criteria) {
        log.debug("REST request to count Players by criteria: {}", criteria);
        return ResponseEntity.ok().body(playerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /players/:id} : get the "id" player.
     *
     * @param id the id of the playerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/players/{id}")
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable Long id) {
        log.debug("REST request to get Player : {}", id);
        Optional<PlayerDTO> playerDTO = playerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(playerDTO);
    }

    @GetMapping("/players/validCategory/{id}/{tId}/{catId}")
    public ResponseEntity<PlayerPointDTO> getPlayer(@PathVariable Long id, @PathVariable Long tId, @PathVariable Long catId) {
        log.debug("REST request to get Player ID and Category ID : {} {} {}", id, tId, catId);
        return ResponseEntity.ok().body(playerPointService.findByUserAndTournament(id, tId, catId));
    }

    /**
     * {@code DELETE  /players/:id} : delete the "id" player.
     *
     * @param id the id of the playerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        log.debug("REST request to delete Player : {}", id);
        playerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/players/own/{id}")
    public ResponseEntity<Long> checkOwner(@PathVariable Long id) {
        log.debug("REST request to get Owner : {}", id);
        Long owner = rosterService.checkOwner(id);
        return ResponseEntity.ok().body(owner);
    }

    @GetMapping("/players/upd/{id}")
    public ResponseEntity<Long> enableUpdate(@PathVariable Long id) {
        log.debug("REST request to check if event is Closed or Inscripcion is Closed: {}", id);
        Long result = rosterService.validRoster(id);
        return ResponseEntity.ok().body(result);
    }
}
