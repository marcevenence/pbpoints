package com.pbpoints.service.util;

import com.pbpoints.domain.*;
import com.pbpoints.domain.enumeration.Status;
import com.pbpoints.repository.*;
import com.pbpoints.service.MailService;
import com.pbpoints.service.PlayerPointService;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
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

    private final MailService mailService;

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
        PlayerPointService playerPointService,
        MailService mailService
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
        this.mailService = mailService;
    }

    public Integer getQtyEvents(User user, List<Event> events) {
        Integer cant = 0;
        for (Event ev : events) {
            Long qtyEvents = rosterEventRepository.countByUserIdAndEventId(user.getId(), ev.getId());
            if (qtyEvents > 0) {
                cant++;
            }
        }
        return cant;
    }

    @Async
    @Scheduled(cron = "${application.cronCloseSeason}")
    public void closeSeason() {
        Float up = 75F;
        Float down = 35F;
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
                log.debug("Puntaje para subir de categoria: {}", up);
                log.debug("Puntaje para bajar de categoria: {}", down);
                List<Season> seasons = seasonRepository.findByTournamentAndStatus(tour, Status.CREATED);
                for (Season sea : seasons) {
                    log.debug("Procesando temporada: {}", sea.getAnio().toString());
                    List<Long> users = rosterEventRepository.findUsersByTournamentId(tour.getId());
                    for (Long user : users) {
                        log.debug("UsuarioId: {}", user.toString());
                        Float points = playerPointService.calculatePoints(user, sea, tour);
                        log.debug("Puntaje Obtenido: {}", points.toString());
                        PlayerPoint player = playerPointRepository.findByUserAndTournament(userRepository.findOneById(user), tour).get();
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
                                pph.setPoints(0F);
                                pph.setCantEvent(0);
                                pph.setTotalPoints(points);
                                playerPointHistoryRepository.save(pph);
                                try {
                                    log.debug("Desciende Categoria");
                                    Category newCategory = categoryRepository.findByTournamentAndOrder(
                                        tour,
                                        player.getCategory().getOrder() + 1
                                    );
                                    player.setCategory(newCategory);
                                    playerPointRepository.save(player);
                                    mailService.sendNoPlayEmail(player.getUser(), tour, player.getCategory(), pph);
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
                                        pph.setPoints(0F);
                                        pph.setCantEvent(0);
                                        pph.setTotalPoints(points);

                                        playerPointHistoryRepository.save(pph);
                                        log.debug("Subio categoria, vuelve a la categoria anterior");
                                        player.setCategory(lastCategory.getCategory());
                                        playerPointRepository.save(player);
                                        mailService.sendNoPlayDescEmail(player.getUser(), tour, lastCategory.getCategory(), pph);
                                    }
                                } catch (Exception e) {
                                    log.debug("Guardo el History");
                                    pph = new PlayerPointHistory();
                                    pph.setCategory(player.getCategory());
                                    pph.setPlayerPoint(player);
                                    pph.setSeason(sea);
                                    pph.setPoints(0F);
                                    pph.setCantEvent(0);
                                    pph.setTotalPoints(points);
                                    playerPointHistoryRepository.save(pph);
                                    log.debug("No hay registro history, se considera que no subio, mantiene");
                                    mailService.sendNoPlayKeepEmail(player.getUser(), tour, player.getCategory(), pph);
                                }
                            }
                        } else {
                            List<Event> eventos = eventRepository.findByTournamentAndSeason(tour, sea);
                            Integer cantEventos = getQtyEvents(player.getUser(), eventos);
                            log.debug("Cant Eventos: {}", cantEventos);
                            log.debug("Tamanio Eventos: {}", eventos);
                            if (eventos.size() < 3 || (eventos.size() >= 3 && cantEventos > 1)) {
                                Float pointsDiv = points / (cantEventos * 100);
                                log.debug("Puntaje: {}", pointsDiv);
                                log.debug("Guardo el History");
                                pph = new PlayerPointHistory();
                                pph.setCategory(player.getCategory());
                                pph.setPlayerPoint(player);
                                pph.setSeason(sea);
                                pph.setPoints(pointsDiv * 100);
                                pph.setCantEvent(cantEventos);
                                pph.setTotalPoints(points);
                                playerPointHistoryRepository.save(pph);
                                if ((pointsDiv * 100) > up) {
                                    log.debug("Asciende");
                                    try {
                                        Category newCategory = categoryRepository.findByTournamentAndOrder(
                                            tour,
                                            player.getCategory().getOrder() - 1
                                        );
                                        if (newCategory.getOrder() != 0) {
                                            log.debug("Nueva Categoria: {}", newCategory);
                                            player.setCategory(newCategory);
                                            playerPointRepository.save(player);
                                            log.debug("Actualizado");
                                            mailService.sendAscendEmail(player.getUser(), tour, newCategory, pph);
                                        } else {
                                            log.debug("No hay categoria mayor, mantiene");
                                            mailService.sendAscendEmail(player.getUser(), tour, player.getCategory(), pph);
                                        }
                                    } catch (Exception e) {
                                        log.debug(e.getMessage());
                                    }
                                } else {
                                    if ((pointsDiv * 100) <= down) {
                                        log.debug("Desciende Categoria");
                                        Category newCategory = categoryRepository.findByTournamentAndOrder(
                                            tour,
                                            player.getCategory().getOrder() + 1
                                        );
                                        log.debug("Nueva Categoria: {}", newCategory);
                                        player.setCategory(newCategory);
                                        playerPointRepository.save(player);
                                        log.debug("Actualizado");
                                        mailService.sendDescendEmail(player.getUser(), tour, newCategory, pph);
                                    } else {
                                        log.debug("Mantiene");
                                        mailService.sendDescendEmail(player.getUser(), tour, player.getCategory(), pph);
                                    }
                                }
                            } else {
                                log.debug("No tiene suficientes puntos para calcular categoria, mantiene categoria");
                                log.debug("Guardo el History");
                                pph = new PlayerPointHistory();
                                pph.setCategory(player.getCategory());
                                pph.setPlayerPoint(player);
                                pph.setSeason(sea);
                                pph.setPoints(0F);
                                pph.setCantEvent(cantEventos);
                                pph.setTotalPoints(points);
                                playerPointHistoryRepository.save(pph);
                                mailService.sendKeepEmail(player.getUser(), tour, player.getCategory(), pph);
                            }
                        }
                    }
                    log.debug("Cierro Temporada: {}", sea);
                    sea.setStatus(Status.DONE);
                    seasonRepository.save(sea);
                    log.debug("Temporada Cerrada");
                }
            } else {
                log.debug("No Categoriza");
            }
        }
        log.info("Cierre proceso de cierre de temporada");
    }
}
