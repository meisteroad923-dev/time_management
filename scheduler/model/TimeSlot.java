//유동일정 자동배치 전 비어있는(fixed, blocked)가 차지하지 않는 공간을 나타내기 위한 클래스
package scheduler.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

public class TimeSlot {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime){
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDate getDate(){
        return date;
    }

    public LocalTime getStartTime(){
        return startTime;
    }

    public LocalTime getEndTime(){
        return endTime;
    }
    // 시작시간, 종료시간을 통해 예상 소요 시간을 분단위로 구함
    public long getDurationMinutes(){
        return Duration.between(startTime, endTime).toMinutes();
    }
}
