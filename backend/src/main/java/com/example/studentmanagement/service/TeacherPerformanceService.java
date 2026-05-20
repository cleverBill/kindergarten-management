package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.TeacherPerformance;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherPerformanceService {

    private final TeacherPerformanceRepository performanceRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final HomeworkRepository homeworkRepository;
    private final RewardRepository rewardRepository;
    private final ClassCirclePostRepository postRepository;
    private final PrivateMessageRepository messageRepository;

    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

    @Transactional
    public void updatePerformance(Long teacherId, String type, Integer increment) {
        String currentMonth = YearMonth.now().format(monthFormatter);
        
        TeacherPerformance performance = performanceRepository
                .findByTeacherIdAndMonth(teacherId, currentMonth)
                .orElseGet(() -> createNewPerformance(teacherId, currentMonth));

        switch (type) {
            case "attendance":
                performance.setAttendanceCount(performance.getAttendanceCount() + increment);
                break;
            case "homework":
                performance.setHomeworkCount(performance.getHomeworkCount() + increment);
                break;
            case "reward":
                performance.setRewardCount(performance.getRewardCount() + increment);
                break;
            case "post":
                performance.setPostCount(performance.getPostCount() + increment);
                break;
            case "message":
                performance.setMessageCount(performance.getMessageCount() + increment);
                break;
        }
        
        performance.setUpdatedAt(LocalDateTime.now());
        performanceRepository.save(performance);
    }

    private TeacherPerformance createNewPerformance(Long teacherId, String month) {
        TeacherPerformance p = new TeacherPerformance();
        p.setTeacherId(teacherId);
        p.setMonth(month);
        p.setAttendanceCount(0);
        p.setHomeworkCount(0);
        p.setRewardCount(0);
        p.setPostCount(0);
        p.setMessageCount(0);
        p.setParentRating(0.0);
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());
        return p;
    }

    public Map<String, Object> getTeacherPerformance(Long teacherId, String month) {
        final String targetMonth = (month == null || month.isEmpty()) ? YearMonth.now().format(monthFormatter) : month;

        TeacherPerformance performance = performanceRepository
                .findByTeacherIdAndMonth(teacherId, targetMonth)
                .orElseGet(() -> createNewPerformance(teacherId, targetMonth));

        Map<String, Object> result = new HashMap<>();
        result.put("teacherId", teacherId);
        result.put("month", targetMonth);
        result.put("attendanceCount", performance.getAttendanceCount());
        result.put("homeworkCount", performance.getHomeworkCount());
        result.put("rewardCount", performance.getRewardCount());
        result.put("postCount", performance.getPostCount());
        result.put("messageCount", performance.getMessageCount());
        result.put("parentRating", performance.getParentRating());

        userRepository.findById(teacherId).ifPresent(u -> result.put("teacherName", u.getUsername()));

        return result;
    }

    public List<Map<String, Object>> getAllTeachersPerformance(String month) {
        if (month == null || month.isEmpty()) {
            month = YearMonth.now().format(monthFormatter);
        }

        List<TeacherPerformance> performances = performanceRepository.findByMonth(month);
        
        return performances.stream().map(p -> {
            Map<String, Object> result = new HashMap<>();
            result.put("teacherId", p.getTeacherId());
            result.put("month", p.getMonth());
            result.put("attendanceCount", p.getAttendanceCount());
            result.put("homeworkCount", p.getHomeworkCount());
            result.put("rewardCount", p.getRewardCount());
            result.put("postCount", p.getPostCount());
            result.put("messageCount", p.getMessageCount());
            result.put("parentRating", p.getParentRating());
            
            userRepository.findById(p.getTeacherId()).ifPresent(u -> result.put("teacherName", u.getUsername()));
            
            int total = p.getAttendanceCount() + p.getHomeworkCount() + p.getRewardCount() + 
                        p.getPostCount() + p.getMessageCount();
            result.put("totalScore", total);
            
            return result;
        }).sorted((a, b) -> Integer.compare((Integer) b.get("totalScore"), (Integer) a.get("totalScore")))
         .collect(Collectors.toList());
    }

    public void calculateMonthlyPerformance() {
        String currentMonth = YearMonth.now().format(monthFormatter);
        LocalDate monthStart = YearMonth.now().atDay(1);
        LocalDate monthEnd = YearMonth.now().atEndOfMonth();

        List<User> teachers = userRepository.findTeachers();
        for (User teacher : teachers) {
            Long teacherId = teacher.getId();

            long attendanceCount = attendanceRepository.count();
            long homeworkCount = homeworkRepository.countByDateRange(monthStart, monthEnd);
            long rewardCount = rewardRepository.countByDateRange(monthStart, monthEnd);
            long postCount = postRepository.count();
            long messageCount = messageRepository.countBySenderId(teacherId);

            TeacherPerformance performance = performanceRepository
                    .findByTeacherIdAndMonth(teacherId, currentMonth)
                    .orElseGet(() -> createNewPerformance(teacherId, currentMonth));

            performance.setAttendanceCount((int) attendanceCount);
            performance.setHomeworkCount((int) homeworkCount);
            performance.setRewardCount((int) rewardCount);
            performance.setPostCount((int) postCount);
            performance.setMessageCount((int) messageCount);
            performance.setUpdatedAt(LocalDateTime.now());

            performanceRepository.save(performance);
        }
    }
}