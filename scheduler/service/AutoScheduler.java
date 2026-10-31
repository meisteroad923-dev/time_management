package scheduler.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import scheduler.model.BlockedTime;
import scheduler.model.FixedSchedule;
import scheduler.model.FlexibleSchedule;
import scheduler.model.WeeklySchedule;


/**
 * 자동 배치 전략을 실행하는 클래스이다.
 *
 * SchedulingStrategy를 이용하여 자동 배치 알고리즘을 실행하며,
 * 필요에 따라 다른 자동 배치 전략으로 교체할 수 있다.
 */
public class AutoScheduler {
    private SchedulingStrategy strategy;

    /**
     * 사용할 자동 배치 전략을 지정하여 AutoScheduler를 생성한다.
     *
     * @param strategy 사용할 자동 배치 알고리즘
     */
    public AutoScheduler(SchedulingStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 실행 중 사용할 자동 배치 전략을 변경한다.
     *
     * @param strategy 새로운 자동 배치 알고리즘
     */
    public void setStrategy(SchedulingStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 현재 설정된 전략을 이용하여 한 주의 유동 일정을 자동 배치한다.
     *
     * @param weekStart 배치할 주의 시작일
     * @param dayStart 하루 배치 시작 시간
     * @param dayEnd 하루 배치 종료 시간
     * @param fixedSchedules 고정 일정 목록
     * @param flexibleSchedules 유동 일정 목록
     * @param blockedTimes 배치 금지 시간 목록
     * @return 한 주의 자동 배치 결과
     */
    public WeeklySchedule autoSchedule(LocalDate weekStart, LocalTime dayStart, LocalTime dayEnd, List<FixedSchedule> fixedSchedules, List<FlexibleSchedule> flexibleSchedules, List<BlockedTime> blockedTimes) {
        return strategy.schedule(flexibleSchedules, fixedSchedules, blockedTimes, weekStart, dayStart, dayEnd
        );
    }
}
