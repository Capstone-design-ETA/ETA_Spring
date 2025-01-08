package ETA.whats_your_eta.api.security;

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
    private final JwtAuthFilter jwtAuthFilter; // 요청을 필터링하여 JWT 인증을 수행하는 필터

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
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
                .antMatchers("/api/user/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated() // 그 외의 모든 요청은 인증이 필요함
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // JWT 이용한 인증 처리 수행

        return http.build();
    }
}