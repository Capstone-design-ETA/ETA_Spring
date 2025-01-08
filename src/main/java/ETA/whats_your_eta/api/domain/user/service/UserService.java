package ETA.whats_your_eta.api.domain.user.service;

import ETA.whats_your_eta.api.config.oauth2.GoogleTokenVerifier;
import ETA.whats_your_eta.api.config.security.JwtUtil;
import ETA.whats_your_eta.api.domain.user.Role;
import ETA.whats_your_eta.api.domain.user.User;
import ETA.whats_your_eta.api.domain.user.dto.AuthResponseDto;
import ETA.whats_your_eta.api.domain.user.dto.UserRequestDto;
import ETA.whats_your_eta.api.domain.user.dto.UserResponseDto;
import ETA.whats_your_eta.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    protected final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final GoogleTokenVerifier googleTokenVerifier;

    @Transactional(readOnly = true)
    public UserResponseDto.Information getMyInfo() {
        return UserResponseDto.Information.of((userRepository.findById(getCurrentUser().getId())).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found."
                )
        ));
    }

    @Transactional
    public AuthResponseDto register(UserRequestDto.Register data) {
        User user = getCurrentUser();

        if (user.getRole() != Role.GUEST) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Only those with guest rights can sign up for membership."
            );
        }

        user.registerProfile(data);
        user.setRole(Role.USER);

        userRepository.save(user);

        // ROLE 업데이트에 따른 새 JWT 토큰 생성
        String newJwt = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().getKey());

        log.info("User successfully registered: {}", user.getEmail());

        return new AuthResponseDto(newJwt, "Bearer", user.getRole().getKey());
    }

    @Transactional
    public AuthResponseDto authenticateWithGoogle(String accessToken) {
        // Google Access Token 검증 및 사용자 정보 가져오기
        Map<String, Object> googleUserInfo = googleTokenVerifier.getGoogleUserInfo(accessToken);
        String email = (String) googleUserInfo.get("email");
        String name = (String) googleUserInfo.get("name");

        // 사용자 존재 여부 확인
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .name(name)
                    .role(Role.GUEST)
                    .build();
            userRepository.save(newUser);
            log.info("New user created as guest: {}", email); // 최초 로그인 한 경우
            return newUser;
        });

        String jwt = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().getKey());
        log.info("User logged in: {} with role {}", email, user.getRole().getKey());

        return new AuthResponseDto(jwt, "Bearer", user.getRole().getKey());
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Login failed."
            );
        }

        return (User) authentication.getPrincipal();
    }

    @Transactional
    public UserResponseDto.Information updateUserInfo(UserRequestDto.Update data) {
        User user = getCurrentUser();

        user.updateProfile(data);

        userRepository.save(user);

        return UserResponseDto.Information.of(user);
    }
}