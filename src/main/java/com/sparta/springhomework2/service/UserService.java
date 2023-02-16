package com.sparta.springhomework2.service;

import com.sparta.springhomework2.dto.LoginRequestDto;
import com.sparta.springhomework2.dto.SignupRequestDto;
import com.sparta.springhomework2.dto.StatusResponseDto;
import com.sparta.springhomework2.entity.User;
import com.sparta.springhomework2.entity.UserRoleEnum;
import com.sparta.springhomework2.jwt.JwtUtil;
import com.sparta.springhomework2.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
//    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    @Transactional
    public StatusResponseDto<String> signup(SignupRequestDto signupRequestDto) {
//        String username = signupRequestDto.getUsername();
//        String password = signupRequestDto.getPassword();


            // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(signupRequestDto.getUsername());
        if (found.isPresent()) {//중복체크
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
        //사용자 등록
//        User user = User.builder().requestDto(signupRequestDto).build();
        User user = User.builder().requestDto(signupRequestDto).build();
        userRepository.save(user);
        return StatusResponseDto.success("회원가입성공");
    }

    @Transactional(readOnly = true)
    public StatusResponseDto<String> login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        //사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        // 비밀번호 확인
        if (!user.getPassword().equals(password) ) {
            throw new IllegalArgumentException("비밀번호를 잘못 입력하였습니다.");
        }

        return StatusResponseDto.success(jwtUtil.createToken(user.getUsername(),user.getRole()));

    }
}
