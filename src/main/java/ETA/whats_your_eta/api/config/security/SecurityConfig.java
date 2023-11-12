package ETA.whats_your_eta.api.config.security;

import ETA.whats_your_eta.api.config.oauth2.CustomOAuth2UserService;
import ETA.whats_your_eta.api.config.oauth2.OAuth2FailureHandler;
import ETA.whats_your_eta.api.config.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService; // OAuth2 로그인 시 사용자 정보 불러오는 서비스
    private final OAuth2SuccessHandler oAuth2SuccessHandler; // OAuth2 로그인 성공 시 실행될 핸들러
    private final JwtAuthFilter jwtAuthFilter; // 요청을 필터링하여 JWT 인증을 수행하는 필터
    private final OAuth2FailureHandler oAuth2FailureHandler; // OAuth2 로그인 실패 시 실행될 핸들러

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, OAuth2SuccessHandler oAuth2SuccessHandler, JwtAuthFilter jwtAuthFilter, OAuth2FailureHandler oAuth2FailureHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.jwtAuthFilter = jwtAuthFilter;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션관리 정책은 STATELESS(세션이 있으면 쓰지도 않고, 없으면 만들지도 않음)
                .and()
                .formLogin().disable() // 폼 기반 로그인 비활성화
                .httpBasic().disable() // 기본적인 HTTP 인증 방식 비활성화
                .authorizeHttpRequests()
                .antMatchers("/api/user/**").permitAll() // 토큰 발급을 위한 경로는 모두 허용하고,
                .anyRequest().authenticated() // 그 외의 모든 요청은 인증이 필요함
                .and()
                .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService) // OAuth2 로그인시 사용자 정보를 가져오는 엔드포인트 & 사용자 서비스 설정
                .and()
                .failureHandler(oAuth2FailureHandler)
                .successHandler(oAuth2SuccessHandler)
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // JWT 이용한 인증 처리 수행

    return http.build();
    }
}
