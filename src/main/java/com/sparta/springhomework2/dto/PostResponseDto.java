package com.sparta.springhomework2.dto;

import com.sparta.springhomework2.entity.Post;
import com.sparta.springhomework2.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponseDto extends Timestamped {
    private Long id;
    private String title;//제목
    private String contents; // 작성내용
    private String username; // 작성자명

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUser().getUsername();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();

    }

}
