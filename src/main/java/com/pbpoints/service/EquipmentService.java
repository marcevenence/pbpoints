package com.pbpoints.service;

import static java.util.Objects.isNull;

import com.pbpoints.domain.Equipment;
import com.pbpoints.repository.EquipmentRepository;
import com.pbpoints.service.dto.EquipmentDTO;
import com.pbpoints.service.mapper.EquipmentMapper;
import com.pbpoints.service.mapper.UserMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Equipment}.
 */
@Service
@Transactional
public class EquipmentService {

    private final Logger log = LoggerFactory.getLogger(EquipmentService.class);

    private final EquipmentRepository equipmentRepository;

    private final UserService userService;

    private final EquipmentMapper equipmentMapper;

    private final UserMapper userMapper;

    public EquipmentService(
        EquipmentRepository equipmentRepository,
        EquipmentMapper equipmentMapper,
        UserService userService,
        UserMapper userMapper
    ) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Save a equipment.
     *
     * @param equipmentDTO the entity to save.
     * @return the persisted entity.
     */
    public EquipmentDTO save(EquipmentDTO equipmentDTO) {
        log.debug("Request to save Equipment : {}", equipmentDTO);
        if (isNull(equipmentDTO.getUser())) {
            log.debug("User ID es nulo");
            equipmentDTO.setUser(userMapper.userToUserDTO(userService.getUserWithAuthorities().get()));
            log.debug("Request to save Equipment 2: {}", equipmentDTO);
        }
        Equipment equipment = equipmentMapper.toEntity(equipmentDTO);
        equipment = equipmentRepository.save(equipment);
        return equipmentMapper.toDto(equipment);
    }

    /**
     * Partially update a equipment.
     *
     * @param equipmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EquipmentDTO> partialUpdate(EquipmentDTO equipmentDTO) {
        log.debug("Request to partially update Equipment : {}", equipmentDTO);

        return equipmentRepository
            .findById(equipmentDTO.getId())
            .map(
                existingEquipment -> {
                    equipmentMapper.partialUpdate(existingEquipment, equipmentDTO);
                    return existingEquipment;
                }
            )
            .map(equipmentRepository::save)
            .map(equipmentMapper::toDto);
    }

    /**
     * Get all the equipment.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EquipmentDTO> findAll() {
        log.debug("Request to get all Equipment");
        return equipmentRepository.findAll().stream().map(equipmentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one equipment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EquipmentDTO> findOne(Long id) {
        log.debug("Request to get Equipment : {}", id);
        return equipmentRepository.findById(id).map(equipmentMapper::toDto);
    }

    /**
     * Delete the equipment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Equipment : {}", id);
        equipmentRepository.deleteById(id);
    }
}
