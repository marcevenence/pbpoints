package com.pbpoints.service;

import com.pbpoints.domain.TeamDetailPoint;
import com.pbpoints.repository.TeamDetailPointRepository;
import com.pbpoints.service.dto.TeamDetailPointDTO;
import com.pbpoints.service.mapper.TeamDetailPointMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TeamDetailPoint}.
 */
@Service
@Transactional
public class TeamDetailPointService {

    private final Logger log = LoggerFactory.getLogger(TeamDetailPointService.class);

    private final TeamDetailPointRepository teamDetailPointRepository;

    private final TeamDetailPointMapper teamDetailPointMapper;

    public TeamDetailPointService(TeamDetailPointRepository teamDetailPointRepository, TeamDetailPointMapper teamDetailPointMapper) {
        this.teamDetailPointRepository = teamDetailPointRepository;
        this.teamDetailPointMapper = teamDetailPointMapper;
    }

    /**
     * Save a teamDetailPoint.
     *
     * @param teamDetailPointDTO the entity to save.
     * @return the persisted entity.
     */
    public TeamDetailPointDTO save(TeamDetailPointDTO teamDetailPointDTO) {
        log.debug("Request to save TeamDetailPoint : {}", teamDetailPointDTO);
        TeamDetailPoint teamDetailPoint = teamDetailPointMapper.toEntity(teamDetailPointDTO);
        teamDetailPoint = teamDetailPointRepository.save(teamDetailPoint);
        return teamDetailPointMapper.toDto(teamDetailPoint);
    }

    /**
     * Partially update a teamDetailPoint.
     *
     * @param teamDetailPointDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TeamDetailPointDTO> partialUpdate(TeamDetailPointDTO teamDetailPointDTO) {
        log.debug("Request to partially update TeamDetailPoint : {}", teamDetailPointDTO);

        return teamDetailPointRepository
            .findById(teamDetailPointDTO.getId())
            .map(
                existingTeamDetailPoint -> {
                    teamDetailPointMapper.partialUpdate(existingTeamDetailPoint, teamDetailPointDTO);
                    return existingTeamDetailPoint;
                }
            )
            .map(teamDetailPointRepository::save)
            .map(teamDetailPointMapper::toDto);
    }

    /**
     * Get all the teamDetailPoints.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TeamDetailPointDTO> findAll() {
        log.debug("Request to get all TeamDetailPoints");
        return teamDetailPointRepository
            .findAll()
            .stream()
            .map(teamDetailPointMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one teamDetailPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TeamDetailPointDTO> findOne(Long id) {
        log.debug("Request to get TeamDetailPoint : {}", id);
        return teamDetailPointRepository.findById(id).map(teamDetailPointMapper::toDto);
    }

    /**
     * Delete the teamDetailPoint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TeamDetailPoint : {}", id);
        teamDetailPointRepository.deleteById(id);
    }
}
