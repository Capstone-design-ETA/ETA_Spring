package ETA.whats_your_eta.api.domain.user.controller;

import ETA.whats_your_eta.api.config.security.AuthRequestDto;
import ETA.whats_your_eta.api.domain.user.dto.AuthResponseDto;
import ETA.whats_your_eta.api.domain.user.dto.UserRequestDto;
import ETA.whats_your_eta.api.domain.user.dto.UserResponseDto;
import ETA.whats_your_eta.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody UserRequestDto.Register data) {
        return ResponseEntity.ok(userService.register(data));
    }

    @PostMapping(value = "/auth/google")
    public ResponseEntity<AuthResponseDto> authenticateWithGoogle(@RequestBody AuthRequestDto request) {
        AuthResponseDto response = userService.authenticateWithGoogle(request.getAccessToken());
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<UserResponseDto.Information> updateUserInfo(@Valid @RequestBody UserRequestDto.Update data) {
        return ResponseEntity.ok(userService.updateUserInfo(data));
    }
}