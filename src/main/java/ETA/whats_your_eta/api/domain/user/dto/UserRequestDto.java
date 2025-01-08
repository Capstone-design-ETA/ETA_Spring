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
        @Size(max = 255, message = "The name can be up to 255 characters.")
        private String name;

        @NotNull(message = "Gender is a required input value.")
        private Character sex;

        @NotNull @Positive(message = "The height must be a positive value.")
        private Integer height;

        @NotNull @Positive(message = "The weight must be a positive value.")
        private Integer weight;

        @NotNull(message = "The goal level is a required input value.")
        private GoalLevel goalLevel;
    }

    @Getter @Setter
    public static class Update {
        @Size(max = 255, message = "The name can be up to 255 characters.")
        private String name;

        private Character sex;

        @Positive(message = "The height must be a positive value.")
        private Integer height;

        @Positive(message = "The weight must be a positive value.")
        private Integer weight;

        private GoalLevel goalLevel;
    }
}
