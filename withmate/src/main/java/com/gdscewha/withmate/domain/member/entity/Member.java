package com.gdscewha.withmate.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private Long birth;

    @Column(nullable = false, name = "nationality")
    private String nationality;

    @Column(nullable = false, name = "regDate")
    private Long regDate;

    @Column(nullable = false, name = "loginDate")
    private Long loginDate;

    @Column(nullable = false, name = "isRelationed")
    private Boolean isRelationed;

}
