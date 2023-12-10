package ETA.whats_your_eta.api.domain.statistics;

import ETA.whats_your_eta.api.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "monthly_statistics")
public class MonthlyStatistics {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
//    @DateTimeFormat(pattern = "yyyy-MM")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM", timezone = "Asia/Seoul") //string형태로
    private LocalDate month;

    private Integer averageStep;

    private Integer visitedLoc;

    @OneToMany(mappedBy = "monthlyStatistics")
    private List<CallRecord> mostCaller;

    @OneToMany(mappedBy = "month")
    private List<VisitedRegion> visitedRegions;


}
