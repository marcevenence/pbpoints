package com.pbpoints.web.rest;

import com.pbpoints.repository.TeamCategoryPointRepository;
import com.pbpoints.service.TeamCategoryPointQueryService;
import com.pbpoints.service.TeamCategoryPointService;
import com.pbpoints.service.criteria.TeamCategoryPointCriteria;
import com.pbpoints.service.dto.TeamCategoryPointDTO;
import com.pbpoints.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pbpoints.domain.TeamCategoryPoint}.
 */
@RestController
@RequestMapping("/api")
public class TeamCategoryPointResource {

    private final Logger log = LoggerFactory.getLogger(TeamCategoryPointResource.class);

    private static final String ENTITY_NAME = "teamCategoryPoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeamCategoryPointService teamCategoryPointService;

    private final TeamCategoryPointRepository teamCategoryPointRepository;

    private final TeamCategoryPointQueryService teamCategoryPointQueryService;

    public TeamCategoryPointResource(
        TeamCategoryPointService teamCategoryPointService,
        TeamCategoryPointRepository teamCategoryPointRepository,
        TeamCategoryPointQueryService teamCategoryPointQueryService
    ) {
        this.teamCategoryPointService = teamCategoryPointService;
        this.teamCategoryPointRepository = teamCategoryPointRepository;
        this.teamCategoryPointQueryService = teamCategoryPointQueryService;
    }

    /**
     * {@code POST  /team-category-points} : Create a new teamCategoryPoint.
     *
     * @param teamCategoryPointDTO the teamCategoryPointDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teamCategoryPointDTO, or with status {@code 400 (Bad Request)} if the teamCategoryPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/team-category-points")
    public ResponseEntity<TeamCategoryPointDTO> createTeamCategoryPoint(@RequestBody TeamCategoryPointDTO teamCategoryPointDTO)
        throws URISyntaxException {
        log.debug("REST request to save TeamCategoryPoint : {}", teamCategoryPointDTO);
        if (teamCategoryPointDTO.getId() != null) {
            throw new BadRequestAlertException("A new teamCategoryPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeamCategoryPointDTO result = teamCategoryPointService.save(teamCategoryPointDTO);
        return ResponseEntity
            .created(new URI("/api/team-category-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /team-category-points/:id} : Updates an existing teamCategoryPoint.
     *
     * @param id the id of the teamCategoryPointDTO to save.
     * @param teamCategoryPointDTO the teamCategoryPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teamCategoryPointDTO,
     * or with status {@code 400 (Bad Request)} if the teamCategoryPointDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teamCategoryPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/team-category-points/{id}")
    public ResponseEntity<TeamCategoryPointDTO> updateTeamCategoryPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TeamCategoryPointDTO teamCategoryPointDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TeamCategoryPoint : {}, {}", id, teamCategoryPointDTO);
        if (teamCategoryPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teamCategoryPointDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teamCategoryPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TeamCategoryPointDTO result = teamCategoryPointService.save(teamCategoryPointDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamCategoryPointDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /team-category-points/:id} : Partial updates given fields of an existing teamCategoryPoint, field will ignore if it is null
     *
     * @param id the id of the teamCategoryPointDTO to save.
     * @param teamCategoryPointDTO the teamCategoryPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teamCategoryPointDTO,
     * or with status {@code 400 (Bad Request)} if the teamCategoryPointDTO is not valid,
     * or with status {@code 404 (Not Found)} if the teamCategoryPointDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the teamCategoryPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/team-category-points/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TeamCategoryPointDTO> partialUpdateTeamCategoryPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TeamCategoryPointDTO teamCategoryPointDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TeamCategoryPoint partially : {}, {}", id, teamCategoryPointDTO);
        if (teamCategoryPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teamCategoryPointDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teamCategoryPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TeamCategoryPointDTO> result = teamCategoryPointService.partialUpdate(teamCategoryPointDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamCategoryPointDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /team-category-points} : get all the teamCategoryPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teamCategoryPoints in body.
     */
    @GetMapping("/team-category-points")
    public ResponseEntity<List<TeamCategoryPointDTO>> getAllTeamCategoryPoints(TeamCategoryPointCriteria criteria) {
        log.debug("REST request to get TeamCategoryPoints by criteria: {}", criteria);
        List<TeamCategoryPointDTO> entityList = teamCategoryPointQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /team-category-points/count} : count all the teamCategoryPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/team-category-points/count")
    public ResponseEntity<Long> countTeamCategoryPoints(TeamCategoryPointCriteria criteria) {
        log.debug("REST request to count TeamCategoryPoints by criteria: {}", criteria);
        return ResponseEntity.ok().body(teamCategoryPointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /team-category-points/:id} : get the "id" teamCategoryPoint.
     *
     * @param id the id of the teamCategoryPointDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teamCategoryPointDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/team-category-points/{id}")
    public ResponseEntity<TeamCategoryPointDTO> getTeamCategoryPoint(@PathVariable Long id) {
        log.debug("REST request to get TeamCategoryPoint : {}", id);
        Optional<TeamCategoryPointDTO> teamCategoryPointDTO = teamCategoryPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teamCategoryPointDTO);
    }

    /**
     * {@code DELETE  /team-category-points/:id} : delete the "id" teamCategoryPoint.
     *
     * @param id the id of the teamCategoryPointDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/team-category-points/{id}")
    public ResponseEntity<Void> deleteTeamCategoryPoint(@PathVariable Long id) {
        log.debug("REST request to delete TeamCategoryPoint : {}", id);
        teamCategoryPointService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
