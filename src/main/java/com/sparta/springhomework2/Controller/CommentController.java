package com.sparta.springhomework2.Controller;

import com.sparta.springhomework2.dto.CommentRequestDto;
import com.sparta.springhomework2.dto.CommentResponseDto;
import com.sparta.springhomework2.dto.StatusResponseDto;
import com.sparta.springhomework2.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{id}")
    public StatusResponseDto<CommentResponseDto> createComment(@PathVariable Long id, String content, HttpServletRequest request){
        return commentService.createComment(id, content, request);
    }

    @PutMapping ("/comment/{id}")
    public StatusResponseDto<CommentResponseDto> updateComment(@PathVariable Long id, CommentRequestDto commentRequestDto, HttpServletRequest request){
        return commentService.updateComment(id, commentRequestDto, request);
    }

    @DeleteMapping("/comment/{id}")
    public StatusResponseDto<String> deleteComment(@PathVariable Long id, HttpServletRequest request){
        return commentService.deleteComment(id, request);
    }

}