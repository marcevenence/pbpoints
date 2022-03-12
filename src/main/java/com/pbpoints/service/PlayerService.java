package com.pbpoints.service;

import com.pbpoints.domain.*;
import com.pbpoints.domain.enumeration.ProfileUser;
import com.pbpoints.repository.*;
import com.pbpoints.service.dto.PlayerDTO;
import com.pbpoints.service.dto.RosterDTO;
import com.pbpoints.service.mapper.PlayerMapper;
import com.pbpoints.service.mapper.RosterMapper;
import java.util.List;
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

    private final PlayerMapper playerMapper;

    private final RosterMapper rosterMapper;

    private final RosterRepository rosterRepository;

    private final EventCategoryRepository eventCategoryRepository;

    private final TournamentRepository tournamentRepository;

    private final PlayerPointRepository playerPointRepository;

    private final CategoryRepository categoryRepository;

    public PlayerService(
        PlayerRepository playerRepository,
        PlayerMapper playerMapper,
        RosterMapper rosterMapper,
        RosterRepository rosterRepository,
        EventCategoryRepository eventCategoryRepository,
        TournamentRepository tournamentRepository,
        PlayerPointRepository playerPointRepository,
        CategoryRepository categoryRepository
    ) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        this.rosterMapper = rosterMapper;
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
        Player player = playerMapper.toEntity(playerDTO);
        player = playerRepository.save(player);
        return playerMapper.toDto(player);
    }

    public Boolean validExists(PlayerDTO playerDTO) {
        log.debug("Request to valid if Player exists in roster : {}", playerDTO);
        Optional<Player> validPlayer = findByUserAndRoster(playerDTO.getUser().getId(), playerDTO.getRoster().getId());
        log.info(
            validPlayer.isPresent() ? "Error: Jugador Ya registrado en el Roster" : "Jugador habilitado para registrarse en el roster"
        );
        return validPlayer.isPresent();
    }

    public Boolean validExistsOtherRoster(PlayerDTO playerDTO) {
        log.debug("Request to valid if Player exists in roster : {}", playerDTO);
        if (playerDTO.getProfile() == ProfileUser.STAFF) {
            log.debug("No valida si se agrega como Staff");
            return false;
        }
        Roster roster = rosterRepository.getOne(playerDTO.getRoster().getId());
        try {
            Optional<Player> validPlayer = findByUserAndEventCategory(playerDTO.getUser().getId(), roster.getEventCategory());
            if (validPlayer.isPresent()) {
                log.debug("Error: Jugador Ya registrado en otro Roster");
                return true;
            } else return false;
        } catch (Exception e) {
            log.debug("Error: {}", e.getMessage());
            return false;
        }
    }

    public boolean validCategory(PlayerDTO playerDTO) {
        Player player = playerMapper.toEntity(playerDTO);
        if (!playerDTO.getProfile().equals(ProfileUser.PLAYER)) {
            return Boolean.FALSE;
        }
        /*Obtengo el eventoCategory del Roster*/
        EventCategory eventCategory = eventCategoryRepository.findByRosters(player.getRoster());
        log.debug("Get EventCategory: {}", eventCategory);
        /*Obtengo el torneo del eventoCategory*/
        Tournament tournament = tournamentRepository.findByEvents(eventCategory.getEvent());
        log.debug("Get Tournament: {}", tournament);
        /*Si el torneo categoriza al jugador*/
        if (tournament.getCategorize() == Boolean.FALSE) {
            return Boolean.FALSE;
        }
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
        log.debug("Event Category Order: {}", eventCategory.getCategory().getOrder());
        log.debug("Player Category Order: {}", playerPoint.getCategory().getOrder());
        if (eventCategory.getCategory().getOrder() <= playerPoint.getCategory().getOrder()) {
            return Boolean.TRUE;
        } else {
            /*O el jugador esta en la proxima categoria*/
            log.debug("Cant Jugadores Proxima Categoria: {}", tournament.getCantPlayersNextCategory());
            log.debug("Categoria Evento: {}", eventCategory.getCategory().getOrder());
            log.debug("Categoria Jugador: {}", playerPoint.getCategory().getOrder());
            log.debug(
                "Cantidad Jugadores Inscriptos: {}",
                rosterRepository.CountPlayerNextCategory(player.getRoster().getId(), playerPoint.getCategory().getId())
            );
            boolean result =
                (
                    tournament.getCantPlayersNextCategory() > 0 &&
                    eventCategory.getCategory().getOrder() + 1 == playerPoint.getCategory().getOrder()
                ) &&
                /*Y no hay nadie inscripto*/
                (
                    rosterRepository.CountPlayerNextCategory(player.getRoster().getId(), playerPoint.getCategory().getId()) <
                    tournament.getCantPlayersNextCategory()
                );
            log.info("Resultado de validacion de categoria: {}", result);
            return result;
        }
    }

    /**
     * Partially update a player.
     *
     * @param playerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PlayerDTO> partialUpdate(PlayerDTO playerDTO) {
        log.debug("Request to partially update Player : {}", playerDTO);

        return playerRepository
            .findById(playerDTO.getId())
            .map(
                existingPlayer -> {
                    playerMapper.partialUpdate(existingPlayer, playerDTO);
                    return existingPlayer;
                }
            )
            .map(playerRepository::save)
            .map(playerMapper::toDto);
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
        log.debug("Entrando en Loop Roster");
        List<Roster> rs = rosterRepository.findByEventCategory(eventCategory);
        for (Roster roster : rs) {
            log.debug("Verificando en Roster: {}", roster);
            Optional<Player> player = this.findPlayer(userId, roster);
            if (player.isPresent() && player.get().getProfile().equals(ProfileUser.PLAYER)) {
                return player;
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
        List<Player> players = playerRepository.findByRoster(roster);
        log.debug("Players: {}", players);
        for (Player player : players) {
            log.debug("Player: {}", player);
            if (player.getUser().getId().equals(userId)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    public void deleteFromRoster(RosterDTO roster, List<PlayerDTO> players) {
        List<Player> pp = playerRepository.findByRoster(rosterMapper.toEntity(roster));
        Boolean exists;
        for (Player p : pp) {
            exists = false;
            for (PlayerDTO pDTO : players) {
                if (p.getUser().equals(pDTO.getUser()) && p.getProfile().equals(pDTO.getProfile())) {
                    exists = true;
                }
            }
            if (!exists) {
                playerRepository.deleteById(p.getId());
            }
        }
    }
}
