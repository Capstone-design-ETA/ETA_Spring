package ETA.whats_your_eta.api.domain.statistics.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitedRegionDto {

    private String regionName;
    private Double percentage;
}
