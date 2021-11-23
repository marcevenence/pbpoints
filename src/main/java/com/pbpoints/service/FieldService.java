package com.pbpoints.service;

import com.pbpoints.domain.Field;
import com.pbpoints.repository.FieldRepository;
import com.pbpoints.service.dto.FieldDTO;
import com.pbpoints.service.mapper.FieldMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Field}.
 */
@Service
@Transactional
public class FieldService {

    private final Logger log = LoggerFactory.getLogger(FieldService.class);

    private final FieldRepository fieldRepository;

    private final FieldMapper fieldMapper;

    public FieldService(FieldRepository fieldRepository, FieldMapper fieldMapper) {
        this.fieldRepository = fieldRepository;
        this.fieldMapper = fieldMapper;
    }

    /**
     * Save a field.
     *
     * @param fieldDTO the entity to save.
     * @return the persisted entity.
     */
    public FieldDTO save(FieldDTO fieldDTO) {
        log.debug("Request to save Field : {}", fieldDTO);
        Field field = fieldMapper.toEntity(fieldDTO);
        field = fieldRepository.save(field);
        return fieldMapper.toDto(field);
    }

    /**
     * Partially update a field.
     *
     * @param fieldDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FieldDTO> partialUpdate(FieldDTO fieldDTO) {
        log.debug("Request to partially update Field : {}", fieldDTO);

        return fieldRepository
            .findById(fieldDTO.getId())
            .map(
                existingField -> {
                    fieldMapper.partialUpdate(existingField, fieldDTO);
                    return existingField;
                }
            )
            .map(fieldRepository::save)
            .map(fieldMapper::toDto);
    }

    /**
     * Get all the fields.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FieldDTO> findAll() {
        log.debug("Request to get all Fields");
        return fieldRepository.findAll().stream().map(fieldMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one field by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FieldDTO> findOne(Long id) {
        log.debug("Request to get Field : {}", id);
        return fieldRepository.findById(id).map(fieldMapper::toDto);
    }

    /**
     * Delete the field by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Field : {}", id);
        fieldRepository.deleteById(id);
    }
}
