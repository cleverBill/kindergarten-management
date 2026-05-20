package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    List<Homework> findByStudentIdOrderByDateDesc(Long studentId);
    List<Homework> findAllByOrderByDateDesc();
    
    long countByDateBetween(LocalDate start, LocalDate end);
    
    @Query("SELECT COUNT(h) FROM Homework h WHERE h.date BETWEEN :start AND :end")
    Integer countByDateRange(LocalDate start, LocalDate end);
    
    @Query("SELECT h.id, h.studentId, h.name, h.score, h.date FROM Homework h WHERE h.studentId = ?1 ORDER BY h.date DESC")
    List<Object[]> findByStudentId(Long studentId);
    
    @Query("SELECT h.id, h.studentId, h.name, h.score, h.date FROM Homework h WHERE h.studentId = ?1 ORDER BY h.date DESC")
    List<Object[]> findRecentByStudentId(Long studentId);
}
