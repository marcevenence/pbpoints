package com.pbpoints.repository;

import com.pbpoints.domain.Format;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Format entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormatRepository extends JpaRepository<Format, Long>, JpaSpecificationExecutor<Format> {}
