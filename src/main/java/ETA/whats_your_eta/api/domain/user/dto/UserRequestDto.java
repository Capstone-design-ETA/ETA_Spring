package ETA.whats_your_eta.api.domain.user.dto;

import ETA.whats_your_eta.api.domain.user.GoalLevel;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class UserRequestDto {

    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {
        @Size(max = 255, message = "이름은 최대 255자까지 입력 가능합니다.")
        private String name;

        @NotNull(message = "성별은 필수 입력 값입니다.")
        private Character sex;

        @NotNull @Positive(message = "키는 양수 값이어야 합니다.")
        private Integer height;

        @NotNull @Positive(message = "몸무게는 양수 값이어야 합니다.")
        private Integer weight;

        @NotNull(message = "목표 레벨은 필수 입력 값입니다.")
        private GoalLevel goalLevel;
    }

    @Getter @Setter
    public static class Update {
        @Size(max = 255, message = "이름은 최대 255자까지 입력 가능합니다.")
        private String name;

        private Character sex;

        @Positive(message = "키는 양수 값이어야 합니다.")
        private Integer height;

        @Positive(message = "몸무게는 양수 값이어야 합니다.")
        private Integer weight;

        private GoalLevel goalLevel;
    }
}
