package com.pbpoints.service;

import com.pbpoints.domain.*;
import com.pbpoints.repository.*;
import com.pbpoints.service.dto.UserExtraDTO;
import com.pbpoints.service.mapper.UserExtraMapper;
import java.util.Random;
import javax.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserExtra}.
 */
@Service
@Transactional
public class BracketService {

    private final Logger log = LoggerFactory.getLogger(BracketService.class);

    private final BracketRepository bracketRepository;

    public BracketService(BracketRepository bracketRepository) {
        this.bracketRepository = bracketRepository;
    }

    @Transactional(readOnly = true)
    public Bracket findByTeams(Integer teams) {
        Bracket bracket = null;
        log.info("Cantidad de teams: {}", teams);
        if (teams < 5) {
            // creo el objeto en memoria, porque no están mapeados los equipos menores a 5 en la base
            bracket = new Bracket();
            bracket.setTeams(teams);
            bracket.setTeams5A(-1);
            bracket.setTeams5A(0);
            bracket.setTeams5B(0);
            bracket.setTeams6A(0);
            bracket.setTeams6B(0);
        } else {
            bracket = bracketRepository.findByTeams(teams).orElseThrow(() -> new NoResultException("No se encontraron datos"));
        }
        // En el caso de que haya una combinación entre 5 y 6 equipos
        if (bracket.getTeams5A() != -1 || (bracket.getTeams5B() != 0 && bracket.getTeams6B() != 0)) {
            Random random = new Random();
            Boolean a = random.nextBoolean();
            if (a) {
                // Se toman los valores de A
                bracket.setTeams5B(0);
                bracket.setTeams6B(0);
            } else {
                // Se toman los valores de B
                bracket.setTeams5A(0);
                bracket.setTeams5B(0);
            }
        }
        log.info(bracket.toString());
        return bracket;
    }
}
