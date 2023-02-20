package com.sparta.springhomework2.repository;
import com.sparta.springhomework2.entity.Comment;
import com.sparta.springhomework2.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndPostId(Long id, Long postId);
}
