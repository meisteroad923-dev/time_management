//자동 배치로 배치된 유동일정의 배치 정보
package scheduler.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

public class ScheduledTask {
    private FlexibleSchedule flexibleSchedule;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public ScheduledTask( FlexibleSchedule flexibleSchedule, LocalDate date, LocalTime startTime, LocalTime endTime){
        this.flexibleSchedule = flexibleSchedule;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public FlexibleSchedule getFlexibleSchedule(){
        return flexibleSchedule;
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

    public long getDurationMinutes(){
        return Duration.between(startTime, endTime).toMinutes();
    }
}
