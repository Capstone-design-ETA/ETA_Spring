package ETA.whats_your_eta.api.domain.statistics.repository;

import ETA.whats_your_eta.api.domain.statistics.DailyStatistics;
import net.bytebuddy.asm.Advice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyStatisticsRepository extends JpaRepository<DailyStatistics, Long> {

    Optional<DailyStatistics> findByDate(LocalDate date);

    /**
     * TODO diaryrepository로 옮기기
     */
    List<DailyStatistics> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
