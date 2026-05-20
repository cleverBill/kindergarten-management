package com.example.studentmanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileDTO {
    private Long studentId;
    private String name;
    private String grade;
    private String className;
    private String gender;
    private Integer age;
    private String parentContact;
    private String homeAddress;
    private String avatar;
    
    private Integer totalRewards;
    private Integer totalAttendance;
    private Integer totalPresent;
    private Integer totalAbsent;
    private Integer totalHomework;
    private Double averageScore;
    
    private List<Map<String, Object>> recentRewards;
    private List<Map<String, Object>> recentAttendance;
    private List<Map<String, Object>> recentHomework;
    
    private LocalDate reportDate;
}
