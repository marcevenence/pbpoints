package com.pbpoints.repository;

import com.pbpoints.domain.Format;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Format entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormatRepository extends JpaRepository<Format, Long>, JpaSpecificationExecutor<Format> {}
