package ETA.whats_your_eta.api.domain.statistics.repository;

import ETA.whats_your_eta.api.domain.statistics.MonthlyStatistics;
import ETA.whats_your_eta.api.domain.statistics.VisitedRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//월별
public interface VisitedRegionRepository extends JpaRepository<VisitedRegion, Long> {

    List<VisitedRegion> findAllByMonth(MonthlyStatistics month);
}
