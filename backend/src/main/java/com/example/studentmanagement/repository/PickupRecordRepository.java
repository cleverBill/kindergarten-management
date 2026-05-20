package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.PickupRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface PickupRecordRepository extends JpaRepository<PickupRecord, Long> {
    List<PickupRecord> findByStudentIdOrderByPickupTimeDesc(Long studentId);

    @Query("SELECT p FROM PickupRecord p WHERE p.pickupTime >= :start AND p.pickupTime <= :end ORDER BY p.pickupTime DESC")
    List<PickupRecord> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<PickupRecord> findByStatusOrderByPickupTimeDesc(String status);

    @Query("SELECT COUNT(p) FROM PickupRecord p WHERE p.pickupTime >= :start AND p.pickupTime <= :end AND p.status = :status")
    Integer countByDateRangeAndStatus(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("status") String status);
}