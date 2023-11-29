package ETA.whats_your_eta.api.domain.statistics.repository;

import ETA.whats_your_eta.api.domain.statistics.VisitedRegion;
import org.springframework.data.jpa.repository.JpaRepository;

//월별
public interface VisitedRegionRepository extends JpaRepository<VisitedRegion, Long> {

}
