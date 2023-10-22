package ETA.whats_your_eta.api.config.auth.dto;

import ETA.whats_your_eta.api.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

// 세션에 사용자 정보를 저장하기 위한 DTO 클래스. User 클래스에 직렬화 코드를 넣는 대신 이렇게 추가로 만들어 두는 것이 이후 유지보수 때 도움 됨.
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;

    // SessionUser에는 인증된 사용자 정보만 필요함
    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
