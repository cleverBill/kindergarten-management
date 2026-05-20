package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.ClassCircleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClassCircleCommentRepository extends JpaRepository<ClassCircleComment, Long> {
    List<ClassCircleComment> findByPostIdOrderByCreatedAtAsc(Long postId);
}