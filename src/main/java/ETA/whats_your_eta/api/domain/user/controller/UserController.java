package ETA.whats_your_eta.api.domain.user.controller;

import ETA.whats_your_eta.api.domain.user.dto.UserRequestDto;
import ETA.whats_your_eta.api.domain.user.dto.UserResponseDto;
import ETA.whats_your_eta.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/my_info")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<UserResponseDto.Information> getMyInfo() {
        return ResponseEntity.ok((userService.getMyInfo()));
    }

    @PostMapping(value = "/register")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<UserResponseDto.Information> register(@RequestBody UserRequestDto.Register data) {
        return ResponseEntity.ok(userService.register(data));
    }
}