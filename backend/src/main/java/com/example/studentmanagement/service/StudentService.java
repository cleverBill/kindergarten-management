package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.AttendanceDTO;
import com.example.studentmanagement.dto.HomeworkDTO;
import com.example.studentmanagement.dto.RewardDTO;
import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.entity.Attendance;
import com.example.studentmanagement.entity.Homework;
import com.example.studentmanagement.entity.Reward;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.AttendanceRepository;
import com.example.studentmanagement.repository.HomeworkRepository;
import com.example.studentmanagement.repository.RewardRepository;
import com.example.studentmanagement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final RewardRepository rewardRepository;
    private final AttendanceRepository attendanceRepository;
    private final HomeworkRepository homeworkRepository;

    @Transactional
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学生不存在: " + id));
        return convertToDTO(student);
    }

    @Transactional
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setGrade(studentDTO.getGrade());
        student.setClassName(studentDTO.getClassName());
        student.setStatus(studentDTO.getStatus() != null ? studentDTO.getStatus() : "正常");
        student.setGender(studentDTO.getGender());
        student.setAge(studentDTO.getAge());
        student.setParentContact(studentDTO.getParentContact());
        student.setHomeAddress(studentDTO.getHomeAddress());
        student.setAvatar(studentDTO.getAvatar());
        Student savedStudent = studentRepository.save(student);
        return convertToDTO(savedStudent);
    }

    @Transactional
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学生不存在: " + id));
        student.setName(studentDTO.getName());
        student.setGrade(studentDTO.getGrade());
        student.setClassName(studentDTO.getClassName());
        student.setStatus(studentDTO.getStatus());
        student.setGender(studentDTO.getGender());
        student.setAge(studentDTO.getAge());
        student.setParentContact(studentDTO.getParentContact());
        student.setHomeAddress(studentDTO.getHomeAddress());
        student.setAvatar(studentDTO.getAvatar());
        Student updatedStudent = studentRepository.save(student);
        return convertToDTO(updatedStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("学生不存在: " + id);
        }
        studentRepository.deleteById(id);
    }

    @Transactional
    public RewardDTO addReward(Long studentId, RewardDTO rewardDTO) {
        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("学生不存在: " + studentId);
        }
        Reward reward = new Reward();
        reward.setStudentId(studentId);
        reward.setType(rewardDTO.getType());
        reward.setDescription(rewardDTO.getReason());
        reward.setDate(rewardDTO.getDate());
        Reward savedReward = rewardRepository.save(reward);
        return convertToDTO(savedReward);
    }

    @Transactional
    public AttendanceDTO addAttendance(Long studentId, AttendanceDTO attendanceDTO) {
        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("学生不存在: " + studentId);
        }
        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setDate(attendanceDTO.getDate());
        attendance.setStatus(attendanceDTO.getStatus());
        attendance.setReason(attendanceDTO.getReason());
        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToDTO(savedAttendance);
    }

    @Transactional
    public void batchAddAttendance(List<AttendanceDTO> attendanceList) {
        for (AttendanceDTO dto : attendanceList) {
            if (dto.getStudentId() != null && dto.getDate() != null && dto.getStatus() != null) {
                Attendance attendance = new Attendance();
                attendance.setStudentId(dto.getStudentId());
                attendance.setDate(dto.getDate());
                attendance.setStatus(dto.getStatus());
                attendance.setReason(dto.getReason());
                attendanceRepository.save(attendance);
            }
        }
    }

    @Transactional
    public void batchMarkAllPresent(LocalDate date, String className) {
        List<Student> students;
        if (className != null && !className.isEmpty()) {
            students = studentRepository.findByClassName(className);
        } else {
            students = studentRepository.findAll();
        }
        for (Student student : students) {
            Attendance attendance = new Attendance();
            attendance.setStudentId(student.getId());
            attendance.setDate(date);
            attendance.setStatus("出勤");
            attendance.setReason("一键全班出勤");
            attendanceRepository.save(attendance);
        }
    }

    @Transactional
    public HomeworkDTO addHomework(Long studentId, HomeworkDTO homeworkDTO) {
        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("学生不存在: " + studentId);
        }
        Homework homework = new Homework();
        homework.setStudentId(studentId);
        homework.setName(homeworkDTO.getName());
        homework.setScore(homeworkDTO.getScore());
        homework.setDate(homeworkDTO.getDate());
        Homework savedHomework = homeworkRepository.save(homework);
        return convertToDTO(savedHomework);
    }

    public List<RewardDTO> getAllRewards() {
        return rewardRepository.findAllByOrderByDateDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceDTO> getAllAttendance() {
        return attendanceRepository.findAllByOrderByDateDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HomeworkDTO> getAllHomework() {
        return homeworkRepository.findAllByOrderByDateDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private StudentDTO convertToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setGrade(student.getGrade());
        dto.setClassName(student.getClassName());
        dto.setStatus(student.getStatus());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setGender(student.getGender());
        dto.setAge(student.getAge());
        dto.setParentContact(student.getParentContact());
        dto.setHomeAddress(student.getHomeAddress());
        dto.setAvatar(student.getAvatar());
        dto.setRewards(null);
        dto.setAttendances(null);
        dto.setHomeworks(null);
        return dto;
    }

    private RewardDTO convertToDTO(Reward reward) {
        RewardDTO dto = new RewardDTO();
        dto.setId(reward.getId());
        dto.setStudentId(reward.getStudentId());
        dto.setType(reward.getType());
        dto.setReason(reward.getDescription());
        dto.setDate(reward.getDate());
        return dto;
    }

    private AttendanceDTO convertToDTO(Attendance attendance) {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setId(attendance.getId());
        dto.setStudentId(attendance.getStudentId());
        dto.setDate(attendance.getDate());
        dto.setStatus(attendance.getStatus());
        dto.setReason(attendance.getReason());
        return dto;
    }

    private HomeworkDTO convertToDTO(Homework homework) {
        HomeworkDTO dto = new HomeworkDTO();
        dto.setId(homework.getId());
        dto.setStudentId(homework.getStudentId());
        dto.setName(homework.getName());
        dto.setScore(homework.getScore());
        dto.setDate(homework.getDate());
        return dto;
    }
}
