package com.sparta.springhomework2.dto;

import com.sparta.springhomework2.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;//제목
    private String contents; // 작성내용

    public PostResponseDto(Post post) {
        this.title = post.getTitle();
        this.contents = post.getContents();
    }
}
