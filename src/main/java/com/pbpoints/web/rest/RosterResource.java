package com.pbpoints.web.rest;

import com.pbpoints.repository.RosterRepository;
import com.pbpoints.service.RosterQueryService;
import com.pbpoints.service.RosterService;
import com.pbpoints.service.criteria.RosterCriteria;
import com.pbpoints.service.dto.RosterDTO;
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

    private final RosterRepository rosterRepository;

    private final RosterQueryService rosterQueryService;

    public RosterResource(RosterService rosterService, RosterRepository rosterRepository, RosterQueryService rosterQueryService) {
        this.rosterService = rosterService;
        this.rosterRepository = rosterRepository;
        this.rosterQueryService = rosterQueryService;
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
        RosterDTO result = rosterService.save(rosterDTO);
        return ResponseEntity
            .created(new URI("/api/rosters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rosters/:id} : Updates an existing roster.
     *
     * @param id the id of the rosterDTO to save.
     * @param rosterDTO the rosterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rosterDTO,
     * or with status {@code 400 (Bad Request)} if the rosterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rosterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rosters/{id}")
    public ResponseEntity<RosterDTO> updateRoster(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RosterDTO rosterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Roster : {}, {}", id, rosterDTO);
        if (rosterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rosterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rosterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RosterDTO result = rosterService.save(rosterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rosterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rosters/:id} : Partial updates given fields of an existing roster, field will ignore if it is null
     *
     * @param id the id of the rosterDTO to save.
     * @param rosterDTO the rosterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rosterDTO,
     * or with status {@code 400 (Bad Request)} if the rosterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rosterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rosterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rosters/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RosterDTO> partialUpdateRoster(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RosterDTO rosterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Roster partially : {}, {}", id, rosterDTO);
        if (rosterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rosterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rosterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RosterDTO> result = rosterService.partialUpdate(rosterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rosterDTO.getId().toString())
        );
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
}
