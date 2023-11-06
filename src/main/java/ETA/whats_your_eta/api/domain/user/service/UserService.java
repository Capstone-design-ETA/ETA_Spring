package ETA.whats_your_eta.api.domain.user.service;

import ETA.whats_your_eta.api.domain.user.Role;
import ETA.whats_your_eta.api.domain.user.User;
import ETA.whats_your_eta.api.domain.user.dto.UserRequestDto;
import ETA.whats_your_eta.api.domain.user.dto.UserResponseDto;
import ETA.whats_your_eta.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    protected final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponseDto.Information getMyInfo() {
        return UserResponseDto.Information.of((userRepository.findById(getCurrentUser().getId())).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."
                )
        ));
    }

    @Transactional
    public UserResponseDto.Information register(UserRequestDto.Register data) {
        User user = getCurrentUser();

        if (user.getRole() != Role.GUEST) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "손님 권한을 가진 사람만 회원가입이 가능합니다."
            );
        }

        user.setSex(data.getSex());
        user.setHeight(data.getHeight());
        user.setWeight(data.getWeight());
        user.setGoalLevel(data.getGoalLevel());
        user.setRole(Role.USER);

        return UserResponseDto.Information.of(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "로그인 되지 않았습니다."
            );
        }

        return (User) authentication.getPrincipal();
    }
}
