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

/**
 * 한 주 동안의 자동 배치 결과를 저장하는 클래스이다.
 *
 * 주의 시작일과 자동 배치에 성공한 일정 목록,
 * 배치할 공간이 부족하여 배치되지 못한 유동 일정 목록을 관리한다.
 */
public class WeeklySchedule {  
    private LocalDate weekStart;
    private List<ScheduledTask> scheduledTasks;

    private List<FlexibleSchedule> unscheduledTasks;    //자리가 없어 배치 되지 못한 일정들
    
    /**
     * 지정된 주의 자동 배치 결과를 저장할 객체를 생성한다.
     *
     * 배치 성공 목록과 배치 실패 목록은 빈 리스트로 초기화된다.
     *
     * @param weekStart 주의 시작일
     */
    public WeeklySchedule(LocalDate  weekStart){
        this.weekStart = weekStart;
        this.scheduledTasks = new ArrayList<>();

        this.unscheduledTasks = new ArrayList<>();
    }

    /**
     * 한 주의 시작일을 반환한다.
     *
     * @return 주의 시작일
     */
    public LocalDate getWeekStart(){
        return weekStart;
    }

    /**
     * 한 주의 종료일을 반환한다.
     *
     * 시작일로부터 6일 뒤의 날짜를 반환한다.
     *
     * @return 주의 종료일
     */
    public LocalDate getWeekEnd(){
        return weekStart.plusDays(6);
    }

    /**
     * 자동 배치에 성공한 일정 목록을 반환한다.
     *
     * @return 자동 배치된 일정 목록
     */
    public List<ScheduledTask> getScheduledTasks(){
        return scheduledTasks;
    }

    /**
     * 자동 배치에 성공한 일정을 결과 목록에 추가한다.
     *
     * @param task 추가할 자동 배치 일정
     */
    public void addScheduledTask(ScheduledTask task){
        scheduledTasks.add(task);
    }

    /**
     * 자동 배치에 실패한 유동 일정 목록을 반환한다.
     *
     * @return 배치되지 못한 유동 일정 목록
     */
    public List<FlexibleSchedule> getUnscheduledTasks() {
        return unscheduledTasks;
    }

    /**
     * 자동 배치에 실패한 유동 일정을 결과 목록에 추가한다.
     *
     * @param schedule 추가할 유동 일정
     */
    public void addUnscheduledTask(FlexibleSchedule schedule) {
        unscheduledTasks.add(schedule);
    }
}
