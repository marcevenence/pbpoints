package com.pbpoints.service;

import com.pbpoints.domain.TournamentGroup;
import com.pbpoints.repository.TournamentGroupRepository;
import com.pbpoints.service.dto.TournamentGroupDTO;
import com.pbpoints.service.mapper.TournamentGroupMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TournamentGroup}.
 */
@Service
@Transactional
public class TournamentGroupService {

    private final Logger log = LoggerFactory.getLogger(TournamentGroupService.class);

    private final TournamentGroupRepository tournamentGroupRepository;

    private final TournamentGroupMapper tournamentGroupMapper;

    public TournamentGroupService(TournamentGroupRepository tournamentGroupRepository, TournamentGroupMapper tournamentGroupMapper) {
        this.tournamentGroupRepository = tournamentGroupRepository;
        this.tournamentGroupMapper = tournamentGroupMapper;
    }

    /**
     * Save a tournamentGroup.
     *
     * @param tournamentGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public TournamentGroupDTO save(TournamentGroupDTO tournamentGroupDTO) {
        log.debug("Request to save TournamentGroup : {}", tournamentGroupDTO);
        TournamentGroup tournamentGroup = tournamentGroupMapper.toEntity(tournamentGroupDTO);
        tournamentGroup = tournamentGroupRepository.save(tournamentGroup);
        return tournamentGroupMapper.toDto(tournamentGroup);
    }

    /**
     * Partially update a tournamentGroup.
     *
     * @param tournamentGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TournamentGroupDTO> partialUpdate(TournamentGroupDTO tournamentGroupDTO) {
        log.debug("Request to partially update TournamentGroup : {}", tournamentGroupDTO);

        return tournamentGroupRepository
            .findById(tournamentGroupDTO.getId())
            .map(
                existingTournamentGroup -> {
                    tournamentGroupMapper.partialUpdate(existingTournamentGroup, tournamentGroupDTO);
                    return existingTournamentGroup;
                }
            )
            .map(tournamentGroupRepository::save)
            .map(tournamentGroupMapper::toDto);
    }

    /**
     * Get all the tournamentGroups.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TournamentGroupDTO> findAll() {
        log.debug("Request to get all TournamentGroups");
        return tournamentGroupRepository
            .findAll()
            .stream()
            .map(tournamentGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one tournamentGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TournamentGroupDTO> findOne(Long id) {
        log.debug("Request to get TournamentGroup : {}", id);
        return tournamentGroupRepository.findById(id).map(tournamentGroupMapper::toDto);
    }

    /**
     * Delete the tournamentGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TournamentGroup : {}", id);
        tournamentGroupRepository.deleteById(id);
    }
}
