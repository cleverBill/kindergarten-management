package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    @Query("SELECT a FROM Announcement a ORDER BY a.isTop DESC, a.createdAt DESC")
    List<Announcement> findAllOrderByPinnedAndCreatedAt();

    List<Announcement> findByScopeOrderByCreatedAtDesc(String scope);

    List<Announcement> findByScopeAndGradeOrderByCreatedAtDesc(String scope, String grade);

    List<Announcement> findByScopeAndClassNameOrderByCreatedAtDesc(String scope, String className);

    @Query("SELECT a FROM Announcement a WHERE a.scope = 'all' OR a.scope = 'GRADE' OR a.scope = 'CLASS' ORDER BY a.isTop DESC, a.createdAt DESC")
    List<Announcement> findAllForUser();
}