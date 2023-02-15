package com.sparta.springhomework2.Controller;

import com.sparta.springhomework2.dto.PostRequestDto;
import com.sparta.springhomework2.dto.PostResponseDto;
import com.sparta.springhomework2.entity.Post;
import com.sparta.springhomework2.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //전체 조회
    @GetMapping("/posts")
    public List<Post> getPosts(){
        return postService.getPosts();
    }

    //선택된 게시글 조회
    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    //포스트 생생
    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request){
        return postService.createPost(requestDto,request);
    }

    //포스트 수정
    @PutMapping("/post/{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto,HttpServletRequest request){
        return postService.update(id,requestDto,request);
    }

    //포스트 삭제
    @DeleteMapping("/post/{id}")
    public Long deletePost(@PathVariable Long id, HttpServletRequest request){
        return postService.deletePost(id,request);
    }

}
