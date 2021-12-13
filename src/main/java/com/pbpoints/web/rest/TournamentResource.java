package com.pbpoints.web.rest;

import com.pbpoints.repository.TournamentRepository;
import com.pbpoints.service.TournamentQueryService;
import com.pbpoints.service.TournamentService;
import com.pbpoints.service.criteria.TournamentCriteria;
import com.pbpoints.service.dto.TournamentDTO;
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
 * REST controller for managing {@link com.pbpoints.domain.Tournament}.
 */
@RestController
@RequestMapping("/api")
public class TournamentResource {

    private final Logger log = LoggerFactory.getLogger(TournamentResource.class);

    private static final String ENTITY_NAME = "tournament";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TournamentService tournamentService;

    private final TournamentRepository tournamentRepository;

    private final TournamentQueryService tournamentQueryService;

    public TournamentResource(
        TournamentService tournamentService,
        TournamentRepository tournamentRepository,
        TournamentQueryService tournamentQueryService
    ) {
        this.tournamentService = tournamentService;
        this.tournamentRepository = tournamentRepository;
        this.tournamentQueryService = tournamentQueryService;
    }

    /**
     * {@code POST  /tournaments} : Create a new tournament.
     *
     * @param tournamentDTO the tournamentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tournamentDTO, or with status {@code 400 (Bad Request)} if the tournament has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tournaments")
    public ResponseEntity<TournamentDTO> createTournament(@Valid @RequestBody TournamentDTO tournamentDTO) throws URISyntaxException {
        log.debug("REST request to save Tournament : {}", tournamentDTO);
        if (tournamentDTO.getId() != null) {
            throw new BadRequestAlertException("A new tournament cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TournamentDTO result = tournamentService.save(tournamentDTO);
        return ResponseEntity
            .created(new URI("/api/tournaments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getName()))
            .body(result);
    }

    /**
     * {@code PUT  /tournaments} : Updates an existing tournament.
     *
     * @param tournamentDTO the tournamentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tournamentDTO,
     * or with status {@code 400 (Bad Request)} if the tournamentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tournamentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tournaments")
    public ResponseEntity<TournamentDTO> updateTournament(@Valid @RequestBody TournamentDTO tournamentDTO) throws URISyntaxException {
        log.debug("REST request to update Tournament : {}", tournamentDTO);
        if (tournamentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TournamentDTO result = tournamentService.save(tournamentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tournamentDTO.getName()))
            .body(result);
    }

    /**
     * {@code PATCH  /tournaments/:id} : Partial updates given fields of an existing tournament, field will ignore if it is null
     *
     * @param id the id of the tournamentDTO to save.
     * @param tournamentDTO the tournamentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tournamentDTO,
     * or with status {@code 400 (Bad Request)} if the tournamentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tournamentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tournamentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tournaments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TournamentDTO> partialUpdateTournament(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TournamentDTO tournamentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tournament partially : {}, {}", id, tournamentDTO);
        if (tournamentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tournamentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tournamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TournamentDTO> result = tournamentService.partialUpdate(tournamentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tournamentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tournaments} : get all the tournaments.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tournaments in body.
     */
    @GetMapping("/tournaments")
    public ResponseEntity<List<TournamentDTO>> getAllTournaments(TournamentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Tournaments by criteria: {}", criteria);
        Page<TournamentDTO> page = tournamentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tournaments/count} : count all the tournaments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tournaments/count")
    public ResponseEntity<Long> countTournaments(TournamentCriteria criteria) {
        log.debug("REST request to count Tournaments by criteria: {}", criteria);
        return ResponseEntity.ok().body(tournamentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tournaments/:id} : get the "id" tournament.
     *
     * @param id the id of the tournamentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tournamentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tournaments/{id}")
    public ResponseEntity<TournamentDTO> getTournament(@PathVariable Long id) {
        log.debug("REST request to get Tournament : {}", id);
        Optional<TournamentDTO> tournamentDTO = tournamentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tournamentDTO);
    }

    /**
     * {@code DELETE  /tournaments/:id} : delete the "id" tournament.
     *
     * @param id the id of the tournamentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tournaments/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        log.debug("REST request to delete Tournament : {}", id);
        tournamentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
