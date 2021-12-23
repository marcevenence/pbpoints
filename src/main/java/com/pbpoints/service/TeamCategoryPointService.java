package com.pbpoints.service;

import com.pbpoints.domain.TeamCategoryPoint;
import com.pbpoints.repository.TeamCategoryPointRepository;
import com.pbpoints.service.dto.TeamCategoryPointDTO;
import com.pbpoints.service.mapper.TeamCategoryPointMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TeamCategoryPoint}.
 */
@Service
@Transactional
public class TeamCategoryPointService {

    private final Logger log = LoggerFactory.getLogger(TeamCategoryPointService.class);

    private final TeamCategoryPointRepository teamCategoryPointRepository;

    private final TeamCategoryPointMapper teamCategoryPointMapper;

    public TeamCategoryPointService(
        TeamCategoryPointRepository teamCategoryPointRepository,
        TeamCategoryPointMapper teamCategoryPointMapper
    ) {
        this.teamCategoryPointRepository = teamCategoryPointRepository;
        this.teamCategoryPointMapper = teamCategoryPointMapper;
    }

    /**
     * Save a teamCategoryPoint.
     *
     * @param teamCategoryPointDTO the entity to save.
     * @return the persisted entity.
     */
    public TeamCategoryPointDTO save(TeamCategoryPointDTO teamCategoryPointDTO) {
        log.debug("Request to save TeamCategoryPoint : {}", teamCategoryPointDTO);
        TeamCategoryPoint teamCategoryPoint = teamCategoryPointMapper.toEntity(teamCategoryPointDTO);
        teamCategoryPoint = teamCategoryPointRepository.save(teamCategoryPoint);
        return teamCategoryPointMapper.toDto(teamCategoryPoint);
    }

    /**
     * Partially update a teamCategoryPoint.
     *
     * @param teamCategoryPointDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TeamCategoryPointDTO> partialUpdate(TeamCategoryPointDTO teamCategoryPointDTO) {
        log.debug("Request to partially update TeamCategoryPoint : {}", teamCategoryPointDTO);

        return teamCategoryPointRepository
            .findById(teamCategoryPointDTO.getId())
            .map(
                existingTeamCategoryPoint -> {
                    teamCategoryPointMapper.partialUpdate(existingTeamCategoryPoint, teamCategoryPointDTO);
                    return existingTeamCategoryPoint;
                }
            )
            .map(teamCategoryPointRepository::save)
            .map(teamCategoryPointMapper::toDto);
    }

    /**
     * Get all the teamCategoryPoints.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TeamCategoryPointDTO> findAll() {
        log.debug("Request to get all TeamCategoryPoints");
        return teamCategoryPointRepository
            .findAll()
            .stream()
            .map(teamCategoryPointMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one teamCategoryPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TeamCategoryPointDTO> findOne(Long id) {
        log.debug("Request to get TeamCategoryPoint : {}", id);
        return teamCategoryPointRepository.findById(id).map(teamCategoryPointMapper::toDto);
    }

    /**
     * Delete the teamCategoryPoint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TeamCategoryPoint : {}", id);
        teamCategoryPointRepository.deleteById(id);
    }
}
