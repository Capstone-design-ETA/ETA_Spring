package ETA.whats_your_eta.api.domain.statistics.dto;

import ETA.whats_your_eta.api.domain.diary.Diary;
import ETA.whats_your_eta.api.domain.diary.dto.DiaryResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class DailyStatisticsResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DailyStatisticsDto{
        private Long id;
        private List<Long> diaryId;
        private Long userId;
        private LocalDate date;
        private Integer steps;
        private List<CallRecordDto> callRecords;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetMonthDto{
        private List<LocalDate> dates;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DiaryImageDto{
        private boolean isEmpty;
        private String imageUrl;
        private LocalDate date;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetDailyStatisticsDto{
        private Long id;
//        private Long userId;
        private LocalDate date;
        private Integer steps;
        private List<CallRecordDto> callRecords;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetDailyDiaryDto{
        private List<DiaryResponseDto.GetStatsticsDiaryDto> diaries; //diarydto로 바꾸기
    }
}
