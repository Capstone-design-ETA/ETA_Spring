package ETA.whats_your_eta.api.domain.diary;

import ETA.whats_your_eta.api.domain.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@ToString
public class Diary {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    @Pattern(regexp = "^\\d\\-(0[1-9]|1[012])$", message = "년월 형식(yyyy-MM)에 맞지 않습니다")
    private String date; // 받아 올 날짜

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt; // 일기 작성 시각

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt; // 일기 수정 시각

    @Column(nullable = false)
    private String content;

//    @ManyToOne
//    @JoinColumn(name = "dailyStatistic_id")
//    private DailyStatistic dailyStatistic;
}
