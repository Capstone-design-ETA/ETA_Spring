package ETA.whats_your_eta.api.domain.statistics.repository;

import ETA.whats_your_eta.api.domain.statistics.MonthlyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyStatisticsRepository extends JpaRepository<MonthlyStatistics, Long> {
}
