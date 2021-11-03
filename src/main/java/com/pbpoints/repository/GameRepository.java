package com.pbpoints.repository;

import com.pbpoints.domain.EventCategory;
import com.pbpoints.domain.Game;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Game entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {
    public List<Game> findByEventCategory(EventCategory eventCategory);

    public List<Game> findByEventCategoryAndClasif(EventCategory eventCategory, String clasif);
}
