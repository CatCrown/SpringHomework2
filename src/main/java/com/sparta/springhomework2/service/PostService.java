package com.sparta.springhomework2.service;

import com.sparta.springhomework2.dto.PostRequestDto;
import com.sparta.springhomework2.dto.PostResponseDto;
import com.sparta.springhomework2.entity.Post;
import com.sparta.springhomework2.entity.User;
import com.sparta.springhomework2.jwt.JwtUtil;
import com.sparta.springhomework2.repository.PostRepository;
import com.sparta.springhomework2.repository.UserRepository;
import com.sun.net.httpserver.Authenticator;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Post> getPosts() {
        // 전체 조회
        return postRepository.findAllByOrderByModifiedAtDesc();
    }
    @Transactional(readOnly = true)
    public Post getPost(Long id) {
        // 선택된 내용 조회
        return postRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("아이디가 존재하지 않습니다")
        );
    }

    // 포스트 생성 및 저장
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
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
            Post post = postRepository.saveAndFlush(new Post(requestDto, user.getId()));
            return new PostResponseDto(post);
        }else {
            return null;
        }
    }

    @Transactional
    public Long update(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 수정 가능
        if (token != null){
            // Token 검증
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

            Post post = postRepository.findByIdAndUserId(id,user.getId()).orElseThrow(
                    ()-> new IllegalArgumentException("해당 게시글은 존재하지 않습니다.")
            );

            post.update(requestDto);
            return post.getId();
        }else {
            return null;
        }

    }

    @Transactional
    public Long deletePost(Long id, HttpServletRequest request) {
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

            if (!post.getUsername().equals(user.getUsername())){
                throw new IllegalArgumentException("다른 사용자가 작성한 글은 삭제할 수 없습니다.");
            }else {
                postRepository.deleteById(id);
                throw new IllegalArgumentException("게시글 삭제 성공");
            }

        }else {
            return null;
        }
    }



}
