package ETA.whats_your_eta.api.domain.statistics.controller;

import ETA.whats_your_eta.api.domain.statistics.dto.DailyStatisticsRequestDto;
import ETA.whats_your_eta.api.domain.statistics.dto.DailyStatisticsResponseDto.*;
import ETA.whats_your_eta.api.domain.statistics.service.DailyStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final DailyStatisticsService dailyStatisticsService;
    /*
    일별통계 생성
     */
    @PostMapping("/{date}")
    public ResponseEntity<DailyStatisticsDto> createDailyStatistics(@PathVariable LocalDate date, @RequestBody DailyStatisticsRequestDto.CreateDailyStatisticsDto request){

        DailyStatisticsDto dailyStatisticsDto = dailyStatisticsService.createDailyStatistics( date, request);
        return ResponseEntity.ok(dailyStatisticsDto);
    }

    /*
     통계 탭 전체 조회
     */
    @GetMapping("/month")
    public ResponseEntity<GetMonthDto> getAllStatistics(@PathVariable Integer month){
        GetMonthDto getMonthDto = dailyStatisticsService.getMonthDailyImage(month);
        return ResponseEntity.ok(getMonthDto);
    }

    /*
    일별 통계 상세 조회
     */
    @GetMapping("/{date}")
    public ResponseEntity<GetDailyStatisticsDto> getDailyStatistics(@PathVariable LocalDate date) {
        GetDailyStatisticsDto getDailyStatisticsDto = dailyStatisticsService.findByDate(date);

        return ResponseEntity.ok(getDailyStatisticsDto);
    }
}
