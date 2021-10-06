package com.pbpoints.service;

import com.pbpoints.service.dto.MainRosterDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.pbpoints.domain.MainRoster}.
 */
public interface MainRosterService {
    /**
     * Save a mainRoster.
     *
     * @param mainRosterDTO the entity to save.
     * @return the persisted entity.
     */
    MainRosterDTO save(MainRosterDTO mainRosterDTO);

    /**
     * Partially updates a mainRoster.
     *
     * @param mainRosterDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MainRosterDTO> partialUpdate(MainRosterDTO mainRosterDTO);

    /**
     * Get all the mainRosters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MainRosterDTO> findAll(Pageable pageable);

    /**
     * Get the "id" mainRoster.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MainRosterDTO> findOne(Long id);

    /**
     * Delete the "id" mainRoster.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
