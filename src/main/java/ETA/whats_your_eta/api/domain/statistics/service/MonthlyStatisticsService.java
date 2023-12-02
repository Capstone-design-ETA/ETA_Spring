package ETA.whats_your_eta.api.domain.statistics.service;

import ETA.whats_your_eta.api.domain.statistics.CallRecord;
import ETA.whats_your_eta.api.domain.statistics.DailyStatistics;
import ETA.whats_your_eta.api.domain.statistics.MonthlyStatistics;
import ETA.whats_your_eta.api.domain.statistics.VisitedRegion;
import ETA.whats_your_eta.api.domain.statistics.dto.DailyStatisticsResponseDto;
import ETA.whats_your_eta.api.domain.statistics.dto.MonthlyStatisticsResponseDto;
import ETA.whats_your_eta.api.domain.statistics.dto.VisitedRegionDto;
import ETA.whats_your_eta.api.domain.statistics.repository.MonthlyStatisticsRepository;
import ETA.whats_your_eta.api.domain.statistics.repository.VisitedRegionRepository;
import ETA.whats_your_eta.api.domain.user.User;
import ETA.whats_your_eta.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthlyStatisticsService {
    //평균걸음수, 방문한 장소수(지역수로 퉁치자), 전화많이 한 top3, 방문 지역 백분율 통계(visitedRegion)
    // 해당 월의 마지막날짜가 되면 계산
    // 해당 월이 총 몇일인지 알아야할 필요 o
    private final MonthlyStatisticsRepository monthlyStatisticsRepository;
    private final VisitedRegionRepository visitedRegionRepository;
    private final UserService userService;

    //다음 월 1일 00:10가 되면 월별 통계 create - 스케줄러
    @Transactional
    public void createMonthlyStatistics() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        int lastMonthDays = lastDayOfLastMonth.getDayOfMonth();

        User user = userService.getCurrentUser();
        YearMonth lastMonth = YearMonth.now().minusMonths(1); //저번 달 구하기


    }


    //월별통계 조회
    public MonthlyStatisticsResponseDto.GetMonthlyStatisticsDto getMonthlyStatistics(YearMonth yearMonth) {

        MonthlyStatistics monthlyStatistics = monthlyStatisticsRepository.findByYearMonth(yearMonth).get();
        List<VisitedRegion> visitedRegions = visitedRegionRepository.findAllByMonth(monthlyStatistics);
        List<VisitedRegionDto> visitedRegionDtos = null;

        for(VisitedRegion visitedRegion : visitedRegions){
            visitedRegionDtos.add(VisitedRegionDto.builder()
                    .regionName(visitedRegion.getRegion())
                    .percentage(visitedRegion.getPercentage())
                    .build());
        }

        MonthlyStatisticsResponseDto.GetMonthlyStatisticsDto result = MonthlyStatisticsResponseDto.GetMonthlyStatisticsDto.builder()
                .steps(monthlyStatistics.getAverageStep())
                .locations(monthlyStatistics.getVisitedLoc())
                .mostCallers(monthlyStatistics.getMostCaller())
                .vistedRegions(visitedRegionDtos)
                .build();

        return result;
    }


}
