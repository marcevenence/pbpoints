package com.pbpoints.repository;

import com.pbpoints.domain.Category;
import com.pbpoints.domain.Event;
import com.pbpoints.domain.EventCategory;
import com.pbpoints.domain.Format;
import com.pbpoints.domain.Roster;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EventCategory entity.
 */
@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Long>, JpaSpecificationExecutor<EventCategory> {
    Optional<List<EventCategory>> findByEvent_EndInscriptionDate(LocalDate localDate);

    public List<EventCategory> findByEvent(Event event);

    EventCategory findByRosters(Roster roster);

    Optional<EventCategory> findByEventAndCategory(Event event, Category category);

    Optional<EventCategory> findByEventAndCategoryAndFormat(Event event, Category category, Format format);
}
