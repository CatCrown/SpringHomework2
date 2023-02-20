package com.sparta.springhomework2.service;

import com.sparta.springhomework2.dto.CommentRequestDto;
import com.sparta.springhomework2.dto.CommentResponseDto;
import com.sparta.springhomework2.dto.StatusResponseDto;
import com.sparta.springhomework2.entity.Comment;
import com.sparta.springhomework2.entity.Post;
import com.sparta.springhomework2.entity.User;
import com.sparta.springhomework2.entity.UserRoleEnum;
import com.sparta.springhomework2.jwt.JwtUtil;
import com.sparta.springhomework2.repository.CommentRepository;
import com.sparta.springhomework2.repository.PostRepository;
import com.sparta.springhomework2.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
//    private final Gson gson;

    public StatusResponseDto<CommentResponseDto> createComment(Long id, String content, HttpServletRequest request){
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
                Post post = postRepository.findById(id).orElseThrow(
                        () -> new NullPointerException("등록되지 않은 게시글입니다."));
                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다. "));
                Comment comment = new Comment(content, user, post);
                commentRepository.save(comment);
                return StatusResponseDto.success(new CommentResponseDto(comment));
            }
        }
        throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }

    @Transactional
    public StatusResponseDto<CommentResponseDto> updateComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request){
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(()-> new IllegalArgumentException("사용자 정보를 찾을 수 없다. "));
                Comment comment= commentRepository.findById(id).orElseThrow(
                        ()-> new NullPointerException("해당 댓글을 찾을 수 없습니다."));

                if (user.getRole() == UserRoleEnum.ADMIN || user.getUsername().equals(comment.getUser().getUsername())){
                    comment.update(commentRequestDto);
                    return StatusResponseDto.success(new CommentResponseDto(comment));
                }else {
                    throw new IllegalArgumentException("작성자만 수정이 가능합니다.");
                }
            }
        }
        throw new IllegalArgumentException("해당 토큰이 유효하지 않습니다.");
    }

    public StatusResponseDto<String> deleteComment(Long id, HttpServletRequest request){
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null){
            if (jwtUtil.validateToken(token)){
                claims = jwtUtil.getUserInfoFromToken(token);
                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(()-> new IllegalArgumentException("사용자 정보를 찾을 수 없다. "));
                Comment comment= commentRepository.findById(id).orElseThrow(()-> new NullPointerException("해당 댓글을 찾을 수 없습니다."));
                if (user.getRole() == UserRoleEnum.ADMIN || user.getUsername().equals(comment.getUser().getUsername())){
                    commentRepository.deleteById(id);
                    return StatusResponseDto.success("delete success!");
                }else {
                    throw new IllegalArgumentException("작성자만 수정이 가능합니다.");
                }
            }
        }
        throw  new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }
}
