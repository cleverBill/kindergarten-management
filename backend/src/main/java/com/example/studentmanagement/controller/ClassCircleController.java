package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.ClassCircleCommentDTO;
import com.example.studentmanagement.dto.ClassCirclePostDTO;
import com.example.studentmanagement.service.ClassCircleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/class-circle")
@RequiredArgsConstructor
public class ClassCircleController {

    private final ClassCircleService classCircleService;

    @PostMapping("/posts")
    public Map<String, Object> createPost(@RequestBody Map<String, Object> request, @RequestHeader("X-User-Id") Long userId) {
        String content = (String) request.get("content");
        String imageUrls = (String) request.get("imageUrls");
        String scope = (String) request.getOrDefault("scope", "all");
        String grade = (String) request.get("grade");
        String className = (String) request.get("className");

        ClassCirclePostDTO post = classCircleService.createPost(userId, content, imageUrls, scope, grade, className);
        return Map.of("success", true, "data", post);
    }

    @GetMapping("/posts")
    public Map<String, Object> getPosts(@RequestHeader("X-User-Id") Long userId) {
        List<ClassCirclePostDTO> posts = classCircleService.getAllPosts(userId);
        return Map.of("success", true, "data", posts);
    }

    @PostMapping("/posts/{postId}/comments")
    public Map<String, Object> addComment(@PathVariable Long postId, @RequestBody Map<String, String> request, @RequestHeader("X-User-Id") Long userId) {
        String content = request.get("content");
        ClassCircleCommentDTO comment = classCircleService.addComment(postId, userId, content);
        return Map.of("success", true, "data", comment);
    }

    @GetMapping("/posts/{postId}/comments")
    public Map<String, Object> getComments(@PathVariable Long postId) {
        List<ClassCircleCommentDTO> comments = classCircleService.getComments(postId);
        return Map.of("success", true, "data", comments);
    }

    @PostMapping("/posts/{postId}/like")
    public Map<String, Object> toggleLike(@PathVariable Long postId, @RequestHeader("X-User-Id") Long userId) {
        ClassCirclePostDTO post = classCircleService.toggleLike(postId, userId);
        return Map.of("success", true, "data", post);
    }

    @DeleteMapping("/posts/{postId}")
    public Map<String, Object> deletePost(@PathVariable Long postId, @RequestHeader("X-User-Id") Long userId) {
        classCircleService.deletePost(postId, userId);
        return Map.of("success", true);
    }
}