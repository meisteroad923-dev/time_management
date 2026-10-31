package scheduler.model;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.Set;

import java.io.Serializable;
/**
 * 사용자가 등록하는 고정 일정을 나타내는 클래스이다.
 *
 * 고정 일정은 적용 시작일과 종료일을 가지며,
 * 반복 유형에 따라 특정 기간 동안 한 번, 매일 또는 매주 반복될 수 있다.
 */
public class FixedSchedule implements Serializable{
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private RepeatType repeatType;
    private Set<DayOfWeek> repeatDays;
    private static final long serialVersionUID = 1L;

    /**
     * 적용 기간, 시간 및 반복 정보를 포함하는 고정 일정을 생성한다.
     *
     * @param name 일정 이름
     * @param startDate 일정 적용 시작일
     * @param endDate 일정 적용 종료일
     * @param startTime 일정 시작 시간
     * @param endTime 일정 종료 시간
     * @param repeatType 반복 유형
     * @param repeatDays 반복 요일 집합
     */
    public FixedSchedule(String name, LocalDate startDate, LocalDate endDate, 
                        LocalTime startTime, LocalTime endTime, RepeatType repeatType, Set<DayOfWeek> repeatDays) {

        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeatType = repeatType;
        this.repeatDays = repeatDays; 
    }

    /**
     * 일정 이름을 반환한다.
     *
     * @return 일정 이름
     */
    public String getName(){
        return this.name;
    }

    /**
     * 일정 적용 시작일을 반환한다.
     *
     * @return 적용 시작일
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * 일정 적용 종료일을 반환한다.
     *
     * @return 적용 종료일
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
    * 일정 시작 시간을 반환한다.
    *
    * @return 일정 시작 시간
    */
    public LocalTime getStartTime(){
        return this.startTime;
    }

    /**
     * 일정 종료 시간을 반환한다.
     *
     * @return 종료 시간
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * 반복 유형을 반환한다.
     *
     * @return 반복 유형
     */
    public RepeatType getRepeatType() {
        return repeatType;
    }

    /**
     * 반복 요일 집합을 반환한다.
     *
     * @return 반복 요일 집합
     */
    public Set<DayOfWeek> getRepeatDays() {
        return repeatDays;
    }
}
