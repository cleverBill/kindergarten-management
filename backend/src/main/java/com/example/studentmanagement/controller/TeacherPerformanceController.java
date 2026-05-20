package com.example.studentmanagement.controller;

import com.example.studentmanagement.service.TeacherPerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/performance")
@RequiredArgsConstructor
public class TeacherPerformanceController {

    private final TeacherPerformanceService performanceService;

    @GetMapping("/teacher/{teacherId}")
    public Map<String, Object> getTeacherPerformance(@PathVariable Long teacherId, 
                                                     @RequestParam(required = false) String month) {
        return Map.of("success", true, "data", performanceService.getTeacherPerformance(teacherId, month));
    }

    @GetMapping("/all")
    public Map<String, Object> getAllTeachersPerformance(@RequestParam(required = false) String month) {
        List<Map<String, Object>> performances = performanceService.getAllTeachersPerformance(month);
        return Map.of("success", true, "data", performances);
    }

    @PostMapping("/update")
    public Map<String, Object> updatePerformance(@RequestBody Map<String, Object> request) {
        Long teacherId = Long.valueOf(request.get("teacherId").toString());
        String type = (String) request.get("type");
        Integer increment = (Integer) request.getOrDefault("increment", 1);

        performanceService.updatePerformance(teacherId, type, increment);
        return Map.of("success", true);
    }

    @PostMapping("/calculate")
    public Map<String, Object> calculateMonthlyPerformance() {
        performanceService.calculateMonthlyPerformance();
        return Map.of("success", true);
    }
}