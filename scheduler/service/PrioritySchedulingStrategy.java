package scheduler.service;

import java.time.DayOfWeek;
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
import scheduler.model.FlexibleSchedule;
import scheduler.model.RepeatType;
import scheduler.model.ScheduledTask;
import scheduler.model.TimeSlot;
import scheduler.model.WeeklySchedule;

public class PrioritySchedulingStrategy implements SchedulingStrategy {
    @Override
    public WeeklySchedule schedule(
            List<FlexibleSchedule> flexibleSchedules,
            List<FixedSchedule> fixedSchedules,
            List<BlockedTime> blockedTimes,
            LocalDate weekStart,
            LocalTime dayStart,
            LocalTime dayEnd) {
        // 고정 일정과 블록시간대 기준으로 유동일정 배치 가능 시간대를 계싼
        Map<LocalDate, List<TimeSlot>> weeklyFreeSlots = createWeeklyFreeSlots(weekStart, dayStart, dayEnd, fixedSchedules, blockedTimes);
        // 완료되지 않은 유동일정을 정렬
        List<FlexibleSchedule> schedulesToPlace = sortFlexibleSchedule(flexibleSchedules);
        // 자동배치 결과 저장 자료구조 객체 생성
        WeeklySchedule weeklySchedule = new WeeklySchedule(weekStart);
        // 유동일정 배치
        for (FlexibleSchedule schedule : schedulesToPlace) {
            ScheduledTask scheduledTask = placeFlexibleSchedule(schedule, weekStart, weeklyFreeSlots);

            // 배치가 성공하면 결과 자료구조에 추가
            if (scheduledTask != null)
                weeklySchedule.addScheduledTask(scheduledTask);
            else
                weeklySchedule.addUnscheduledTask(schedule);
        }
        return weeklySchedule;
    }

    /**
    * 일정의 적용 기간과 반복 유형을 기준으로
    * 특정 날짜에 일정이 적용되는지 판단한다.
    *
    * @param startDate 일정 적용 시작일
    * @param endDate 일정 적용 종료일
    * @param repeatType 반복 유형
    * @param repeatDays 반복 요일 집합
    * @param targetDate 확인할 날짜
    * @return 해당 날짜에 일정이 적용되면 true
    */
    private boolean appliesToDate(LocalDate startDate, LocalDate endDate,
                                    RepeatType repeatType, Set<DayOfWeek> repeatDays, LocalDate targetDate){
        if (targetDate.isBefore(startDate) || targetDate.isAfter(endDate)) return false;

        //반봅 없는 고정 일정
        if (repeatType == RepeatType.NONE) return targetDate.equals(startDate);
        //매일 반복되는 고정 일정
        if (repeatType == RepeatType.DAILY) return true;
        //요일마다 반복되는 고정 일정 
        if (repeatType == RepeatType.WEEKLY) return repeatDays.contains(targetDate.getDayOfWeek());
        
    return false;
    }

        //해당 날짜의 사용중인 시간대를 찾아서 리스트로 저장 -> 사이의 빈공간 계산 후 반환( 추후 자동 배치 시 사용)
    private List<TimeSlot> createFreeSlotsForDate(LocalDate targetDate, LocalTime dayStart, LocalTime dayEnd, 
                                                List<FixedSchedule> fixedSchedules, List<BlockedTime> blockedTimes){
        List<TimeSlot> occupiedSlots = new ArrayList<>();
        //FixedSchedule을 occupiedSlots에 추가
        for (FixedSchedule schedule : fixedSchedules){
            if (appliesToDate(schedule.getStartDate(), schedule.getEndDate(),schedule.getRepeatType(), schedule.getRepeatDays(), targetDate)){
                occupiedSlots.add(new TimeSlot(targetDate, schedule.getStartTime(), schedule.getEndTime()));
            }
        }
        //BlockedTime도 똑같이 occupiedSlots에 추가
        for (BlockedTime time : blockedTimes){
            if( appliesToDate( time.getStartDate(), time.getEndDate(),time.getRepeatType(), time.getRepeatDays(), targetDate)){
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

//----------------------------------------------자동 배치 알고리즘 구현-----------------------------------------------------------------------
    // 마감일, 중요도, 예상 시간이 긴 순서로 정렬
    private List<FlexibleSchedule> sortFlexibleSchedule(List<FlexibleSchedule> flexibleSchedules){
        List<FlexibleSchedule> schedulesToPlace = new ArrayList<>();

        //완료된 유동 일정은 새로운 자동 배치에서 제거
        for (FlexibleSchedule schedule: flexibleSchedules){
            if (!schedule.isCompleted()) schedulesToPlace.add(schedule);
        }

        // 마감일이 빠른순 > 중요도가 높은순, 예상시간이 긴순으로 배치할 예정
        schedulesToPlace.sort(Comparator.comparing(FlexibleSchedule::getDeadline)
            .thenComparing(Comparator.comparingInt(FlexibleSchedule::getImportance).reversed())
            .thenComparing(Comparator.comparingInt(FlexibleSchedule::getEstimatedMinutes).reversed()));

        return schedulesToPlace;
    }

    //유동일정을 하나씩 배치
    private ScheduledTask placeFlexibleSchedule(FlexibleSchedule schedule, LocalDate weekStart, Map<LocalDate, List<TimeSlot>> weeklyFreeSlots) {
        //월-일까지 순서대로 꺼내면서 배치
        for (int i = 0; i < 7; i++) {
            LocalDate date = weekStart.plusDays(i);

            //현재 날짜가 deadLine이후면 종료
            if (date.isAfter(schedule.getDeadline())) break;

            List<TimeSlot> slots = weeklyFreeSlots.get(date);   //해당 날짜의 빈 시간 리스트를 꺼냄
            //슬롯의 빈칸을 하나씩 꺼내서 유동일정이 들어갈 공간이 있음 바로 넣음
            for (int j = 0; j < slots.size(); j++){
                TimeSlot slot = slots.get(j);
                
                if ( slot.getDurationMinutes() >= schedule.getEstimatedMinutes()){
                    LocalTime taskStart = slot.getStartTime();
                    LocalTime taskEnd = taskStart.plusMinutes(schedule.getEstimatedMinutes());

                    ScheduledTask scheduledTask = new ScheduledTask(schedule, date, taskStart, taskEnd);
                    
                    //슬롯 사이즈와 스케줄 사이즈가 일치한 경우
                    if (taskEnd.equals(slot.getEndTime())){
                        slots.remove(j);
                    }
                    else{   //슬롯 사이즈가 스케줄 사이즈보다 큰 경우
                        slots.set(j, new TimeSlot(date, taskEnd, slot.getEndTime()));
                    }
                    return scheduledTask;
                }
            }
        }
         return null;
    }
}
