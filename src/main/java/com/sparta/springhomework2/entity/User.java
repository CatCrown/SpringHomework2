package com.sparta.springhomework2.entity;

import com.sparta.springhomework2.dto.PostRequestDto;
import com.sparta.springhomework2.dto.SignupRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name="users")
@Getter
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

//    public User(String username, String password, UserRoleEnum role) {
//        this.username = username;
//        this.password = password;
//        this.role = role;
//    }

    @Builder
    public User(SignupRequestDto requestDto){
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        role = requestDto.isAdminCheck() ? UserRoleEnum.ADMIN : UserRoleEnum.USER;
    }
    //  도경님은 requestDto.isAdminCheck() ?  로 되어 있는데 왜 안될까?


}
