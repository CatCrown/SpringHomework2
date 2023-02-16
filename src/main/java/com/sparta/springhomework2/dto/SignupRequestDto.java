package com.sparta.springhomework2.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class SignupRequestDto {


    @Size(min=4, max=10, message = "사용자명은 4~10자리까지만 가능합니다")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9]).{4,10}$",message = "사용자명은 소문자, 숫자만 가능합니다.")
    private String username;
    @Size(min=8, max=15, message = "비밀번호은 8~15자리까지만 가능합니다")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,15}$",message = "비밀번호는 대/소문자, 숫자만 가능합니다.")
    private String password;
    private boolean adminCheck;

}
