package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.ConversationDTO;
import com.example.studentmanagement.dto.PrivateMessageDTO;
import com.example.studentmanagement.service.PrivateMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class PrivateMessageController {

    private final PrivateMessageService messageService;

    @PostMapping("/send")
    public Map<String, Object> sendMessage(@RequestBody Map<String, Object> request, @RequestHeader("X-User-Id") Long senderId) {
        Long receiverId = Long.valueOf(request.get("receiverId").toString());
        String content = (String) request.get("content");
        PrivateMessageDTO message = messageService.sendMessage(senderId, receiverId, content);
        return Map.of("success", true, "data", message);
    }

    @GetMapping("/conversation/{otherUserId}")
    public Map<String, Object> getConversation(@PathVariable Long otherUserId, @RequestHeader("X-User-Id") Long currentUserId) {
        messageService.markAsRead(currentUserId, otherUserId);
        List<PrivateMessageDTO> messages = messageService.getConversation(currentUserId, otherUserId);
        return Map.of("success", true, "data", messages);
    }

    @GetMapping("/conversations")
    public Map<String, Object> getConversationList(@RequestHeader("X-User-Id") Long currentUserId) {
        List<ConversationDTO> conversations = messageService.getConversationList(currentUserId);
        return Map.of("success", true, "data", conversations);
    }

    @GetMapping("/unread-count")
    public Map<String, Object> getUnreadCount(@RequestHeader("X-User-Id") Long currentUserId) {
        Integer count = messageService.getUnreadCount(currentUserId);
        return Map.of("success", true, "data", count);
    }
}