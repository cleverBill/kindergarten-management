package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.KindergartenConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface KindergartenConfigRepository extends JpaRepository<KindergartenConfig, Long> {
    Optional<KindergartenConfig> findByConfigKey(String configKey);
}
