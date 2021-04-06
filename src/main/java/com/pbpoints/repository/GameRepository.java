package com.pbpoints.repository;

import com.pbpoints.domain.EventCategory;
import com.pbpoints.domain.Game;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Game entity.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {
    public List<Game> findByEventCategory(EventCategory eventCategory);
}
