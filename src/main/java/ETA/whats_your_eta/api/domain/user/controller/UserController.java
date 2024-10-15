package ETA.whats_your_eta.api.domain.user.controller;

import ETA.whats_your_eta.api.config.oauth2.GoogleTokenVerifier;
import ETA.whats_your_eta.api.config.security.JwtUtil;
import ETA.whats_your_eta.api.domain.user.dto.UserRequestDto;
import ETA.whats_your_eta.api.domain.user.dto.UserResponseDto;
import ETA.whats_your_eta.api.domain.user.repository.UserRepository;
import ETA.whats_your_eta.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/my_info")
    public ResponseEntity<UserResponseDto.Information> getMyInfo() {
        return ResponseEntity.ok((userService.getMyInfo()));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserResponseDto.Information> registerUser(@RequestBody UserRequestDto.Register data) {
        return ResponseEntity.ok(userService.register(data));
    }

    @PostMapping(value = "/auth/google")
    public ResponseEntity<String> authenticateWithGoogle(@RequestBody Map<String, String> request) {
        String accessToken = request.get("accessToken");
        String jwt = userService.authenticateWithGoogle(accessToken);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwt)
                .body(jwt);
    }
}