package com.sparta.springhomework2.Controller;

import com.sparta.springhomework2.dto.LoginRequestDto;
import com.sparta.springhomework2.dto.SignupRequestDto;
import com.sparta.springhomework2.dto.StatusResponseDto;
import com.sparta.springhomework2.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

//    @GetMapping("/signup")
//    public ModelAndView signupPage() {
//        return new ModelAndView("signup");
//    }
//
//    @GetMapping("/login")
//    public ModelAndView loginPage() {
//        return new ModelAndView("login");
//    }

    @ResponseBody
    @PostMapping("/signup")
    public StatusResponseDto<String> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }


    @ResponseBody
    @PostMapping("/login")
    public StatusResponseDto<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

}
