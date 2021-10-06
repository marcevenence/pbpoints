package com.pbpoints.web.rest;

import com.pbpoints.repository.MainRosterRepository;
import com.pbpoints.service.MainRosterQueryService;
import com.pbpoints.service.MainRosterService;
import com.pbpoints.service.criteria.MainRosterCriteria;
import com.pbpoints.service.dto.MainRosterDTO;
import com.pbpoints.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.pbpoints.domain.MainRoster}.
 */
@RestController
@RequestMapping("/api")
public class MainRosterResource {

    private final Logger log = LoggerFactory.getLogger(MainRosterResource.class);

    private static final String ENTITY_NAME = "mainRoster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MainRosterService mainRosterService;

    private final MainRosterRepository mainRosterRepository;

    private final MainRosterQueryService mainRosterQueryService;

    public MainRosterResource(
        MainRosterService mainRosterService,
        MainRosterRepository mainRosterRepository,
        MainRosterQueryService mainRosterQueryService
    ) {
        this.mainRosterService = mainRosterService;
        this.mainRosterRepository = mainRosterRepository;
        this.mainRosterQueryService = mainRosterQueryService;
    }

    /**
     * {@code POST  /main-rosters} : Create a new mainRoster.
     *
     * @param mainRosterDTO the mainRosterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mainRosterDTO, or with status {@code 400 (Bad Request)} if the mainRoster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/main-rosters")
    public ResponseEntity<MainRosterDTO> createMainRoster(@RequestBody MainRosterDTO mainRosterDTO) throws URISyntaxException {
        log.debug("REST request to save MainRoster : {}", mainRosterDTO);
        if (mainRosterDTO.getId() != null) {
            throw new BadRequestAlertException("A new mainRoster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MainRosterDTO result = mainRosterService.save(mainRosterDTO);
        return ResponseEntity
            .created(new URI("/api/main-rosters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /main-rosters/:id} : Updates an existing mainRoster.
     *
     * @param id the id of the mainRosterDTO to save.
     * @param mainRosterDTO the mainRosterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mainRosterDTO,
     * or with status {@code 400 (Bad Request)} if the mainRosterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mainRosterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/main-rosters/{id}")
    public ResponseEntity<MainRosterDTO> updateMainRoster(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MainRosterDTO mainRosterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MainRoster : {}, {}", id, mainRosterDTO);
        if (mainRosterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mainRosterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mainRosterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MainRosterDTO result = mainRosterService.save(mainRosterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mainRosterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /main-rosters/:id} : Partial updates given fields of an existing mainRoster, field will ignore if it is null
     *
     * @param id the id of the mainRosterDTO to save.
     * @param mainRosterDTO the mainRosterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mainRosterDTO,
     * or with status {@code 400 (Bad Request)} if the mainRosterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mainRosterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mainRosterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/main-rosters/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MainRosterDTO> partialUpdateMainRoster(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MainRosterDTO mainRosterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MainRoster partially : {}, {}", id, mainRosterDTO);
        if (mainRosterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mainRosterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mainRosterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MainRosterDTO> result = mainRosterService.partialUpdate(mainRosterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mainRosterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /main-rosters} : get all the mainRosters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mainRosters in body.
     */
    @GetMapping("/main-rosters")
    public ResponseEntity<List<MainRosterDTO>> getAllMainRosters(MainRosterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MainRosters by criteria: {}", criteria);
        Page<MainRosterDTO> page = mainRosterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /main-rosters/count} : count all the mainRosters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/main-rosters/count")
    public ResponseEntity<Long> countMainRosters(MainRosterCriteria criteria) {
        log.debug("REST request to count MainRosters by criteria: {}", criteria);
        return ResponseEntity.ok().body(mainRosterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /main-rosters/:id} : get the "id" mainRoster.
     *
     * @param id the id of the mainRosterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mainRosterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/main-rosters/{id}")
    public ResponseEntity<MainRosterDTO> getMainRoster(@PathVariable Long id) {
        log.debug("REST request to get MainRoster : {}", id);
        Optional<MainRosterDTO> mainRosterDTO = mainRosterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mainRosterDTO);
    }

    /**
     * {@code DELETE  /main-rosters/:id} : delete the "id" mainRoster.
     *
     * @param id the id of the mainRosterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/main-rosters/{id}")
    public ResponseEntity<Void> deleteMainRoster(@PathVariable Long id) {
        log.debug("REST request to delete MainRoster : {}", id);
        mainRosterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
