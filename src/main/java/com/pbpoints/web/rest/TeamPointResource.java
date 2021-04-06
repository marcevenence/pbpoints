package com.pbpoints.web.rest;

import com.pbpoints.service.TeamPointQueryService;
import com.pbpoints.service.TeamPointService;
import com.pbpoints.service.dto.TeamPointCriteria;
import com.pbpoints.service.dto.TeamPointDTO;
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
 * REST controller for managing {@link com.pbpoints.domain.TeamPoint}.
 */
@RestController
@RequestMapping("/api")
public class TeamPointResource {

    private final Logger log = LoggerFactory.getLogger(TeamPointResource.class);

    private static final String ENTITY_NAME = "teamPoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeamPointService teamPointService;

    private final TeamPointQueryService teamPointQueryService;

    public TeamPointResource(TeamPointService teamPointService, TeamPointQueryService teamPointQueryService) {
        this.teamPointService = teamPointService;
        this.teamPointQueryService = teamPointQueryService;
    }

    /**
     * {@code POST  /team-points} : Create a new teamPoint.
     *
     * @param teamPointDTO the teamPointDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teamPointDTO, or with status {@code 400 (Bad Request)} if the teamPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/team-points")
    public ResponseEntity<TeamPointDTO> createTeamPoint(@Valid @RequestBody TeamPointDTO teamPointDTO) throws URISyntaxException {
        log.debug("REST request to save TeamPoint : {}", teamPointDTO);
        if (teamPointDTO.getId() != null) {
            throw new BadRequestAlertException("A new teamPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeamPointDTO result = teamPointService.save(teamPointDTO);
        return ResponseEntity
            .created(new URI("/api/team-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /team-points} : Updates an existing teamPoint.
     *
     * @param teamPointDTO the teamPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teamPointDTO,
     * or with status {@code 400 (Bad Request)} if the teamPointDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teamPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/team-points")
    public ResponseEntity<TeamPointDTO> updateTeamPoint(@Valid @RequestBody TeamPointDTO teamPointDTO) throws URISyntaxException {
        log.debug("REST request to update TeamPoint : {}", teamPointDTO);
        if (teamPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TeamPointDTO result = teamPointService.save(teamPointDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamPointDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /team-points} : get all the teamPoints.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teamPoints in body.
     */
    @GetMapping("/team-points")
    public ResponseEntity<List<TeamPointDTO>> getAllTeamPoints(TeamPointCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TeamPoints by criteria: {}", criteria);
        Page<TeamPointDTO> page = teamPointQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /team-points/count} : count all the teamPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/team-points/count")
    public ResponseEntity<Long> countTeamPoints(TeamPointCriteria criteria) {
        log.debug("REST request to count TeamPoints by criteria: {}", criteria);
        return ResponseEntity.ok().body(teamPointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /team-points/:id} : get the "id" teamPoint.
     *
     * @param id the id of the teamPointDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teamPointDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/team-points/{id}")
    public ResponseEntity<TeamPointDTO> getTeamPoint(@PathVariable Long id) {
        log.debug("REST request to get TeamPoint : {}", id);
        Optional<TeamPointDTO> teamPointDTO = teamPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teamPointDTO);
    }

    /**
     * {@code DELETE  /team-points/:id} : delete the "id" teamPoint.
     *
     * @param id the id of the teamPointDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/team-points/{id}")
    public ResponseEntity<Void> deleteTeamPoint(@PathVariable Long id) {
        log.debug("REST request to delete TeamPoint : {}", id);
        teamPointService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
