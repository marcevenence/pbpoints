package com.pbpoints.service;

import com.pbpoints.domain.*;
import com.pbpoints.domain.enumeration.Status;
import com.pbpoints.repository.*;
import com.pbpoints.service.dto.TournamentDTO;
import com.pbpoints.service.mapper.TournamentMapper;
import com.pbpoints.service.mapper.UserMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tournament}.
 */
@Service
@Transactional
public class TournamentService {

    private final Logger log = LoggerFactory.getLogger(TournamentService.class);

    private final TournamentRepository tournamentRepository;

    private final UserService userService;

    private final TournamentMapper tournamentMapper;

    private final PlayerPointRepository playerPointRepository;

    private final SeasonRepository seasonRepository;

    private final RosterEventRepository rosterEventRepository;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final PlayerRepository playerRepository;

    private final CategoryRepository categoryRepository;

    public TournamentService(
        TournamentRepository tournamentRepository,
        TournamentMapper tournamentMapper,
        UserService userService,
        UserMapper userMapper,
        PlayerPointRepository playerPointRepository,
        SeasonRepository seasonRepository,
        RosterEventRepository rosterEventRepository,
        UserRepository userRepository,
        PlayerRepository playerRepository,
        CategoryRepository categoryRepository
    ) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentMapper = tournamentMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.playerPointRepository = playerPointRepository;
        this.seasonRepository = seasonRepository;
        this.rosterEventRepository = rosterEventRepository;
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Save a tournament.
     *
     * @param tournamentDTO the entity to save.
     * @return the persisted entity.
     */
    public TournamentDTO save(TournamentDTO tournamentDTO) {
        log.debug("Request to save Tournament : {}", tournamentDTO);
        if (tournamentDTO.getOwner().getId() == 0) {
            tournamentDTO.setOwner(userMapper.userToUserDTO(userService.getUserWithAuthorities().get()));
            log.debug("Request to save Tournament 2: {}", tournamentDTO);
        }
        Tournament tournament = tournamentMapper.toEntity(tournamentDTO);
        tournament = tournamentRepository.save(tournament);
        return tournamentMapper.toDto(tournament);
    }

    /**
     * Partially update a tournament.
     *
     * @param tournamentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TournamentDTO> partialUpdate(TournamentDTO tournamentDTO) {
        log.debug("Request to partially update Tournament : {}", tournamentDTO);

        return tournamentRepository
            .findById(tournamentDTO.getId())
            .map(
                existingTournament -> {
                    tournamentMapper.partialUpdate(existingTournament, tournamentDTO);
                    return existingTournament;
                }
            )
            .map(tournamentRepository::save)
            .map(tournamentMapper::toDto);
    }

    /**
     * Get all the tournaments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TournamentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tournaments");
        return tournamentRepository.findAll(pageable).map(tournamentMapper::toDto);
    }

    /**
     * Get one tournament by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TournamentDTO> findOne(Long id) {
        log.debug("Request to get Tournament : {}", id);
        return tournamentRepository.findById(id).map(tournamentMapper::toDto);
    }

    /**
     * Delete the tournament by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tournament : {}", id);
        tournamentRepository.deleteById(id);
    }

    @Scheduled(cron = "${application.cronCloseSeason}")
    public void closeSeason() {
        log.info("Inicio proceso de cierre de temporada");
        List<Tournament> tournaments = tournamentRepository.findByEndSeasonDate(
            LocalDate.now().getDayOfMonth(),
            LocalDate.now().getMonthValue()
        );
        for (Tournament tour : tournaments) {
            log.debug("torneos que cierran temporada: {}", tour);
            if (tour.getCategorize()) {
                log.debug("Categoriza");
                log.debug("Puntaje para subir de categoria: 75");
                log.debug("Puntaje para bajar de categoria: 35");
                List<Season> seasons = seasonRepository.findByTournamentAndStatus(tour, Status.CREATED);
                for (Season sea : seasons) {
                    log.debug("Procesando temporada: {}", sea.getAnio().toString());
                    List<Long> users = rosterEventRepository.findUsersByTournamentId(tour.getId());
                    for (Long user : users) {
                        List<RosterEvent> actual = rosterEventRepository.findPlayersByTournamentIdAndPlayerIdAndAnio(
                            tour.getId(),
                            user,
                            sea.getAnio()
                        );
                        if (actual.size() == 0) {
                            List<RosterEvent> anterior = rosterEventRepository.findPlayersByTournamentIdAndPlayerIdAndAnio(
                                tour.getId(),
                                user,
                                sea.getAnio() - 1
                            );
                            if (anterior.size() == 0) {
                                PlayerPoint player = playerPointRepository.findByUserAndTournament(userRepository.findOneById(user), tour);
                                try {
                                    Category newCategory = categoryRepository.findByTournamentAndOrder(
                                        tour,
                                        player.getCategory().getOrder() + 1
                                    );
                                    player.setCategory(newCategory);
                                    playerPointRepository.save(player);
                                } catch (Exception e) {
                                    log.debug("No hay categoria menor, no actualiza");
                                }
                            } else {
                                log.debug("FALTA Calcular si subio antes y descenderlo");
                            }
                        } else {
                            log.debug("FALTA Cuento la cantidad de fechas del torneo");
                        }
                    }
                }
            } else {
                log.debug("No Categoriza");
            }
        }
        log.info("Cierre proceso de cierre de temporada");
    }
}
