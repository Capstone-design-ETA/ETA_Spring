package ETA.whats_your_eta.api.domain.statistics.service;

import ETA.whats_your_eta.api.domain.diary.Diary;
import ETA.whats_your_eta.api.domain.diary.dto.DiaryResponseDto;
import ETA.whats_your_eta.api.domain.diary.repository.DiaryRepository;
import ETA.whats_your_eta.api.domain.image.Image;
import ETA.whats_your_eta.api.domain.statistics.CallRecord;
import ETA.whats_your_eta.api.domain.statistics.DailyStatistics;
import ETA.whats_your_eta.api.domain.statistics.dto.CallRecordDto;
import ETA.whats_your_eta.api.domain.statistics.dto.DailyStatisticsRequestDto.*;
import ETA.whats_your_eta.api.domain.statistics.dto.DailyStatisticsResponseDto.*;
import ETA.whats_your_eta.api.domain.statistics.repository.CallRecordRepository;
import ETA.whats_your_eta.api.domain.statistics.repository.DailyStatisticsRepository;
import ETA.whats_your_eta.api.domain.user.User;
import ETA.whats_your_eta.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.aspectj.weaver.ast.Call;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyStatisticsService {
    //나의발자취, 걸음수, 칼로리, 통화기록, 오늘의일기
    private final DailyStatisticsRepository dailyStatisticsRepository;
    private final CallRecordRepository callRecordRepository;
    private final DiaryRepository diaryRepository;
    private final UserService userService;

    //밤 11:59 되면 자동으로(프론트에서 post호출) 받아서 create

    /**
     * 다이어리 추가
     */
    @Transactional
    public DailyStatisticsDto createDailyStatistics(CreateDailyStatisticsDto request) {
//        User user = userService.getCurrentUser();
//        Long userId = user.getId();
        DailyStatistics dailyStatistics = dailyStatisticsRepository.save(DailyStatistics.builder()
//                                                                                            .user(user)
                .steps(request.getSteps())
                .date(request.getDate())
                .build());

        for (CallRecordDto callRecordDto : request.getCallRecords()) {
            callRecordRepository.save(CallRecord.builder()
//                                                    .user(user)
                    .name(callRecordDto.getName())
                    .date(callRecordDto.getDate())
                    .duration(callRecordDto.getDuration())
                    .dailyStatistics(dailyStatistics)
                    .build());
        }

        /**
         * TODO 다이어리 갖고오는 건 날짜 땜에 보류
         */

        return DailyStatisticsDto.builder()
//                .userId(userId)
                .date(request.getDate())
                .steps(request.getSteps())
                .callRecords(request.getCallRecords())
                .build();

    }


    //일별통계 전체 조회(일단은 자동조회가 아니라 탭해서 보는걸로 기준) - 프론트에서 sqlite로 저장해서 쓴는게 나을 것 같지만 아직 프로젝트 규모가 작으므로 그냥
//    매번 전체 조회에서 불러오는 걸로 하자 - 2023-12-2 이런 형식
//    일단 사진은 제외

    public GetMonthDto getMonthDailyEvent(LocalDate month) {
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        List<DailyStatistics> dailyStatistics = dailyStatisticsRepository.findByDateBetween(startDate, endDate); //해당기간 존재하는 일간통계 구함
        List<LocalDate> localDates = null;
        for (DailyStatistics daily : dailyStatistics) {
            localDates.add(daily.getDate());
        }

        return GetMonthDto.builder()
                .dates(localDates)
                .build();
    }

    //일별통계 상세조회 -
    //diary 추가
    public GetDailyStatisticsDto findByDate(LocalDate date) {

        DailyStatistics dailyStatistics = dailyStatisticsRepository.findByDate(date).get();
        List<CallRecord> callRecords = callRecordRepository.findAllByDailyStatistics(dailyStatistics);
        List<CallRecordDto> callRecordDtos = null;
        for (CallRecord callRecord : callRecords) {
            callRecordDtos.add(CallRecordDto.builder()
                    .date(callRecord.getDate())
                    .name(callRecord.getName())
                    .duration(callRecord.getDuration())
                    .build()
            );
        }

        return GetDailyStatisticsDto.builder()
                .date(dailyStatistics.getDate())
                .steps(dailyStatistics.getSteps())
                .callRecords(callRecordDtos)
                /**
                 * TODO diary 추가
                 */
                .build();
    }

    /**
     * 일별통계 diary 상세조회
     */
    public GetDailyDiaryDto getDailyDiarybyDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(formatter); //date 날짜 형식 바꿈
        List<Diary> diaries = diaryRepository.findByDate(formattedDate);
        List<DiaryResponseDto.GetStatsticsDiaryDto> getStatsticsDiaryDto = null;
        for (Diary diary : diaries) {
            getStatsticsDiaryDto.add(
                    DiaryResponseDto.GetStatsticsDiaryDto.builder()
                            .diaryId(diary.getId())
                            .imageUrls(diary.getImages().stream()
                                    .map(Image::getUrl)
                                    .collect(Collectors.toList()))
                            .content(diary.getContent())
                            .date(diary.getDate())
                            .build()
            );
        }

        return GetDailyDiaryDto.builder()
                .diaries(getStatsticsDiaryDto)
                .build();
    }
}