package com.sparta.springhomework2.service;

import com.sparta.springhomework2.dto.PostRequestDto;
import com.sparta.springhomework2.dto.PostResponseDto;
import com.sparta.springhomework2.dto.StatusResponseDto;
import com.sparta.springhomework2.entity.Post;
import com.sparta.springhomework2.entity.User;
import com.sparta.springhomework2.entity.UserRoleEnum;
import com.sparta.springhomework2.jwt.JwtUtil;
import com.sparta.springhomework2.repository.CommentRepository;
import com.sparta.springhomework2.repository.PostRepository;
import com.sparta.springhomework2.repository.UserRepository;
import com.sun.net.httpserver.Authenticator;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    //전체 조회
    @Transactional(readOnly = true)
    public StatusResponseDto<List<PostResponseDto>> findPosts() {
        List<Post> list = postRepository.findAll();
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for(Post post : list){
            postResponseDtos.add(new PostResponseDto(post));
        }
        return StatusResponseDto.success(postResponseDtos);
    }

    @Transactional(readOnly = true)
    public StatusResponseDto<PostResponseDto> findPost(Long id) {
        // 선택된 내용 조회
        PostResponseDto postResponseDto = new PostResponseDto(postRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("아이디가 존재하지 않습니다")
        ));
        return StatusResponseDto.success(postResponseDto);

    }

    // 포스트 생성 및 저장
    @Transactional
    public StatusResponseDto<PostResponseDto> createPost(PostRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 추가 가능
        if(token != null){
            if (jwtUtil.validateToken(token)){
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            }else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    ()-> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            Post post = postRepository.save(new Post(requestDto, user));
            return StatusResponseDto.success(new PostResponseDto(post));
        }
        throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }

    @Transactional
    public StatusResponseDto<PostResponseDto> update(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 수정 가능
        if (token != null){
            // Token 검증
            if (jwtUtil.validateToken(token)){
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
                 // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    ()-> new IllegalArgumentException("사용자가 존재하지 않습니다.")
                );
                Post post = postRepository.findByIdAndUserId(id,user.getId()).orElseThrow(
                    ()-> new IllegalArgumentException("해당 게시글은 존재하지 않습니다.")
                );
                if (user.getRole()==UserRoleEnum.ADMIN || user.getUsername().equals(post.getUser().getUsername())){
                    post.update(requestDto);
                    return StatusResponseDto.success(new PostResponseDto(post));
                }else {
                    throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
                }
            }
        }

        throw new IllegalArgumentException("토큰이 유효하지 않습니다");
    }

    @Transactional
    public StatusResponseDto<PostResponseDto> delete(Long id, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 수정 가능
        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Post post = postRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("조회된 내용 없습니다.")
            );

            if (user.getRole() == UserRoleEnum.ADMIN || user.getUsername().equals(post.getUser().getUsername())) {
                postRepository.deleteById(id);
            } else {
                throw new IllegalArgumentException("다른 사용자가 작성한 글은 삭제할 수 없습니다.");
            }
            throw new IllegalArgumentException("게시글 삭제 성공");
        }
        throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }


}
