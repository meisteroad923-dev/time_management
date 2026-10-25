//자동 배치 결과를 저장할 자료구조
/*
WeeklySchedule
 ├─ LocalDate weekStart
 └─ List<ScheduledTask> scheduledTasks
  */


package scheduler.model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class WeeklySchedule {  
    private LocalDate weekStart;
    private List<ScheduledTask> scheduledTasks;
    
    public WeeklySchedule(LocalDate  weekStart){
        this.weekStart = weekStart;
        this.scheduledTasks = new ArrayList<>();
    }

    public LocalDate getWeekStart(){
        return weekStart;
    }

    public LocalDate getWeekEnd(){
        return weekStart.plusDays(6);
    }

    public List<ScheduledTask> getScheduledTasks(){
        return scheduledTasks;
    }

    public void addScheduledTask(ScheduledTask task){
        scheduledTasks.add(task);
    }

}
