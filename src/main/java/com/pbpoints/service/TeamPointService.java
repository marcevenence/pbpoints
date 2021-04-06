package com.pbpoints.service;

import com.pbpoints.service.dto.TeamPointDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.pbpoints.domain.TeamPoint}.
 */
public interface TeamPointService {
    /**
     * Save a teamPoint.
     *
     * @param teamPointDTO the entity to save.
     * @return the persisted entity.
     */
    TeamPointDTO save(TeamPointDTO teamPointDTO);

    /**
     * Partially updates a teamPoint.
     *
     * @param teamPointDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TeamPointDTO> partialUpdate(TeamPointDTO teamPointDTO);

    /**
     * Get all the teamPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TeamPointDTO> findAll(Pageable pageable);

    /**
     * Get the "id" teamPoint.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TeamPointDTO> findOne(Long id);

    /**
     * Delete the "id" teamPoint.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
