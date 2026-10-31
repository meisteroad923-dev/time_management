package scheduler.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import scheduler.model.FixedSchedule;
import scheduler.model.FlexibleSchedule;
import scheduler.model.BlockedTime;
import scheduler.model.WeeklySchedule;

/**
 * 유동 일정, 자동 배치 알고리즘의 공통 인터페이스이다.
 *
 * 서로 다른 자동 배치 알고리즘을 동일한 방식으로
 * 교체하여 사용할 수 있도록 한다.
 */
public interface SchedulingStrategy {
    /**
     * 입력된 일정 정보를 바탕으로 한 주의 유동 일정을 자동 배치한다.
     *
     * @param flexibleSchedules 유동 일정 목록
     * @param fixedSchedules 고정 일정 목록
     * @param blockedTimes 배치 금지 시간 목록
     * @param weekStart 배치할 주의 시작일
     * @param dayStart 하루 배치 시작 시간
     * @param dayEnd 하루 배치 종료 시간
     * @return 한 주의 자동 배치 결과
     */
    WeeklySchedule schedule(
            List<FlexibleSchedule> flexibleSchedules,
            List<FixedSchedule> fixedSchedules,
            List<BlockedTime> blockedTimes,
            LocalDate weekStart,
            LocalTime dayStart,
            LocalTime dayEnd
    );
}