package com.pbpoints.web.rest;

import com.pbpoints.repository.SuspensionRepository;
import com.pbpoints.service.SuspensionQueryService;
import com.pbpoints.service.SuspensionService;
import com.pbpoints.service.criteria.SuspensionCriteria;
import com.pbpoints.service.dto.SuspensionDTO;
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
 * REST controller for managing {@link com.pbpoints.domain.Suspension}.
 */
@RestController
@RequestMapping("/api")
public class SuspensionResource {

    private final Logger log = LoggerFactory.getLogger(SuspensionResource.class);

    private static final String ENTITY_NAME = "suspension";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuspensionService suspensionService;

    private final SuspensionRepository suspensionRepository;

    private final SuspensionQueryService suspensionQueryService;

    public SuspensionResource(
        SuspensionService suspensionService,
        SuspensionRepository suspensionRepository,
        SuspensionQueryService suspensionQueryService
    ) {
        this.suspensionService = suspensionService;
        this.suspensionRepository = suspensionRepository;
        this.suspensionQueryService = suspensionQueryService;
    }

    /**
     * {@code POST  /suspensions} : Create a new suspension.
     *
     * @param suspensionDTO the suspensionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new suspensionDTO, or with status {@code 400 (Bad Request)} if the suspension has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/suspensions")
    public ResponseEntity<SuspensionDTO> createSuspension(@RequestBody SuspensionDTO suspensionDTO) throws URISyntaxException {
        log.debug("REST request to save Suspension : {}", suspensionDTO);
        if (suspensionDTO.getId() != null) {
            throw new BadRequestAlertException("A new suspension cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(suspensionDTO.getUser())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        SuspensionDTO result = suspensionService.save(suspensionDTO);
        return ResponseEntity
            .created(new URI("/api/suspensions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /suspensions/:id} : Updates an existing suspension.
     *
     * @param id the id of the suspensionDTO to save.
     * @param suspensionDTO the suspensionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suspensionDTO,
     * or with status {@code 400 (Bad Request)} if the suspensionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the suspensionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/suspensions/{id}")
    public ResponseEntity<SuspensionDTO> updateSuspension(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SuspensionDTO suspensionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Suspension : {}, {}", id, suspensionDTO);
        if (suspensionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, suspensionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!suspensionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SuspensionDTO result = suspensionService.save(suspensionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, suspensionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /suspensions/:id} : Partial updates given fields of an existing suspension, field will ignore if it is null
     *
     * @param id the id of the suspensionDTO to save.
     * @param suspensionDTO the suspensionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suspensionDTO,
     * or with status {@code 400 (Bad Request)} if the suspensionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the suspensionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the suspensionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/suspensions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SuspensionDTO> partialUpdateSuspension(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SuspensionDTO suspensionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Suspension partially : {}, {}", id, suspensionDTO);
        if (suspensionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, suspensionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!suspensionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SuspensionDTO> result = suspensionService.partialUpdate(suspensionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, suspensionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /suspensions} : get all the suspensions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suspensions in body.
     */
    @GetMapping("/suspensions")
    public ResponseEntity<List<SuspensionDTO>> getAllSuspensions(SuspensionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Suspensions by criteria: {}", criteria);
        Page<SuspensionDTO> page = suspensionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /suspensions/count} : count all the suspensions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/suspensions/count")
    public ResponseEntity<Long> countSuspensions(SuspensionCriteria criteria) {
        log.debug("REST request to count Suspensions by criteria: {}", criteria);
        return ResponseEntity.ok().body(suspensionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /suspensions/:id} : get the "id" suspension.
     *
     * @param id the id of the suspensionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the suspensionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/suspensions/{id}")
    public ResponseEntity<SuspensionDTO> getSuspension(@PathVariable Long id) {
        log.debug("REST request to get Suspension : {}", id);
        Optional<SuspensionDTO> suspensionDTO = suspensionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(suspensionDTO);
    }

    /**
     * {@code DELETE  /suspensions/:id} : delete the "id" suspension.
     *
     * @param id the id of the suspensionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/suspensions/{id}")
    public ResponseEntity<Void> deleteSuspension(@PathVariable Long id) {
        log.debug("REST request to delete Suspension : {}", id);
        suspensionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
