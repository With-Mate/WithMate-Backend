package com.gdscewha.withmate.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //private final CustomOAuth2UserService customOAuth2UserService;

    private String[] permitList = {
            "/login", "/signup", "/intro", "/"
    };

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .requestMatchers(new AntPathRequestMatcher("/static/**"));
    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin((form) -> form.disable())
                .csrf((csrf) -> csrf.disable())
                .cors((cors)->cors.disable())
                .httpBasic((httpBasic)->httpBasic.disable())

                .authorizeHttpRequests((authorizeRequests) -> { // 인증, 인가 설정
                    authorizeRequests
//                            .requestMatchers(permitList).permitAll()
//                            .requestMatchers("/api/**").authenticated()
                            .anyRequest().permitAll();
                })

                .oauth2Login( (oauth2Login) -> oauth2Login
                                .authorizationEndpoint(authorization -> authorization
                                        .baseUri("/login/oauth2/code/{registrationId}")
                                )
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("/api/home"); // 로그인 성공 시 리디렉션
                        })
//
                )
                .logout((log) -> {
                    log
                            .logoutSuccessUrl("/intro")
                            .invalidateHttpSession(true);
                })
                .build();

    }
    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
//        return auth.build();
//    }
}
