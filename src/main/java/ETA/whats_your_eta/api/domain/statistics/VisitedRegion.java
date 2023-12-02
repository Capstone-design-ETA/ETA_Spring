package ETA.whats_your_eta.api.domain.statistics;

import ETA.whats_your_eta.api.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitedRegion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "month_id")
    private MonthlyStatistics month;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String region;

    private Double percentage;
}
