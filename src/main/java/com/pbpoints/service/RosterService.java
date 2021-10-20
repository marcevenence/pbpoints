package com.pbpoints.service;

import com.pbpoints.domain.EventCategory;
import com.pbpoints.domain.Roster;
import com.pbpoints.domain.User;
import com.pbpoints.repository.EventCategoryRepository;
import com.pbpoints.repository.RosterRepository;
import com.pbpoints.service.dto.EventCategoryDTO;
import com.pbpoints.service.dto.EventDTO;
import com.pbpoints.service.dto.RosterDTO;
import com.pbpoints.service.dto.TeamDTO;
import com.pbpoints.service.mapper.EventCategoryMapper;
import com.pbpoints.service.mapper.RosterMapper;
import com.pbpoints.service.mapper.TeamMapper;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Roster}.
 */
@Service
@Transactional
public class RosterService {

    private final Logger log = LoggerFactory.getLogger(RosterService.class);

    private final RosterRepository rosterRepository;

    private final RosterMapper rosterMapper;

    private final TeamMapper teamMapper;

    private final EventCategoryMapper eventCategoryMapper;

    private final UserService userService;

    private final EventCategoryRepository eventCategoryRepository;

    private final EventCategoryService eventCategoryService;

    private final EventService eventService;

    public RosterService(
        RosterRepository rosterRepository,
        RosterMapper rosterMapper,
        TeamMapper teamMapper,
        EventCategoryMapper eventCategoryMapper,
        UserService userService,
        EventCategoryRepository eventCategoryRepository,
        EventCategoryService eventCategoryService,
        EventService eventService
    ) {
        this.rosterRepository = rosterRepository;
        this.rosterMapper = rosterMapper;
        this.eventCategoryMapper = eventCategoryMapper;
        this.teamMapper = teamMapper;
        this.userService = userService;
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventCategoryService = eventCategoryService;
        this.eventService = eventService;
    }

    /**
     * Save a roster.
     *
     * @param rosterDTO the entity to save.
     * @return the persisted entity.
     */
    public RosterDTO save(RosterDTO rosterDTO) {
        log.debug("Request to save Roster : {}", rosterDTO);
        Roster roster = rosterMapper.toEntity(rosterDTO);
        roster = rosterRepository.save(roster);
        return rosterMapper.toDto(roster);
    }

    /**
     * Get all the rosters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RosterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rosters");
        return rosterRepository.findAll(pageable).map(rosterMapper::toDto);
    }

    /**
     * Get one roster by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RosterDTO> findOne(Long id) {
        log.debug("Request to get Roster : {}", id);
        return rosterRepository.findById(id).map(rosterMapper::toDto);
    }

    /**
     * Delete the roster by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Roster : {}", id);
        rosterRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<List<RosterDTO>> findByLogguedUser() {
        User user = Optional
            .of(userService.getUserWithAuthorities().orElseThrow(() -> new IllegalArgumentException("No hay usuario logueado")))
            .get();
        return rosterRepository.findByTeam_Owner(user).map(rosterMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<List<RosterDTO>> findAvailableByEventCategory(Long idEventCategory) {
        log.debug("Request to find Rosters available for EventCategory {}", idEventCategory);
        EventCategory eventCategory = eventCategoryRepository
            .findById(idEventCategory)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró un EventCategory con los datos ingresados"));
        // Busco todos los rosters disponibles
        List<Roster> all = rosterRepository.findAll();
        // dejo los rosters que no fueron usados
        log.debug("Rosters del EventCategory: {}", eventCategory.getRosters());
        log.debug("Tdodos los rosters: {}", all);
        all.removeIf(x -> eventCategory.getRosters().contains(x));
        log.debug("Rosters Disponibles: {}", all);
        return Optional.of(all).map(rosterMapper::toDto);
    }

    public Long checkOwner(Long id) {
        return rosterRepository.findByRosterId(id);
    }

    public RosterDTO findByTeamAndEventCategory(TeamDTO teamDTO, EventCategoryDTO eventCategoryDTO) {
        return rosterMapper.toDto(
            rosterRepository.findByTeamAndEventCategory(teamMapper.toEntity(teamDTO), eventCategoryMapper.toEntity(eventCategoryDTO))
        );
    }

    public long validRoster(Long rosterId) {
        Optional<RosterDTO> roster = findOne(rosterId);
        Optional<EventCategoryDTO> evCat = eventCategoryService.findOne(roster.get().getEventCategory().getId());
        Optional<EventDTO> event = eventService.findOne(evCat.get().getEvent().getId());
        Long result;
        if (event.isPresent()) {
            Date date = new Date();
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date endInsDate = Date.from(event.get().getEndInscriptionDate().atStartOfDay(defaultZoneId).toInstant());
            if (endInsDate.compareTo(date) > 0) {
                result = 1L;
            } else {
                result = 0L;
            }
        } else {
            result = 0L;
        }
        log.debug("Result: {}" + result);
        return result;
    }
}
