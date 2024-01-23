package com.gdscewha.withmate.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long id;

    @Column(nullable = false, name = "userName")
    private String userName;

    @Column(nullable = false, name = "nickname")
    private String nickname;

    @Column(nullable = false, name = "passwd")
    private String passwd;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "birth")
    private String birth;

    @Column(nullable = false, name = "country")
    private String country;

    @Column(nullable = false, name = "regDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate regDate;

    @Column(nullable = false, name = "loginDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate loginDate;

    @Column(nullable = false, name = "isRelationed")
    private Boolean isRelationed;

}
