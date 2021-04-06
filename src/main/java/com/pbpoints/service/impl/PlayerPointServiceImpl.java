package com.pbpoints.service.impl;

import com.pbpoints.domain.PlayerPoint;
import com.pbpoints.repository.PlayerPointRepository;
import com.pbpoints.service.PlayerPointService;
import com.pbpoints.service.dto.PlayerPointDTO;
import com.pbpoints.service.mapper.PlayerPointMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PlayerPoint}.
 */
@Service
@Transactional
public class PlayerPointServiceImpl implements PlayerPointService {

    private final Logger log = LoggerFactory.getLogger(PlayerPointServiceImpl.class);

    private final PlayerPointRepository playerPointRepository;

    private final PlayerPointMapper playerPointMapper;

    public PlayerPointServiceImpl(PlayerPointRepository playerPointRepository, PlayerPointMapper playerPointMapper) {
        this.playerPointRepository = playerPointRepository;
        this.playerPointMapper = playerPointMapper;
    }

    @Override
    public PlayerPointDTO save(PlayerPointDTO playerPointDTO) {
        log.debug("Request to save PlayerPoint : {}", playerPointDTO);
        PlayerPoint playerPoint = playerPointMapper.toEntity(playerPointDTO);
        playerPoint = playerPointRepository.save(playerPoint);
        return playerPointMapper.toDto(playerPoint);
    }

    @Override
    public Optional<PlayerPointDTO> partialUpdate(PlayerPointDTO playerPointDTO) {
        log.debug("Request to partially update PlayerPoint : {}", playerPointDTO);

        return playerPointRepository
            .findById(playerPointDTO.getId())
            .map(
                existingPlayerPoint -> {
                    playerPointMapper.partialUpdate(existingPlayerPoint, playerPointDTO);
                    return existingPlayerPoint;
                }
            )
            .map(playerPointRepository::save)
            .map(playerPointMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlayerPointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PlayerPoints");
        return playerPointRepository.findAll(pageable).map(playerPointMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlayerPointDTO> findOne(Long id) {
        log.debug("Request to get PlayerPoint : {}", id);
        return playerPointRepository.findById(id).map(playerPointMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PlayerPoint : {}", id);
        playerPointRepository.deleteById(id);
    }
}
