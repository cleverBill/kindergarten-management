package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.FeeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FeeItemRepository extends JpaRepository<FeeItem, Long> {
    List<FeeItem> findByStatusOrderByCreatedAtDesc(String status);

    @Query("SELECT f FROM FeeItem f WHERE f.grade = :grade OR f.grade IS NULL")
    List<FeeItem> findByGrade(@Param("grade") String grade);

    @Query("SELECT f FROM FeeItem f WHERE f.className = :className OR f.className IS NULL")
    List<FeeItem> findByClassName(@Param("className") String className);
}