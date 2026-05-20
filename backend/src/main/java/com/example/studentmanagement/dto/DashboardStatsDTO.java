package com.example.studentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalStudents;
    private Long todayPresent;
    private Long todayAbsent;
    private Long totalHomeworks;
    private Long totalRewards;
    private Integer teachersOnDuty;
    private List<Map<String, Object>> attendanceTrend;
    private List<Map<String, Object>> homeworkStats;
    private List<Map<String, Object>> rewardRanking;
}
