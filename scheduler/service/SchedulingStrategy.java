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
     * 지정된 주간에서 유동 일정을 자동 배치한다.
     *
     * @param flexibleSchedules 등록된 유동 일정 목록
     * @param fixedSchedules 등록된 고정 일정 목록
     * @param blockedTimes 등록된 배치 금지 시간 목록
     * @param weekStart 해당 주의 시작일
     * @param scheduleStartDate 자동 배치를 시작할 날짜
     * @param dayStart 하루 배치 가능 시작 시간
     * @param dayEnd 하루 배치 가능 종료 시간
     * @return 자동 배치 결과
     */
    WeeklySchedule schedule(
        List<FlexibleSchedule> flexibleSchedules,
        List<FixedSchedule> fixedSchedules,
        List<BlockedTime> blockedTimes,
        LocalDate weekStart,
        LocalDate scheduleStartDate,
        LocalTime dayStart,
        LocalTime dayEnd);
}