package scheduler.model;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.Set;

import java.io.Serializable;
/**
 * 유동 일정의 자동 배치를 금지하는 시간대를 나타내는 클래스이다.
 *
 * 배치 금지 시간은 적용 시작일과 종료일을 가지며,
 * 반복 유형에 따라 특정 기간 동안 반복될 수 있다.
 */
public class BlockedTime implements Serializable{
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private RepeatType repeatType;
    private Set<DayOfWeek> repeatDays;
    private static final long serialVersionUID = 1L;
    

    /**
     * 적용 기간을 포함하는 배치 금지 시간을 생성한다.
     *
     * @param name 배치 금지 시간 이름
     * @param startDate 적용 시작일
     * @param endDate 적용 종료일
     * @param startTime 시작 시간
     * @param endTime 종료 시간
     * @param repeatType 반복 유형
     * @param repeatDays 반복 요일 집합
     */
    public BlockedTime(String name, LocalDate startDate, LocalDate endDate, 
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
     * 배치 금지 시간의 이름을 반환한다.
     *
     * @return 이름
     */
    public String getName() {
        return name;
    }

    /**
     * 적용 시작일을 반환한다.
     *
     * @return 적용 시작일
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * 적용 종료일을 반환한다.
     *
     * @return 적용 종료일
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * 시작 시간을 반환한다.
     *
     * @return 시작 시간
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * 종료 시간을 반환한다.
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
