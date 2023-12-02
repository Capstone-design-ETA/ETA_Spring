package ETA.whats_your_eta.api.domain.statistics.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyStatisticsService {
    //나의발자취, 걸음수, 칼로리, 통화기록, 오늘의일기
    private final DailyStatisticsRepository dailyStatisticsRepository;
    private final CallRecordRepository callRecordRepository;
    private final UserService userService;

    //밤 11:59 되면 자동으로(프론트에서 post호출) 받아서 create

    /**
     * 다이어리 추가
     */
    @Transactional
    public DailyStatisticsDto createDailyStatistics(LocalDate date, CreateDailyStatisticsDto request) {
        User user = userService.getCurrentUser();
        Long userId = user.getId();
        DailyStatistics dailyStatistics = dailyStatisticsRepository.save(DailyStatistics.builder()
                                                                                            .user(user)
                                                                                            .steps(request.getSteps())
                                                                                            .date(request.getDate())
                                                                                            .build());

        for(CallRecordDto callRecordDto : request.getCallRecords()){
            callRecordRepository.save(CallRecord.builder()
                                                    .user(user)
                                                    .name(callRecordDto.getName())
                                                    .date(callRecordDto.getDate())
                                                    .duration(callRecordDto.getDuration())
                                                    .dailyStatistics(dailyStatistics)
                                                    .build());
        }

        return DailyStatisticsDto.builder()
                .userId(userId)
                .date(request.getDate())
                .steps(request.getSteps())
                .callRecords(request.getCallRecords())
                .build();

    }


    //일별통계 전체 조회(일단은 자동조회가 아니라 탭해서 보는걸로 기준) - 프론트에서 sqlite로 저장해서 쓴는게 나을 것 같지만 아직 프로젝트 규모가 작으므로 그냥
//    매번 전체 조회에서 불러오는 걸로 하자
//    사진이 있냐 없냐, 그리고 사진이 있으면 그 첫번쨰 사진만 받아오자

//    public GetMonthDto getMonthDailyImage(Integer month) {
//
//
//    }

    //일별통계 상세조회 - date로 할지 id로 할지 고민
    //diary 추가
    public GetDailyStatisticsDto findByDate(LocalDate date) {

        DailyStatistics dailyStatistics = dailyStatisticsRepository.findByDate(date).get();
        List<CallRecord> callRecords = callRecordRepository.findAllByDailyStatistics(dailyStatistics);


        return GetDailyStatisticsDto.builder()
                .date(dailyStatistics.getDate())
                .steps(dailyStatistics.getSteps())
//                .callRecords(dailyStatistics.)
                .build();
    }


}