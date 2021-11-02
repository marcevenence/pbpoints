package com.pbpoints.web.rest;

import com.pbpoints.domain.Event;
import com.pbpoints.domain.enumeration.Status;
import com.pbpoints.service.EventQueryService;
import com.pbpoints.service.EventService;
import com.pbpoints.service.dto.EventCriteria;
import com.pbpoints.service.dto.EventDTO;
import com.pbpoints.service.mapper.EventMapper;
import com.pbpoints.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pbpoints.domain.Event}.
 */
@RestController
@RequestMapping("/api")
public class EventResource {

    private static final String ENTITY_NAME = "event";
    private final Logger log = LoggerFactory.getLogger(EventResource.class);
    private final EventService eventService;
    private final EventQueryService eventQueryService;
    private final EventMapper eventMapper;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public EventResource(EventService eventService, EventQueryService eventQueryService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventQueryService = eventQueryService;
        this.eventMapper = eventMapper;
    }

    /**
     * {@code POST  /events} : Create a new event.
     *
     * @param eventDTO the eventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventDTO, or with status {@code 400 (Bad Request)} if the event has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/events")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) throws URISyntaxException {
        log.debug("REST request to save Event : {}", eventDTO);
        if (eventDTO.getId() != null) {
            throw new BadRequestAlertException("A new event cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventDTO result = eventService.save(eventDTO);
        return ResponseEntity
            .created(new URI("/api/events/" + result.getId()))
            //.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getName()))
            .body(result);
    }

    /**
     * {@code PUT  /events} : Updates an existing event.
     *
     * @param eventDTO the eventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventDTO,
     * or with status {@code 400 (Bad Request)} if the eventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/events")
    public ResponseEntity<EventDTO> updateEvent(@RequestBody EventDTO eventDTO) throws URISyntaxException {
        log.debug("REST request to update Event : {}", eventDTO);
        if (eventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EventDTO result = eventService.save(eventDTO);
        return ResponseEntity
            .ok()
            //.headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventDTO.getId().toString()))
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventDTO.getName()))
            .body(result);
    }

    /**
     * {@code GET  /events} : get all the events.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of events in body.
     */
    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getAllEvents(EventCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Events by criteria: {}", criteria);
        Page<EventDTO> page = eventQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /events/count} : count all the events.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/events/count")
    public ResponseEntity<Long> countEvents(EventCriteria criteria) {
        log.debug("REST request to count Events by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /events/:id} : get the "id" event.
     *
     * @param id the id of the eventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/events/{id}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable Long id) {
        log.debug("REST request to get Event : {}", id);
        Optional<EventDTO> eventDTO = eventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventDTO);
    }

    /**
     * {@code DELETE  /events/:id} : delete the "id" event.
     *
     * @param id the id of the eventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        log.debug("REST request to delete Event : {}", id);
        eventService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/events/generateXML/{id}")
    public ResponseEntity<String> createEventXML(@PathVariable Long id)
        throws URISyntaxException, ParserConfigurationException, TransformerConfigurationException, IOException {
        log.debug("REST request to generar a fixture from: {}", id);
        if (id == null) {
            throw new BadRequestAlertException("A event cannot have an empty ID", ENTITY_NAME, "idexists");
        }
        try {
            Optional<EventDTO> eventDTO = eventService.findOne(id);
            if (eventDTO.isPresent()) {
                Event event = eventMapper.toEntity(eventDTO.get());
                log.debug("Event: {}" + event);
                if (event.getStatus() == Status.valueOf("CANCEL")) throw new BadRequestAlertException(
                    "Event Cancelled",
                    ENTITY_NAME,
                    "EventCancelled"
                );
                if (event.getEndInscriptionDate().isAfter(LocalDate.now())) throw new BadRequestAlertException(
                    "Subscription Date no End",
                    ENTITY_NAME,
                    "inscrNotFinish"
                );
                if (!eventService.hasCategories(event)) throw new BadRequestAlertException(
                    "No EventCategories Found",
                    ENTITY_NAME,
                    "noEventCategoriesFound"
                );
                if (!eventService.hasGames(event)) throw new BadRequestAlertException("No Games Found", ENTITY_NAME, "noGamesFound");
                eventService.generarXML(event);
                return ResponseEntity.ok().body("Archivo generado con éxito");
            } else {
                throw new BadRequestAlertException("Event Not Found", ENTITY_NAME, "eventNotFound");
            }
        } catch (NoResultException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/events/importXML", consumes = { "multipart/form-data" })
    public ResponseEntity<?> write(@RequestParam(value = "file") MultipartFile multipartFile) throws Exception {
        log.debug("REST request to Import file: {}", multipartFile);
        return ResponseEntity.ok(eventService.submitXML(multipartFile));
    }

    @GetMapping("/events/generatePDF/{id}")
    public ResponseEntity<Void> generatePdf(@PathVariable Long id) throws IOException, URISyntaxException {
        log.debug("REST request to Generate a Pdf File from: {}", id);
        if (id == null) {
            throw new BadRequestAlertException("A event cannot have an empty ID", ENTITY_NAME, "idexists");
        }
        Optional<EventDTO> eventDTO = eventService.findOne(id);
        if (eventDTO.isPresent()) {
            Event event = eventMapper.toEntity(eventDTO.get());
            log.debug("Event: {}" + event);
            if (event.getStatus() == Status.valueOf("CANCEL")) throw new BadRequestAlertException(
                "Event Cancelled",
                ENTITY_NAME,
                "EventCancelled"
            );
            if (event.getEndInscriptionDate().isAfter(LocalDate.now())) throw new BadRequestAlertException(
                "Subscription Date no End",
                ENTITY_NAME,
                "inscrNotFinish"
            );
            if (!eventService.hasCategories(event)) throw new BadRequestAlertException(
                "No EventCategories Found",
                ENTITY_NAME,
                "noEventCategoriesFound"
            );
            if (!eventService.hasGames(event)) throw new BadRequestAlertException("No Games Found", ENTITY_NAME, "noGamesFound");
            eventService.generatePdf(event);
            return ResponseEntity.noContent().build();
        } else throw new BadRequestAlertException("Event Not Found", ENTITY_NAME, "eventNotFound");
    }
}
