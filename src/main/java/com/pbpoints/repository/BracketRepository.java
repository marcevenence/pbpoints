package com.pbpoints.repository;

import com.pbpoints.domain.Bracket;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Bracket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BracketRepository extends JpaRepository<Bracket, Long> {}
