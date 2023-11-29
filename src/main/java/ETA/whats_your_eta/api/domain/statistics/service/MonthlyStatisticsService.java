package ETA.whats_your_eta.api.domain.statistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonthlyStatisticsService {
    //평균걸음수, 방문한 장소수(지역수로 퉁치자), 전화많이 한 top3, 방문 지역 백분율 통계(visitedRegion)
    // 해당 월의 마지막날짜가 되면 계산
    // 해당 월이 총 몇일인지 알아야할 필요 o

    //다음 월 1일 00:00가 되면 월별 통계 create - 스케줄러


    //월별통계 조회


}
