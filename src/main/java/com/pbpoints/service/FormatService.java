package com.pbpoints.service;

import com.pbpoints.service.dto.FormatDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.pbpoints.domain.Format}.
 */
public interface FormatService {
    /**
     * Save a format.
     *
     * @param formatDTO the entity to save.
     * @return the persisted entity.
     */
    FormatDTO save(FormatDTO formatDTO);

    /**
     * Partially updates a format.
     *
     * @param formatDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormatDTO> partialUpdate(FormatDTO formatDTO);

    /**
     * Get all the formats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormatDTO> findAll(Pageable pageable);

    /**
     * Get the "id" format.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormatDTO> findOne(Long id);

    /**
     * Delete the "id" format.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
