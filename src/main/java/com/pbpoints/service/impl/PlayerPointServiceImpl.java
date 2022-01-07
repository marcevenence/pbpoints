package com.pbpoints.service.impl;

import com.pbpoints.domain.*;
import com.pbpoints.domain.PlayerPoint;
import com.pbpoints.repository.*;
import com.pbpoints.repository.PlayerPointRepository;
import com.pbpoints.service.CategoryService;
import com.pbpoints.service.PlayerPointService;
import com.pbpoints.service.TournamentService;
import com.pbpoints.service.UserService;
import com.pbpoints.service.dto.CategoryDTO;
import com.pbpoints.service.dto.PlayerPointDTO;
import com.pbpoints.service.dto.TournamentDTO;
import com.pbpoints.service.dto.xml.PositionDTO;
import com.pbpoints.service.mapper.PlayerPointMapper;
import com.pbpoints.service.mapper.TournamentMapper;
import com.pbpoints.web.rest.errors.BadRequestAlertException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PlayerPoint}.
 */
@Service
@Transactional
public class PlayerPointServiceImpl implements PlayerPointService {

    private final Logger log = LoggerFactory.getLogger(PlayerPointServiceImpl.class);

    private final PlayerPointRepository playerPointRepository;
    private final PlayerPointMapper playerPointMapper;
    private final CategoryService categoryService;
    private final UserService userService;
    private final TournamentService tournamentService;
    private final TournamentMapper tournamentMapper;

    private final RosterRepository rosterRepository;

    private final TeamRepository teamRepository;

    private final EventCategoryRepository eventCategoryRepository;

    private final CategoryRepository categoryRepository;

    private final PlayerRepository playerRepository;

    private final PlayerDetailPointRepository playerDetailPointRepository;

    public PlayerPointServiceImpl(
        PlayerPointRepository playerPointRepository,
        PlayerPointMapper playerPointMapper,
        CategoryService categoryService,
        UserService userService,
        TournamentService tournamentService,
        TournamentMapper tournamentMapper,
        RosterRepository rosterRepository,
        TeamRepository teamRepository,
        EventCategoryRepository eventCategoryRepository,
        CategoryRepository categoryRepository,
        PlayerRepository playerRepository,
        PlayerDetailPointRepository playerDetailPointRepository
    ) {
        this.playerPointRepository = playerPointRepository;
        this.playerPointMapper = playerPointMapper;
        this.categoryService = categoryService;
        this.userService = userService;
        this.tournamentService = tournamentService;
        this.tournamentMapper = tournamentMapper;
        this.rosterRepository = rosterRepository;
        this.teamRepository = teamRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.playerRepository = playerRepository;
        this.playerDetailPointRepository = playerDetailPointRepository;
    }

    /**
     * Save a playerPoint.
     *
     * @param playerPointDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PlayerPointDTO save(PlayerPointDTO playerPointDTO) {
        log.debug("Request to save PlayerPoint : {}", playerPointDTO);
        PlayerPoint playerPoint = playerPointMapper.toEntity(playerPointDTO);
        playerPoint = playerPointRepository.save(playerPoint);
        return playerPointMapper.toDto(playerPoint);
    }

    /**
     * Get all the playerPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PlayerPointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PlayerPoints");
        return playerPointRepository.findAll(pageable).map(playerPointMapper::toDto);
    }

    /**
     * Get one playerPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PlayerPointDTO> findOne(Long id) {
        log.debug("Request to get PlayerPoint : {}", id);
        return playerPointRepository.findById(id).map(playerPointMapper::toDto);
    }

    @Transactional
    public PlayerPointDTO findByUserAndTournament(User user, Tournament tournament) throws BadRequestAlertException {
        return playerPointMapper.toDto(playerPointRepository.findByUserAndTournament(user, tournament));
    }

    @Transactional
    public PlayerPointDTO findByUserAndTournamentAndCategory(Long userId, Long tournamentId, Long categoryId)
        throws BadRequestAlertException {
        log.debug("Request to get PlayerPoint : UserId: {}, TournamentId: {}, CategoryId: {}", userId, tournamentId, categoryId);

        // Busco los datos que me pasaron por request
        Optional<CategoryDTO> categoryDTO = categoryService.findOne(categoryId);
        Optional<User> user = userService.getUser(userId);
        Optional<TournamentDTO> tournamentDTO = tournamentService.findOne(tournamentId);

        // Valido los datos
        if (tournamentDTO.isEmpty()) {
            throw new BadRequestAlertException("No se encontró el torneo", "TOURNAMENT", "playerPointCategoryError");
        }
        if (categoryDTO.isEmpty()) {
            throw new BadRequestAlertException("No se encontró la categoria", "CATEGORY", "playerPointCategoryError");
        }
        if (user.isEmpty()) {
            throw new BadRequestAlertException("No se encontró al usuario", "USER", "playerPointCategoryError");
        }

        // Paso el torneo a entidad
        Tournament tournament = tournamentMapper.toEntity(tournamentDTO.get());

        // Busco al usuario para ese torneo
        PlayerPoint playerPoint = playerPointRepository.findByUserAndTournament(user.get(), tournament);

        // Si no encuentro al playerPoint, es porque es la primera vez que entra.. lo creo
        if (playerPoint == null) {
            Category category = categoryRepository.LastCategoryByTournamentId(tournamentId);
            playerPoint = new PlayerPoint();
            playerPoint.setTournament(tournament);
            playerPoint.setPoints((float) 0);
            playerPoint.setUser(user.get());
            playerPoint.setCategory(category);

            playerPoint = playerPointRepository.save(playerPoint);
        }

        // valido si el usuario, puede participar de la categoria del torneo
        if (playerPoint.getCategory().getOrder() >= categoryDTO.get().getOrder()) {
            log.debug(
                "Categoria validada --> PlayerCategory: {}, Category{}",
                playerPoint.getCategory().getOrder(),
                categoryDTO.get().getOrder()
            );
            return playerPointMapper.toDto(playerPoint);
        }
        throw new BadRequestAlertException("Categoría no válida para el player", "PLAYER_POINT", "playerPointCategoryError");
    }

    /**
     * Delete the playerPoint by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PlayerPoint : {}", id);
        playerPointRepository.deleteById(id);
    }

    public void distPoints(List<PositionDTO> positions, Event event) {
        for (PositionDTO pos : positions) {
            Team team = teamRepository.findById(pos.getTeamId()).get();
            log.info("Team: {}", team);
            Category category = categoryRepository.findById(pos.getCategoryId()).get();
            log.info("Category: {}", category);
            EventCategory eventCategory = eventCategoryRepository.findByEventAndCategory(event, category).get();
            log.info("EventCategory: {}", eventCategory);
            Roster roster = rosterRepository.findByTeamAndEventCategory(team, eventCategory);
            log.info("Roster: {}", roster);
            List<Player> players = playerRepository.findByRoster(roster);
            log.info("Players: {}", players);
            PlayerPoint playerPoint;
            for (Player pl : players) {
                /*Cargo Player Points*/
                try {
                    playerPoint = playerPointRepository.findByUserAndTournament(pl.getUser(), event.getTournament());
                    playerPoint.setPoints(playerPoint.getPoints() + pos.getPoints());
                    playerPointRepository.save(playerPoint);
                } catch (Exception e) {
                    playerPoint = new PlayerPoint();
                    playerPoint.setPoints(pos.getPoints());
                    playerPoint.setTournament(event.getTournament());
                    playerPoint.setUser(pl.getUser());
                    playerPoint.setCategory(categoryRepository.LastCategoryByTournamentId(event.getTournament().getId()));
                    playerPointRepository.save(playerPoint);
                }
                /*Cargo Player Detail Points*/
                PlayerDetailPoint playerDetailPoint = new PlayerDetailPoint();
                playerDetailPoint.setPlayerPoint(playerPoint);
                playerDetailPoint.setPoints(pos.getPoints());
                playerDetailPoint.setEventCategory(eventCategory);
                playerDetailPointRepository.save(playerDetailPoint);
            }
        }
    }
}
