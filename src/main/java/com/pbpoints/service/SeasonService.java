package com.pbpoints.service;

import com.pbpoints.domain.Season;
import com.pbpoints.repository.SeasonRepository;
import com.pbpoints.service.dto.SeasonDTO;
import com.pbpoints.service.dto.TournamentDTO;
import com.pbpoints.service.mapper.SeasonMapper;
import com.pbpoints.service.mapper.TournamentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Season}.
 */
@Service
@Transactional
public class SeasonService {

    private final Logger log = LoggerFactory.getLogger(SeasonService.class);

    private final SeasonRepository seasonRepository;

    private final SeasonMapper seasonMapper;

    private final TournamentMapper tournamentMapper;

    public SeasonService(SeasonRepository seasonRepository, SeasonMapper seasonMapper, TournamentMapper tournamentMapper) {
        this.seasonRepository = seasonRepository;
        this.seasonMapper = seasonMapper;
        this.tournamentMapper = tournamentMapper;
    }

    /**
     * Save a season.
     *
     * @param seasonDTO the entity to save.
     * @return the persisted entity.
     */
    public SeasonDTO save(SeasonDTO seasonDTO) {
        log.debug("Request to save Season : {}", seasonDTO);
        Season season = seasonMapper.toEntity(seasonDTO);
        season = seasonRepository.save(season);
        return seasonMapper.toDto(season);
    }

    /**
     * Partially update a season.
     *
     * @param seasonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SeasonDTO> partialUpdate(SeasonDTO seasonDTO) {
        log.debug("Request to partially update Season : {}", seasonDTO);

        return seasonRepository
            .findById(seasonDTO.getId())
            .map(
                existingSeason -> {
                    seasonMapper.partialUpdate(existingSeason, seasonDTO);

                    return existingSeason;
                }
            )
            .map(seasonRepository::save)
            .map(seasonMapper::toDto);
    }

    /**
     * Get all the seasons.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SeasonDTO> findAll() {
        log.debug("Request to get all Seasons");
        return seasonRepository.findAll().stream().map(seasonMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one season by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SeasonDTO> findOne(Long id) {
        log.debug("Request to get Season : {}", id);
        return seasonRepository.findById(id).map(seasonMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<SeasonDTO> findByTournamentAndAnio(TournamentDTO tournament, Integer anio) {
        log.debug("Request to get Season by Tournament and Year : {}", tournament, anio);
        return Optional.of(seasonMapper.toDto(seasonRepository.findByTournamentAndAnio(tournamentMapper.toEntity(tournament), anio)));
    }

    /**
     * Delete the season by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Season : {}", id);
        seasonRepository.deleteById(id);
    }
}
