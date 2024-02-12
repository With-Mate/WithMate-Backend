package com.gdscewha.withmate.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.model.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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
    @Column(name = "memberId", updatable = false)
    private Long id;

    @Column(nullable = false, name = "userName", unique = true)
    private String userName;

    @Column(nullable = false, name = "nickname")
    private String nickname;

    @Column(nullable = false, name = "passwd")
    private String passwd;

    @Column(nullable = false, name = "email", unique = true)
    private String email;

    @Column(nullable = false, name = "birth")
    private String birth;

    @Column(nullable = false, name = "country")
    private String country;

    @Builder.Default
    @Column(nullable = false, name = "regDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate regDate = LocalDate.now();

    @Builder.Default
    @Column(nullable = false, name = "loginDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate loginDate = LocalDate.now();

    @Builder.Default
    @Column(nullable = false, name = "isRelationed")
    private Boolean isRelationed = false;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private Role role = Role.USER;

    @OneToOne
    @JsonManagedReference
    @JoinColumn(name="matchingId") //FK
    @NotFound(action= NotFoundAction.IGNORE)
    private Matching matching;

    public Member updateLoginDate() {
        this.loginDate = LocalDate.now();
        return this;
    }

    public Member updateBySocialProfileWhenLogin(String name, String birthday, String locale, LocalDate date){
        this.nickname = name;
        this.birth = birthday;
        this.country = locale;
        this.loginDate = date;
        return this;
    }
}
