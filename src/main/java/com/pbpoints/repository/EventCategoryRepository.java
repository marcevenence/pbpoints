package com.pbpoints.repository;

import com.pbpoints.domain.EventCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EventCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Long>, JpaSpecificationExecutor<EventCategory> {}
