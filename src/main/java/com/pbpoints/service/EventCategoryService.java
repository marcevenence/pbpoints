package com.pbpoints.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.pbpoints.domain.*;
import com.pbpoints.domain.enumeration.TimeType;
import com.pbpoints.repository.*;
import com.pbpoints.service.dto.EventCategoryDTO;
import com.pbpoints.service.dto.EventDTO;
import com.pbpoints.service.dto.RosterDTO;
import com.pbpoints.service.dto.xml.GameResultDTO;
import com.pbpoints.service.mapper.EventCategoryMapper;
import com.pbpoints.service.util.FixtureUtils;
import com.pbpoints.service.util.FixtureUtils.Partido;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import javax.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EventCategory}.
 */
@Service
@Transactional
public class EventCategoryService {

    private final Logger log = LoggerFactory.getLogger(EventCategoryService.class);

    private final EventCategoryRepository eventCategoryRepository;

    private final EventCategoryMapper eventCategoryMapper;

    private final EventService eventService;

    private final RosterRepository rosterRepository;

    private final GameRepository gameRepository;

    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final UserExtraRepository userExtraRepository;

    private final com.pbpoints.service.GameService gameService;

    private final com.pbpoints.service.BracketService bracketService;

    public EventCategoryService(
        EventCategoryRepository eventCategoryRepository,
        EventCategoryMapper eventCategoryMapper,
        EventService eventService,
        RosterRepository rosterRepository,
        GameRepository gameRepository,
        EventRepository eventRepository,
        CategoryRepository categoryRepository,
        UserExtraRepository userExtraRepository,
        com.pbpoints.service.GameService gameService,
        com.pbpoints.service.BracketService bracketService
    ) {
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventCategoryMapper = eventCategoryMapper;
        this.eventService = eventService;
        this.rosterRepository = rosterRepository;
        this.gameRepository = gameRepository;
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userExtraRepository = userExtraRepository;
        this.gameService = gameService;
        this.bracketService = bracketService;
    }

    /**
     * Save a eventCategory.
     *
     * @param eventCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public EventCategoryDTO save(EventCategoryDTO eventCategoryDTO) {
        log.debug("Request to save EventCategory : {}", eventCategoryDTO);
        EventCategory eventCategory = eventCategoryMapper.toEntity(eventCategoryDTO);
        // Validaciones de duplicidad
        Optional<EventCategory> optional = eventCategoryRepository.findByEventAndCategory(
            eventCategory.getEvent(),
            eventCategory.getCategory()
        );
        if (optional.isPresent()) {
            log.error(optional.get().toString());
            throw new DuplicateKeyException("Ya existe un eventCategory con los datos ingresados");
        }
        eventCategory = eventCategoryRepository.save(eventCategory);
        return eventCategoryMapper.toDto(eventCategory);
    }

    /**
     * Get all the eventCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EventCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventCategories");
        return eventCategoryRepository.findAll(pageable).map(eventCategoryMapper::toDto);
    }

    /**
     * Get one eventCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventCategoryDTO> findOne(Long id) {
        log.debug("Request to get EventCategory : {}", id);
        return eventCategoryRepository.findById(id).map(eventCategoryMapper::toDto);
    }

    /**
     * Delete the eventCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EventCategory : {}", id);
        eventCategoryRepository.deleteById(id);
    }

    /**
     * A partir de todos los los equipos que van a participar en un
     * evento-categoria, se genera el fixture para generar los games
     *
     * @param eventCategory {@link EventCategory}
     */
    public void generarFixture(EventCategory eventCategory) {
        if (eventCategory.getEvent() == null) {
            throw new NoResultException("No hay un evento cargado");
        }
        log.info("*** Generando fixture para el evento {} - categoria {}", eventCategory.getEvent(), eventCategory.getCategory());
        List<Roster> rosters = rosterRepository.findByEventCategory(eventCategory);
        log.debug(rosters.toString());
        if (!rosters.isEmpty()) {
            // Creo un set para que se guarden los teams que sean diferentes
            Set<Team> teamsSet = new HashSet<>();
            rosters.forEach(r -> teamsSet.add(r.getTeam()));
            // elimino los viejos games
            eventCategory.getGames().forEach(gameRepository::delete);
            eventCategory.getGames().clear();
            // paso el set a Array, para poder usar mejor las busquedas
            List<Team> teams = new ArrayList<>(teamsSet);
            log.debug("Equipos que disputan el evento-categoria: {}", teams);
            // Desordeno los equipos para modificar el orden de juego siempre
            Collections.shuffle(teams);
            log.debug("Teams desordenados: {}", teams);
            log.debug("Cantidad de equipos: {}", teams.size());

            // Separo los grupos de acuerdo a la cantidad de equipos que haya
            Map<Integer, List<Team>> map = this.armarTeams(teams);
            // con la cantidad de grupos que tengo, creo los juegos
            Set<Game> games = this.armarGames(eventCategory, map);
            eventCategory.setGames(games);
            eventCategoryRepository.save(eventCategory);
        }
    }

    public Map<Integer, List<Team>> armarTeams(List<Team> teamsOrigin) {
        Map<Integer, List<Team>> map = new HashMap<>();
        int cantGrupos = 0;
        int posicion = 0;
        List<Team> teams;
        Bracket bracket = bracketService.findByTeams(teamsOrigin.size());
        log.debug(bracket.toString());
        // Si es -1, es un RR asi que uso toda la lista de teams
        if (bracket.getTeams5A() == -1) {
            map.put(0, teamsOrigin);
            log.info(map.toString());
            return map;
        }
        Integer cantidad5 = bracket.getTeams5A() != 0 ? bracket.getTeams5A() : bracket.getTeams5B();
        Integer cantidad6 = bracket.getTeams6A() != 0 ? bracket.getTeams6A() : bracket.getTeams6B();
        log.info("Cantidad de grupos de 5 Teams: {}", cantidad5);
        log.info("Cantidad de grupos de 6 Teams: {}", cantidad6);
        // Agrupo por la cantidad de repeticiones de juegos de 5
        for (int j = 0; j < cantidad5; j++) {
            teams = new ArrayList<>();
            for (int k = 0; k < 5; k++) {
                teams.add(teamsOrigin.get(posicion++));
            }
            map.put(cantGrupos++, teams);
        }
        // Agrupo por la cantidad de repeticiones de juegos de 6
        for (int j = 0; j < cantidad6; j++) {
            teams = new ArrayList<>();
            for (int k = 0; k < 6; k++) {
                teams.add(teamsOrigin.get(posicion++));
            }
            map.put(cantGrupos++, teams);
        }
        for (Integer key : map.keySet()) {
            log.info("Grupo: {}", (key + 1));
            log.info("Teams: {}", map.get(key));
        }
        return map;
    }

    private Set<Game> armarGames(EventCategory eventCategory, Map<Integer, List<Team>> map) {
        log.debug("Mapeando fixture a Games");
        Set<Game> games = new HashSet<>();
        map.forEach(
            (k, v) -> {
                // Calculo el fixture con los equipos que tengo
                Partido[][] rondas = FixtureUtils.calcularLiga(v.size());
                FixtureUtils.mostrarPartidos(rondas, Boolean.FALSE);
                int serie = 1;
                int cantSerie = 0;
                for (int i = 0; i < rondas.length; i++) {
                    log.debug("Game " + (i + 1) + ": ");
                    for (int j = 0; j < rondas[i].length; j++) {
                        Game game = new Game();
                        game.setGroup(k + 1);
                        /*Agrego Logica de Nro se Series*/
                        if (eventCategory.getSplitDeck()) {
                            cantSerie = cantSerie + 1;
                            if (cantSerie == 3) {
                                cantSerie = 1;
                                serie = serie + 1;
                            }
                            game.setSplitDeckNum(serie);
                        } else {
                            game.setSplitDeckNum(0);
                        }
                        if (eventCategory.getCategory().getGameTimeType() == TimeType.MINUTES) game.setTimeLeft(
                            eventCategory.getCategory().getGameTime() * 60
                        );
                        if (eventCategory.getCategory().getGameTimeType() == TimeType.SECONDS) game.setTimeLeft(
                            eventCategory.getCategory().getGameTime()
                        );
                        game.setTeamA(v.get((rondas[i][j].local)));
                        game.setTeamB(v.get((rondas[i][j].visitante)));
                        log.info("   " + game.getTeamA().toString() + "-" + game.getTeamB().toString());
                        game.setEventCategory(eventCategory);
                        games.add(game);
                    }
                }
            }
        );
        return games;
    }

    public void generarFixture(Long idEventCategory) throws NoResultException {
        Optional<EventCategory> eventCategory = eventCategoryRepository.findById(idEventCategory);
        if (eventCategory.isPresent()) {
            this.generarFixture(eventCategory.get());
        } else {
            throw new NoResultException("No se encontró un eventCategory para generar fixture");
        }
    }

    @Scheduled(cron = "${application.cronFixture}")
    public void generarTodosfixture() {
        log.info("*** Inicio de generación automática de fixtures ***");
        Optional<List<EventCategory>> eventCategories = eventCategoryRepository.findByEvent_EndInscriptionDate(LocalDate.now());
        if (eventCategories.isPresent()) {
            for (EventCategory eventCategory : eventCategories.get()) {
                this.generarFixture(eventCategory);
            }
        } else {
            log.info("no hay eventos-categorías para el día actual");
        }
        log.info("*** Fin de generación automática de fixtures ***");
    }

    public long validEvent(Long eventId) {
        Optional<EventDTO> event = eventService.findOne(eventId);
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
