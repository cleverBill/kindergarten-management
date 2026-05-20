package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.ClassCircleCommentDTO;
import com.example.studentmanagement.dto.ClassCirclePostDTO;
import com.example.studentmanagement.entity.ClassCircleComment;
import com.example.studentmanagement.entity.ClassCircleLike;
import com.example.studentmanagement.entity.ClassCirclePost;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.ClassCircleCommentRepository;
import com.example.studentmanagement.repository.ClassCircleLikeRepository;
import com.example.studentmanagement.repository.ClassCirclePostRepository;
import com.example.studentmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassCircleService {

    private final ClassCirclePostRepository postRepository;
    private final ClassCircleCommentRepository commentRepository;
    private final ClassCircleLikeRepository likeRepository;
    private final UserRepository userRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Transactional
    public ClassCirclePostDTO createPost(Long userId, String content, String imageUrls, String scope, String grade, String className) {
        ClassCirclePost post = new ClassCirclePost();
        post.setUserId(userId);
        post.setContent(content);
        post.setImageUrls(imageUrls);
        post.setScope(scope);
        post.setGrade(grade);
        post.setClassName(className);
        post.setCreatedAt(LocalDateTime.now());
        post = postRepository.save(post);
        return convertToDTO(post, userId);
    }

    public List<ClassCirclePostDTO> getAllPosts(Long currentUserId) {
        return postRepository.findByOrderByCreatedAtDesc().stream()
                .map(post -> convertToDTO(post, currentUserId))
                .collect(Collectors.toList());
    }

    @Transactional
    public ClassCircleCommentDTO addComment(Long postId, Long userId, String content) {
        ClassCircleComment comment = new ClassCircleComment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment = commentRepository.save(comment);

        ClassCirclePost post = postRepository.findById(postId).orElseThrow();
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        return convertCommentToDTO(comment);
    }

    public List<ClassCircleCommentDTO> getComments(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(this::convertCommentToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClassCirclePostDTO toggleLike(Long postId, Long userId) {
        ClassCirclePost post = postRepository.findById(postId).orElseThrow();

        if (likeRepository.existsByPostIdAndUserId(postId, userId)) {
            likeRepository.deleteByPostIdAndUserId(postId, userId);
            post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
        } else {
            ClassCircleLike like = new ClassCircleLike();
            like.setPostId(postId);
            like.setUserId(userId);
            like.setCreatedAt(LocalDateTime.now());
            likeRepository.save(like);
            post.setLikeCount(post.getLikeCount() + 1);
        }
        postRepository.save(post);
        return convertToDTO(post, userId);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        ClassCirclePost post = postRepository.findById(postId).orElseThrow();
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此动态");
        }
        commentRepository.deleteAll(commentRepository.findByPostIdOrderByCreatedAtAsc(postId));
        likeRepository.deleteAll(likeRepository.findByPostId(postId));
        postRepository.delete(post);
    }

    private ClassCirclePostDTO convertToDTO(ClassCirclePost post, Long currentUserId) {
        ClassCirclePostDTO dto = new ClassCirclePostDTO();
        dto.setId(post.getId());
        dto.setUserId(post.getUserId());
        dto.setContent(post.getContent());
        dto.setImageUrls(post.getImageUrls());
        dto.setScope(post.getScope());
        dto.setGrade(post.getGrade());
        dto.setClassName(post.getClassName());
        dto.setLikeCount(post.getLikeCount());
        dto.setCommentCount(post.getCommentCount());
        dto.setIsLiked(likeRepository.existsByPostIdAndUserId(post.getId(), currentUserId));
        dto.setCreatedAt(post.getCreatedAt().format(formatter));

        userRepository.findById(post.getUserId()).ifPresent(user -> {
            dto.setUsername(user.getUsername());
        });

        return dto;
    }

    private ClassCircleCommentDTO convertCommentToDTO(ClassCircleComment comment) {
        ClassCircleCommentDTO dto = new ClassCircleCommentDTO();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPostId());
        dto.setUserId(comment.getUserId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt().format(formatter));

        userRepository.findById(comment.getUserId()).ifPresent(user -> {
            dto.setUsername(user.getUsername());
        });

        return dto;
    }
}