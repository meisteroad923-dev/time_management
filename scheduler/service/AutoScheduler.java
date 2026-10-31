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
     * 지정된 주간과 자동 배치 시작일을 기준으로
     * 유동 일정의 자동 배치를 수행한다.
     *
     * 실제 배치 로직은 현재 설정된 SchedulingStrategy에 위임하며,
     * 고정 일정과 배치 금지 시간을 제외한 사용 가능한 시간에
     * 유동 일정을 배치한 결과를 WeeklySchedule 객체로 반환한다.
     *
     * @param weekStart 자동 배치를 수행할 주의 시작일(월요일)
     * @param scheduleStartDate 해당 주에서 자동 배치를 시작할 날짜
     * @param dayStart 하루 자동 배치 가능 시작 시간
     * @param dayEnd 하루 자동 배치 가능 종료 시간
     * @param fixedSchedules 등록된 고정 일정 목록
     * @param flexibleSchedules 등록된 유동 일정 목록
     * @param blockedTimes 등록된 배치 금지 시간 목록
     * @return 자동 배치 결과를 포함하는 WeeklySchedule 객체
     */
    public WeeklySchedule autoSchedule( LocalDate weekStart, LocalDate scheduleStartDate, LocalTime dayStart, LocalTime dayEnd, 
        List<FixedSchedule> fixedSchedules, List<FlexibleSchedule> flexibleSchedules, List<BlockedTime> blockedTimes) {
        return strategy.schedule( flexibleSchedules, fixedSchedules, blockedTimes, weekStart, scheduleStartDate, dayStart, dayEnd);
    }
}
