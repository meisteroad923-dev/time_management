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
<<<<<<< HEAD
=======
    private List<FlexibleSchedule> unscheduledTasks;    //자리가 없어 배치 되지 못한 일정들
>>>>>>> 2867760 (feat: implement automatic schedule placement algorithm)
    
    public WeeklySchedule(LocalDate  weekStart){
        this.weekStart = weekStart;
        this.scheduledTasks = new ArrayList<>();
<<<<<<< HEAD
=======
        this.unscheduledTasks = new ArrayList<>();
>>>>>>> 2867760 (feat: implement automatic schedule placement algorithm)
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

<<<<<<< HEAD
=======
    public List<FlexibleSchedule> getUnscheduledTasks() {
        return unscheduledTasks;
    }

    public void addUnscheduledTask(FlexibleSchedule schedule) {
        unscheduledTasks.add(schedule);
    }
>>>>>>> 2867760 (feat: implement automatic schedule placement algorithm)
}
