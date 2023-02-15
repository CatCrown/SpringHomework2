package com.sparta.springhomework2.service;

import com.sparta.springhomework2.dto.LoginRequestDto;
import com.sparta.springhomework2.dto.SignupRequestDto;
import com.sparta.springhomework2.entity.User;
import com.sparta.springhomework2.entity.UserRoleEnum;
import com.sparta.springhomework2.jwt.JwtUtil;
import com.sparta.springhomework2.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        String un = "^(?=.*[a-z])(?=.*[0-9]).{4,10}$";
        Pattern unPattern = Pattern.compile(un);
        Matcher unMatcher = unPattern.matcher(username);
        //username: 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)
        String pw = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,15}$";
        Pattern pwPattern = Pattern.compile(pw);
        Matcher pwMatcher = pwPattern.matcher(password);
        //password: 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)


        // 유저넴과 비번이 조건 만족 할 때
        if (unMatcher.matches() && pwMatcher.matches()) {
            // 사용자 Role확인
            UserRoleEnum role = UserRoleEnum.USER;
            if (signupRequestDto.isAdmin()) {
                if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                    throw new IllegalArgumentException("관리자 암포가 틀려 등록이 불가합니다.");
                }
                role = UserRoleEnum.ADMIN;
            }
            // 회원 중복 확인
            Optional<User> found = userRepository.findByUsername(username);
            if (found.isPresent()) {
                throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
            }
            User user = new User(username, password, role);
            userRepository.save(user);
            throw new IllegalArgumentException("회원가입 성공하였습니다.");
        }
    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        //사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        // 비밀번호 확인
        if (!(user.getPassword().equals(password) && user.getUsername().equals(username))) {
            throw new IllegalArgumentException("유저넴 혹은 비밀번호를 잘못 입력하였습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER,jwtUtil.createToken(user.getUsername(),user.getRole()));
    }
}
