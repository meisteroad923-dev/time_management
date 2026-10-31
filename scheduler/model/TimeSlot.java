package scheduler.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

/**
 * 특정 날짜의 하나의 시간 구간을 나타내는 클래스이다.
 *
 * 유동 일정을 자동 배치하기 전에 고정 일정과 배치 금지 시간이
 * 차지하지 않는 빈 시간 구간을 표현하는 데 사용한다.
 */
public class TimeSlot {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    /**
     * 날짜와 시작 및 종료 시간을 이용하여 시간 구간을 생성한다.
     *
     * @param date 시간 구간이 속한 날짜
     * @param startTime 시간 구간의 시작 시간
     * @param endTime 시간 구간의 종료 시간
     */
    public TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime){
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * 시간 구간이 속한 날짜를 반환한다.
     *
     * @return 시간 구간의 날짜
     */
    public LocalDate getDate(){
        return date;
    }

    /**
     * 시간 구간의 시작 시간을 반환한다.
     *
     * @return 시작 시간
     */
    public LocalTime getStartTime(){
        return startTime;
    }

    /**
     * 시간 구간의 종료 시간을 반환한다.
     *
     * @return 종료 시간
     */
    public LocalTime getEndTime(){
        return endTime;
    }


    /**
     * 시작 시간과 종료 시간 사이의 길이를 분 단위로 계산한다.
     *
     * @return 시간 구간의 길이(분)
     */
    public long getDurationMinutes(){
        return Duration.between(startTime, endTime).toMinutes();
    }
}
