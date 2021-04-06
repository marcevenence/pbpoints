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

    /**
     * Save a format.
     *
     * @param formatDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FormatDTO save(FormatDTO formatDTO) {
        log.debug("Request to save Format : {}", formatDTO);
        Format format = formatMapper.toEntity(formatDTO);
        format = formatRepository.save(format);
        return formatMapper.toDto(format);
    }

    /**
     * Get all the formats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FormatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Formats");
        return formatRepository.findAll(pageable).map(formatMapper::toDto);
    }

    /**
     * Get one format by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FormatDTO> findOne(Long id) {
        log.debug("Request to get Format : {}", id);
        return formatRepository.findById(id).map(formatMapper::toDto);
    }

    /**
     * Delete the format by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Format : {}", id);
        formatRepository.deleteById(id);
    }
}
