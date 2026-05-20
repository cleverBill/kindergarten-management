package com.example.studentmanagement.dto;

import lombok.Data;

@Data
public class ClassCircleCommentDTO {
    private Long id;
    private Long postId;
    private Long userId;
    private String username;
    private String userAvatar;
    private String content;
    private String createdAt;
}