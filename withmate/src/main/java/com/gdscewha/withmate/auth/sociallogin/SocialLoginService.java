package com.gdscewha.withmate.auth.sociallogin;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SocialLoginService {
/*
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

        log.info("소셜 로그인 중 - JsonNode로 Member 업데이트 혹은 신규 생성으로 진입");
        return saveOrUpdate(userResourceNode);
    }
    // JsonNode로 Member 업데이트 혹은 신규 생성
    private Member saveOrUpdate(JsonNode userResourceNode) {
        String email = userResourceNode.get("email").asText();
        String name = userResourceNode.get("name").asText();
        String birthday = userResourceNode.get("birthday").asText();
        String locale = userResourceNode.get("locale").asText();

        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()){
            log.info("Doing Social Login - MemberRepository에서 email로 member를 찾았고, 존재합니다.");
            Member member = memberOptional.get().updateWhenLogin(name, birthday, locale, LocalDate.now());
            log.info(member.getUserName()); //  Member의 이메일 확인
            log.info(member.getNickname()); //  Member의 닉네임 확인
            log.info(member.getEmail()); //  Member의 이메일 확인
            log.info(member.getBirth()); // Member의 생일 확인
            log.info(member.getCountry()); // Member의 국가 확인
            log.info(member.getRole().toString()); // Member의 역할 확인
            return memberRepository.save(member);
        } else {
            log.info("Doing Social Login - MemberRepository에서 email로 member를 찾았고, 존재하지 않으므로 생성합니다.");
            Member member = generateMember(userResourceNode);
            log.info(member.getUserName()); //  Member의 이메일 확인
            log.info(member.getNickname()); //  Member의 닉네임 확인
            log.info(member.getEmail()); //  Member의 이메일 확인
            log.info(member.getBirth()); // Member의 생일 확인
            log.info(member.getCountry()); // Member의 국가 확인
            log.info(member.getRole().toString()); // Member의 역할 확인
            return memberRepository.save(member);
        }
    }
    // JsonNode로 새 계정 생성
    private Member generateMember(JsonNode userResourceNode) {
        log.info("Doing Social Login - JsonNode로 새 계정 생성");
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
*/
}