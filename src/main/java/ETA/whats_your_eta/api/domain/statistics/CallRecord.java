package ETA.whats_your_eta.api.domain.statistics;

import ETA.whats_your_eta.api.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Getter
@Entity
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CallRecord {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private DailyStatistics dailyStatistics;

    //탑3에 드는 call record만 해당
    @ManyToOne(fetch = FetchType.LAZY)
    private MonthlyStatistics monthlyStatistics;

    @Column(nullable = false)
    private LocalDateTime date; //몇시에 전화왔는지

    @Column(nullable = false)
    private String name;

    private int duration;

}
