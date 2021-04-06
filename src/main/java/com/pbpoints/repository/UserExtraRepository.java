package com.pbpoints.repository;

import com.pbpoints.domain.User;
import com.pbpoints.domain.UserExtra;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the UserExtra entity.
 */
@Repository
public interface UserExtraRepository extends JpaRepository<UserExtra, Long> {
    public Optional<UserExtra> findByUser(User user);
}
