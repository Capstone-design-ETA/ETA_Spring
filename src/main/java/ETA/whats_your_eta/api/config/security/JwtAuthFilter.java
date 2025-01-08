package ETA.whats_your_eta.api.config.security;

import ETA.whats_your_eta.api.domain.user.User;
import ETA.whats_your_eta.api.domain.user.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // 헤더 값 검증
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        // Authorization 헤더가 없거나 비어 있을 경우 토큰 검사 생략 (모두 허용 URL의 경우 토큰 검사 통과. JwtAuthFilter는 매 요청마다 인증을 수행하기 때문)
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " 제외 실제 토큰만 추출
        String accessToken = authorizationHeader.substring(7);

        // AccessToken을 검증하고, 만료되었을 경우 예외 발생
        if (!jwtUtil.verifyToken(accessToken)) {
            throw new JwtException("Access Token Expired!");
        }

        // AccessToken의 값이 있고, 유효한 경우
        String email = jwtUtil.getUid(accessToken);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found."));

        // Authentication 객체로 변환하여 Security Context에 넣어 인증 정보 설정
        Authentication auth = getAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    public Authentication getAuthentication(User user) {
        return new UsernamePasswordAuthenticationToken(
                user, // 인증 주체 (Principal)
                "", // 자격 증명 (Credentials). 비밀번호를 사용하는 경우 여기에 저장하지만, JWT 인증에서는 빈 문자열 사용
                List.of(new SimpleGrantedAuthority(user.getRole().getKey())) // 사용자의 권한 목록 (GrantedAuthorities)
        );
    }
}
