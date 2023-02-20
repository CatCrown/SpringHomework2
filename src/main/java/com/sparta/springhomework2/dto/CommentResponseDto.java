package com.sparta.springhomework2.dto;

import com.sparta.springhomework2.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContents();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }

    public static CommentResponseDto from(Comment comment){
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContents())
                .username(comment.getUser().getUsername())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }
}