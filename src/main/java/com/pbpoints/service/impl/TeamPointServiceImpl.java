package com.pbpoints.service.impl;

import com.pbpoints.domain.TeamPoint;
import com.pbpoints.repository.TeamPointRepository;
import com.pbpoints.service.TeamPointService;
import com.pbpoints.service.dto.TeamPointDTO;
import com.pbpoints.service.mapper.TeamPointMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TeamPoint}.
 */
@Service
@Transactional
public class TeamPointServiceImpl implements TeamPointService {

    private final Logger log = LoggerFactory.getLogger(TeamPointServiceImpl.class);

    private final TeamPointRepository teamPointRepository;

    private final TeamPointMapper teamPointMapper;

    public TeamPointServiceImpl(TeamPointRepository teamPointRepository, TeamPointMapper teamPointMapper) {
        this.teamPointRepository = teamPointRepository;
        this.teamPointMapper = teamPointMapper;
    }

    /**
     * Save a teamPoint.
     *
     * @param teamPointDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TeamPointDTO save(TeamPointDTO teamPointDTO) {
        log.debug("Request to save TeamPoint : {}", teamPointDTO);
        TeamPoint teamPoint = teamPointMapper.toEntity(teamPointDTO);
        teamPoint = teamPointRepository.save(teamPoint);
        return teamPointMapper.toDto(teamPoint);
    }

    /**
     * Get all the teamPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeamPointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TeamPoints");
        return teamPointRepository.findAll(pageable).map(teamPointMapper::toDto);
    }

    /**
     * Get one teamPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TeamPointDTO> findOne(Long id) {
        log.debug("Request to get TeamPoint : {}", id);
        return teamPointRepository.findById(id).map(teamPointMapper::toDto);
    }

    /**
     * Delete the teamPoint by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TeamPoint : {}", id);
        teamPointRepository.deleteById(id);
    }
}
