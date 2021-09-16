package com.pbpoints.service.impl;

import com.pbpoints.domain.Suspension;
import com.pbpoints.repository.SuspensionRepository;
import com.pbpoints.repository.UserRepository;
import com.pbpoints.service.SuspensionService;
import com.pbpoints.service.dto.SuspensionDTO;
import com.pbpoints.service.mapper.SuspensionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Suspension}.
 */
@Service
@Transactional
public class SuspensionServiceImpl implements SuspensionService {

    private final Logger log = LoggerFactory.getLogger(SuspensionServiceImpl.class);

    private final SuspensionRepository suspensionRepository;

    private final SuspensionMapper suspensionMapper;

    private final UserRepository userRepository;

    public SuspensionServiceImpl(
        SuspensionRepository suspensionRepository,
        SuspensionMapper suspensionMapper,
        UserRepository userRepository
    ) {
        this.suspensionRepository = suspensionRepository;
        this.suspensionMapper = suspensionMapper;
        this.userRepository = userRepository;
    }

    @Override
    public SuspensionDTO save(SuspensionDTO suspensionDTO) {
        log.debug("Request to save Suspension : {}", suspensionDTO);
        Suspension suspension = suspensionMapper.toEntity(suspensionDTO);
        Long userId = suspensionDTO.getUser().getId();
        userRepository.findById(userId).ifPresent(suspension::user);
        suspension = suspensionRepository.save(suspension);
        return suspensionMapper.toDto(suspension);
    }

    @Override
    public Optional<SuspensionDTO> partialUpdate(SuspensionDTO suspensionDTO) {
        log.debug("Request to partially update Suspension : {}", suspensionDTO);

        return suspensionRepository
            .findById(suspensionDTO.getId())
            .map(
                existingSuspension -> {
                    suspensionMapper.partialUpdate(existingSuspension, suspensionDTO);
                    return existingSuspension;
                }
            )
            .map(suspensionRepository::save)
            .map(suspensionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SuspensionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Suspensions");
        return suspensionRepository.findAll(pageable).map(suspensionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SuspensionDTO> findOne(Long id) {
        log.debug("Request to get Suspension : {}", id);
        return suspensionRepository.findById(id).map(suspensionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Suspension : {}", id);
        suspensionRepository.deleteById(id);
    }
}
