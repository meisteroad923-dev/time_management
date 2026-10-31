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

/**
 * 마감일, 중요도, 예상 소요 시간을 기준으로
 * 유동 일정을 자동 배치하는 스케줄링 전략 클래스이다.
 *
 * 고정 일정과 배치 금지 시간을 제외한 빈 시간대를 계산한 후,
 * 완료되지 않은 유동 일정을 우선순위에 따라 정렬하여
 * 월요일부터 일요일 순서로 배치한다.
 */
public class PrioritySchedulingStrategy implements SchedulingStrategy {
    /**
     * 지정된 주간에서 완료되지 않은 유동 일정들을 우선순위에 따라 자동 배치한다.
     *
     * 고정 일정과 배치 금지 시간을 제외하여 사용 가능한 시간대를 계산하고,
     * 사용자가 지정한 자동 배치 시작일부터 주의 마지막 날까지만 배치 대상으로 사용한다.
     *
     * 유동 일정은 마감일이 빠른 순, 중요도가 높은 순,
     * 예상 소요시간이 긴 순으로 정렬한 뒤 사용 가능한 시간대에 배치한다.
     *
     * @param flexibleSchedules 자동 배치할 유동 일정 목록
     * @param fixedSchedules    등록된 고정 일정 목록
     * @param blockedTimes      등록된 배치 금지 시간 목록
     * @param weekStart         자동 배치를 수행할 주의 시작일
     * @param scheduleStartDate 해당 주에서 자동 배치를 시작할 날짜
     * @param dayStart          하루 자동 배치 가능 시작 시간
     * @param dayEnd            하루 자동 배치 가능 종료 시간
     * @return 배치된 일정과 배치되지 못한 일정을 포함하는 WeeklySchedule
     */
    @Override
    public WeeklySchedule schedule(
            List<FlexibleSchedule> flexibleSchedules,
            List<FixedSchedule> fixedSchedules,
            List<BlockedTime> blockedTimes,
            LocalDate weekStart,
            LocalDate scheduleStartDate,
            LocalTime dayStart,
            LocalTime dayEnd) {
        // 고정 일정과 블록시간대 기준으로 유동일정 배치 가능 시간대를 계산
        Map<LocalDate, List<TimeSlot>> weeklyFreeSlots = createWeeklyFreeSlots(weekStart, scheduleStartDate, dayStart,
                dayEnd,
                fixedSchedules, blockedTimes);
        // 완료되지 않은 유동일정을 정렬
        List<FlexibleSchedule> schedulesToPlace = sortFlexibleSchedule(flexibleSchedules);
        // 자동배치 결과 저장 자료구조 객체 생성
        WeeklySchedule weeklySchedule = new WeeklySchedule(weekStart);
        // 유동일정 배치
        for (FlexibleSchedule schedule : schedulesToPlace) {
            ScheduledTask scheduledTask = placeFlexibleSchedule(schedule, weekStart, scheduleStartDate,
                    weeklyFreeSlots);

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
     * @param startDate  일정 적용 시작일
     * @param endDate    일정 적용 종료일
     * @param repeatType 반복 유형
     * @param repeatDays 반복 요일 집합
     * @param targetDate 확인할 날짜
     * @return 해당 날짜에 일정이 적용되면 true, 아니면 false
     */
    private boolean appliesToDate(LocalDate startDate, LocalDate endDate,
            RepeatType repeatType, Set<DayOfWeek> repeatDays, LocalDate targetDate) {
        if (targetDate.isBefore(startDate) || targetDate.isAfter(endDate))
            return false;

        // 반봅 없는 고정 일정
        if (repeatType == RepeatType.NONE)
            return targetDate.equals(startDate);
        // 매일 반복되는 고정 일정
        if (repeatType == RepeatType.DAILY)
            return true;
        // 요일마다 반복되는 고정 일정
        if (repeatType == RepeatType.WEEKLY)
            return repeatDays.contains(targetDate.getDayOfWeek());

        return false;
    }

    /**
     * 특정 날짜에 고정 일정과 배치 금지 시간이 차지하지 않는
     * 빈 시간 구간을 계산한다.
     *
     * 계산된 빈 시간 구간은 유동 일정의 자동 배치에 사용되며,
     * 시작 및 종료 시간은 30분 단위에 맞게 보정된다.
     *
     * @param targetDate     빈 시간대를 계산할 날짜
     * @param dayStart       하루 배치 시작 시간
     * @param dayEnd         하루 배치 종료 시간
     * @param fixedSchedules 고정 일정 목록
     * @param blockedTimes   배치 금지 시간 목록
     * @return 해당 날짜의 배치 가능한 빈 시간 구간 목록
     */
    private List<TimeSlot> createFreeSlotsForDate(LocalDate targetDate, LocalTime dayStart, LocalTime dayEnd,
            List<FixedSchedule> fixedSchedules, List<BlockedTime> blockedTimes) {
        List<TimeSlot> occupiedSlots = new ArrayList<>();
        // FixedSchedule을 occupiedSlots에 추가
        for (FixedSchedule schedule : fixedSchedules) {
            if (appliesToDate(schedule.getStartDate(), schedule.getEndDate(), schedule.getRepeatType(),
                    schedule.getRepeatDays(), targetDate)) {
                occupiedSlots.add(new TimeSlot(targetDate, schedule.getStartTime(), schedule.getEndTime()));
            }
        }
        // BlockedTime도 똑같이 occupiedSlots에 추가
        for (BlockedTime time : blockedTimes) {
            if (appliesToDate(time.getStartDate(), time.getEndDate(), time.getRepeatType(), time.getRepeatDays(),
                    targetDate)) {
                occupiedSlots.add(new TimeSlot(targetDate, time.getStartTime(), time.getEndTime()));
            }
        }

        // occupiedSlots 시간순 정렬
        occupiedSlots.sort(Comparator.comparing(TimeSlot::getStartTime));

        // 빈 시간 계산( 하루 시작시간, 종료시간을 고려하여)
        List<TimeSlot> freeSlots = new ArrayList<>();
        LocalTime cursor = dayStart;

        for (TimeSlot slot : occupiedSlots) {
            LocalTime slotStart = slot.getStartTime();
            LocalTime slotEnd = slot.getEndTime();

            // dayStart 이전에 완전히 끝난 일정
            if (!slotEnd.isAfter(dayStart)) {
                continue;
            }
            // dayEnd 이후에 시작하는 일정
            if (!slotStart.isBefore(dayEnd)) {
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
            // 30분 단위 보정
            if (cursor.isBefore(slotStart)) {
                LocalTime freeStart = ceilTo30Minutes(cursor);
                LocalTime freeEnd = floorTo30Minutes(slotStart);

                if (freeStart.isBefore(freeEnd))
                    freeSlots.add(new TimeSlot(targetDate, freeStart, freeEnd));
            }

            // 점유시간 끝까지 cursor 이동
            if (cursor.isBefore(slotEnd)) {
                cursor = slotEnd;
            }
        }
        if (cursor.isBefore(dayEnd)) {
            LocalTime freeStart = ceilTo30Minutes(cursor);
            LocalTime freeEnd = floorTo30Minutes(dayEnd);

            if (freeStart.isBefore(freeEnd)) {
                freeSlots.add(new TimeSlot(targetDate, freeStart, freeEnd));
            }
        }
        return freeSlots;
    }

    /**
     * 지정된 자동 배치 시작일부터 주의 마지막 날까지
     * 각 날짜의 빈 시간대를 계산한다.
     *
     * 자동 배치 시작일 이전 날짜는 빈 시간 계산 대상에서 제외한다.
     *
     * @param weekStart         주의 시작일
     * @param scheduleStartDate 자동 배치를 시작할 날짜
     * @param dayStart          하루 배치 시작 시간
     * @param dayEnd            하루 배치 종료 시간
     * @param fixedSchedules    고정 일정 목록
     * @param blockedTimes      배치 금지 시간 목록
     * @return 날짜별 빈 시간 구간을 저장한 Map
     */
    private Map<LocalDate, List<TimeSlot>> createWeeklyFreeSlots(LocalDate weekStart, LocalDate scheduleStartDate,
            LocalTime dayStart, LocalTime dayEnd,
            List<FixedSchedule> fixedSchedules, List<BlockedTime> blockedTimes) {
        Map<LocalDate, List<TimeSlot>> weeklyFreeSlots = new HashMap<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = weekStart.plusDays(i);
            // 자동 배치 시작일 이전 날짜는 배치 대상에서 제외
            if (date.isBefore(scheduleStartDate)) {
                continue;
            }
            List<TimeSlot> freeSlots = createFreeSlotsForDate(date, dayStart, dayEnd, fixedSchedules, blockedTimes);
            weeklyFreeSlots.put(date, freeSlots);
        }

        return weeklyFreeSlots;
    }

    /**
     * 완료되지 않은 유동 일정을 자동 배치 우선순위에 따라 정렬한다.
     *
     * 마감일이 빠른 일정, 중요도가 높은 일정,
     * 예상 소요 시간이 긴 일정 순으로 우선순위를 결정한다.
     *
     * @param flexibleSchedules 전체 유동 일정 목록
     * @return 완료되지 않은 유동 일정의 우선순위 정렬 결과
     */
    private List<FlexibleSchedule> sortFlexibleSchedule(List<FlexibleSchedule> flexibleSchedules) {
        List<FlexibleSchedule> schedulesToPlace = new ArrayList<>();

        // 완료된 유동 일정은 새로운 자동 배치에서 제거
        for (FlexibleSchedule schedule : flexibleSchedules) {
            if (!schedule.isCompleted())
                schedulesToPlace.add(schedule);
        }

        // 마감일이 빠른순 > 중요도가 높은순, 예상시간이 긴순으로 배치할 예정
        schedulesToPlace.sort(Comparator.comparing(FlexibleSchedule::getDeadline)
                .thenComparing(Comparator.comparingInt(FlexibleSchedule::getImportance).reversed())
                .thenComparing(Comparator.comparingInt(FlexibleSchedule::getEstimatedMinutes).reversed()));

        return schedulesToPlace;
    }

    /**
     * 하나의 유동 일정을 자동 배치 시작일부터 주의 마지막 날까지
     * 사용 가능한 빈 시간대에 배치한다.
     *
     * 각 날짜를 순서대로 탐색하며 일정의 마감일을 초과하지 않는 범위에서
     * 필요한 시간을 확보할 수 있는 첫 번째 빈 시간대에 일정을 배치한다.
     *
     * @param schedule          배치할 유동 일정
     * @param weekStart         주의 시작일
     * @param scheduleStartDate 자동 배치를 시작할 날짜
     * @param weeklyFreeSlots   날짜별 빈 시간 구간
     * @return 배치에 성공한 일정 정보, 배치할 수 없으면 null
     */
    private ScheduledTask placeFlexibleSchedule( FlexibleSchedule schedule, LocalDate weekStart, LocalDate scheduleStartDate, Map<LocalDate, List<TimeSlot>> weeklyFreeSlots) {

        for (int i = 0; i < 7; i++) {
            LocalDate date = weekStart.plusDays(i);
            // 자동 배치 시작일 이전 날짜는 제외
            if (date.isBefore(scheduleStartDate)) continue;
            
            // 마감일 이후에는 더 이상 배치할 수 없음
            if (date.isAfter(schedule.getDeadline())) break;
            
            List<TimeSlot> slots = weeklyFreeSlots.get(date);
            if (slots == null) continue;

            for (int j = 0; j < slots.size(); j++) {
                TimeSlot slot = slots.get(j);

                if (slot.getDurationMinutes() >= schedule.getEstimatedMinutes()) {
                    LocalTime taskStart = slot.getStartTime();
                    LocalTime taskEnd = taskStart.plusMinutes(schedule.getEstimatedMinutes());
                    ScheduledTask scheduledTask = new ScheduledTask(schedule,date,taskStart,taskEnd);

                    // 빈 슬롯을 전부 사용한 경우
                    if (taskEnd.equals(slot.getEndTime())) {
                        slots.remove(j);
                    } else {
                        // 남은 빈 시간을 다시 슬롯으로 저장
                        slots.set(j, new TimeSlot( date, taskEnd, slot.getEndTime()));
                    }
                    return scheduledTask;
                }
            }
        }

        return null;
    }

    /**
     * 주어진 시간을 다음 30분 단위 시간으로 올림한다.
     *
     * 이미 00분 또는 30분 단위인 경우에는 입력 시간을 그대로 반환한다.
     *
     * @param time 보정할 시간
     * @return 다음 30분 단위로 올림된 시간
     */
    private LocalTime ceilTo30Minutes(LocalTime time) {
        int minute = time.getMinute();

        if (minute == 0 || minute == 30)
            return time;
        if (minute < 30)
            return time.withMinute(30).withSecond(0).withNano(0);

        return time.plusHours(1).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 주어진 시간을 이전 30분 단위 시간으로 내림한다.
     *
     * @param time 보정할 시간
     * @return 이전 30분 단위로 내림된 시간
     */
    private LocalTime floorTo30Minutes(LocalTime time) {
        int minute = time.getMinute();

        if (minute < 30)
            return time.withMinute(0).withSecond(0).withNano(0);

        return time.withMinute(30).withSecond(0).withNano(0);
    }
}
