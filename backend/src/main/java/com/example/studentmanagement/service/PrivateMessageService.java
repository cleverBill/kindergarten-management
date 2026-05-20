package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.ConversationDTO;
import com.example.studentmanagement.dto.PrivateMessageDTO;
import com.example.studentmanagement.entity.PrivateMessage;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.PrivateMessageRepository;
import com.example.studentmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateMessageService {

    private final PrivateMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Transactional
    public PrivateMessageDTO sendMessage(Long senderId, Long receiverId, String content) {
        PrivateMessage message = new PrivateMessage();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());
        message.setIsRead(false);
        message = messageRepository.save(message);
        return convertToDTO(message);
    }

    public List<PrivateMessageDTO> getConversation(Long userId1, Long userId2) {
        return messageRepository.findConversation(userId1, userId2).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ConversationDTO> getConversationList(Long currentUserId) {
        List<PrivateMessage> allMessages = messageRepository.findAll();

        Map<Long, PrivateMessage> latestMessageMap = new HashMap<>();
        Set<Long> conversationUsers = new HashSet<>();

        for (PrivateMessage msg : allMessages) {
            if (msg.getSenderId().equals(currentUserId) || msg.getReceiverId().equals(currentUserId)) {
                Long otherUserId = msg.getSenderId().equals(currentUserId) ? msg.getReceiverId() : msg.getSenderId();
                conversationUsers.add(otherUserId);

                PrivateMessage existing = latestMessageMap.get(otherUserId);
                if (existing == null || msg.getCreatedAt().isAfter(existing.getCreatedAt())) {
                    latestMessageMap.put(otherUserId, msg);
                }
            }
        }

        List<ConversationDTO> conversations = new ArrayList<>();
        for (Long otherUserId : conversationUsers) {
            User user = userRepository.findById(otherUserId).orElse(null);
            if (user != null) {
                PrivateMessage lastMsg = latestMessageMap.get(otherUserId);
                ConversationDTO dto = new ConversationDTO();
                dto.setUserId(otherUserId);
                dto.setUsername(user.getUsername());
                dto.setLastMessage(lastMsg != null ? lastMsg.getContent() : "");
                dto.setLastMessageTime(lastMsg != null ? lastMsg.getCreatedAt().format(formatter) : "");

                long unreadCount = allMessages.stream()
                        .filter(m -> m.getReceiverId().equals(currentUserId))
                        .filter(m -> m.getSenderId().equals(otherUserId))
                        .filter(m -> !m.getIsRead())
                        .count();
                dto.setUnreadCount((int) unreadCount);
                conversations.add(dto);
            }
        }

        conversations.sort((a, b) -> {
            if (a.getLastMessageTime().isEmpty() || b.getLastMessageTime().isEmpty()) {
                return b.getLastMessageTime().compareTo(a.getLastMessageTime());
            }
            return b.getLastMessageTime().compareTo(a.getLastMessageTime());
        });

        return conversations;
    }

    @Transactional
    public void markAsRead(Long receiverId, Long senderId) {
        List<PrivateMessage> messages = messageRepository.findConversation(receiverId, senderId);
        for (PrivateMessage msg : messages) {
            if (msg.getReceiverId().equals(receiverId) && !msg.getIsRead()) {
                msg.setIsRead(true);
                messageRepository.save(msg);
            }
        }
    }

    public Integer getUnreadCount(Long userId) {
        return messageRepository.countUnreadByReceiverId(userId);
    }

    private PrivateMessageDTO convertToDTO(PrivateMessage message) {
        PrivateMessageDTO dto = new PrivateMessageDTO();
        dto.setId(message.getId());
        dto.setSenderId(message.getSenderId());
        dto.setReceiverId(message.getReceiverId());
        dto.setContent(message.getContent());
        dto.setIsRead(message.getIsRead());
        dto.setCreatedAt(message.getCreatedAt().format(formatter));

        userRepository.findById(message.getSenderId()).ifPresent(u -> dto.setSenderName(u.getUsername()));
        userRepository.findById(message.getReceiverId()).ifPresent(u -> dto.setReceiverName(u.getUsername()));

        return dto;
    }
}