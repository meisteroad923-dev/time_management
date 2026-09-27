package scheduler.service;

import java.util.ArrayList;
import java.util.List;

import scheduler.model.FixedSchedule;
import scheduler.model.FlexibleSchedule;
import scheduler.model.BlockedTime;

public class ScheduleManager {
    private List<FixedSchedule> fixedSchedules;
    private List<FlexibleSchedule> flexibleSchedules;
    private List<BlockedTime> blockedTimes;

    public ScheduleManager(){
        fixedSchedules = new ArrayList<>();
        flexibleSchedules = new ArrayList<>();
        blockedTimes = new ArrayList<>();
    }

    public void addFixedSchedule(FixedSchedule schedule){
        fixedSchedules.add(schedule);
    }

    public List<FixedSchedule> getFixedSchedule(){
        return fixedSchedules;
    }

    public void addFlexibleSchedule(FlexibleSchedule schedule){
        flexibleSchedules.add(schedule);
    }

    public List<FlexibleSchedule> getFlexibleSchedule(){
        return flexibleSchedules;
    }

    public void addBlockedTime(BlockedTime schedule){
        blockedTimes.add(schedule);
    }

    public List<BlockedTime> getBlockedTime(){
        return blockedTimes;
    }
}
