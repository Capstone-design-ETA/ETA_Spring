package ETA.whats_your_eta.api.domain.user.dto;

import ETA.whats_your_eta.api.domain.user.GoalLevel;
import lombok.*;

public class UserRequestDto {

    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {
        private Character sex;
        private Integer height;
        private Integer weight;
        private GoalLevel goalLevel;
    }
}
