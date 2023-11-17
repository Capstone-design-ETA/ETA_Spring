package ETA.whats_your_eta.api.config.oauth2;

import ETA.whats_your_eta.api.config.security.GeneratedToken;
import ETA.whats_your_eta.api.config.security.JwtUtil;
import ETA.whats_your_eta.api.domain.user.Role;
import ETA.whats_your_eta.api.domain.user.User;
import ETA.whats_your_eta.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // OAuth2User로 캐스팅하여 인증된 사용자 정보를 가져온다.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // 사용자 이메일을 가져온다.
        String email = oAuth2User.getAttribute("email");
        log.info(String.valueOf(oAuth2User.getAttributes()));
        // 서비스 제공 플랫폼(Google, Kakao)이 어디인지 가져온다.
        String provider = oAuth2User.getAttribute("provider");
        // CustomOAuth2UserService에서 세팅한 로그인한 회원 존재 여부를 가져온다.
        Optional<User> userOptional = userRepository.findByEmail(email);

        // OAuth2User로부터 Role을 얻어온다.
        String role = oAuth2User.getAuthorities().stream()
                .findFirst() // 첫번째 Role을 찾아온다.
                .orElseThrow(IllegalAccessError::new) // 존재하지 않을 시 예외를 던진다.
                .getAuthority(); // Role을 가져온다.


        if (!(userOptional.isPresent())) {
            User user = new User();
            user.setEmail(email);
            user.setName(authentication.getName());
            user.setRole(Role.GUEST);
            userRepository.save(user);
        }

        GeneratedToken token = jwtUtil.generateToken(email, role);
        log.info("jwtToken = {}", token.getAccessToken());

        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/api/user/jwt-test")
                .queryParam("accessToken", token.getAccessToken())
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
        log.info("redirect 준비");
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }


/*        // 회원이 존재할 경우
        if (userOptional.isPresent()) {

            // jwt token 발행을 시작한다.
            GeneratedToken token = jwtUtil.generateToken(email, role);
            log.info("존재하는 회원의 jwtToken = {}", token.getAccessToken());

            // accessToken을 쿼리 스트링에 담는 url을 만든다.
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/api/user/jwt-test")
                    .queryParam("accessToken", token.getAccessToken())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            log.info("존재하는 회원 redirect 준비");
            // 로그인 확인 페이지로 리다이렉트 시킨다.
            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        } else { // 회원이 존재하지 않을 경우

            // 서비스 제공자와 email을 쿼리 스트링에 담는 url을 만든다.
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/api/user/jwt-test")
                    .queryParam("email", (String) oAuth2User.getAttribute("email"))
                    .queryParam("provider", provider)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            log.info("redirect 준비");
            // 회원 가입 페이지로 리다이렉트 시킨다.
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
*/
}