package com.gdscewha.withmate.auth.jwt;

import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.repository.MemberRepository;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

// JWT(JSON Web Token)을 생성하고 검증하는 클래스
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final MemberRepository memberRepository;

    private static final long JWT_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 10; //개발 테스트용: 10일

    /* 토큰을 서명하고 검증하는 데 사용되는 비밀 키 */
    @Value("${jwt.secret}")
    private String secretKey;

    /* 보안 강화를 위해 secretKey의 바이트를 Base64 타입으로 인코딩*/
    @PostConstruct
    protected void init() {
        this.secretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
    }

    /* 해싱 알고리즘(HMAC512) 사용해 서명 생성
     * 서명을 통해서 데이터의 무결성과, 특정 소유자가 생성한 것임을 확인 가능 */
    private Algorithm getSign(){
        return Algorithm.HMAC512(secretKey);
    }

    // Jwt 토큰 생성 메소드
    public String generateJwtToken(Long id, String userName){
        Date tokenExpireDate = new Date(System.currentTimeMillis() + (JWT_EXPIRATION_TIME)); //토큰의 만료 날짜 생성
        return JWT.create()
                .withSubject(userName) // 토큰의 사용자를 식별하는 고유 주제
                .withExpiresAt(tokenExpireDate) // 토큰의 만료 시간
                .withClaim("id", id) // 토큰에 포함되는 정보인 Claim 설정
                .withClaim("userName", userName)
                .sign(this.getSign());
    }

    // Jwt 토큰이 유효한지 확인하는 메소드
    public Member validJwtToken(String jwtToken){
        // userName 값이 null이 아닌지
        String userName = getUserNameClaim(jwtToken);
        if (userName == null){
            return null;
        }
        // JWT_EXPIRATION_TIME이 지나지 않았는지
        Date expiresAt = getExpireTimeClaim(jwtToken);
        if (!this.validExpiredTime(expiresAt)) {
            return null;
        }
        /* email 값이 정상적으로 있고, JWT_EXPIRATION_TIME도 지나지 않았다면,
         * 해당 토큰의 userName 정보를 가진 멤버가 있는지 DB에서 확인 */
        return memberRepository.findMemberByUserName(userName);
    }

    private String getUserNameClaim(String jwtToken) {
        return JWT.require(this.getSign())  //JWT 토큰의 유효성을 검사하기 위해 필요한 설정을 수행
                .build()
                .verify(jwtToken)  //주어진 JWT 토큰을 검증
                .getClaim("userName")  //JWT 토큰에서 "userName" 클레임을 가져옴
                .asString();  //가져온 클레임을 문자열로 변환하여 반환
    }

    private Date getExpireTimeClaim(String jwtToken) {
        return JWT.require(this.getSign())
                .acceptExpiresAt(JWT_EXPIRATION_TIME)
                .build()
                .verify(jwtToken)
                .getExpiresAt();
    }

    // Jwt 토큰의 만료 시간 검증
    private boolean validExpiredTime(Date expireTime){
        // 만료시간을 LocalDateTime 객체로 변경
        LocalDateTime localTimeExpireTime = expireTime
                .toInstant()
                .atZone(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
        // 현재 시간과 만료시간을 비교해 반환
        return LocalDateTime.now().isBefore(localTimeExpireTime);
    }

}