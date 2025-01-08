package ETA.whats_your_eta.api.domain.user.controller;

import ETA.whats_your_eta.api.domain.user.dto.AuthRequestDto;
import ETA.whats_your_eta.api.domain.user.dto.AuthResponseDto;
import ETA.whats_your_eta.api.domain.user.dto.UserRequestDto;
import ETA.whats_your_eta.api.domain.user.dto.UserResponseDto;
import ETA.whats_your_eta.api.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "User management APIs")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user information", description = "Fetches user information based on the authenticated user.")
    @GetMapping(value = "/my_info")
    public ResponseEntity<UserResponseDto.Information> getMyInfo() {
        return ResponseEntity.ok((userService.getMyInfo()));
    }

    @Operation(summary = "Register a new user", description = "Registers a new user in the system.")
    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody UserRequestDto.Register data) {
        return ResponseEntity.ok(userService.register(data));
    }

    @Operation(summary = "Authenticate user with Google", description = "Authenticates a user using Google OAuth2 access token.")
    @PostMapping(value = "/auth/google")
    public ResponseEntity<AuthResponseDto> authenticateWithGoogle(@RequestBody AuthRequestDto request) {
        AuthResponseDto response = userService.authenticateWithGoogle(request.getAccessToken());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update user information", description = "Updates user details like name, sex, etc.")
    @PutMapping(value = "/update")
    public ResponseEntity<UserResponseDto.Information> updateUserInfo(@Valid @RequestBody UserRequestDto.Update data) {
        return ResponseEntity.ok(userService.updateUserInfo(data));
    }
}