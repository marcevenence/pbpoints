package com.pbpoints.service.impl;

import com.pbpoints.domain.PlayerDetailPoint;
import com.pbpoints.repository.PlayerDetailPointRepository;
import com.pbpoints.service.PlayerDetailPointService;
import com.pbpoints.service.dto.PlayerDetailPointDTO;
import com.pbpoints.service.mapper.PlayerDetailPointMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PlayerDetailPoint}.
 */
@Service
@Transactional
public class PlayerDetailPointServiceImpl implements PlayerDetailPointService {

    private final Logger log = LoggerFactory.getLogger(PlayerDetailPointServiceImpl.class);

    private final PlayerDetailPointRepository playerDetailPointRepository;

    private final PlayerDetailPointMapper playerDetailPointMapper;

    public PlayerDetailPointServiceImpl(
        PlayerDetailPointRepository playerDetailPointRepository,
        PlayerDetailPointMapper playerDetailPointMapper
    ) {
        this.playerDetailPointRepository = playerDetailPointRepository;
        this.playerDetailPointMapper = playerDetailPointMapper;
    }

    @Override
    public PlayerDetailPointDTO save(PlayerDetailPointDTO playerDetailPointDTO) {
        log.debug("Request to save PlayerDetailPoint : {}", playerDetailPointDTO);
        PlayerDetailPoint playerDetailPoint = playerDetailPointMapper.toEntity(playerDetailPointDTO);
        playerDetailPoint = playerDetailPointRepository.save(playerDetailPoint);
        return playerDetailPointMapper.toDto(playerDetailPoint);
    }

    @Override
    public Optional<PlayerDetailPointDTO> partialUpdate(PlayerDetailPointDTO playerDetailPointDTO) {
        log.debug("Request to partially update PlayerDetailPoint : {}", playerDetailPointDTO);

        return playerDetailPointRepository
            .findById(playerDetailPointDTO.getId())
            .map(
                existingPlayerDetailPoint -> {
                    playerDetailPointMapper.partialUpdate(existingPlayerDetailPoint, playerDetailPointDTO);
                    return existingPlayerDetailPoint;
                }
            )
            .map(playerDetailPointRepository::save)
            .map(playerDetailPointMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlayerDetailPointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PlayerDetailPoints");
        return playerDetailPointRepository.findAll(pageable).map(playerDetailPointMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlayerDetailPointDTO> findOne(Long id) {
        log.debug("Request to get PlayerDetailPoint : {}", id);
        return playerDetailPointRepository.findById(id).map(playerDetailPointMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PlayerDetailPoint : {}", id);
        playerDetailPointRepository.deleteById(id);
    }
}
