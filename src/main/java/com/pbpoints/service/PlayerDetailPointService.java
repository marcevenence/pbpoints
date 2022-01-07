package com.pbpoints.service;

import com.pbpoints.service.dto.PlayerDetailPointDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.pbpoints.domain.PlayerDetailPoint}.
 */
public interface PlayerDetailPointService {
    /**
     * Save a playerDetailPoint.
     *
     * @param playerDetailPointDTO the entity to save.
     * @return the persisted entity.
     */
    PlayerDetailPointDTO save(PlayerDetailPointDTO playerDetailPointDTO);

    /**
     * Partially updates a playerDetailPoint.
     *
     * @param playerDetailPointDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlayerDetailPointDTO> partialUpdate(PlayerDetailPointDTO playerDetailPointDTO);

    /**
     * Get all the playerDetailPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlayerDetailPointDTO> findAll(Pageable pageable);

    /**
     * Get the "id" playerDetailPoint.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlayerDetailPointDTO> findOne(Long id);

    /**
     * Delete the "id" playerDetailPoint.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
