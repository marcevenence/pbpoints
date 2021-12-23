package com.pbpoints.repository;

import com.pbpoints.domain.RosterEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RosterEventRepository extends JpaRepository<RosterEvent, Long> {
    List<RosterEvent> findByEvCatId(Long evCatId);
}
