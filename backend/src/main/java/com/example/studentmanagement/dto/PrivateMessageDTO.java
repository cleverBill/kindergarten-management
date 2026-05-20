package com.example.studentmanagement.dto;

import lombok.Data;

@Data
public class PrivateMessageDTO {
    private Long id;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private String content;
    private Boolean isRead;
    private String createdAt;
}