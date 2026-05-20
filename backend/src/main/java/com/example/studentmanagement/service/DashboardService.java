package com.example.studentmanagement.service;

import com.example.studentmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final HomeworkRepository homeworkRepository;
    private final RewardRepository rewardRepository;
    private final FeeRecordRepository feeRecordRepository;
    private final FeeItemRepository feeItemRepository;
    private final PickupRecordRepository pickupRecordRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Map<String, Object> getOverviewStatistics() {
        Map<String, Object> stats = new HashMap<>();

        long totalStudents = studentRepository.count();
        stats.put("totalStudents", totalStudents);

        LocalDate today = LocalDate.now();
        long todayAttendance = attendanceRepository.countByDate(today);
        stats.put("todayAttendance", todayAttendance);
        stats.put("attendanceRate", totalStudents > 0 ? String.format("%.1f", (todayAttendance * 100.0 / totalStudents)) : "0");

        Integer todayHomework = homeworkRepository.countByDateRange(today, today);
        stats.put("todayHomework", todayHomework != null ? todayHomework : 0);

        Integer todayRewards = rewardRepository.countByDateRange(today, today);
        stats.put("todayRewards", todayRewards != null ? todayRewards : 0);

        return stats;
    }

    public Map<String, Object> getOperationsStatistics() {
        Map<String, Object> stats = new HashMap<>();

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);

        Integer todayIn = pickupRecordRepository.countByDateRangeAndStatus(start, end, "in");
        Integer todayOut = pickupRecordRepository.countByDateRangeAndStatus(start, end, "out");
        Integer abnormalCount = pickupRecordRepository.countByDateRangeAndStatus(start, end, "abnormal");

        stats.put("todayIn", todayIn != null ? todayIn : 0);
        stats.put("todayOut", todayOut != null ? todayOut : 0);
        stats.put("abnormalPickup", abnormalCount != null ? abnormalCount : 0);

        return stats;
    }

    public Map<String, Object> getFinanceStatistics() {
        Map<String, Object> stats = new HashMap<>();

        var items = feeItemRepository.findAll();
        double totalAmount = 0;
        double paidAmount = 0;
        int paidCount = 0;
        int unpaidCount = 0;

        for (var item : items) {
            totalAmount += item.getAmount().doubleValue();
            var records = feeRecordRepository.findByFeeItemId(item.getId());
            for (var record : records) {
                if ("paid".equals(record.getStatus())) {
                    paidAmount += record.getAmount().doubleValue();
                    paidCount++;
                } else {
                    unpaidCount++;
                }
            }
        }

        stats.put("totalAmount", String.format("%.2f", totalAmount));
        stats.put("paidAmount", String.format("%.2f", paidAmount));
        stats.put("unpaidAmount", String.format("%.2f", totalAmount - paidAmount));
        stats.put("paidCount", paidCount);
        stats.put("unpaidCount", unpaidCount);
        stats.put("collectionRate", totalAmount > 0 ? String.format("%.1f", (paidAmount * 100 / totalAmount)) : "0");

        return stats;
    }

    public Map<String, Object> getWeeklyTrend() {
        Map<String, Object> trend = new HashMap<>();
        LocalDate today = LocalDate.now();

        int[] attendanceTrend = new int[7];
        int[] homeworkTrend = new int[7];
        String[] dates = new String[7];

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            dates[6 - i] = date.format(formatter);

            long attendance = attendanceRepository.countByDate(date);
            attendanceTrend[6 - i] = (int) attendance;

            Integer homework = homeworkRepository.countByDateRange(date, date);
            homeworkTrend[6 - i] = homework != null ? homework : 0;
        }

        trend.put("dates", dates);
        trend.put("attendance", attendanceTrend);
        trend.put("homework", homeworkTrend);

        return trend;
    }

    public Map<String, Object> getClassComparison() {
        Map<String, Object> comparison = new HashMap<>();

        var classes = studentRepository.findAllClassNames();
        Map<String, Integer> classStudentCount = new HashMap<>();
        Map<String, Long> classAttendance = new HashMap<>();
        Map<Long, String> studentClassMap = new HashMap<>();

        for (var s : studentRepository.findAll()) {
            String className = s.getClassName();
            if (className != null && !className.isEmpty()) {
                classStudentCount.merge(className, 1, Integer::sum);
                studentClassMap.put(s.getId(), className);
            }
        }

        for (var a : attendanceRepository.findByDate(LocalDate.now())) {
            String className = studentClassMap.get(a.getStudentId());
            if (className != null) {
                classAttendance.merge(className, 1L, Long::sum);
            }
        }

        comparison.put("classNames", classes);
        comparison.put("studentCounts", classStudentCount);
        comparison.put("todayAttendance", classAttendance);

        return comparison;
    }
}