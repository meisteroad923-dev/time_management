package scheduler.service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import scheduler.model.BlockedTime;
import scheduler.model.FixedSchedule;
import scheduler.model.RepeatType;
import scheduler.model.TimeSlot;

import java.time.DayOfWeek;

public class AutoScheduler {
    //일정이 특정 날짜에 실제로 적용되는 일정인지 판단
    private boolean appliesToDate(LocalDate scheduleDate, RepeatType repeatType, Set<DayOfWeek> repeatDays, LocalDate targetDate){
        if (repeatType == RepeatType.NONE){
            return scheduleDate.equals(targetDate);
        }

        if (repeatType == RepeatType.DAILY){
            return true;
        }

        if (repeatType == RepeatType.WEEKLY){
            return repeatDays.contains(targetDate.getDayOfWeek());
        }
    return false;
    }

    //해당 날짜의 사용중인 시간대를 찾아서 리스트로 저장 -> 사이의 빈공간 계산 후 반환( 추후 자동 배치 시 사용)
    private List<TimeSlot> createFreeSlotsForDate(LocalDate targetDate, LocalTime dayStart, LocalTime dayEnd, 
                                                List<FixedSchedule> fixedSchedules, List<BlockedTime> blockedTimes){
        List<TimeSlot> occupiedSlots = new ArrayList<>();
        //FixedSchedule을 occupiedSlots에 추가
        for (FixedSchedule schedule : fixedSchedules){
            if (appliesToDate(schedule.getDate(), schedule.getRepeatType(), schedule.getRepeatDays(), targetDate)){
                occupiedSlots.add(new TimeSlot(targetDate, schedule.getStartTime(), schedule.getEndTime()));
            }
        }
        //BlockedTime도 똑같이 occupiedSlots에 추가
        for (BlockedTime time : blockedTimes){
            if( appliesToDate( time.getDate(), time.getRepeatType(), time.getRepeatDays(), targetDate)){
                occupiedSlots.add(new TimeSlot(targetDate, time.getStartTime(), time.getEndTime()));
            }
        }

        //occupiedSlots 시간순 정렬
        occupiedSlots.sort(Comparator.comparing(TimeSlot::getStartTime));

        //빈 시간 계산( 하루 시작시간, 종료시간을 고려하여)
        List<TimeSlot> freeSlots = new ArrayList<>();
        LocalTime cursor = dayStart;

        for (TimeSlot slot : occupiedSlots){
            LocalTime slotStart = slot.getStartTime();
            LocalTime slotEnd = slot.getEndTime();

            // dayStart 이전에 완전히 끝난 일정
            if (!slotEnd.isAfter(dayStart)){
                continue;
            }
            // dayEnd 이후에 시작하는 일정
            if (!slotStart.isBefore(dayEnd)){
                break;
            }
            // 시작이 dayStart보다 빠르면 dayStart까지만 잘라서 사용
            if (slotStart.isBefore(dayStart)) {
                slotStart = dayStart;
            }

            // 종료가 dayEnd보다 늦으면 dayEnd까지만 사용
            if (slotEnd.isAfter(dayEnd)) {
                slotEnd = dayEnd;
            }

            // cursor와 점유시간 사이가 비어있으면 free slot
            if (cursor.isBefore(slotStart)){
                freeSlots.add(new TimeSlot(targetDate, cursor, slotStart));
            }

            // 점유시간 끝까지 cursor 이동
            if (cursor.isBefore(slotEnd)) {
                cursor = slotEnd;
            }
        }
        if (cursor.isBefore(dayEnd)){
            freeSlots.add(new TimeSlot(targetDate, cursor, dayEnd));
        }
        return freeSlots;
    }
    //날짜별로 순회하면서 freeSlot를 만들어서 일주일치 빈 시간 2차원 배열을 만듬
    private Map<LocalDate, List<TimeSlot>> createWeeklyFreeSlots(LocalDate weekStart, LocalTime dayStart, LocalTime dayEnd, 
                                                            List<FixedSchedule> fixedSchedules, List<BlockedTime> blockedTimes) {
        Map<LocalDate, List<TimeSlot>> weeklyFreeSlots = new HashMap<>();

        for (int i = 0; i < 7 ; i++){
            LocalDate date = weekStart.plusDays(i);

            List<TimeSlot> freeSlots = createFreeSlotsForDate(date, dayStart, dayEnd, fixedSchedules, blockedTimes);
            weeklyFreeSlots.put(date, freeSlots);
        }

        return weeklyFreeSlots;
    }


}
