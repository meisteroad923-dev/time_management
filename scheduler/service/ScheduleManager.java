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

import java.io.Serializable;

/**
 * 애플리케이션의 일정 데이터를 관리하는 서비스 클래스이다.
 *
 * 고정 일정, 유동 일정, 배치 금지 시간의 등록·조회·수정·삭제를 담당하며,
 * 일정 간 충돌 검사와 유동 일정의 예상 소요 시간 보정,
 * 완료 상태 변경 및 데이터 저장 기능을 제공한다.
 */
public class ScheduleManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<FixedSchedule> fixedSchedules;
    private List<FlexibleSchedule> flexibleSchedules;
    private List<BlockedTime> blockedTimes;
    private long dataVersion = 0;

    /**
     * 비어 있는 일정 목록을 가진 ScheduleManager를 생성한다.
     *
     * 고정 일정, 유동 일정, 배치 금지 시간 목록을
     * 각각 빈 리스트로 초기화한다.
     */
    public ScheduleManager() {
        fixedSchedules = new ArrayList<>();
        flexibleSchedules = new ArrayList<>();
        blockedTimes = new ArrayList<>();
    }

    /**
     * 새로운 고정 일정을 등록하고 변경된 데이터를 저장한다.
     *
     * @param schedule 등록할 고정 일정
     */
    public void addFixedSchedule(FixedSchedule schedule) {
        fixedSchedules.add(schedule);
        saveData();
    }

    /**
     * 등록된 고정 일정 목록을 반환한다.
     *
     * @return 고정 일정 목록
     */
    public List<FixedSchedule> getFixedSchedule() {
        return fixedSchedules;
    }

    /**
     * 새로운 유동 일정을 등록하고 변경된 데이터를 저장한다.
     *
     * 예상 소요 시간은 등록 전에 30분 단위로 올림하여 보정한다.
     *
     * @param schedule 등록할 유동 일정
     */
    public void addFlexibleSchedule(FlexibleSchedule schedule) {
        int adjustedMinutes =adjustEstimatedTime(schedule.getEstimatedMinutes());
        schedule.setEstimatedMinutes(adjustedMinutes);

        flexibleSchedules.add(schedule);
        saveData();
    }

    /**
     * 등록된 유동 일정 목록을 반환한다.
     *
     * @return 유동 일정 목록
     */
    public List<FlexibleSchedule> getFlexibleSchedule() {
        return flexibleSchedules;
    }

    /**
     * 새로운 배치 금지 시간을 등록하고 변경된 데이터를 저장한다.
     *
     * @param schedule 등록할 배치 금지 시간
     */
    public void addBlockedTime(BlockedTime schedule) {
        blockedTimes.add(schedule);
        saveData();
    }

    /**
     * 등록된 배치 금지 시간 목록을 반환한다.
     *
     * @return 배치 금지 시간 목록
     */
    public List<BlockedTime> getBlockedTime() {
        return blockedTimes;
    }

    /**
     * 지정된 고정 일정을 삭제한다.
     *
     * 삭제에 성공한 경우 변경된 데이터를 저장한다.
     *
     * @param schedule 삭제할 고정 일정
     * @return 삭제에 성공하면 true, 대상 일정이 없으면 false
     */
    public boolean removeFixedSchedule(FixedSchedule schedule) {
        boolean removed = fixedSchedules.remove(schedule);
        if (removed)
            saveData();
        return removed;
    }

    /**
     * 지정된 유동 일정을 삭제한다.
     *
     * 삭제에 성공한 경우 변경된 데이터를 저장한다.
     *
     * @param schedule 삭제할 유동 일정
     * @return 삭제에 성공하면 true, 대상 일정이 없으면 false
     */
    public boolean removeFlexibleSchedule(FlexibleSchedule schedule) {
        boolean removed = flexibleSchedules.remove(schedule);
        if (removed)
            saveData();
        return removed;
    }

    /**
     * 지정된 배치 금지 시간을 삭제한다.
     *
     * 삭제에 성공한 경우 변경된 데이터를 저장한다.
     *
     * @param blockedTime 삭제할 배치 금지 시간
     * @return 삭제에 성공하면 true, 대상 시간이 없으면 false
     */
    public boolean removeBlockedTime(BlockedTime blockedTime) {
        boolean removed = blockedTimes.remove(blockedTime);
        if (removed)
            saveData();
        return removed;
    }

    /**
     * 기존 고정 일정을 새로운 고정 일정으로 수정한다.
     *
     * 수정 대상이 존재하지 않거나 새로운 일정이 다른 일정과
     * 충돌하는 경우 수정하지 않는다.
     *
     * @param oldSchedule 수정할 기존 고정 일정
     * @param newSchedule 변경할 새로운 고정 일정
     * @return 수정에 성공하면 true, 실패하면 false
     */
    public boolean updateFixedSchedule(FixedSchedule oldSchedule, FixedSchedule newSchedule) {
        int index = fixedSchedules.indexOf(oldSchedule);

        if (index == -1)
            return false;
        if (checkConflict(oldSchedule, newSchedule))
            return false;

        fixedSchedules.set(index, newSchedule);
        saveData();
        return true;
    }

    /**
     * 기존 유동 일정을 새로운 유동 일정으로 수정한다.
     *
     * 수정할 유동 일정의 예상 소요 시간은
     * 30분 단위로 올림하여 보정한다.
     *
     * @param oldSchedule 수정할 기존 유동 일정
     * @param newSchedule 변경할 새로운 유동 일정
     * @return 수정에 성공하면 true, 대상 일정이 없으면 false
     */
    public boolean updateFlexibleSchedule(FlexibleSchedule oldSchedule, FlexibleSchedule newSchedule) {
        int index = flexibleSchedules.indexOf(oldSchedule);
        if (index == -1)
            return false;

        newSchedule.setEstimatedMinutes(adjustEstimatedTime(newSchedule.getEstimatedMinutes()));

        flexibleSchedules.set(index, newSchedule);
        saveData();
        return true;
    }

    /**
     * 기존 배치 금지 시간을 새로운 배치 금지 시간으로 수정한다.
     *
     * 수정 대상이 존재하지 않거나 새로운 시간이 다른 일정과
     * 충돌하는 경우 수정하지 않는다.
     *
     * @param oldBlockedTime 수정할 기존 배치 금지 시간
     * @param newBlockedTime 변경할 새로운 배치 금지 시간
     * @return 수정에 성공하면 true, 실패하면 false
     */
    public boolean updateBlockedTime(BlockedTime oldBlockedTime, BlockedTime newBlockedTime) {
        int index = blockedTimes.indexOf(oldBlockedTime);

        if (index == -1)
            return false;
        if (checkConflict(oldBlockedTime, newBlockedTime))
            return false;

        blockedTimes.set(index, newBlockedTime);
        saveData();
        return true;
    }

    /**
     * 일정 데이터의 현재 변경 버전을 반환한다.
     *
     * @return 데이터 변경 버전
     */
    public long getDataVersion() {
        return dataVersion;
    }

    /**
     * 두 시간 구간이 서로 겹치는지 확인한다.
     *
     * 한 일정의 종료 시간과 다른 일정의 시작 시간이 같은 경우에는
     * 서로 겹치지 않는 것으로 판단한다.
     *
     * @param start1 첫 번째 시간 구간의 시작 시간
     * @param end1 첫 번째 시간 구간의 종료 시간
     * @param start2 두 번째 시간 구간의 시작 시간
     * @param end2 두 번째 시간 구간의 종료 시간
     * @return 두 시간 구간이 겹치면 true, 아니면 false
     */
    private boolean isTimeOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    /**
     * 일정의 적용 기간과 반복 유형을 기준으로
     * 특정 날짜에 일정이 적용되는지 판단한다.
     *
     * @param startDate 일정 적용 시작일
     * @param endDate 일정 적용 종료일
     * @param repeatType 반복 유형
     * @param repeatDays 반복 요일
     * @param targetDate 확인할 날짜
     * @return 해당 날짜에 일정이 적용되면 true, 아니면 false
     */
    private boolean appliesToDate(LocalDate startDate, LocalDate endDate, RepeatType repeatType,
            Set<DayOfWeek> repeatDays, LocalDate targetDate) {
        if (targetDate.isBefore(startDate) || targetDate.isAfter(endDate))
            return false;
        if (repeatType == RepeatType.NONE)
            return targetDate.equals(startDate);
        if (repeatType == RepeatType.DAILY)
            return true;
        if (repeatType == RepeatType.WEEKLY)
            return repeatDays.contains(targetDate.getDayOfWeek());
        return false;
    }

    /**
     * 두 일정의 적용 기간과 반복 조건을 비교하여
     * 실제로 겹치는 날짜가 존재하는지 확인한다.
     *
     * @param startDate1 첫 번째 일정 시작일
     * @param endDate1 첫 번째 일정 종료일
     * @param repeatType1 첫 번째 일정 반복 유형
     * @param repeatDays1 첫 번째 일정 반복 요일
     * @param startDate2 두 번째 일정 시작일
     * @param endDate2 두 번째 일정 종료일
     * @param repeatType2 두 번째 일정 반복 유형
     * @param repeatDays2 두 번째 일정 반복 요일
     * @return 두 일정이 같은 날짜에 적용되는 경우 true
     */
    private boolean isDateOverlap(LocalDate startDate1, LocalDate endDate1, RepeatType repeatType1,
            Set<DayOfWeek> repeatDays1,
            LocalDate startDate2, LocalDate endDate2, RepeatType repeatType2, Set<DayOfWeek> repeatDays2) {
        // 두 기간 중 더 늦게 시작하는 날짜
        LocalDate overlapStart = startDate1.isAfter(startDate2) ? startDate1 : startDate2;

        // 두 기간 중 더 빨리 끝나는 날짜
        LocalDate overlapEnd = endDate1.isBefore(endDate2) ? endDate1 : endDate2;

        // 기간 자체가 겹치지 않음
        if (overlapStart.isAfter(overlapEnd))
            return false;

        // 겹치는 기간 안에서 실제 반복 날짜 확인
        LocalDate date = overlapStart;

        while (!date.isAfter(overlapEnd)) {
            boolean firstApplies = appliesToDate(startDate1, endDate1, repeatType1, repeatDays1, date);
            boolean secondApplies = appliesToDate(startDate2, endDate2, repeatType2, repeatDays2, date);

            if (firstApplies && secondApplies)
                return true;
            date = date.plusDays(1);
        }

        return false;
    }

    /**
     * 새 고정 일정이 기존 고정 일정 또는 배치 금지 시간과
     * 충돌하는지 확인한다.
     *
     * @param newSchedule 새로 등록할 고정 일정
     * @return 충돌하면 true, 충돌하지 않으면 false
     */
    public boolean checkConflict(FixedSchedule newSchedule) {
        // 기존 고정 일정과 충돌 검사
        for (FixedSchedule existing : fixedSchedules) {
            boolean dateOverlap = isDateOverlap(newSchedule.getStartDate(), newSchedule.getEndDate(),
                    newSchedule.getRepeatType(), newSchedule.getRepeatDays(),
                    existing.getStartDate(), existing.getEndDate(), existing.getRepeatType(), existing.getRepeatDays());
            boolean timeOverlap = isTimeOverlap(newSchedule.getStartTime(), newSchedule.getEndTime(),
                    existing.getStartTime(), existing.getEndTime());

            if (dateOverlap && timeOverlap) {
                return true;
            }
        }
        // 기존 배치 금지 시간과 충돌 검사
        for (BlockedTime existing : blockedTimes) {

            boolean dateOverlap = isDateOverlap(newSchedule.getStartDate(), newSchedule.getEndDate(),
                    newSchedule.getRepeatType(), newSchedule.getRepeatDays(),
                    existing.getStartDate(), existing.getEndDate(), existing.getRepeatType(), existing.getRepeatDays());
            boolean timeOverlap = isTimeOverlap(newSchedule.getStartTime(), newSchedule.getEndTime(),
                    existing.getStartTime(), existing.getEndTime());

            if (dateOverlap && timeOverlap) {
                return true;
            }
        }
        return false;
    }

    /**
     * 새 배치 금지 시간이 기존 고정 일정 또는 배치 금지 시간과
     * 충돌하는지 확인한다.
     *
     * @param newBlockedTime 새로 등록할 배치 금지 시간
     * @return 충돌하면 true, 충돌하지 않으면 false
     */
    public boolean checkConflict(BlockedTime newBlockedTime) {
        // fixedSchedule이 blockedtime보다 우선순위가 높음
        // BlockedTime은 기존 FixedSchedule과 BlockedTime 모두와 충돌하면 등록할 수 없음
        for (FixedSchedule existing : fixedSchedules) {
            boolean dateOverlap = isDateOverlap(newBlockedTime.getStartDate(), newBlockedTime.getEndDate(),
                    newBlockedTime.getRepeatType(), newBlockedTime.getRepeatDays(),
                    existing.getStartDate(), existing.getEndDate(), existing.getRepeatType(), existing.getRepeatDays());
            boolean timeOverlap = isTimeOverlap(newBlockedTime.getStartTime(), newBlockedTime.getEndTime(),
                    existing.getStartTime(), existing.getEndTime());
            if (dateOverlap && timeOverlap) {
                return true;
            }
        }

        for (BlockedTime existing : blockedTimes) {
            boolean dateOverlap = isDateOverlap(newBlockedTime.getStartDate(), newBlockedTime.getEndDate(),
                    newBlockedTime.getRepeatType(), newBlockedTime.getRepeatDays(),
                    existing.getStartDate(), existing.getEndDate(), existing.getRepeatType(), existing.getRepeatDays());
            boolean timeOverlap = isTimeOverlap(newBlockedTime.getStartTime(), newBlockedTime.getEndTime(),
                    existing.getStartTime(), existing.getEndTime());
            if (dateOverlap && timeOverlap) {
                return true;
            }
        }
        return false;
    }

    /**
     * 고정 일정 수정 시 수정 대상 자신을 제외하고
     * 다른 일정 및 배치 금지 시간과의 충돌을 확인한다.
     *
     * @param oldSchedule 수정 전 고정 일정
     * @param newSchedule 수정할 새로운 고정 일정
     * @return 충돌하면 true, 충돌하지 않으면 false
     */
    public boolean checkConflict(FixedSchedule oldSchedule, FixedSchedule newSchedule) {
        for (FixedSchedule existing : fixedSchedules) {
            if (oldSchedule == existing)
                continue;
            boolean dateOverlap = isDateOverlap(newSchedule.getStartDate(), newSchedule.getEndDate(),
                    newSchedule.getRepeatType(), newSchedule.getRepeatDays(),
                    existing.getStartDate(), existing.getEndDate(), existing.getRepeatType(), existing.getRepeatDays());
            boolean timeOverlap = isTimeOverlap(newSchedule.getStartTime(), newSchedule.getEndTime(),
                    existing.getStartTime(), existing.getEndTime());

            if (dateOverlap && timeOverlap) {
                return true;
            }
        }
        // 배치 금지 시간과 검사
        for (BlockedTime existing : blockedTimes) {

            boolean dateOverlap = isDateOverlap(newSchedule.getStartDate(), newSchedule.getEndDate(), newSchedule.getRepeatType(), newSchedule.getRepeatDays(),
                                                existing.getStartDate(),existing.getEndDate(),existing.getRepeatType(),existing.getRepeatDays());

            boolean timeOverlap = isTimeOverlap(newSchedule.getStartTime(), newSchedule.getEndTime(), existing.getStartTime(), existing.getEndTime());

            if (dateOverlap && timeOverlap) {
                return true;
            }
        }
        return false;
    }
    /**
     * 배치 금지 시간 수정 시 수정 대상 자신을 제외하고
     * 다른 고정 일정 및 배치 금지 시간과의 충돌을 확인한다.
     *
     * @param oldBlockedTime 수정 전 배치 금지 시간
     * @param newBlockedTime 수정할 새로운 배치 금지 시간
     * @return 충돌하면 true, 충돌하지 않으면 false
     */
    public boolean checkConflict(BlockedTime oldBlockedTime, BlockedTime newBlockedTime) {
        // fixedSchedule이 blockedtime보다 우선순위가 높음
        // BlockedTime은 기존 FixedSchedule과 BlockedTime 모두와 충돌하면 등록할 수 없음
        for (FixedSchedule existing : fixedSchedules) {
            boolean dateOverlap = isDateOverlap(newBlockedTime.getStartDate(), newBlockedTime.getEndDate(),
                    newBlockedTime.getRepeatType(), newBlockedTime.getRepeatDays(),
                    existing.getStartDate(), existing.getEndDate(), existing.getRepeatType(), existing.getRepeatDays());
            boolean timeOverlap = isTimeOverlap(newBlockedTime.getStartTime(), newBlockedTime.getEndTime(),
                    existing.getStartTime(), existing.getEndTime());
            if (dateOverlap && timeOverlap) {
                return true;
            }
        }

        for (BlockedTime existing : blockedTimes) {
            if (oldBlockedTime == existing)
                continue;
            boolean dateOverlap = isDateOverlap(newBlockedTime.getStartDate(), newBlockedTime.getEndDate(),
                    newBlockedTime.getRepeatType(), newBlockedTime.getRepeatDays(),
                    existing.getStartDate(), existing.getEndDate(), existing.getRepeatType(), existing.getRepeatDays());
            boolean timeOverlap = isTimeOverlap(newBlockedTime.getStartTime(), newBlockedTime.getEndTime(),
                    existing.getStartTime(), existing.getEndTime());
            if (dateOverlap && timeOverlap) {
                return true;
            }
        }
        return false;
    }

    /**
     * 예상 소요 시간을 30분 단위로 올림하여 보정한다.
     *
     * 예를 들어 31분은 60분, 61분은 90분으로 보정된다.
     *
     * @param estimatedTime 보정할 예상 소요 시간(분)
     * @return 30분 단위로 보정된 예상 소요 시간(분)
     */
    public int adjustEstimatedTime(int estimatedTime) {
        if (estimatedTime % 30 == 0)
            return (estimatedTime / 30) * 30;
        else
            return ((estimatedTime / 30) + 1) * 30;
    }

    /**
     * 지정된 유동 일정의 완료 상태를 반대로 변경하고
     * 변경된 데이터를 저장한다.
     *
     * @param schedule 완료 상태를 변경할 유동 일정
     */
    public void updateCompletionStatus(FlexibleSchedule schedule) {
        schedule.toggleCompleted();
        saveData();
    }

    /**
     * 일정 데이터의 변경 버전을 증가시키고
     * 현재 ScheduleManager 상태를 저장한다.
     */
    private void saveData() {
        // 일정 데이터가 변경되었음을 기록
        dataVersion++;
        DataStorage.save(this);
    }
}
