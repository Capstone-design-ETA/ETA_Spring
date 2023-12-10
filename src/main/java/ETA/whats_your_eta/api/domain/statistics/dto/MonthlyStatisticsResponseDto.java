package ETA.whats_your_eta.api.domain.statistics.dto;

import lombok.*;

import java.util.List;

public class MonthlyStatisticsResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetMonthlyStatisticsDto{
        private Integer steps;
        private Integer locations;
        private List<String> mostCallers;
        private List<VisitedRegionDto> vistedRegions;
    }
}
