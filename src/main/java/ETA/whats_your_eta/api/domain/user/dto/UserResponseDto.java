package ETA.whats_your_eta.api.domain.user.dto;

import ETA.whats_your_eta.api.domain.user.GoalLevel;
import ETA.whats_your_eta.api.domain.user.User;
import lombok.*;

public class UserResponseDto {

    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Information {
        private Long id;
        private String name;
        private Character sex;
        private Integer height;
        private Integer weight;
        private GoalLevel goalLevel;

        public static Information of(User user) {
            return Information.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .sex(user.getSex())
                    .height(user.getHeight())
                    .weight(user.getWeight())
                    .goalLevel(user.getGoalLevel())
                    .build();
        }


    }
}
