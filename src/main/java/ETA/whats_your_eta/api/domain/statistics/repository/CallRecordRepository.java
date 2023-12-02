package ETA.whats_your_eta.api.domain.statistics.repository;

import ETA.whats_your_eta.api.domain.statistics.CallRecord;
import ETA.whats_your_eta.api.domain.statistics.DailyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CallRecordRepository extends JpaRepository<CallRecord, Long> {

    List<CallRecord> findAllByDailyStatistics(DailyStatistics dailyStatistics);
}
