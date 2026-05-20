package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.AnnouncementReadRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnouncementReadRecordRepository extends JpaRepository<AnnouncementReadRecord, Long> {

    List<AnnouncementReadRecord> findByAnnouncementId(Long announcementId);

    boolean existsByAnnouncementIdAndUserId(Long announcementId, Long userId);

    @Query("SELECT COUNT(r) FROM AnnouncementReadRecord r WHERE r.announcementId = ?1")
    long countByAnnouncementId(Long announcementId);

    @Query("SELECT r FROM AnnouncementReadRecord r WHERE r.announcementId = ?1 AND r.userId = ?2")
    AnnouncementReadRecord findByAnnouncementIdAndUserId(Long announcementId, Long userId);

    // ==========补上你缺失的删除方法（修复报错）==========
    void deleteByAnnouncementId(Long announcementId);
}
