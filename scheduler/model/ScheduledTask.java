package scheduler.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

/**
 * 자동 배치된 유동 일정의 실제 배치 정보를 나타내는 클래스이다.
 *
 * 배치된 유동 일정과 해당 일정이 배치된 날짜,
 * 시작 시간 및 종료 시간을 저장한다.
 */
public class ScheduledTask {
    private FlexibleSchedule flexibleSchedule;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    /**
     * 자동 배치된 유동 일정의 배치 정보를 생성한다.
     *
     * @param flexibleSchedule 배치된 유동 일정
     * @param date 일정이 배치된 날짜
     * @param startTime 배치 시작 시간
     * @param endTime 배치 종료 시간
     */
    public ScheduledTask( FlexibleSchedule flexibleSchedule, LocalDate date, LocalTime startTime, LocalTime endTime){
        this.flexibleSchedule = flexibleSchedule;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * 배치된 유동 일정을 반환한다.
     *
     * @return 배치된 유동 일정
     */
    public FlexibleSchedule getFlexibleSchedule(){
        return flexibleSchedule;
    }

    /**
     * 일정이 배치된 날짜를 반환한다.
     *
     * @return 배치 날짜
     */
    public LocalDate getDate(){
        return date;
    }

    /**
     * 일정의 배치 시작 시간을 반환한다.
     *
     * @return 배치 시작 시간
     */
    public LocalTime getStartTime(){
        return startTime;
    }

    /**
     * 일정의 배치 종료 시간을 반환한다.
     *
     * @return 배치 종료 시간
     */
    public LocalTime getEndTime(){
        return endTime;
    }

    /**
     * 배치된 일정의 실행 시간을 분 단위로 계산한다.
     *
     * @return 배치된 일정의 실행 시간(분)
     */
    public long getDurationMinutes(){
        return Duration.between(startTime, endTime).toMinutes();
    }
}
