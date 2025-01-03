package ETA.whats_your_eta.api.domain.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDto {
    private String token; // JWT access token
    private String tokenType;
    private String role;
}
