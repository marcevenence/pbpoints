package com.pbpoints.service;

import com.pbpoints.domain.EventCategory;
import com.pbpoints.repository.EventCategoryRepository;
import com.pbpoints.service.dto.EventCategoryDTO;
import com.pbpoints.service.mapper.EventCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EventCategory}.
 */
@Service
@Transactional
public class EventCategoryService {

    private final Logger log = LoggerFactory.getLogger(EventCategoryService.class);

    private final EventCategoryRepository eventCategoryRepository;

    private final EventCategoryMapper eventCategoryMapper;

    public EventCategoryService(EventCategoryRepository eventCategoryRepository, EventCategoryMapper eventCategoryMapper) {
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventCategoryMapper = eventCategoryMapper;
    }

    /**
     * Save a eventCategory.
     *
     * @param eventCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public EventCategoryDTO save(EventCategoryDTO eventCategoryDTO) {
        log.debug("Request to save EventCategory : {}", eventCategoryDTO);
        EventCategory eventCategory = eventCategoryMapper.toEntity(eventCategoryDTO);
        eventCategory = eventCategoryRepository.save(eventCategory);
        return eventCategoryMapper.toDto(eventCategory);
    }

    /**
     * Partially update a eventCategory.
     *
     * @param eventCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EventCategoryDTO> partialUpdate(EventCategoryDTO eventCategoryDTO) {
        log.debug("Request to partially update EventCategory : {}", eventCategoryDTO);

        return eventCategoryRepository
            .findById(eventCategoryDTO.getId())
            .map(
                existingEventCategory -> {
                    eventCategoryMapper.partialUpdate(existingEventCategory, eventCategoryDTO);
                    return existingEventCategory;
                }
            )
            .map(eventCategoryRepository::save)
            .map(eventCategoryMapper::toDto);
    }

    /**
     * Get all the eventCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EventCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventCategories");
        return eventCategoryRepository.findAll(pageable).map(eventCategoryMapper::toDto);
    }

    /**
     * Get one eventCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventCategoryDTO> findOne(Long id) {
        log.debug("Request to get EventCategory : {}", id);
        return eventCategoryRepository.findById(id).map(eventCategoryMapper::toDto);
    }

    /**
     * Delete the eventCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EventCategory : {}", id);
        eventCategoryRepository.deleteById(id);
    }
}
