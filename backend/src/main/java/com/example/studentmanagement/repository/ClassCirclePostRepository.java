package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.ClassCirclePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ClassCirclePostRepository extends JpaRepository<ClassCirclePost, Long> {

    @Query("SELECT p FROM ClassCirclePost p WHERE p.scope = 'all' ORDER BY p.createdAt DESC")
    List<ClassCirclePost> findAllPosts(@Param("scope") String scope);

    @Query("SELECT p FROM ClassCirclePost p WHERE p.scope = 'all' OR p.scope = 'grade' AND p.grade = :grade OR p.scope = 'class' AND p.className = :className ORDER BY p.createdAt DESC")
    List<ClassCirclePost> findPostsByScope(@Param("grade") String grade, @Param("className") String className);

    List<ClassCirclePost> findByOrderByCreatedAtDesc();
}