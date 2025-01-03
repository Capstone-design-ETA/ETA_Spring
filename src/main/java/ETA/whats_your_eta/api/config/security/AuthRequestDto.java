package ETA.whats_your_eta.api.config.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDto {

    private String accessToken;
    //private String RefreshToken;
}
