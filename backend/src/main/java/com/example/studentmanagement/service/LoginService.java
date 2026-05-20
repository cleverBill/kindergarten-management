package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.dto.LoginResponse;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    private static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    
    private void checkAdminPermission(String currentUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("当前用户不存在"));
        if (!ROLE_SUPER_ADMIN.equals(currentUser.getRole())) {
            throw new RuntimeException("权限不足，只有超级管理员可以执行此操作");
        }
    }

    @jakarta.annotation.PostConstruct
    public void initAdminUser() {
        User admin = userRepository.findByUsername("admin").orElse(new User());
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setRole(ROLE_SUPER_ADMIN);
        admin.setStatus("approved");
        userRepository.save(admin);
    }

    public User updateUserRole(Long userId, String role, String currentUsername) {
        checkAdminPermission(currentUsername);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setRole(role);
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername() != null ? request.getUsername().trim() : null;
        String password = request.getPassword() != null ? request.getPassword().trim() : null;
        
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        if ("pending".equals(user.getStatus())) {
            throw new RuntimeException("您的账号正在等待管理员审核，请耐心等待！");
        }
        
        if ("rejected".equals(user.getStatus())) {
            throw new RuntimeException("您的账号审核未通过，请联系管理员！");
        }
        
        String token = Base64.getEncoder().encodeToString((user.getUsername() + ":" + UUID.randomUUID()).getBytes());

        return new LoginResponse(token, user.getUsername(), user.getRole(), user.getId(), user.getKindergarten());
    }

    public void register(LoginRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        
        if (request.getKindergarten() == null || request.getKindergarten().trim().isEmpty()) {
            throw new RuntimeException("请输入幼儿园名称");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");
        user.setStatus("pending");
        user.setKindergarten(request.getKindergarten().trim());
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getPendingUsers() {
        return userRepository.findByStatus("pending");
    }

    public User approveUser(Long userId, String currentUsername) {
        checkAdminPermission(currentUsername);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setStatus("approved");
        return userRepository.save(user);
    }

    public User rejectUser(Long userId, String currentUsername) {
        checkAdminPermission(currentUsername);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setStatus("rejected");
        return userRepository.save(user);
    }

    public void deleteUser(Long userId, String currentUsername) {
        checkAdminPermission(currentUsername);
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("用户不存在");
        }
        userRepository.deleteById(userId);
    }
}
