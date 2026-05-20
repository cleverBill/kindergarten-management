package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.ClassCircleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClassCircleLikeRepository extends JpaRepository<ClassCircleLike, Long> {
    Optional<ClassCircleLike> findByPostIdAndUserId(Long postId, Long userId);
    List<ClassCircleLike> findByPostId(Long postId);
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    void deleteByPostIdAndUserId(Long postId, Long userId);
}