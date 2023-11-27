package ETA.whats_your_eta.api.domain.diary;

import ETA.whats_your_eta.api.domain.image.Image;
import ETA.whats_your_eta.api.domain.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "날짜 형식(yyyy-MM-dd)에 맞지 않습니다")
    private String date; // 받아 올 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String content;

    @CreatedDate
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt; // 일기 작성 시각

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt; // 일기 수정 시각

    @Transient
    private final List<Image> images = new ArrayList<>();


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "dailyStatistic_id")
//    private DailyStatistic dailyStatistic;
}
