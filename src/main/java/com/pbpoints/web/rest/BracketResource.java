package com.pbpoints.web.rest;

import com.pbpoints.domain.Bracket;
import com.pbpoints.repository.BracketRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pbpoints.domain.Bracket}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BracketResource {

    private final Logger log = LoggerFactory.getLogger(BracketResource.class);

    private static final String ENTITY_NAME = "bracket";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BracketRepository bracketRepository;

    public BracketResource(BracketRepository bracketRepository) {
        this.bracketRepository = bracketRepository;
    }

    /**
     * {@code POST  /brackets} : Create a new bracket.
     *
     * @param bracket the bracket to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bracket, or with status {@code 400 (Bad Request)} if the bracket has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/brackets")
    public ResponseEntity<Bracket> createBracket(@Valid @RequestBody Bracket bracket) throws URISyntaxException {
        log.debug("REST request to save Bracket : {}", bracket);
        if (bracket.getId() != null) {
            throw new BadRequestAlertException("A new bracket cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bracket result = bracketRepository.save(bracket);
        return ResponseEntity
            .created(new URI("/api/brackets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /brackets/:id} : Updates an existing bracket.
     *
     * @param id the id of the bracket to save.
     * @param bracket the bracket to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bracket,
     * or with status {@code 400 (Bad Request)} if the bracket is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bracket couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/brackets/{id}")
    public ResponseEntity<Bracket> updateBracket(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Bracket bracket
    ) throws URISyntaxException {
        log.debug("REST request to update Bracket : {}, {}", id, bracket);
        if (bracket.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bracket.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bracketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Bracket result = bracketRepository.save(bracket);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bracket.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /brackets/:id} : Partial updates given fields of an existing bracket, field will ignore if it is null
     *
     * @param id the id of the bracket to save.
     * @param bracket the bracket to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bracket,
     * or with status {@code 400 (Bad Request)} if the bracket is not valid,
     * or with status {@code 404 (Not Found)} if the bracket is not found,
     * or with status {@code 500 (Internal Server Error)} if the bracket couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/brackets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Bracket> partialUpdateBracket(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Bracket bracket
    ) throws URISyntaxException {
        log.debug("REST request to partial update Bracket partially : {}, {}", id, bracket);
        if (bracket.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bracket.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bracketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Bracket> result = bracketRepository
            .findById(bracket.getId())
            .map(
                existingBracket -> {
                    if (bracket.getTeams() != null) {
                        existingBracket.setTeams(bracket.getTeams());
                    }
                    if (bracket.getTeams5A() != null) {
                        existingBracket.setTeams5A(bracket.getTeams5A());
                    }
                    if (bracket.getTeams5B() != null) {
                        existingBracket.setTeams5B(bracket.getTeams5B());
                    }
                    if (bracket.getTeams6A() != null) {
                        existingBracket.setTeams6A(bracket.getTeams6A());
                    }
                    if (bracket.getTeams6B() != null) {
                        existingBracket.setTeams6B(bracket.getTeams6B());
                    }

                    return existingBracket;
                }
            )
            .map(bracketRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bracket.getId().toString())
        );
    }

    /**
     * {@code GET  /brackets} : get all the brackets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of brackets in body.
     */
    @GetMapping("/brackets")
    public List<Bracket> getAllBrackets() {
        log.debug("REST request to get all Brackets");
        return bracketRepository.findAll();
    }

    /**
     * {@code GET  /brackets/:id} : get the "id" bracket.
     *
     * @param id the id of the bracket to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bracket, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/brackets/{id}")
    public ResponseEntity<Bracket> getBracket(@PathVariable Long id) {
        log.debug("REST request to get Bracket : {}", id);
        Optional<Bracket> bracket = bracketRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bracket);
    }

    /**
     * {@code DELETE  /brackets/:id} : delete the "id" bracket.
     *
     * @param id the id of the bracket to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/brackets/{id}")
    public ResponseEntity<Void> deleteBracket(@PathVariable Long id) {
        log.debug("REST request to delete Bracket : {}", id);
        bracketRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
