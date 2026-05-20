package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.TeacherPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface TeacherPerformanceRepository extends JpaRepository<TeacherPerformance, Long> {
    Optional<TeacherPerformance> findByTeacherIdAndMonth(Long teacherId, String month);
    List<TeacherPerformance> findByTeacherIdOrderByMonthDesc(Long teacherId);
    List<TeacherPerformance> findByMonth(String month);
}
