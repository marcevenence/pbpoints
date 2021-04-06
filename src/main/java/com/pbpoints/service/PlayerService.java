package com.pbpoints.service;

import com.pbpoints.domain.EventCategory;
import com.pbpoints.domain.Player;
import com.pbpoints.domain.PlayerPoint;
import com.pbpoints.domain.Roster;
import com.pbpoints.domain.Tournament;
import com.pbpoints.domain.User;
import com.pbpoints.domain.enumeration.ProfileUser;
import com.pbpoints.repository.CategoryRepository;
import com.pbpoints.repository.EventCategoryRepository;
import com.pbpoints.repository.PlayerPointRepository;
import com.pbpoints.repository.PlayerRepository;
import com.pbpoints.repository.RosterRepository;
import com.pbpoints.repository.TournamentRepository;
import com.pbpoints.service.dto.PlayerDTO;
import com.pbpoints.service.mapper.PlayerMapper;
import com.pbpoints.web.rest.errors.BadRequestAlertException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Player}.
 */
@Service
@Transactional
public class PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;
    private final RosterRepository rosterRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final TournamentRepository tournamentRepository;
    private final PlayerPointRepository playerPointRepository;
    private final CategoryRepository categoryRepository;

    private final PlayerMapper playerMapper;

    public PlayerService(
        PlayerRepository playerRepository,
        PlayerMapper playerMapper,
        RosterRepository rosterRepository,
        EventCategoryRepository eventCategoryRepository,
        TournamentRepository tournamentRepository,
        PlayerPointRepository playerPointRepository,
        CategoryRepository categoryRepository
    ) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        this.rosterRepository = rosterRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.tournamentRepository = tournamentRepository;
        this.playerPointRepository = playerPointRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Save a player.
     *
     * @param playerDTO the entity to save.
     * @return the persisted entity.
     */
    public PlayerDTO save(PlayerDTO playerDTO) {
        log.debug("Request to save Player : {}", playerDTO);
        Player player = playerRepository.save(playerMapper.toEntity(playerDTO));
        return playerMapper.toDto(player);
    }

    public Boolean validExists(PlayerDTO playerDTO) {
        log.debug("Request to valid if Player exists in roster : {}", playerDTO);
        try {
            Optional<Player> validPlayer = findByUserAndRoster(playerDTO.getUserId(), playerDTO.getRosterId());
            if (validPlayer.isPresent()) {
                log.debug("Error: Jugador Ya registrado en el Roster");
                return true;
            } else return false;
        } catch (Exception e) {
            log.debug("Error: " + e);
            return false;
        }
    }

    public Boolean validExistsOtherRoster(PlayerDTO playerDTO) {
        log.debug("Request to valid if Player exists in roster : {}", playerDTO);
        if (playerDTO.getProfile() == ProfileUser.STAFF) {
            log.debug("No valida si se agrega como Staff");
            return false;
        }
        Roster roster = rosterRepository.getOne(playerDTO.getRosterId());
        try {
            Optional<Player> validPlayer = findByUserAndEventCategory(playerDTO.getUserId(), roster.getEventCategory());
            if (validPlayer.isPresent()) {
                log.debug("Error: Jugador Ya registrado en otro Roster");
                return true;
            } else return false;
        } catch (Exception e) {
            log.debug("Error: " + e);
            return false;
        }
    }

    public boolean validCategory(PlayerDTO playerDTO) {
        Player player = playerMapper.toEntity(playerDTO);
        if (playerDTO.getProfile().equals(ProfileUser.PLAYER)) {
            /*Obtengo el eventoCategory del Roster*/
            EventCategory eventCategory = eventCategoryRepository.findByRosters(player.getRoster());
            log.debug("Get EventCategory: {}", eventCategory);
            /*Obtengo el torneo del eventoCategory*/
            Tournament tournament = tournamentRepository.findByEvents(eventCategory.getEvent());
            log.debug("Get Tournament: {}", tournament);
            /*Si el torneo categoriza al jugador*/
            if (tournament.getCategorize()) {
                log.debug("Tournament Categorize");
                /*Obtengo la categoria a la que pertenece el jugador*/
                PlayerPoint playerPoint = playerPointRepository.findByUserAndTournament(player.getUser(), tournament);
                log.debug("Get PlayerPoint: {}", playerPoint);
                if (playerPoint == null) {
                    playerPoint = new PlayerPoint();
                    playerPoint.setPoints((float) 0);
                    playerPoint.setTournament(tournament);
                    playerPoint.setUser(player.getUser());
                    playerPoint.setCategory(categoryRepository.LastCategoryByTournamentId(tournament.getId()));
                    log.debug("Get PlayerPoint: {}", playerPoint);
                    playerPoint = playerPointRepository.save(playerPoint);
                }
                /*Si la categoria del jugador es menor o igual a la del EventoCategoria (orden invertido)*/
                log.debug("Event Category Order: " + eventCategory.getCategory().getOrder());
                log.debug("Player Category Order: " + playerPoint.getCategory().getOrder());
                if (eventCategory.getCategory().getOrder() <= playerPoint.getCategory().getOrder()) return true; else {
                    /*O el jugador esta en la proxima categoria*/
                    log.debug("Cant Jugadores Proxima Categoria: " + tournament.getCantPlayersNextCategory());
                    log.debug("Categoria Evento: " + eventCategory.getCategory().getOrder());
                    log.debug("Categoria Jugador: " + playerPoint.getCategory().getOrder());
                    log.debug(
                        "Cantidad Jugadores Inscriptos: " +
                        rosterRepository.CountPlayerNextCategory(player.getRoster().getId(), playerPoint.getCategory().getId())
                    );
                    if (
                        (
                            tournament.getCantPlayersNextCategory() > 0 &&
                            eventCategory.getCategory().getOrder() + 1 == playerPoint.getCategory().getOrder()
                        ) &&
                        /*Y no hay nadie inscripto*/
                        (
                            rosterRepository.CountPlayerNextCategory(player.getRoster().getId(), playerPoint.getCategory().getId()) <
                            tournament.getCantPlayersNextCategory()
                        )
                    ) return true; else return false;
                }
            } else return true;
        } else return true;
    }

    /**
     * Get all the players.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlayerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Players");
        return playerRepository.findAll(pageable).map(playerMapper::toDto);
    }

    /**
     * Get one player by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlayerDTO> findOne(Long id) {
        log.debug("Request to get Player : {}", id);
        return playerRepository.findById(id).map(playerMapper::toDto);
    }

    /**
     * Delete the player by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Player : {}", id);
        playerRepository.deleteById(id);
    }

    public Optional<Player> findByUserAndEventCategory(Long userId, EventCategory eventCategory) {
        log.debug("Buscando User en EventoCategoria: {}, {}", userId, eventCategory);
        for (Roster roster : eventCategory.getRosters()) {
            Optional<Player> player = this.findPlayer(userId, roster);
            if (player.isPresent()) {
                if (player.get().getProfile() == ProfileUser.PLAYER) return player;
            }
        }
        return Optional.empty();
    }

    public Optional<Player> findByUserAndRoster(Long userId, Long rosterId) {
        log.debug("Buscando User en Roster: {}, {}", userId, rosterId);
        Roster roster = rosterRepository.getOne(rosterId);
        log.debug("Roster: {}", roster);
        return this.findPlayer(userId, roster);
    }

    private Optional<Player> findPlayer(Long userId, Roster roster) {
        for (Player player : roster.getPlayers()) {
            log.debug("Player: {}", player);
            if (player.getUser().getId().equals(userId)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }
}
