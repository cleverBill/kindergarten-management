package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.AttendanceDTO;
import com.example.studentmanagement.dto.HomeworkDTO;
import com.example.studentmanagement.dto.RewardDTO;
import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/students")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping("/students")
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.updateStudent(id, studentDTO));
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/rewards")
    public ResponseEntity<RewardDTO> addReward(@RequestBody RewardDTO rewardDTO) {
        RewardDTO createdReward = studentService.addReward(rewardDTO.getStudentId(), rewardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReward);
    }

    @GetMapping("/rewards")
    public ResponseEntity<List<RewardDTO>> getAllRewards() {
        return ResponseEntity.ok(studentService.getAllRewards());
    }

    @GetMapping("/attendance")
    public ResponseEntity<List<AttendanceDTO>> getAllAttendance() {
        return ResponseEntity.ok(studentService.getAllAttendance());
    }

    @PostMapping("/attendance")
    public ResponseEntity<AttendanceDTO> addAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        AttendanceDTO createdAttendance = studentService.addAttendance(attendanceDTO.getStudentId(), attendanceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAttendance);
    }

    @PostMapping("/attendance/batch")
    public ResponseEntity<Void> batchAddAttendance(@RequestBody List<AttendanceDTO> attendanceList) {
        studentService.batchAddAttendance(attendanceList);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/attendance/batch-present")
    public ResponseEntity<Void> batchMarkAllPresent(@RequestParam LocalDate date, @RequestParam(required = false) String className) {
        studentService.batchMarkAllPresent(date, className);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/homework")
    public ResponseEntity<HomeworkDTO> addHomework(@RequestBody HomeworkDTO homeworkDTO) {
        HomeworkDTO createdHomework = studentService.addHomework(homeworkDTO.getStudentId(), homeworkDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHomework);
    }

    @GetMapping("/homework")
    public ResponseEntity<List<HomeworkDTO>> getAllHomework() {
        return ResponseEntity.ok(studentService.getAllHomework());
    }
}
