package com.example.studentmanagement.dto;

import lombok.Data;

@Data
public class ClassCirclePostDTO {
    private Long id;
    private Long userId;
    private String username;
    private String userAvatar;
    private String content;
    private String imageUrls;
    private String scope;
    private String grade;
    private String className;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean isLiked;
    private String createdAt;
}