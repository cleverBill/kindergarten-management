package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StudentProfileDTO;
import com.example.studentmanagement.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileService profileService;

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentProfileDTO> getStudentProfile(@PathVariable Long studentId) {
        StudentProfileDTO profile = profileService.getStudentProfile(studentId);
        return ResponseEntity.ok(profile);
    }
}
