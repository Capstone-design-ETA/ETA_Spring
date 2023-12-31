package ETA.whats_your_eta.api.domain.statistics;

import ETA.whats_your_eta.api.domain.diary.Diary;
import ETA.whats_your_eta.api.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Entity
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "dailyStatistic")
    private List<Diary> diary;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer steps;

    @OneToMany(mappedBy = "dailyStatistics")
    private List<CallRecord> callRecords;
}
