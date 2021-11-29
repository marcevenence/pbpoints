package com.pbpoints.repository;

import com.pbpoints.domain.Equipment;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Equipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>, JpaSpecificationExecutor<Equipment> {
    @Query("select equipment from Equipment equipment where equipment.user.login = ?#{principal.username}")
    List<Equipment> findByUserIsCurrentUser();
}
