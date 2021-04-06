package com.pbpoints.repository;

import com.pbpoints.domain.Team;
import com.pbpoints.domain.TeamPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TeamPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamPointRepository extends JpaRepository<TeamPoint, Long>, JpaSpecificationExecutor<TeamPoint> {
    TeamPoint findByTeam(Team team);
}
