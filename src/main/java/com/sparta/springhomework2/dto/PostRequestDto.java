package com.sparta.springhomework2.dto;

import lombok.Getter;

@Getter
public class PostRequestDto {
    private String title;//제목
    private String username;//작성자명
    private String contents; // 작성내용
}
