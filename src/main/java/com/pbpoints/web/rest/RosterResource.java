package com.pbpoints.web.rest;

import com.pbpoints.domain.*;
import com.pbpoints.domain.enumeration.ProfileUser;
import com.pbpoints.repository.*;
import com.pbpoints.service.*;
import com.pbpoints.service.dto.*;
import com.pbpoints.service.mapper.*;
import com.pbpoints.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
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
 * REST controller for managing {@link com.pbpoints.domain.Roster}.
 */
@RestController
@RequestMapping("/api")
public class RosterResource {

    private final Logger log = LoggerFactory.getLogger(RosterResource.class);

    private static final String ENTITY_NAME = "roster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RosterService rosterService;

    private final PlayerService playerService;

    private final PlayerPointService playerPointService;

    private final RosterQueryService rosterQueryService;

    private final UserExtraService userExtraService;

    private final UserRepository userRepository;

    private final EventCategoryMapper eventCategoryMapper;

    private final EventCategoryService eventCategoryService;

    private final TournamentMapper tournamentMapper;

    private final CategoryRepository categoryRepository;

    private final PlayerPointRepository playerPointRepository;

    private final PlayerPointMapper playerPointMapper;

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;

    private final EventCategoryRepository eventCategoryRepository;

    public RosterResource(
        RosterService rosterService,
        RosterQueryService rosterQueryService,
        PlayerService playerService,
        PlayerPointService playerPointService,
        UserExtraService userExtraService,
        UserRepository userRepository,
        EventCategoryRepository eventCategoryRepository,
        EventCategoryService eventCategoryService,
        TournamentMapper tournamentMapper,
        CategoryRepository categoryRepository,
        PlayerPointRepository playerPointRepository,
        PlayerPointMapper playerPointMapper,
        EventCategoryMapper eventCategoryMapper,
        TeamRepository teamRepository,
        TeamMapper teamMapper
    ) {
        this.rosterService = rosterService;
        this.rosterQueryService = rosterQueryService;
        this.playerService = playerService;
        this.playerPointService = playerPointService;
        this.userExtraService = userExtraService;
        this.userRepository = userRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventCategoryMapper = eventCategoryMapper;
        this.eventCategoryService = eventCategoryService;
        this.tournamentMapper = tournamentMapper;
        this.categoryRepository = categoryRepository;
        this.playerPointRepository = playerPointRepository;
        this.playerPointMapper = playerPointMapper;
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    /**
     * {@code POST  /rosters} : Create a new roster.
     *
     * @param rosterDTO the rosterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rosterDTO, or with status {@code 400 (Bad Request)} if the roster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rosters")
    public ResponseEntity<RosterDTO> createRoster(@Valid @RequestBody RosterDTO rosterDTO) throws URISyntaxException {
        log.debug("REST request to save Roster : {}", rosterDTO);
        if (rosterDTO.getId() != null) {
            throw new BadRequestAlertException("A new roster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RosterDTO exists = rosterService.findByTeamAndEventCategory(rosterDTO.getTeam(), rosterDTO.getEventCategory());
        if (exists.getId() != null) {
            throw new BadRequestAlertException("Already Exists", ENTITY_NAME, "alreadyInRoster");
        }
        rosterDTO.setActive(true);
        RosterDTO result = rosterService.save(rosterDTO);
        return ResponseEntity
            .created(new URI("/api/rosters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /rosters/players} : Create a new roster with players.
     *
     * @param rosterWithPlayersDTO the rosterWithPlayersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rosterDTO, or with status {@code 400 (Bad Request)} if the roster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rosters/players")
    public ResponseEntity<RosterDTO> createRosterWithPlayers(@Valid @RequestBody RosterWithPlayersDTO rosterWithPlayersDTO)
        throws URISyntaxException {
        log.debug("REST request to save Roster with Players: {}", rosterWithPlayersDTO);
        /*try {
            RosterDTO exists = rosterService.findByTeamAndEventCategory(
                rosterWithPlayersDTO.getTeam(),
                rosterWithPlayersDTO.getEventCategory()
            );
            if (exists.getId() != null) {
                throw new BadRequestAlertException("Already Team Exists", ENTITY_NAME, "TeamAlreadyInRoster");
            }
        } finally {
            log.debug("Fallo el Try");
        }*/
        RosterDTO rosterDTO = new RosterDTO();
        rosterDTO.setTeam(teamMapper.toDto(teamRepository.findById(rosterWithPlayersDTO.getTeamId()).get()));
        rosterDTO.setEventCategory(
            eventCategoryMapper.toDto(eventCategoryRepository.findById(rosterWithPlayersDTO.getEventCategoryId()).get())
        );
        rosterDTO.setActive(true);
        log.debug("Before Roster Saved:");
        RosterDTO result = rosterService.save(rosterDTO);
        log.debug("Roster Saved:");

        PlayerDTO result2 = new PlayerDTO();
        List<PlayerDTO> playersDTO = rosterWithPlayersDTO.getPlayers();
        for (PlayerDTO playerDTO : playersDTO) {
            playerDTO.setRoster(result);
            if (playerService.validExists(playerDTO)) {
                throw new BadRequestAlertException("Already Exists", ENTITY_NAME, "alreadyInRoster");
            }
            if (playerService.validExistsOtherRoster(playerDTO)) {
                throw new BadRequestAlertException("Already Exists", ENTITY_NAME, "alreadyInOtherRoster");
            }
            result2 = playerService.save(playerDTO);
        }
        return ResponseEntity
            .created(new URI("/api/rosters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rosters} : Updates an existing roster.
     *
     * @param rosterDTO the rosterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rosterDTO,
     * or with status {@code 400 (Bad Request)} if the rosterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rosterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rosters")
    public ResponseEntity<RosterDTO> updateRoster(@Valid @RequestBody RosterDTO rosterDTO) throws URISyntaxException {
        log.debug("REST request to update Roster : {}", rosterDTO);
        if (rosterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RosterDTO result = rosterService.save(rosterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rosterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rosters/players} : Updates an existing roster.
     *
     * @param players the rosterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rosterDTO,
     * or with status {@code 400 (Bad Request)} if the rosterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rosterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rosters/players/{id}")
    public ResponseEntity<RosterDTO> updateRosterWithPlayers(@PathVariable Long id, @Valid @RequestBody List<PlayerDTO> players)
        throws URISyntaxException {
        log.debug("REST request to update Roster : {}", id);
        log.debug("With Players : {}", players);
        if (id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RosterDTO result = rosterService.findOne(id).get();

        /* Elimino los players que no estan en la lista*/
        playerService.deleteFromRoster(result, players);

        /* Agrego los players que no estan en el roster*/
        PlayerDTO result2 = new PlayerDTO();
        for (PlayerDTO playerDTO : players) {
            Optional<Player> opp = playerService.findByUserAndRoster(playerDTO.getUser().getId(), result.getId());
            if (!opp.isPresent()) {
                playerDTO.setRoster(result);
                result2 = playerService.save(playerDTO);
            }
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rosters} : get all the rosters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rosters in body.
     */
    @GetMapping("/rosters")
    public ResponseEntity<List<RosterDTO>> getAllRosters(RosterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Rosters by criteria: {}", criteria);
        Page<RosterDTO> page = rosterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rosters/count} : count all the rosters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rosters/count")
    public ResponseEntity<Long> countRosters(RosterCriteria criteria) {
        log.debug("REST request to count Rosters by criteria: {}", criteria);
        return ResponseEntity.ok().body(rosterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rosters/:id} : get the "id" roster.
     *
     * @param id the id of the rosterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rosterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rosters/{id}")
    public ResponseEntity<RosterDTO> getRoster(@PathVariable Long id) {
        log.debug("REST request to get Roster : {}", id);
        Optional<RosterDTO> rosterDTO = rosterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rosterDTO);
    }

    @GetMapping("/rosters/check/{id}/{profile}")
    public ResponseEntity<RosterDTO> checkInRoster(@PathVariable Long id, @PathVariable String profile) {
        log.debug("Verificando en Rosters por UserExtraID y Profile : {} {}", id, profile);
        Optional<RosterDTO> rosterDTO = rosterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rosterDTO);
    }

    /**
     * {@code DELETE  /rosters/:id} : delete the "id" roster.
     *
     * @param id the id of the rosterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rosters/{id}")
    public ResponseEntity<Void> deleteRoster(@PathVariable Long id) {
        log.debug("REST request to delete Roster : {}", id);
        rosterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /rosters} : get all the rosters for the {@link User} logged.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rosters in body.
     */
    @GetMapping("/rosters/owner")
    public ResponseEntity<List<RosterDTO>> getRosterByLoggedUser() {
        log.debug("REST request to get Rosters by User Logged");
        Optional<List<RosterDTO>> rostersDTO = rosterService.findByLogguedUser();
        return ResponseUtil.wrapOrNotFound(rostersDTO);
    }

    /**
     * {@code GET  /rosters} : get all the rosters for the {@link EventCategory} available.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rosters in body.
     */
    @GetMapping("/rosters/event-category/available")
    public ResponseEntity<List<RosterDTO>> findAvailableByEventCategory(
        @RequestParam(value = "idEventCategory", required = true) Long idEventCategory
    ) {
        log.debug("REST request to get Rosters Available for EventCategory");
        if (idEventCategory == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Optional<List<RosterDTO>> rostersDTO = rosterService.findAvailableByEventCategory(idEventCategory);
        return ResponseUtil.wrapOrNotFound(rostersDTO);
    }

    @PutMapping("/rosters/validatePlayer")
    public ResponseEntity<PlayerDTO> validatePlayer(@Valid @RequestBody RosterSubsDTO rosterSubsDTO) throws URISyntaxException {
        log.debug("validatePlayer with Params: {}", rosterSubsDTO.toString());
        PlayerDTO playerd = new PlayerDTO();

        Optional<EventCategoryDTO> eventCategoryDTO = eventCategoryService.findOne(rosterSubsDTO.getEventCategoryId());

        Optional<UserExtraDTO> userExtraDTO = userExtraService.findOneByIdAndCode(rosterSubsDTO.getId(), rosterSubsDTO.getCode());
        if (!userExtraDTO.isPresent()) {
            throw new BadRequestAlertException("No se encontro un User Extra", ENTITY_NAME, "idexists");
        } else {
            User user = userRepository.findOneById(userExtraDTO.get().getId());
            UserMapper um = new UserMapper();
            playerd.setUser(um.userToUserDTO(user));
            playerd.setRoster(rosterSubsDTO.getRoster());

            if (rosterSubsDTO.getProfile().equals("STAFF")) {
                playerd.setProfile(ProfileUser.STAFF);
            }
            if (rosterSubsDTO.getProfile().equals("PLAYER")) {
                playerd.setProfile(ProfileUser.PLAYER);
            }
        }
        Optional<PlayerPointDTO> ppdto = playerPointService.findByUserAndTournament(
            userRepository.findOneById(rosterSubsDTO.getId()),
            tournamentMapper.toEntity(rosterSubsDTO.getRoster().getEventCategory().getEvent().getTournament())
        );
        if (!ppdto.isPresent()) {
            Category category = categoryRepository.LastCategoryByTournamentId(
                rosterSubsDTO.getRoster().getEventCategory().getEvent().getTournament().getId()
            );
            PlayerPoint playerPoint = new PlayerPoint();
            playerPoint.setTournament(tournamentMapper.toEntity(rosterSubsDTO.getRoster().getEventCategory().getEvent().getTournament()));
            playerPoint.setPoints((float) 0);
            playerPoint.setUser(userRepository.findOneById(rosterSubsDTO.getId()));
            playerPoint.setCategory(category);

            playerPoint = playerPointRepository.save(playerPoint);
            ppdto = Optional.of(playerPointMapper.toDto(playerPoint));
        }

        playerd.setCategory(ppdto.get().getCategory());

        if (rosterSubsDTO.getProfile().equals(ProfileUser.STAFF.toString())) {
            log.debug("Es Staff");
        } else {
            log.debug("Es Jugador");
            if (eventCategoryDTO.get().getEvent().getTournament().isCategorize()) {
                /*Valido si es una categoria mas alta, que no haya otros jugadores de esa categoria*/
                if (ppdto.get().getCategory().getOrder() >= rosterSubsDTO.getRoster().getEventCategory().getCategory().getOrder()) {
                    log.debug(
                        "Categoria validada --> PlayerCategory: {}, Category{}",
                        ppdto.get().getCategory().getOrder(),
                        rosterSubsDTO.getRoster().getEventCategory().getCategory().getOrder()
                    );
                } else {
                    if (ppdto.get().getCategory().getOrder() == rosterSubsDTO.getRoster().getEventCategory().getCategory().getOrder() - 1) {
                        /*Verificar si no hay otro jugador en el roster con misma categoria, Si hay tiro error*/
                        Integer qty = rosterSubsDTO.getRoster().getEventCategory().getEvent().getTournament().getCantPlayersNextCategory();
                        if (qty == 0 || qty == null) {
                            log.debug("No se permiten jugadores de una categoria mas alta");
                            throw new BadRequestAlertException(
                                "Categor??a no v??lida para el player",
                                "PLAYER_POINT",
                                "playerPointCategoryError"
                            );
                        }
                        Integer qtyEx = 0;
                        for (PlayerDTO playerDTO : rosterSubsDTO.getPlayers()) {
                            if (playerDTO.getProfile().equals(ProfileUser.PLAYER)) {
                                PlayerPoint pp = playerPointRepository
                                    .findByUserAndTournament(
                                        userRepository.findById(playerDTO.getUser().getId()).get(),
                                        tournamentMapper.toEntity(rosterSubsDTO.getRoster().getEventCategory().getEvent().getTournament())
                                    )
                                    .get();
                                if (pp.getCategory().getOrder() == ppdto.get().getCategory().getOrder()) {
                                    qtyEx++;
                                }
                            }
                        }
                        if (qty < qtyEx + 1) {
                            throw new BadRequestAlertException(
                                "Categor??a no v??lida para el player",
                                "PLAYER_POINT",
                                "playerPointCategoryError"
                            );
                        }
                        log.debug("Unico jugador con una categoria mas alta");
                    } else {
                        throw new BadRequestAlertException(
                            "Categor??a no v??lida para el player",
                            "PLAYER_POINT",
                            "playerPointCategoryError"
                        );
                    }
                }
            }
        }
        /*Valido que el jugador no este en otro equipo*/
        try {
            Optional<Player> player = playerService.findByUserAndEventCategory(
                rosterSubsDTO.getId(),
                eventCategoryMapper.toEntity(eventCategoryDTO.get())
            );
            if (player.isPresent()) {
                log.debug("Player Encontrado. Valido: {}", player.get());
                log.debug("Player Profile vs rosterSubs.Profile: {}", player.get().getProfile().toString());
                log.debug("Player Profile vs rosterSubs.Profile: {}", rosterSubsDTO.getProfile());
                if (player.get().getProfile().toString().equals(rosterSubsDTO.getProfile())) {
                    throw new BadRequestAlertException("alreadyInOtherRoster", ENTITY_NAME, "alreadyInOtherRoster");
                }
            } else {
                log.debug("Player No encontrado en otro roster.");
            }
        } catch (NoResultException e) {
            log.debug("Player No encontrado en otro roster");
        } catch (BadRequestAlertException e) {
            throw new BadRequestAlertException("alreadyInOtherRoster", ENTITY_NAME, "alreadyInOtherRoster");
        }
        return ResponseEntity.ok().body(playerd);
    }

    @PutMapping("/rosters/validatePlayer2")
    public ResponseEntity<PlayerDTO> validatePlayer2(@Valid @RequestBody RosterSubsPlDTO rosterSubsPlDTO) throws URISyntaxException {
        log.debug("validatePlayerPlDTO with Params: {}", rosterSubsPlDTO.toString());
        if (rosterSubsPlDTO.getPlayer().getRoster().getEventCategory().getEvent().getTournament().isCategorize()) {
            /*Valido si es una categoria mas alta, que no haya otros jugadores de esa categoria*/
            if (
                rosterSubsPlDTO.getPlayer().getCategory().getOrder() >=
                rosterSubsPlDTO.getPlayer().getRoster().getEventCategory().getCategory().getOrder()
            ) {
                log.debug(
                    "Categoria validada --> PlayerCategory: {}, Category{}",
                    rosterSubsPlDTO.getPlayer().getCategory().getOrder(),
                    rosterSubsPlDTO.getPlayer().getRoster().getEventCategory().getCategory().getOrder()
                );
            } else {
                if (
                    rosterSubsPlDTO.getPlayer().getCategory().getOrder() ==
                    rosterSubsPlDTO.getPlayer().getRoster().getEventCategory().getCategory().getOrder() -
                    1
                ) {
                    /*Verificar si no hay otro jugador en el roster con misma categoria, Si hay tiro error*/
                    Integer qty = rosterSubsPlDTO
                        .getPlayer()
                        .getRoster()
                        .getEventCategory()
                        .getEvent()
                        .getTournament()
                        .getCantPlayersNextCategory();
                    if (qty == 0 || qty == null) {
                        log.debug("No se permiten jugadores de una categoria mas alta");
                        throw new BadRequestAlertException(
                            "Categor??a no v??lida para el player",
                            "PLAYER_POINT",
                            "playerPointCategoryError"
                        );
                    }
                    Integer qtyEx = 0;
                    for (PlayerDTO playerDTO : rosterSubsPlDTO.getPlayers()) {
                        if (playerDTO.getProfile().equals(ProfileUser.PLAYER)) {
                            PlayerPoint pp = playerPointRepository
                                .findByUserAndTournament(
                                    userRepository.findById(playerDTO.getUser().getId()).get(),
                                    tournamentMapper.toEntity(
                                        rosterSubsPlDTO.getPlayer().getRoster().getEventCategory().getEvent().getTournament()
                                    )
                                )
                                .get();
                            if (pp.getCategory().getOrder() == rosterSubsPlDTO.getPlayer().getCategory().getOrder()) {
                                qtyEx++;
                            }
                        }
                    }
                    if (qty < qtyEx + 1) {
                        throw new BadRequestAlertException(
                            "Categor??a no v??lida para el player",
                            "PLAYER_POINT",
                            "playerPointCategoryError"
                        );
                    }
                    log.debug("Unico jugador con una categoria mas alta");
                } else {
                    throw new BadRequestAlertException("Categor??a no v??lida para el player", "PLAYER_POINT", "playerPointCategoryError");
                }
            }
        }
        /*Valido que el jugador no este en otro equipo*/
        try {
            Optional<Player> player = playerService.findByUserAndEventCategory(
                rosterSubsPlDTO.getPlayer().getUser().getId(),
                eventCategoryMapper.toEntity(rosterSubsPlDTO.getPlayer().getRoster().getEventCategory())
            );
            if (player.isPresent()) {
                log.debug("Player Encontrado. Valido: {}", player.get());
                log.debug("Player Profile vs rosterSubs.Profile: {}", player.get().getProfile().toString());
                log.debug("Player Profile vs rosterSubs.Profile: {}", rosterSubsPlDTO.getPlayer().getProfile());
                if (player.get().getProfile().toString().equals(rosterSubsPlDTO.getPlayer().getProfile())) {
                    throw new BadRequestAlertException("alreadyInOtherRoster", ENTITY_NAME, "alreadyInOtherRoster");
                }
            }
        } catch (NoResultException e) {
            log.debug("Player No encontrado en otro roster");
        } catch (BadRequestAlertException e) {
            throw new BadRequestAlertException("alreadyInOtherRoster", ENTITY_NAME, "alreadyInOtherRoster");
        }
        return ResponseEntity.ok().body(rosterSubsPlDTO.getPlayer());
    }
}
