package com.pbpoints.service;

import com.pbpoints.service.dto.SuspensionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.pbpoints.domain.Suspension}.
 */
public interface SuspensionService {
    /**
     * Save a suspension.
     *
     * @param suspensionDTO the entity to save.
     * @return the persisted entity.
     */
    SuspensionDTO save(SuspensionDTO suspensionDTO);

    /**
     * Partially updates a suspension.
     *
     * @param suspensionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SuspensionDTO> partialUpdate(SuspensionDTO suspensionDTO);

    /**
     * Get all the suspensions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SuspensionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" suspension.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SuspensionDTO> findOne(Long id);

    /**
     * Delete the "id" suspension.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
