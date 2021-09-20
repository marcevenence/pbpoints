package com.pbpoints.web.rest;

import com.pbpoints.domain.Bracket;
import com.pbpoints.service.scheduler.DataBackupService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Bracket}.
 */
@RestController
@RequestMapping("/api/data-backup")
public class DataBackupResource {

    private static final String ENTITY_NAME = "formula";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(DataBackupResource.class);

    private final DataBackupService dataBackupService;

    public DataBackupResource(DataBackupService dataBackupService) {
        this.dataBackupService = dataBackupService;
    }

    /**
     * {@code GET  /users} : get all the brackets.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of brackets in body.
     */
    @GetMapping("/users")
    public ResponseEntity<Void> getAllUsers() throws IOException {
        log.debug("REST request to get all Users");
        dataBackupService.exportUsers();
        return ResponseEntity.ok().headers(HeaderUtil.createAlert(applicationName, "true", ENTITY_NAME)).build();
    }
}
