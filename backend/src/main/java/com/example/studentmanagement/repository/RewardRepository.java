package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findByStudentIdOrderByDateDesc(Long studentId);
    List<Reward> findAllByOrderByDateDesc();
    
    @Query("SELECT COUNT(r) FROM Reward r WHERE r.date BETWEEN :start AND :end")
    Integer countByDateRange(LocalDate start, LocalDate end);
    
    @Query("SELECT s.name, COUNT(r) FROM Reward r JOIN Student s ON r.studentId = s.id GROUP BY s.id, s.name ORDER BY COUNT(r) DESC")
    List<Object[]> findRewardRanking();
    
    @Query("SELECT COUNT(r) FROM Reward r WHERE r.studentId = ?1")
    List<Object[]> countByStudentId(Long studentId);
    
    @Query("SELECT r.id, s.name, r.type, r.description, r.date FROM Reward r JOIN Student s ON r.studentId = s.id WHERE r.studentId = ?1 ORDER BY r.date DESC")
    List<Object[]> findRecentByStudentId(Long studentId);
}
