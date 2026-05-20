package com.example.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "teacher_performance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(length = 50)
    private String month;

    @Column(name = "attendance_count")
    private Integer attendanceCount = 0;

    @Column(name = "homework_count")
    private Integer homeworkCount = 0;

    @Column(name = "reward_count")
    private Integer rewardCount = 0;

    @Column(name = "post_count")
    private Integer postCount = 0;

    @Column(name = "message_count")
    private Integer messageCount = 0;

    @Column(name = "parent_rating")
    private Double parentRating = 0.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}