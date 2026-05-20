package com.example.studentmanagement.dto;

import lombok.Data;

@Data
public class ConversationDTO {
    private Long userId;
    private String username;
    private String lastMessage;
    private String lastMessageTime;
    private Integer unreadCount;
}