package com.pbpoints.web.rest;

import com.pbpoints.domain.RosterEvent;
import com.pbpoints.service.RosterEventService;
import com.pbpoints.service.dto.RosterCriteria;
import com.pbpoints.service.dto.RosterDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing {@link com.pbpoints.domain.RosterEvent}.
 */
@RestController
@RequestMapping("/api")
public class RosterEventResource {

    private final Logger log = LoggerFactory.getLogger(RosterEventResource.class);

    private static final String ENTITY_NAME = "rosterEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RosterEventService rosterEventService;

    public RosterEventResource(RosterEventService rosterEventService) {
        this.rosterEventService = rosterEventService;
    }

    @GetMapping("/rosterEvent/{id}")
    public ResponseEntity<List<RosterEvent>> getAllRosters(@PathVariable Long id) {
        log.debug("REST request to get Rosters for an EventCategory by evCatId: {}", id);
        List<RosterEvent> rosters = rosterEventService.findByEvCatId(id);
        log.debug("rosters: {}", rosters);
        return ResponseEntity.ok().body(rosters);
    }
}
