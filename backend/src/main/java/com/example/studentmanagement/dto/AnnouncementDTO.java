package com.example.studentmanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDTO {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private String scope;
    private String grade;
    private String className;
    private Boolean isPinned;
    private Boolean isUrgent;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long readCount;
    private Long totalCount;
    private Boolean isRead;
}