package com.pbpoints.service.impl;

import com.pbpoints.domain.TeamDetailPoint;
import com.pbpoints.repository.TeamDetailPointRepository;
import com.pbpoints.service.TeamDetailPointService;
import com.pbpoints.service.dto.TeamDetailPointDTO;
import com.pbpoints.service.mapper.TeamDetailPointMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TeamDetailPoint}.
 */
@Service
@Transactional
public class TeamDetailPointServiceImpl implements TeamDetailPointService {

    private final Logger log = LoggerFactory.getLogger(TeamDetailPointServiceImpl.class);

    private final TeamDetailPointRepository teamDetailPointRepository;

    private final TeamDetailPointMapper teamDetailPointMapper;

    public TeamDetailPointServiceImpl(TeamDetailPointRepository teamDetailPointRepository, TeamDetailPointMapper teamDetailPointMapper) {
        this.teamDetailPointRepository = teamDetailPointRepository;
        this.teamDetailPointMapper = teamDetailPointMapper;
    }

    /**
     * Save a teamDetailPoint.
     *
     * @param teamDetailPointDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TeamDetailPointDTO save(TeamDetailPointDTO teamDetailPointDTO) {
        log.debug("Request to save TeamDetailPoint : {}", teamDetailPointDTO);
        TeamDetailPoint teamDetailPoint = teamDetailPointMapper.toEntity(teamDetailPointDTO);
        teamDetailPoint = teamDetailPointRepository.save(teamDetailPoint);
        return teamDetailPointMapper.toDto(teamDetailPoint);
    }

    /**
     * Get all the teamDetailPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeamDetailPointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TeamDetailPoints");
        return teamDetailPointRepository.findAll(pageable).map(teamDetailPointMapper::toDto);
    }

    /**
     * Get one teamDetailPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
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
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TeamDetailPoint : {}", id);
        teamDetailPointRepository.deleteById(id);
    }
}
