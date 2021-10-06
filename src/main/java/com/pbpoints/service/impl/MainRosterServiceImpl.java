package com.pbpoints.service.impl;

import com.pbpoints.domain.MainRoster;
import com.pbpoints.repository.MainRosterRepository;
import com.pbpoints.service.MainRosterService;
import com.pbpoints.service.dto.MainRosterDTO;
import com.pbpoints.service.mapper.MainRosterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MainRoster}.
 */
@Service
@Transactional
public class MainRosterServiceImpl implements MainRosterService {

    private final Logger log = LoggerFactory.getLogger(MainRosterServiceImpl.class);

    private final MainRosterRepository mainRosterRepository;

    private final MainRosterMapper mainRosterMapper;

    public MainRosterServiceImpl(MainRosterRepository mainRosterRepository, MainRosterMapper mainRosterMapper) {
        this.mainRosterRepository = mainRosterRepository;
        this.mainRosterMapper = mainRosterMapper;
    }

    @Override
    public MainRosterDTO save(MainRosterDTO mainRosterDTO) {
        log.debug("Request to save MainRoster : {}", mainRosterDTO);
        MainRoster mainRoster = mainRosterMapper.toEntity(mainRosterDTO);
        mainRoster = mainRosterRepository.save(mainRoster);
        return mainRosterMapper.toDto(mainRoster);
    }

    @Override
    public Optional<MainRosterDTO> partialUpdate(MainRosterDTO mainRosterDTO) {
        log.debug("Request to partially update MainRoster : {}", mainRosterDTO);

        return mainRosterRepository
            .findById(mainRosterDTO.getId())
            .map(
                existingMainRoster -> {
                    mainRosterMapper.partialUpdate(existingMainRoster, mainRosterDTO);
                    return existingMainRoster;
                }
            )
            .map(mainRosterRepository::save)
            .map(mainRosterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MainRosterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MainRosters");
        return mainRosterRepository.findAll(pageable).map(mainRosterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MainRosterDTO> findOne(Long id) {
        log.debug("Request to get MainRoster : {}", id);
        return mainRosterRepository.findById(id).map(mainRosterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MainRoster : {}", id);
        mainRosterRepository.deleteById(id);
    }
}
