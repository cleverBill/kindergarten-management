package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentProfileDTO;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.AttendanceRepository;
import com.example.studentmanagement.repository.HomeworkRepository;
import com.example.studentmanagement.repository.RewardRepository;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentProfileService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private HomeworkRepository homeworkRepository;

    @Autowired
    private RewardRepository rewardRepository;

    public StudentProfileDTO getStudentProfile(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("学生不存在"));

        StudentProfileDTO profile = new StudentProfileDTO();
        profile.setStudentId(student.getId());
        profile.setName(student.getName());
        profile.setGrade(student.getGrade());
        profile.setClassName(student.getClassName());
        profile.setGender(student.getGender());
        profile.setAge(student.getAge());
        profile.setParentContact(student.getParentContact());
        profile.setHomeAddress(student.getHomeAddress());
        profile.setAvatar(student.getAvatar());
        profile.setReportDate(LocalDate.now());

        // ====================== 修复完成 ======================
        List<Object[]> rewardData = rewardRepository.countByStudentId(studentId);
        int totalRewards = 0;
        if (!rewardData.isEmpty() && rewardData.get(0).length > 0) {
            totalRewards = ((Number) rewardData.get(0)[0]).intValue();
        }
        profile.setTotalRewards(totalRewards);
        // ======================================================

        List<Object[]> attendanceData = attendanceRepository.findByStudentId(studentId);
        profile.setTotalAttendance(attendanceData.size());
        long present = attendanceData.stream()
                .filter(a -> "出勤".equals(a[2]))
                .count();
        long absent = attendanceData.stream()
                .filter(a -> "缺勤".equals(a[2]))
                .count();
        profile.setTotalPresent((int) present);
        profile.setTotalAbsent((int) absent);

        List<Object[]> homeworkData = homeworkRepository.findByStudentId(studentId);
        profile.setTotalHomework(homeworkData.size());

        if (!homeworkData.isEmpty()) {
            double avgScore = homeworkData.stream()
                    .filter(h -> h[3] != null)
                    .mapToDouble(h -> ((Number) h[3]).doubleValue())
                    .average()
                    .orElse(0.0);
            profile.setAverageScore(Math.round(avgScore * 10.0) / 10.0);
        } else {
            profile.setAverageScore(0.0);
        }

        profile.setRecentRewards(getRecentRewards(studentId, 5));
        profile.setRecentAttendance(getRecentAttendance(studentId, 5));
        profile.setRecentHomework(getRecentHomework(studentId, 5));

        return profile;
    }

    private List<Map<String, Object>> getRecentRewards(Long studentId, int limit) {
        List<Object[]> data = rewardRepository.findRecentByStudentId(studentId);
        return data.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", row[0]);
            map.put("studentName", row[1]);
            map.put("type", row[2]);
            map.put("description", row[3]);
            map.put("createdAt", row[4]);
            return map;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> getRecentAttendance(Long studentId, int limit) {
        List<Object[]> data = attendanceRepository.findRecentByStudentId(studentId);
        return data.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", row[0]);
            map.put("date", row[2]);
            map.put("status", row[3]);
            map.put("reason", row.length > 4 ? row[4] : "");
            return map;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> getRecentHomework(Long studentId, int limit) {
        List<Object[]> data = homeworkRepository.findRecentByStudentId(studentId);
        return data.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", row[0]);
            map.put("name", row[2]);
            map.put("score", row[3]);
            map.put("date", row[4]);
            return map;
        }).collect(Collectors.toList());
    }
}