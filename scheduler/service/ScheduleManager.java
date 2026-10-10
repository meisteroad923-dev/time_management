package scheduler.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import scheduler.model.FixedSchedule;
import scheduler.model.FlexibleSchedule;
import scheduler.model.RepeatType;
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

    //두 일정의 시작, 종료 시간을 받아서 겹치는지 확인
    private boolean isTimeOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2){
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    //날짜가 겹치는지 확인, 날짜가 겹치는 경우의 수는 총 4가지, 4가지 경우를 각각 확인
    //(NONE, NONE), (DAILY), (WEEKLY, NONE), (WEEKLLY,WEEKLY)  // DAILY는 반드시 충돌이 남
    private boolean isDateOverlap(LocalDate date1, RepeatType type1, Set<DayOfWeek> days1, LocalDate date2, RepeatType type2, Set<DayOfWeek> days2){
        
        if (type1 == RepeatType.NONE && type2 == RepeatType.NONE){
            return date1.equals(date2);
        }
        //RepeatType.DAILY는 매일 반복하는 경우,필연적으로 충돌 발생 true반환
        if (type1 == RepeatType.DAILY || type2 == RepeatType.DAILY){
            return true;
        }
        if (type1 == RepeatType.NONE && type2 == RepeatType.WEEKLY){
            return days2.contains(date1.getDayOfWeek());  // day1의 요일이 day2의 요일에 포함되는지 검사
        }
        if (type2 == RepeatType.NONE && type1 == RepeatType.WEEKLY){
            return days1.contains(date2.getDayOfWeek());  // day1의 요일이 day2의 요일에 포함되는지 검사
        }
        if (type1 == RepeatType.WEEKLY && type2 == RepeatType.WEEKLY){
            for (DayOfWeek day : days1){
                if (days2.contains(day)){
                    return true;
                }
            }
        }
        return false;
    }
    //고정일정의 기존 일정과 충돌 여부 확인
    public boolean checkConflict(FixedSchedule newSchedule){
        for (FixedSchedule existing : fixedSchedules){
            boolean dateOverlap = isDateOverlap(newSchedule.getDate(),newSchedule.getRepeatType(), newSchedule.getRepeatDays(),
                                                existing.getDate(), existing.getRepeatType(), existing.getRepeatDays());
            boolean timeOverlap = isTimeOverlap(newSchedule.getStartTime(), newSchedule.getEndTime(),
                                                existing.getStartTime(), existing.getEndTime());
            
            if (dateOverlap && timeOverlap){
                return true;
            }
        }
        return false;
    }

    public boolean checkConflict(BlockedTime newBlockedTime) {
        // fixedSchedule이 blockedtime보다 우선순위가 높음 
        // BlockedTime은 기존 FixedSchedule과 BlockedTime 모두와 충돌하면 등록할 수 없음
        for (FixedSchedule existing : fixedSchedules){
            boolean dateOverlap = isDateOverlap(newBlockedTime.getDate(), newBlockedTime.getRepeatType(),newBlockedTime.getRepeatDays(), 
                                                existing.getDate(), existing.getRepeatType(), existing.getRepeatDays());
            boolean timeOverlap = isTimeOverlap(newBlockedTime.getStartTime(),newBlockedTime.getEndTime(), 
                                                existing.getStartTime(), existing.getEndTime());
            if (dateOverlap && timeOverlap){
                return true;
            }
        }

        for (BlockedTime existing : blockedTimes){
            boolean dateOverlap = isDateOverlap(newBlockedTime.getDate(), newBlockedTime.getRepeatType(),newBlockedTime.getRepeatDays(), 
                                                existing.getDate(), existing.getRepeatType(), existing.getRepeatDays());
            boolean timeOverlap = isTimeOverlap(newBlockedTime.getStartTime(),newBlockedTime.getEndTime(), 
                                                existing.getStartTime(), existing.getEndTime());
            if (dateOverlap && timeOverlap){
                return true;
            }
        }
        return false;
    }
}


