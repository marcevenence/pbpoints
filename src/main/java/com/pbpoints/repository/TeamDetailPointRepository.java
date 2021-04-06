package com.pbpoints.repository;

import com.pbpoints.domain.TeamDetailPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TeamDetailPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamDetailPointRepository extends JpaRepository<TeamDetailPoint, Long>, JpaSpecificationExecutor<TeamDetailPoint> {}
