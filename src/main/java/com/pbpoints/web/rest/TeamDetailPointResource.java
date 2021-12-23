package com.pbpoints.web.rest;

import com.pbpoints.repository.TeamDetailPointRepository;
import com.pbpoints.service.TeamDetailPointQueryService;
import com.pbpoints.service.TeamDetailPointService;
import com.pbpoints.service.criteria.TeamDetailPointCriteria;
import com.pbpoints.service.dto.TeamDetailPointDTO;
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
 * REST controller for managing {@link com.pbpoints.domain.TeamDetailPoint}.
 */
@RestController
@RequestMapping("/api")
public class TeamDetailPointResource {

    private final Logger log = LoggerFactory.getLogger(TeamDetailPointResource.class);

    private static final String ENTITY_NAME = "teamDetailPoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeamDetailPointService teamDetailPointService;

    private final TeamDetailPointRepository teamDetailPointRepository;

    private final TeamDetailPointQueryService teamDetailPointQueryService;

    public TeamDetailPointResource(
        TeamDetailPointService teamDetailPointService,
        TeamDetailPointRepository teamDetailPointRepository,
        TeamDetailPointQueryService teamDetailPointQueryService
    ) {
        this.teamDetailPointService = teamDetailPointService;
        this.teamDetailPointRepository = teamDetailPointRepository;
        this.teamDetailPointQueryService = teamDetailPointQueryService;
    }

    /**
     * {@code POST  /team-detail-points} : Create a new teamDetailPoint.
     *
     * @param teamDetailPointDTO the teamDetailPointDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teamDetailPointDTO, or with status {@code 400 (Bad Request)} if the teamDetailPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/team-detail-points")
    public ResponseEntity<TeamDetailPointDTO> createTeamDetailPoint(@Valid @RequestBody TeamDetailPointDTO teamDetailPointDTO)
        throws URISyntaxException {
        log.debug("REST request to save TeamDetailPoint : {}", teamDetailPointDTO);
        if (teamDetailPointDTO.getId() != null) {
            throw new BadRequestAlertException("A new teamDetailPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeamDetailPointDTO result = teamDetailPointService.save(teamDetailPointDTO);
        return ResponseEntity
            .created(new URI("/api/team-detail-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /team-detail-points/:id} : Updates an existing teamDetailPoint.
     *
     * @param id the id of the teamDetailPointDTO to save.
     * @param teamDetailPointDTO the teamDetailPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teamDetailPointDTO,
     * or with status {@code 400 (Bad Request)} if the teamDetailPointDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teamDetailPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/team-detail-points/{id}")
    public ResponseEntity<TeamDetailPointDTO> updateTeamDetailPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TeamDetailPointDTO teamDetailPointDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TeamDetailPoint : {}, {}", id, teamDetailPointDTO);
        if (teamDetailPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teamDetailPointDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teamDetailPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TeamDetailPointDTO result = teamDetailPointService.save(teamDetailPointDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamDetailPointDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /team-detail-points/:id} : Partial updates given fields of an existing teamDetailPoint, field will ignore if it is null
     *
     * @param id the id of the teamDetailPointDTO to save.
     * @param teamDetailPointDTO the teamDetailPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teamDetailPointDTO,
     * or with status {@code 400 (Bad Request)} if the teamDetailPointDTO is not valid,
     * or with status {@code 404 (Not Found)} if the teamDetailPointDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the teamDetailPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/team-detail-points/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TeamDetailPointDTO> partialUpdateTeamDetailPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TeamDetailPointDTO teamDetailPointDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TeamDetailPoint partially : {}, {}", id, teamDetailPointDTO);
        if (teamDetailPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teamDetailPointDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teamDetailPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TeamDetailPointDTO> result = teamDetailPointService.partialUpdate(teamDetailPointDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamDetailPointDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /team-detail-points} : get all the teamDetailPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teamDetailPoints in body.
     */
    @GetMapping("/team-detail-points")
    public ResponseEntity<List<TeamDetailPointDTO>> getAllTeamDetailPoints(TeamDetailPointCriteria criteria) {
        log.debug("REST request to get TeamDetailPoints by criteria: {}", criteria);
        List<TeamDetailPointDTO> entityList = teamDetailPointQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /team-detail-points/count} : count all the teamDetailPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/team-detail-points/count")
    public ResponseEntity<Long> countTeamDetailPoints(TeamDetailPointCriteria criteria) {
        log.debug("REST request to count TeamDetailPoints by criteria: {}", criteria);
        return ResponseEntity.ok().body(teamDetailPointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /team-detail-points/:id} : get the "id" teamDetailPoint.
     *
     * @param id the id of the teamDetailPointDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teamDetailPointDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/team-detail-points/{id}")
    public ResponseEntity<TeamDetailPointDTO> getTeamDetailPoint(@PathVariable Long id) {
        log.debug("REST request to get TeamDetailPoint : {}", id);
        Optional<TeamDetailPointDTO> teamDetailPointDTO = teamDetailPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teamDetailPointDTO);
    }

    /**
     * {@code DELETE  /team-detail-points/:id} : delete the "id" teamDetailPoint.
     *
     * @param id the id of the teamDetailPointDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/team-detail-points/{id}")
    public ResponseEntity<Void> deleteTeamDetailPoint(@PathVariable Long id) {
        log.debug("REST request to delete TeamDetailPoint : {}", id);
        teamDetailPointService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
