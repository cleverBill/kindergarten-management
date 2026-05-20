package com.example.studentmanagement.controller;

import com.example.studentmanagement.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    public Map<String, Object> getOverview() {
        return Map.of("success", true, "data", dashboardService.getOverviewStatistics());
    }

    @GetMapping("/operations")
    public Map<String, Object> getOperations() {
        return Map.of("success", true, "data", dashboardService.getOperationsStatistics());
    }

    @GetMapping("/finance")
    public Map<String, Object> getFinance() {
        return Map.of("success", true, "data", dashboardService.getFinanceStatistics());
    }

    @GetMapping("/weekly-trend")
    public Map<String, Object> getWeeklyTrend() {
        return Map.of("success", true, "data", dashboardService.getWeeklyTrend());
    }

    @GetMapping("/class-comparison")
    public Map<String, Object> getClassComparison() {
        return Map.of("success", true, "data", dashboardService.getClassComparison());
    }
}