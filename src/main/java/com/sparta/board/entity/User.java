package com.sparta.board.entity;


import com.sparta.board.dto.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable: null 허용 여부
    // unique: 중복 허용 여부 (false 일때 중복 허용)
//    @Column(nullable = false, unique = true)
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();

    public User(String username, String password, UserRoleEnum role) {
        this.username   = username;
        this.password   = password;
        this.role       = role;
    }
}