package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {

    @Query("SELECT m FROM PrivateMessage m WHERE (m.senderId = :userId1 AND m.receiverId = :userId2) OR (m.senderId = :userId2 AND m.receiverId = :userId1) ORDER BY m.createdAt ASC")
    List<PrivateMessage> findConversation(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    @Query("SELECT m FROM PrivateMessage m WHERE m.receiverId = :userId AND m.isRead = false")
    List<PrivateMessage> findUnreadByReceiverId(@Param("userId") Long userId);

    @Query("SELECT COUNT(m) FROM PrivateMessage m WHERE m.receiverId = :userId AND m.isRead = false")
    Integer countUnreadByReceiverId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(m) FROM PrivateMessage m WHERE m.senderId = :senderId")
    Integer countBySenderId(@Param("senderId") Long senderId);
}