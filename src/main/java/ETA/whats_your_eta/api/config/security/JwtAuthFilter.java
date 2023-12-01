package ETA.whats_your_eta.api.config.security;

import ETA.whats_your_eta.api.domain.user.User;
import ETA.whats_your_eta.api.domain.user.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // 헤더 값 검증
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        // request Header에서 AccessToken을 가져온다.
        String atc = request.getHeader("Authorization");

        // AccessToken 비어 있을 경우 토큰 검사 생략 (모두 허용 URL의 경우 토큰 검사 통과. JwtAuthFilter는 매 요청마다 인증을 수행하기 때문)
        if (!StringUtils.hasText(atc)) {
            doFilter(request, response, filterChain);
            return;
        }

        // AccessToken 있을 경우
        // AccessToken을 검증하고, 만료되었을 경우 예외를 발생시킨다.
        if (!jwtUtil.verityToken(atc)) {
            throw new JwtException("Access Token 만료!");
        }

        // AccessToken의 값이 있고, 유효한 경우
        if (jwtUtil.verityToken(atc)) {
            // AccessToken 내부의 payload에 있는 이메일로 사용자를 조회한다. 없다면 예외를 발생시킨다.
            User user = userRepository.findByEmail(jwtUtil.getUid(atc)).orElseThrow(IllegalStateException::new);
            // Authenticataion 객체로 변환하여 Security Context에 넣어준다.
            Authentication auth = getAuthentication(user);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }


    public Authentication getAuthentication(User user) {
        return new UsernamePasswordAuthenticationToken(user, "", Arrays.asList(new SimpleGrantedAuthority(user.getRole().getKey())));
    }
}
