package com.pbpoints.service;

import com.pbpoints.domain.Roster;
import com.pbpoints.repository.RosterRepository;
import com.pbpoints.service.dto.RosterDTO;
import com.pbpoints.service.mapper.RosterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Roster}.
 */
@Service
@Transactional
public class RosterService {

    private final Logger log = LoggerFactory.getLogger(RosterService.class);

    private final RosterRepository rosterRepository;

    private final RosterMapper rosterMapper;

    public RosterService(RosterRepository rosterRepository, RosterMapper rosterMapper) {
        this.rosterRepository = rosterRepository;
        this.rosterMapper = rosterMapper;
    }

    /**
     * Save a roster.
     *
     * @param rosterDTO the entity to save.
     * @return the persisted entity.
     */
    public RosterDTO save(RosterDTO rosterDTO) {
        log.debug("Request to save Roster : {}", rosterDTO);
        Roster roster = rosterMapper.toEntity(rosterDTO);
        roster = rosterRepository.save(roster);
        return rosterMapper.toDto(roster);
    }

    /**
     * Partially update a roster.
     *
     * @param rosterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RosterDTO> partialUpdate(RosterDTO rosterDTO) {
        log.debug("Request to partially update Roster : {}", rosterDTO);

        return rosterRepository
            .findById(rosterDTO.getId())
            .map(
                existingRoster -> {
                    rosterMapper.partialUpdate(existingRoster, rosterDTO);
                    return existingRoster;
                }
            )
            .map(rosterRepository::save)
            .map(rosterMapper::toDto);
    }

    /**
     * Get all the rosters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RosterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rosters");
        return rosterRepository.findAll(pageable).map(rosterMapper::toDto);
    }

    /**
     * Get one roster by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RosterDTO> findOne(Long id) {
        log.debug("Request to get Roster : {}", id);
        return rosterRepository.findById(id).map(rosterMapper::toDto);
    }

    /**
     * Delete the roster by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Roster : {}", id);
        rosterRepository.deleteById(id);
    }
}
