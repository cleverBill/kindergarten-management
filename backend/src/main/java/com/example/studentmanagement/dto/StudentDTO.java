package com.example.studentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String name;
    private String grade;
    private String className;
    private String status;
    private LocalDateTime createdAt;
    private String gender;
    private Integer age;
    private String parentContact;
    private String homeAddress;
    private String avatar;
    private List<RewardDTO> rewards;
    private List<AttendanceDTO> attendances;
    private List<HomeworkDTO> homeworks;
}
