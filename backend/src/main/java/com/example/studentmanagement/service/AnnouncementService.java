package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.AnnouncementDTO;
import com.example.studentmanagement.entity.Announcement;
import com.example.studentmanagement.entity.AnnouncementReadRecord;
import com.example.studentmanagement.repository.AnnouncementReadRecordRepository;
import com.example.studentmanagement.repository.AnnouncementRepository;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private AnnouncementReadRecordRepository readRecordRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<AnnouncementDTO> getAllAnnouncements() {
        List<Announcement> announcements = announcementRepository.findAllOrderByPinnedAndCreatedAt();
        return announcements.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public AnnouncementDTO createAnnouncement(AnnouncementDTO dto) {
        Announcement announcement = new Announcement();
        announcement.setTitle(dto.getTitle());
        announcement.setContent(dto.getContent());
        announcement.setImageUrl(dto.getImageUrl());
        announcement.setScope(dto.getScope());
        announcement.setGrade(dto.getGrade());
        announcement.setClassName(dto.getClassName());
        announcement.setIsTop(dto.getIsPinned() != null ? dto.getIsPinned() : false);
        announcement.setIsUrgent(dto.getIsUrgent() != null ? dto.getIsUrgent() : false);
        announcement.setCreatedBy(dto.getCreatedBy());
        Announcement saved = announcementRepository.save(announcement);
        return convertToDTO(saved);
    }

    @Transactional
    public AnnouncementDTO updateAnnouncement(Long id, AnnouncementDTO dto) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("公告不存在"));
        announcement.setTitle(dto.getTitle());
        announcement.setContent(dto.getContent());
        announcement.setImageUrl(dto.getImageUrl());
        announcement.setScope(dto.getScope());
        announcement.setGrade(dto.getGrade());
        announcement.setClassName(dto.getClassName());
        announcement.setIsTop(dto.getIsPinned());
        announcement.setIsUrgent(dto.getIsUrgent());
        Announcement saved = announcementRepository.save(announcement);
        return convertToDTO(saved);
    }

    @Transactional
    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
        readRecordRepository.deleteByAnnouncementId(id);
    }

    @Transactional
    public void markAsRead(Long announcementId, Long userId, String username) {
        if (!readRecordRepository.existsByAnnouncementIdAndUserId(announcementId, userId)) {
            AnnouncementReadRecord record = new AnnouncementReadRecord();
            record.setAnnouncementId(announcementId);
            record.setUserId(userId);
            record.setUsername(username);
            readRecordRepository.save(record);
        }
    }

    public AnnouncementDTO getAnnouncementById(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("公告不存在"));
        return convertToDTO(announcement);
    }

    private AnnouncementDTO convertToDTO(Announcement announcement) {
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setId(announcement.getId());
        dto.setTitle(announcement.getTitle());
        dto.setContent(announcement.getContent());
        dto.setImageUrl(announcement.getImageUrl());
        dto.setScope(announcement.getScope());
        dto.setGrade(announcement.getGrade());
        dto.setClassName(announcement.getClassName());
        dto.setIsPinned(announcement.getIsTop());
        dto.setIsUrgent(announcement.getIsUrgent());
        dto.setCreatedBy(announcement.getCreatedBy());
        dto.setCreatedAt(announcement.getCreatedAt());
        dto.setUpdatedAt(announcement.getUpdatedAt());

        long readCount = readRecordRepository.countByAnnouncementId(announcement.getId());
        dto.setReadCount(readCount);

        long totalCount = studentRepository.count();
        dto.setTotalCount(totalCount);

        return dto;
    }
}
