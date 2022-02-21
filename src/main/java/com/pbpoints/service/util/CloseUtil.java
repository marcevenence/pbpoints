package com.pbpoints.service.util;

import com.pbpoints.domain.*;
import com.pbpoints.domain.enumeration.Status;
import com.pbpoints.domain.enumeration.status;
import com.pbpoints.repository.*;
import com.pbpoints.service.PlayerPointService;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class CloseUtil {

    private final Logger log = LoggerFactory.getLogger(CloseUtil.class);

    private final TournamentRepository tournamentRepository;

    private final UserRepository userRepository;

    private final PlayerPointRepository playerPointRepository;

    private final SeasonRepository seasonRepository;

    private final RosterEventRepository rosterEventRepository;

    private final PlayerRepository playerRepository;

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    private final PlayerPointHistoryRepository playerPointHistoryRepository;

    private final PlayerPointService playerPointService;

    public CloseUtil(
        TournamentRepository tournamentRepository,
        PlayerPointRepository playerPointRepository,
        SeasonRepository seasonRepository,
        RosterEventRepository rosterEventRepository,
        UserRepository userRepository,
        PlayerRepository playerRepository,
        CategoryRepository categoryRepository,
        EventRepository eventRepository,
        PlayerPointHistoryRepository playerPointHistoryRepository,
        PlayerPointService playerPointService
    ) {
        this.tournamentRepository = tournamentRepository;
        this.playerPointRepository = playerPointRepository;
        this.seasonRepository = seasonRepository;
        this.rosterEventRepository = rosterEventRepository;
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.playerPointHistoryRepository = playerPointHistoryRepository;
        this.playerPointService = playerPointService;
    }

    public Integer getQtyEvents(User user, List<Event> events) {
        for (Event ev : events) {
            List<Long> qtyEvents = rosterEventRepository.countByUserIdAndEventId(user.getId(), ev.getId());
            return qtyEvents.size();
        }
        return 0;
    }

    @Scheduled(cron = "${application.cronCloseSeason}")
    public void closeSeason() {
        PlayerPointHistory pph;
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
                        Float points = playerPointService.calculatePoints(user, sea, tour);
                        PlayerPoint player = playerPointRepository.findByUserAndTournament(userRepository.findOneById(user), tour);
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
                                log.debug("Guardo el History");
                                pph = new PlayerPointHistory();
                                pph.setCategory(player.getCategory());
                                pph.setPlayerPoint(player);
                                pph.setSeason(sea);
                                pph.setPoints(points);
                                playerPointHistoryRepository.save(pph);
                                try {
                                    log.debug("Desciende Categoria");
                                    Category newCategory = categoryRepository.findByTournamentAndOrder(
                                        tour,
                                        player.getCategory().getOrder() + 1
                                    );
                                    player.setCategory(newCategory);
                                    playerPointRepository.save(player);
                                } catch (Exception e) {
                                    log.debug("No hay categoria menor, mantiene categoria");
                                }
                            } else {
                                try {
                                    PlayerPointHistory lastCategory = playerPointHistoryRepository.findByPlayerPointAndSeason(
                                        player,
                                        seasonRepository.findByTournamentAndAnio(tour, sea.getAnio() - 1)
                                    );
                                    if (player.getCategory().getOrder() > lastCategory.getCategory().getOrder()) {
                                        log.debug("Guardo el History");
                                        pph = new PlayerPointHistory();
                                        pph.setCategory(player.getCategory());
                                        pph.setPlayerPoint(player);
                                        pph.setSeason(sea);
                                        pph.setPoints(points);
                                        playerPointHistoryRepository.save(pph);
                                        log.debug("Subio categoria, vuelve a la categoria anterior");
                                        player.setCategory(lastCategory.getCategory());
                                        playerPointRepository.save(player);
                                    }
                                } catch (Exception e) {
                                    log.debug("Guardo el History");
                                    pph = new PlayerPointHistory();
                                    pph.setCategory(player.getCategory());
                                    pph.setPlayerPoint(player);
                                    pph.setSeason(sea);
                                    pph.setPoints(points);
                                    playerPointHistoryRepository.save(pph);
                                    log.debug("No hay registro history, se considera que no subio, mantiene");
                                }
                            }
                        } else {
                            List<Event> eventos = eventRepository.findByTournamentAndSeason(tour, sea);
                            Integer cantEventos = getQtyEvents(player.getUser(), eventos);
                            if (eventos.size() < 3 || (eventos.size() >= 3 && cantEventos > 1)) {
                                log.debug(
                                    "FALTA cambiar al IF:  == 4 por -> AND si jugo en mas de un evento en una categoria sino mantiene categoria"
                                );
                                log.debug("Guardo el History");

                                pph = new PlayerPointHistory();
                                pph.setCategory(player.getCategory());
                                pph.setPlayerPoint(player);
                                pph.setSeason(sea);
                                pph.setPoints(points);
                                playerPointHistoryRepository.save(pph);
                                log.debug("FALTA Valido los puntos");
                                if (points > 75) {
                                    log.debug("Asciende");
                                    try {
                                        Category newCategory = categoryRepository.findByTournamentAndOrder(
                                            tour,
                                            player.getCategory().getOrder() - 1
                                        );
                                        if (newCategory.getOrder() != 0) {
                                            player.setCategory(newCategory);
                                            playerPointRepository.save(player);
                                        } else {
                                            log.debug("No hay categoria mayor, mantiene");
                                        }
                                    } catch (Exception e) {}
                                } else {
                                    if (points <= 35) {
                                        log.debug("Desciende Categoria");
                                        Category newCategory = categoryRepository.findByTournamentAndOrder(
                                            tour,
                                            player.getCategory().getOrder() + 1
                                        );
                                        player.setCategory(newCategory);
                                        playerPointRepository.save(player);
                                    } else {
                                        log.debug("Mantiene");
                                    }
                                }
                            } else {
                                log.debug("No tiene suficientes puntos para calcular categoria, mantiene categoria");
                                log.debug("Guardo el History");
                                pph = new PlayerPointHistory();
                                pph.setCategory(player.getCategory());
                                pph.setPlayerPoint(player);
                                pph.setSeason(sea);
                                pph.setPoints(points);
                                playerPointHistoryRepository.save(pph);
                            }
                        }
                    }
                    sea.setStatus(Status.DONE);
                    seasonRepository.save(sea);
                }
            } else {
                log.debug("No Categoriza");
            }
        }
        log.info("Cierre proceso de cierre de temporada");
    }
}
