package com.pbpoints.repository;

import com.pbpoints.domain.Bracket;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Bracket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BracketRepository extends JpaRepository<Bracket, Long> {
    Optional<Bracket> findByTeams(Integer teams);
}
