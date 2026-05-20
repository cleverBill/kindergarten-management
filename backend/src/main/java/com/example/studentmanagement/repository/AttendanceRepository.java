package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentIdOrderByDateDesc(Long studentId);
    List<Attendance> findAllByOrderByDateDesc();
    List<Attendance> findByDate(LocalDate date);
    long countByDate(LocalDate date);
    
    @Query("SELECT a.status, COUNT(a) FROM Attendance a WHERE a.date BETWEEN :start AND :end GROUP BY a.status")
    List<Object[]> findAttendanceCountByDate(LocalDate start, LocalDate end);
    
    @Query("SELECT a.id, a.studentId, a.date, a.status, a.reason FROM Attendance a WHERE a.studentId = ?1 ORDER BY a.date DESC")
    List<Object[]> findByStudentId(Long studentId);
    
    @Query("SELECT a.id, a.studentId, a.date, a.status, a.reason FROM Attendance a WHERE a.studentId = ?1 ORDER BY a.date DESC")
    List<Object[]> findRecentByStudentId(Long studentId);
}
