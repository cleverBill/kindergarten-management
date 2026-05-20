package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.dto.LoginResponse;
import com.example.studentmanagement.dto.UserDTO;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = loginService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody LoginRequest request) {
        loginService.register(request);
        return ResponseEntity.ok("注册成功，请等待管理员审核");
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = loginService.getAllUsers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/pending")
    public ResponseEntity<List<UserDTO>> getPendingUsers() {
        List<UserDTO> users = loginService.getPendingUsers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}/approve")
    public ResponseEntity<UserDTO> approveUser(@PathVariable Long id, @RequestHeader("X-Current-User") String currentUser) {
        User user = loginService.approveUser(id, currentUser);
        return ResponseEntity.ok(convertToDTO(user));
    }

    @PutMapping("/users/{id}/reject")
    public ResponseEntity<UserDTO> rejectUser(@PathVariable Long id, @RequestHeader("X-Current-User") String currentUser) {
        User user = loginService.rejectUser(id, currentUser);
        return ResponseEntity.ok(convertToDTO(user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, @RequestHeader("X-Current-User") String currentUser) {
        loginService.deleteUser(id, currentUser);
        return ResponseEntity.ok("用户删除成功");
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<UserDTO> updateUserRole(@PathVariable Long id, @RequestBody String role, @RequestHeader("X-Current-User") String currentUser) {
        User user = loginService.updateUserRole(id, role.replace("\"", ""), currentUser);
        return ResponseEntity.ok(convertToDTO(user));
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getRole(), user.getStatus());
    }
}
