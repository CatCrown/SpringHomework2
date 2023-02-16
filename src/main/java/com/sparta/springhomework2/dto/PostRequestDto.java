package com.sparta.springhomework2.dto;

import lombok.Getter;

@Getter
public class PostRequestDto {
    private String title;//제목
    private String contents; // 작성내용

    public PostRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
