package scheduler.model;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.Set;

public class BlockedTime{
    private String name;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private RepeatType repeatType;
    private Set<DayOfWeek> repeatDays;
    

    
    public BlockedTime(String name, LocalDate date, LocalTime startTime, LocalTime endTime, 
                        RepeatType repeatType, Set<DayOfWeek> repeatDays){
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeatType = repeatType;
        this.repeatDays = repeatDays;
    }
    
    public String getName(){
        return this.name;
    }
    public LocalTime getStartTime(){
        return this.startTime;
    }
    public LocalTime getEndTime(){
        return endTime;
    }
    public RepeatType getRepeatType(){
        return repeatType;
    }
    public Set<DayOfWeek> getRepeatDays(){
        return repeatDays;
    }
    public LocalDate getDate(){
        return date;
    }
}
