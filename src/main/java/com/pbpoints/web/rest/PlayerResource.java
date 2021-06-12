package com.pbpoints.web.rest;

import com.pbpoints.domain.User;
import com.pbpoints.repository.UserRepository;
import com.pbpoints.service.*;
import com.pbpoints.service.dto.*;
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
    private final RosterService rosterService;
    private final PlayerQueryService playerQueryService;
    private final UserRepository userRepository;

    public PlayerResource(
        PlayerService playerService,
        PlayerQueryService playerQueryService,
        RosterService rosterService,
        UserRepository userRepository
    ) {
        this.playerService = playerService;
        this.playerQueryService = playerQueryService;
        this.rosterService = rosterService;
        this.userRepository = userRepository;
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
        if (playerService.validCategory(playerDTO)) {
            PlayerDTO result = playerService.save(playerDTO);
            if (result.getId() == null) {
                throw new BadRequestAlertException("No se puede agregar al jugador", ENTITY_NAME, "InvalidPlayer");
            } else {
                User user = userRepository.findOneById(result.getUser().getId());
                return ResponseEntity
                    .created(new URI("/api/players/" + result.getId()))
                    .headers(
                        HeaderUtil.createEntityCreationAlert(
                            applicationName,
                            true,
                            ENTITY_NAME,
                            user.getLastName() + ", " + user.getFirstName()
                        )
                    )
                    .body(result);
            }
        } else throw new BadRequestAlertException("La Categoria no es valida", ENTITY_NAME, "invalidCategoryPlayer");
    }

    /**
     * {@code PUT  /players} : Updates an existing player.
     *
     * @param playerDTO the playerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerDTO,
     * or with status {@code 400 (Bad Request)} if the playerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/players")
    public ResponseEntity<PlayerDTO> updatePlayer(@Valid @RequestBody PlayerDTO playerDTO) throws URISyntaxException {
        log.debug("REST request to update Player : {}", playerDTO);
        if (playerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (playerDTO.getProfile() == null) {
            throw new BadRequestAlertException("Profile cant be Null", ENTITY_NAME, "nullProfile");
        }
        if (playerService.validExistsOtherRoster(playerDTO)) {
            throw new BadRequestAlertException("Already Exists", ENTITY_NAME, "alreadyInOtherRoster");
        }
        PlayerDTO result = playerService.save(playerDTO);
        User user = userRepository.findOneById(result.getUser().getId());
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, user.getLastName() + ", " + user.getFirstName())
            )
            .body(result);
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
        ResponseEntity<Long> resp = ResponseEntity.ok().body(owner);
        return resp;
    }

    @GetMapping("/players/upd/{id}")
    public ResponseEntity<Long> enableUpdate(@PathVariable Long id) {
        log.debug("REST request to check if event is Closed or Inscripcion is Closed: {}", id);
        Long result = rosterService.validRoster(id);
        ResponseEntity<Long> resp = ResponseEntity.ok().body(result);
        return resp;
    }
}
