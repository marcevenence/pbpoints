package com.pbpoints.web.rest;

import com.pbpoints.repository.SeasonRepository;
import com.pbpoints.service.SeasonQueryService;
import com.pbpoints.service.SeasonService;
import com.pbpoints.service.criteria.SeasonCriteria;
import com.pbpoints.service.dto.SeasonDTO;
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
 * REST controller for managing {@link com.pbpoints.domain.Season}.
 */
@RestController
@RequestMapping("/api")
public class SeasonResource {

    private final Logger log = LoggerFactory.getLogger(SeasonResource.class);

    private static final String ENTITY_NAME = "season";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SeasonService seasonService;

    private final SeasonRepository seasonRepository;

    private final SeasonQueryService seasonQueryService;

    public SeasonResource(SeasonService seasonService, SeasonRepository seasonRepository, SeasonQueryService seasonQueryService) {
        this.seasonService = seasonService;
        this.seasonRepository = seasonRepository;
        this.seasonQueryService = seasonQueryService;
    }

    /**
     * {@code POST  /seasons} : Create a new season.
     *
     * @param seasonDTO the seasonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new seasonDTO, or with status {@code 400 (Bad Request)} if the season has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/seasons")
    public ResponseEntity<SeasonDTO> createSeason(@Valid @RequestBody SeasonDTO seasonDTO) throws URISyntaxException {
        log.debug("REST request to save Season : {}", seasonDTO);
        if (seasonDTO.getId() != null) {
            throw new BadRequestAlertException("A new season cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SeasonDTO result = seasonService.save(seasonDTO);
        return ResponseEntity
            .created(new URI("/api/seasons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /seasons/:id} : Updates an existing season.
     *
     * @param id the id of the seasonDTO to save.
     * @param seasonDTO the seasonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seasonDTO,
     * or with status {@code 400 (Bad Request)} if the seasonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the seasonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/seasons/{id}")
    public ResponseEntity<SeasonDTO> updateSeason(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SeasonDTO seasonDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Season : {}, {}", id, seasonDTO);
        if (seasonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seasonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SeasonDTO result = seasonService.save(seasonDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, seasonDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /seasons/:id} : Partial updates given fields of an existing season, field will ignore if it is null
     *
     * @param id the id of the seasonDTO to save.
     * @param seasonDTO the seasonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seasonDTO,
     * or with status {@code 400 (Bad Request)} if the seasonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the seasonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the seasonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/seasons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SeasonDTO> partialUpdateSeason(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SeasonDTO seasonDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Season partially : {}, {}", id, seasonDTO);
        if (seasonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seasonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SeasonDTO> result = seasonService.partialUpdate(seasonDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, seasonDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /seasons} : get all the seasons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of seasons in body.
     */
    @GetMapping("/seasons")
    public ResponseEntity<List<SeasonDTO>> getAllSeasons(SeasonCriteria criteria) {
        log.debug("REST request to get Seasons by criteria: {}", criteria);
        List<SeasonDTO> entityList = seasonQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /seasons/count} : count all the seasons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/seasons/count")
    public ResponseEntity<Long> countSeasons(SeasonCriteria criteria) {
        log.debug("REST request to count Seasons by criteria: {}", criteria);
        return ResponseEntity.ok().body(seasonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /seasons/:id} : get the "id" season.
     *
     * @param id the id of the seasonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the seasonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/seasons/{id}")
    public ResponseEntity<SeasonDTO> getSeason(@PathVariable Long id) {
        log.debug("REST request to get Season : {}", id);
        Optional<SeasonDTO> seasonDTO = seasonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(seasonDTO);
    }

    /**
     * {@code DELETE  /seasons/:id} : delete the "id" season.
     *
     * @param id the id of the seasonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/seasons/{id}")
    public ResponseEntity<Void> deleteSeason(@PathVariable Long id) {
        log.debug("REST request to delete Season : {}", id);
        seasonService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
