package com.pbpoints.service;

import com.pbpoints.domain.PlayerPointHistory;
import com.pbpoints.repository.PlayerPointHistoryRepository;
import com.pbpoints.service.dto.PlayerPointHistoryDTO;
import com.pbpoints.service.mapper.PlayerPointHistoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PlayerPointHistory}.
 */
@Service
@Transactional
public class PlayerPointHistoryService {

    private final Logger log = LoggerFactory.getLogger(PlayerPointHistoryService.class);

    private final PlayerPointHistoryRepository playerPointHistoryRepository;

    private final PlayerPointHistoryMapper playerPointHistoryMapper;

    public PlayerPointHistoryService(
        PlayerPointHistoryRepository playerPointHistoryRepository,
        PlayerPointHistoryMapper playerPointHistoryMapper
    ) {
        this.playerPointHistoryRepository = playerPointHistoryRepository;
        this.playerPointHistoryMapper = playerPointHistoryMapper;
    }

    /**
     * Save a playerPointHistory.
     *
     * @param playerPointHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public PlayerPointHistoryDTO save(PlayerPointHistoryDTO playerPointHistoryDTO) {
        log.debug("Request to save PlayerPointHistory : {}", playerPointHistoryDTO);
        PlayerPointHistory playerPointHistory = playerPointHistoryMapper.toEntity(playerPointHistoryDTO);
        playerPointHistory = playerPointHistoryRepository.save(playerPointHistory);
        return playerPointHistoryMapper.toDto(playerPointHistory);
    }

    /**
     * Partially update a playerPointHistory.
     *
     * @param playerPointHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PlayerPointHistoryDTO> partialUpdate(PlayerPointHistoryDTO playerPointHistoryDTO) {
        log.debug("Request to partially update PlayerPointHistory : {}", playerPointHistoryDTO);

        return playerPointHistoryRepository
            .findById(playerPointHistoryDTO.getId())
            .map(
                existingPlayerPointHistory -> {
                    playerPointHistoryMapper.partialUpdate(existingPlayerPointHistory, playerPointHistoryDTO);

                    return existingPlayerPointHistory;
                }
            )
            .map(playerPointHistoryRepository::save)
            .map(playerPointHistoryMapper::toDto);
    }

    /**
     * Get all the playerPointHistories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PlayerPointHistoryDTO> findAll() {
        log.debug("Request to get all PlayerPointHistories");
        return playerPointHistoryRepository
            .findAll()
            .stream()
            .map(playerPointHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one playerPointHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlayerPointHistoryDTO> findOne(Long id) {
        log.debug("Request to get PlayerPointHistory : {}", id);
        return playerPointHistoryRepository.findById(id).map(playerPointHistoryMapper::toDto);
    }

    /**
     * Delete the playerPointHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PlayerPointHistory : {}", id);
        playerPointHistoryRepository.deleteById(id);
    }
}
