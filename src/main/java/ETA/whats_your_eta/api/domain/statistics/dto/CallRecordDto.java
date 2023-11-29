package ETA.whats_your_eta.api.domain.statistics.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CallRecordDto {

    private Long userId;
    private LocalDateTime date;
    private String name; //상대방
    private Integer duration; //수정 필요

}
