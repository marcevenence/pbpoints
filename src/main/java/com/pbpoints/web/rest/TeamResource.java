package com.pbpoints.web.rest;

import com.pbpoints.repository.TeamRepository;
import com.pbpoints.service.MainRosterService;
import com.pbpoints.service.TeamQueryService;
import com.pbpoints.service.TeamService;
import com.pbpoints.service.criteria.TeamCriteria;
import com.pbpoints.service.dto.MainRosterDTO;
import com.pbpoints.service.dto.TeamDTO;
import com.pbpoints.service.dto.TeamWithRostersDTO;
import com.pbpoints.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pbpoints.domain.Team}.
 */
@RestController
@RequestMapping("/api")
public class TeamResource {

    private final Logger log = LoggerFactory.getLogger(TeamResource.class);

    private static final String ENTITY_NAME = "team";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeamService teamService;

    private final TeamRepository teamRepository;

    private final TeamQueryService teamQueryService;

    private MainRosterService mainRosterService;

    public TeamResource(
        TeamService teamService,
        TeamRepository teamRepository,
        TeamQueryService teamQueryService,
        MainRosterService mainRosterService
    ) {
        this.teamService = teamService;
        this.teamRepository = teamRepository;
        this.teamQueryService = teamQueryService;
        this.mainRosterService = mainRosterService;
    }

    /**
     * {@code POST  /teams} : Create a new team.
     *
     * @param teamDTO the teamDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teamDTO, or with status {@code 400 (Bad Request)} if the team has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/teams")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO) throws URISyntaxException {
        log.debug("REST request to save Team : {}", teamDTO);
        if (teamDTO.getId() != null) {
            throw new BadRequestAlertException("A new team cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (teamService.findByNameAndIdOwner(teamDTO.getName(), teamDTO.getOwner().getId()).isPresent()) {
            throw new BadRequestAlertException("The Team has already exists", ENTITY_NAME, "teamexists");
        }
        teamDTO.setActive(true);
        TeamDTO result = teamService.save(teamDTO);
        return ResponseEntity
            .created(new URI("/api/teams/" + result.getId()))
            //.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getName()))
            .body(result);
    }

    /**
     * {@code POST  /teams/players} : Create a new roster with players.
     *
     * @param teamWithRostersDTO the rosterWithPlayersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rosterDTO, or with status {@code 400 (Bad Request)} if the roster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/teams/mainRoster")
    public ResponseEntity<TeamDTO> createTeamWithRosters(@Valid @RequestBody TeamWithRostersDTO teamWithRostersDTO)
        throws URISyntaxException {
        log.debug("REST request to save Team with Rosters: {}", teamWithRostersDTO);
        if (teamWithRostersDTO.getTeam().getId() != null) {
            throw new BadRequestAlertException("A new team cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (
            teamService
                .findByNameAndIdOwner(teamWithRostersDTO.getTeam().getName(), teamWithRostersDTO.getTeam().getOwner().getId())
                .isPresent()
        ) {
            throw new BadRequestAlertException("The Team has already exists", ENTITY_NAME, "teamexists");
        }

        TeamDTO result = teamService.save(teamWithRostersDTO.getTeam());
        List<MainRosterDTO> mainRostersDTO = teamWithRostersDTO.getMainRosters();
        MainRosterDTO result2 = new MainRosterDTO();

        int i = 0;
        for (MainRosterDTO mainRosterDTO : mainRostersDTO) {
            mainRosterDTO.setTeam(result);
            result2 = mainRosterService.save(mainRosterDTO);
            i++;
        }
        return ResponseEntity
            .created(new URI("/api/teams/players/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getName()))
            .body(result);
    }

    /**
     * {@code PUT  /teams/:id} : Updates an existing team.
     *
     * @param id the id of the teamDTO to save.
     * @param teamDTO the teamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teamDTO,
     * or with status {@code 400 (Bad Request)} if the teamDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/teams")
    public ResponseEntity<TeamDTO> updateTeam(@RequestBody TeamDTO teamDTO) throws URISyntaxException {
        log.debug("REST request to update Team : {}", teamDTO);
        if (teamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TeamDTO result = teamService.save(teamDTO);
        return ResponseEntity
            .ok()
            //.headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamDTO.getId().toString()))
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamDTO.getName()))
            .body(result);
    }

    /**
     * {@code PATCH  /teams/:id} : Partial updates given fields of an existing team, field will ignore if it is null
     *
     * @param id the id of the teamDTO to save.
     * @param teamDTO the teamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teamDTO,
     * or with status {@code 400 (Bad Request)} if the teamDTO is not valid,
     * or with status {@code 404 (Not Found)} if the teamDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the teamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/teams/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TeamDTO> partialUpdateTeam(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TeamDTO teamDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Team partially : {}, {}", id, teamDTO);
        if (teamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TeamDTO> result = teamService.partialUpdate(teamDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamDTO.getId().toString())
        );
    }

    /**
     * {@code PUT  /teams/mainRoster} : Updates an existing team and add Players.
     *
     * @param teamWithRostersDTO the teamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teamDTO,
     * or with status {@code 400 (Bad Request)} if the teamDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/teams/mainRoster")
    public ResponseEntity<TeamDTO> updateTeamWithRoster(@RequestBody TeamWithRostersDTO teamWithRostersDTO) throws URISyntaxException {
        log.debug("REST request to update Team : {}", teamWithRostersDTO.getTeam());
        if (teamWithRostersDTO.getTeam().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TeamDTO result = teamService.save(teamWithRostersDTO.getTeam());
        List<MainRosterDTO> mainRostersDTO = teamWithRostersDTO.getMainRosters();
        MainRosterDTO result2 = new MainRosterDTO();
        log.debug("Cantidad : {}", mainRostersDTO.size());
        int i = 0;
        for (MainRosterDTO mainRosterDTO : mainRostersDTO) {
            mainRosterDTO.setTeam(result);
            result2 = mainRosterService.save(mainRosterDTO);
            i++;
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamWithRostersDTO.getTeam().getName()))
            .body(result);
    }

    /**
     * {@code GET  /teams} : get all the teams.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teams in body.
     */
    @GetMapping("/teams")
    public ResponseEntity<List<TeamDTO>> getAllTeams(TeamCriteria criteria) {
        log.debug("REST request to get Teams by criteria: {}", criteria);
        List<TeamDTO> entityList = teamQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /teams/findNotAll/:ownerId} : get all the teams.
     *
     * @param ownerId get algo XD.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teams in body.
     */
    @GetMapping("/teams/findNotAll/{ownerId}/{evCatId}")
    public ResponseEntity<List<TeamDTO>> getTeamsNoRoster(@PathVariable Long ownerId, @PathVariable Long evCatId) {
        log.debug("REST request to get Teams Not Subscribe with ownerID: {}", ownerId);
        log.debug("REST request to get Teams Not Subscribe with ownerID: {}", evCatId);
        List<TeamDTO> teams = teamService.getTeamsNoSubs(ownerId, evCatId);
        return ResponseEntity.ok().body(teams);
    }

    /**
     * {@code GET  /teams/count} : count all the teams.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/teams/count")
    public ResponseEntity<Long> countTeams(TeamCriteria criteria) {
        log.debug("REST request to count Teams by criteria: {}", criteria);
        return ResponseEntity.ok().body(teamQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /teams/:id} : get the "id" team.
     *
     * @param id the id of the teamDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teamDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teams/{id}")
    public ResponseEntity<TeamDTO> getTeam(@PathVariable Long id) {
        log.debug("REST request to get Team : {}", id);
        Optional<TeamDTO> teamDTO = teamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teamDTO);
    }

    /**
     * {@code DELETE  /teams/:id} : delete the "id" team.
     *
     * @param id the id of the teamDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        log.debug("REST request to delete Team : {}", id);
        teamService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
