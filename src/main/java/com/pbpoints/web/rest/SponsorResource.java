package com.pbpoints.web.rest;

import com.pbpoints.repository.SponsorRepository;
import com.pbpoints.service.SponsorQueryService;
import com.pbpoints.service.SponsorService;
import com.pbpoints.service.criteria.SponsorCriteria;
import com.pbpoints.service.dto.SponsorDTO;
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
 * REST controller for managing {@link com.pbpoints.domain.Sponsor}.
 */
@RestController
@RequestMapping("/api")
public class SponsorResource {

    private final Logger log = LoggerFactory.getLogger(SponsorResource.class);

    private static final String ENTITY_NAME = "sponsor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SponsorService sponsorService;

    private final SponsorRepository sponsorRepository;

    private final SponsorQueryService sponsorQueryService;

    public SponsorResource(SponsorService sponsorService, SponsorRepository sponsorRepository, SponsorQueryService sponsorQueryService) {
        this.sponsorService = sponsorService;
        this.sponsorRepository = sponsorRepository;
        this.sponsorQueryService = sponsorQueryService;
    }

    /**
     * {@code POST  /sponsors} : Create a new sponsor.
     *
     * @param sponsorDTO the sponsorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sponsorDTO, or with status {@code 400 (Bad Request)} if the sponsor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sponsors")
    public ResponseEntity<SponsorDTO> createSponsor(@Valid @RequestBody SponsorDTO sponsorDTO) throws URISyntaxException {
        log.debug("REST request to save Sponsor : {}", sponsorDTO);
        if (sponsorDTO.getId() != null) {
            throw new BadRequestAlertException("A new sponsor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SponsorDTO result = sponsorService.save(sponsorDTO);
        return ResponseEntity
            .created(new URI("/api/sponsors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sponsors/:id} : Updates an existing sponsor.
     *
     * @param id the id of the sponsorDTO to save.
     * @param sponsorDTO the sponsorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sponsorDTO,
     * or with status {@code 400 (Bad Request)} if the sponsorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sponsorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sponsors/{id}")
    public ResponseEntity<SponsorDTO> updateSponsor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SponsorDTO sponsorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Sponsor : {}, {}", id, sponsorDTO);
        if (sponsorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sponsorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sponsorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SponsorDTO result = sponsorService.save(sponsorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sponsorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sponsors/:id} : Partial updates given fields of an existing sponsor, field will ignore if it is null
     *
     * @param id the id of the sponsorDTO to save.
     * @param sponsorDTO the sponsorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sponsorDTO,
     * or with status {@code 400 (Bad Request)} if the sponsorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sponsorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sponsorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sponsors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SponsorDTO> partialUpdateSponsor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SponsorDTO sponsorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sponsor partially : {}, {}", id, sponsorDTO);
        if (sponsorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sponsorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sponsorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SponsorDTO> result = sponsorService.partialUpdate(sponsorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sponsorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sponsors} : get all the sponsors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sponsors in body.
     */
    @GetMapping("/sponsors")
    public ResponseEntity<List<SponsorDTO>> getAllSponsors(SponsorCriteria criteria) {
        log.debug("REST request to get Sponsors by criteria: {}", criteria);
        List<SponsorDTO> entityList = sponsorQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /sponsors/count} : count all the sponsors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sponsors/count")
    public ResponseEntity<Long> countSponsors(SponsorCriteria criteria) {
        log.debug("REST request to count Sponsors by criteria: {}", criteria);
        return ResponseEntity.ok().body(sponsorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sponsors/:id} : get the "id" sponsor.
     *
     * @param id the id of the sponsorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sponsorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sponsors/{id}")
    public ResponseEntity<SponsorDTO> getSponsor(@PathVariable Long id) {
        log.debug("REST request to get Sponsor : {}", id);
        Optional<SponsorDTO> sponsorDTO = sponsorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sponsorDTO);
    }

    /**
     * {@code DELETE  /sponsors/:id} : delete the "id" sponsor.
     *
     * @param id the id of the sponsorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sponsors/{id}")
    public ResponseEntity<Void> deleteSponsor(@PathVariable Long id) {
        log.debug("REST request to delete Sponsor : {}", id);
        sponsorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
