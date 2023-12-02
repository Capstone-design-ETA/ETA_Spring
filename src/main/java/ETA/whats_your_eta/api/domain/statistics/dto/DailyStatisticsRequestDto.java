package ETA.whats_your_eta.api.domain.statistics.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;


public class DailyStatisticsRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateDailyStatisticsDto{
        private LocalDate date;
        private Integer steps;
        private List<CallRecordDto> callRecords;
    }
}
