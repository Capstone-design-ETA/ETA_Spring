package ETA.whats_your_eta.api.domain.statistics.controller;

import ETA.whats_your_eta.api.domain.statistics.dto.DailyStatisticsRequestDto;
import ETA.whats_your_eta.api.domain.statistics.dto.DailyStatisticsResponseDto.*;
import ETA.whats_your_eta.api.domain.statistics.dto.MonthlyStatisticsResponseDto;
import ETA.whats_your_eta.api.domain.statistics.service.DailyStatisticsService;
import ETA.whats_your_eta.api.domain.statistics.service.MonthlyStatisticsService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final DailyStatisticsService dailyStatisticsService;
    private final MonthlyStatisticsService monthlyStatisticsService;

    /*
    일별통계 생성
     */
    @PostMapping("/date")
    public ResponseEntity<DailyStatisticsDto> createDailyStatistics(@JsonFormat(pattern = "yyyy-MM-dd") @RequestBody DailyStatisticsRequestDto.CreateDailyStatisticsDto request) {

        DailyStatisticsDto dailyStatisticsDto = dailyStatisticsService.createDailyStatistics(request);
        return ResponseEntity.ok(dailyStatisticsDto);
    }

    /**
     * 통계 탭 전체 조회 - 이미지 썸네일 NO
     */

    @GetMapping("/{month}")
    public ResponseEntity<GetMonthDto> getAllStatistics(@PathVariable LocalDate month){
        GetMonthDto getMonthDto = dailyStatisticsService.getMonthDailyEvent(month);
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

    /**
     * 일별 통게 - 일기 조회
     */
    @GetMapping("/{date}/diary")
    public ResponseEntity<GetDailyDiaryDto> getDailyDiary(@PathVariable LocalDate date) {
        GetDailyDiaryDto getDailyDiaryDto = dailyStatisticsService.getDailyDiarybyDate(date);
        return ResponseEntity.ok(getDailyDiaryDto);
    }

    /**
     * 월별통계 조회
     */
    @PostMapping("/month/{month}")
    public ResponseEntity<MonthlyStatisticsResponseDto.GetMonthlyStatisticsDto> getMonthlyStatistics(@PathVariable LocalDate month) {
        MonthlyStatisticsResponseDto.GetMonthlyStatisticsDto result = monthlyStatisticsService.getMonthlyStatistics(month);
        return ResponseEntity.ok(result);
    }
}
