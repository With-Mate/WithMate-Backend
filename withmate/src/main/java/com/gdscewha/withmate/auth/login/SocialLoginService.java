package com.gdscewha.withmate.auth.login;


import com.fasterxml.jackson.databind.JsonNode;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.entity.Role;
import com.gdscewha.withmate.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Objects;

@Service
@Slf4j
public class SocialLoginService {

    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();
    private final MemberRepository memberRepository;

    public SocialLoginService(Environment env, MemberRepository memberRepository) {
        this.env = env;
        this.memberRepository = memberRepository;
    }

    // 소셜 로그인
    public Member socialLogin(String code, String registrationId) {
        if (!Objects.equals(registrationId, "google"))
            throw new RuntimeException("UNSUPPORTED SOCIAL TYPE");

        String accessToken = getAccessToken(code, registrationId);
        JsonNode userResourceNode = getUserResource(accessToken, registrationId);

        return saveOrUpdate(userResourceNode);
    }
    // JsonNode로 Member 업데이트 혹은 신규 생성
    private Member saveOrUpdate(JsonNode userResourceNode) {
        String email = userResourceNode.get("email").asText();
        String name = userResourceNode.get("name").asText();
        String birthday = userResourceNode.get("birthday").asText();
        String locale = userResourceNode.get("locale").asText();

        Member member = memberRepository.findByEmail(email)
                .map(entity -> entity.updateWhenLogin(name, birthday, locale, LocalDate.now())) // 이름, 생일, 지역, 날짜 업데이트
                .orElse(generateMember(userResourceNode));
        return memberRepository.save(member);
    }
    // JsonNode로 새 계정 생성
    private Member generateMember(JsonNode userResourceNode) {
        return Member.builder()
                .userName(userResourceNode.get("email").asText()) // userName을 email로 설정
                .nickname(userResourceNode.get("name").asText())
                .passwd(generatePasswordById(userResourceNode.get("id").asText())) // 랜덤한 비밀번호 생성
                .email(userResourceNode.get("email").asText())
                .birth(userResourceNode.get("birthday").asText())
                .country(userResourceNode.get("locale").asText().toUpperCase())
                .role(Role.SOCIAL) // 소셜 로그인
                .build();
    }

    private String getAccessToken(String authorizationCode, String registrationId) {
        String tokenUri = env.getProperty("oauth2." + registrationId + ".token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", env.getProperty("oauth2." + registrationId + ".client-id"));
        params.add("client_secret", env.getProperty("oauth2." + registrationId + ".client-secret"));
        params.add("redirect_uri", env.getProperty("oauth2." + registrationId + ".redirect-uri"));
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }

    private JsonNode getUserResource(String accessToken, String registrationId) {
        String resourceUri = env.getProperty("oauth2." + registrationId + ".resource-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(resourceUri)
                .queryParam("fields", "id, email, name, birthday, locale")
                .queryParam("access_token", accessToken);

        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, JsonNode.class).getBody();
    }

    // 사용자의 고유 아이디로 BCryptPasswordEncoder를 사용하여 비밀번호를 해싱
    private String generatePasswordById(String rawId) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(rawId);
    }

}