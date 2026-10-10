package scheduler.model;

import java.time.LocalDate;

public class FlexibleSchedule {
    private String name;
    private int estimatedMinutes;
    private LocalDate deadline;
    private int importance;
    private boolean completed;

    public FlexibleSchedule(String name, int estimatedMinutes, LocalDate deadline,
                            int importance){
        this.name = name;
        this.estimatedMinutes = estimatedMinutes;
        this.deadline = deadline;
        this.importance = importance;
        this.completed =false;
    }

    public String getName(){
        return this.name;
    }
    public int getEstimatedMinutes(){
        return this.estimatedMinutes;
    }
    public LocalDate getDeadline(){
        return deadline;
    }
    public int getImportance(){
        return importance;
    }
    public boolean isCompleted(){
        return completed;
    }

    
}
