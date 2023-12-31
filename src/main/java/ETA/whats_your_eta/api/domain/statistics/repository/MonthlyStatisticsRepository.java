package ETA.whats_your_eta.api.domain.statistics.repository;

import ETA.whats_your_eta.api.domain.statistics.CallRecord;
import ETA.whats_your_eta.api.domain.statistics.MonthlyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

public interface MonthlyStatisticsRepository extends JpaRepository<MonthlyStatistics, Long> {

    Optional<MonthlyStatistics> findByMonth(LocalDate yearMonth);
}
