package com.sparta.springhomework2.entity;

import com.sparta.springhomework2.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.swing.text.AbstractDocument;

@Entity
@Getter
@NoArgsConstructor

public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(String contents, User user, Post post) {
        this.contents = contents;
        this.user = user;
        this.post = post;
    }

    public void update(CommentRequestDto requestDto){
        this.contents = requestDto.getContent();
    }
}
