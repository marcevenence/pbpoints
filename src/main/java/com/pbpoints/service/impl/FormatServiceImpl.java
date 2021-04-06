package com.pbpoints.service.impl;

import com.pbpoints.domain.Format;
import com.pbpoints.repository.FormatRepository;
import com.pbpoints.service.FormatService;
import com.pbpoints.service.dto.FormatDTO;
import com.pbpoints.service.mapper.FormatMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Format}.
 */
@Service
@Transactional
public class FormatServiceImpl implements FormatService {

    private final Logger log = LoggerFactory.getLogger(FormatServiceImpl.class);

    private final FormatRepository formatRepository;

    private final FormatMapper formatMapper;

    public FormatServiceImpl(FormatRepository formatRepository, FormatMapper formatMapper) {
        this.formatRepository = formatRepository;
        this.formatMapper = formatMapper;
    }

    @Override
    public FormatDTO save(FormatDTO formatDTO) {
        log.debug("Request to save Format : {}", formatDTO);
        Format format = formatMapper.toEntity(formatDTO);
        format = formatRepository.save(format);
        return formatMapper.toDto(format);
    }

    @Override
    public Optional<FormatDTO> partialUpdate(FormatDTO formatDTO) {
        log.debug("Request to partially update Format : {}", formatDTO);

        return formatRepository
            .findById(formatDTO.getId())
            .map(
                existingFormat -> {
                    formatMapper.partialUpdate(existingFormat, formatDTO);
                    return existingFormat;
                }
            )
            .map(formatRepository::save)
            .map(formatMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Formats");
        return formatRepository.findAll(pageable).map(formatMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormatDTO> findOne(Long id) {
        log.debug("Request to get Format : {}", id);
        return formatRepository.findById(id).map(formatMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Format : {}", id);
        formatRepository.deleteById(id);
    }
}
