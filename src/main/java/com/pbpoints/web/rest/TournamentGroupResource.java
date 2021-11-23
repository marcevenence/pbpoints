package com.pbpoints.web.rest;

import com.pbpoints.repository.TournamentGroupRepository;
import com.pbpoints.service.TournamentGroupQueryService;
import com.pbpoints.service.TournamentGroupService;
import com.pbpoints.service.criteria.TournamentGroupCriteria;
import com.pbpoints.service.dto.TournamentGroupDTO;
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
 * REST controller for managing {@link com.pbpoints.domain.TournamentGroup}.
 */
@RestController
@RequestMapping("/api")
public class TournamentGroupResource {

    private final Logger log = LoggerFactory.getLogger(TournamentGroupResource.class);

    private static final String ENTITY_NAME = "tournamentGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TournamentGroupService tournamentGroupService;

    private final TournamentGroupRepository tournamentGroupRepository;

    private final TournamentGroupQueryService tournamentGroupQueryService;

    public TournamentGroupResource(
        TournamentGroupService tournamentGroupService,
        TournamentGroupRepository tournamentGroupRepository,
        TournamentGroupQueryService tournamentGroupQueryService
    ) {
        this.tournamentGroupService = tournamentGroupService;
        this.tournamentGroupRepository = tournamentGroupRepository;
        this.tournamentGroupQueryService = tournamentGroupQueryService;
    }

    /**
     * {@code POST  /tournament-groups} : Create a new tournamentGroup.
     *
     * @param tournamentGroupDTO the tournamentGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tournamentGroupDTO, or with status {@code 400 (Bad Request)} if the tournamentGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tournament-groups")
    public ResponseEntity<TournamentGroupDTO> createTournamentGroup(@Valid @RequestBody TournamentGroupDTO tournamentGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save TournamentGroup : {}", tournamentGroupDTO);
        if (tournamentGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new tournamentGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TournamentGroupDTO result = tournamentGroupService.save(tournamentGroupDTO);
        return ResponseEntity
            .created(new URI("/api/tournament-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tournament-groups/:id} : Updates an existing tournamentGroup.
     *
     * @param id the id of the tournamentGroupDTO to save.
     * @param tournamentGroupDTO the tournamentGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tournamentGroupDTO,
     * or with status {@code 400 (Bad Request)} if the tournamentGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tournamentGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tournament-groups/{id}")
    public ResponseEntity<TournamentGroupDTO> updateTournamentGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TournamentGroupDTO tournamentGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TournamentGroup : {}, {}", id, tournamentGroupDTO);
        if (tournamentGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tournamentGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tournamentGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TournamentGroupDTO result = tournamentGroupService.save(tournamentGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tournamentGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tournament-groups/:id} : Partial updates given fields of an existing tournamentGroup, field will ignore if it is null
     *
     * @param id the id of the tournamentGroupDTO to save.
     * @param tournamentGroupDTO the tournamentGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tournamentGroupDTO,
     * or with status {@code 400 (Bad Request)} if the tournamentGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tournamentGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tournamentGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tournament-groups/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TournamentGroupDTO> partialUpdateTournamentGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TournamentGroupDTO tournamentGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TournamentGroup partially : {}, {}", id, tournamentGroupDTO);
        if (tournamentGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tournamentGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tournamentGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TournamentGroupDTO> result = tournamentGroupService.partialUpdate(tournamentGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tournamentGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tournament-groups} : get all the tournamentGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tournamentGroups in body.
     */
    @GetMapping("/tournament-groups")
    public ResponseEntity<List<TournamentGroupDTO>> getAllTournamentGroups(TournamentGroupCriteria criteria) {
        log.debug("REST request to get TournamentGroups by criteria: {}", criteria);
        List<TournamentGroupDTO> entityList = tournamentGroupQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /tournament-groups/count} : count all the tournamentGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tournament-groups/count")
    public ResponseEntity<Long> countTournamentGroups(TournamentGroupCriteria criteria) {
        log.debug("REST request to count TournamentGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(tournamentGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tournament-groups/:id} : get the "id" tournamentGroup.
     *
     * @param id the id of the tournamentGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tournamentGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tournament-groups/{id}")
    public ResponseEntity<TournamentGroupDTO> getTournamentGroup(@PathVariable Long id) {
        log.debug("REST request to get TournamentGroup : {}", id);
        Optional<TournamentGroupDTO> tournamentGroupDTO = tournamentGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tournamentGroupDTO);
    }

    /**
     * {@code DELETE  /tournament-groups/:id} : delete the "id" tournamentGroup.
     *
     * @param id the id of the tournamentGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tournament-groups/{id}")
    public ResponseEntity<Void> deleteTournamentGroup(@PathVariable Long id) {
        log.debug("REST request to delete TournamentGroup : {}", id);
        tournamentGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
