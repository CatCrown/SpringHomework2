package com.sparta.springhomework2.Controller;

import com.sparta.springhomework2.dto.PostRequestDto;
import com.sparta.springhomework2.dto.PostResponseDto;
import com.sparta.springhomework2.dto.StatusResponseDto;
import com.sparta.springhomework2.entity.UserRoleEnum;
import com.sparta.springhomework2.security.UserDetailsImpl;
import com.sparta.springhomework2.service.PostService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //전체 조회
    @GetMapping("/posts")
    public StatusResponseDto<List<PostResponseDto>> getPosts(){
        return postService.findPosts();
    }

    //선택된 게시글 조회
    @GetMapping("/posts/{id}")
    public StatusResponseDto<PostResponseDto> getPost(@PathVariable Long id){
        return postService.findPost(id);
    }

    //포스트 생생
//    @Secured(UserRoleEnum.Authority.ADMIN)
    @PostMapping("/post")
    public StatusResponseDto<PostResponseDto> createdPost(@RequestBody PostRequestDto requestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.createPost(requestDto,userDetails.getUser());
    }

    //포스트 수정
    @PutMapping("/post/{id}")
    public StatusResponseDto<PostResponseDto> updatePost(@PathVariable Long id,
                                                         @RequestBody PostRequestDto requestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.update(id,requestDto,userDetails.getUser());
    }

    //포스트 삭제
    @DeleteMapping("/post/{id}")
    public StatusResponseDto<PostResponseDto> deletePost(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.delete(id,userDetails.getUser());
    }

}













