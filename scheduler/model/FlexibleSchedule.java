package scheduler.model;

import java.time.LocalDate;
import java.io.Serializable;

/**
 * 사용자가 등록하는 유동 일정을 나타내는 클래스이다.
 *
 * 유동 일정은 예상 소요 시간, 마감일, 중요도와 완료 상태를 가지며,
 * 자동 배치 알고리즘의 배치 대상이 된다.
 */
public class FlexibleSchedule implements Serializable {
    private String name;
    private int estimatedMinutes;
    private LocalDate deadline;
    private int importance;
    private boolean completed;
    private static final long serialVersionUID = 1L;
    
    /**
     * 새로운 유동 일정을 생성한다.
     *
     * 최초 생성 시 완료 상태는 자동으로 false로 설정된다.
     *
     * @param name 일정 이름
     * @param estimatedMinutes 예상 소요 시간(분)
     * @param deadline 마감일
     * @param importance 중요도
     */
    public FlexibleSchedule(String name, int estimatedMinutes, LocalDate deadline, int importance){
        this.name = name; 
        this.estimatedMinutes = estimatedMinutes;
        this.deadline = deadline;
        this.importance = importance;
        this.completed =false;
    }

    /**
     * 완료 상태를 포함하여 유동 일정을 생성한다.
     *
     * 기존 일정을 수정하거나 완료 상태를 유지한 채
     * 새로운 객체를 생성할 때 사용한다.
     *
     * @param name 일정 이름
     * @param estimatedMinutes 예상 소요 시간(분)
     * @param deadline 마감일
     * @param importance 중요도
     * @param completed 완료 여부
     */
    public FlexibleSchedule(String name, int estimatedMinutes, LocalDate deadline, int importance, boolean completed){
        this.name = name;
        this.estimatedMinutes = estimatedMinutes;
        this.deadline = deadline;
        this.importance = importance;
        this.completed =completed;
    }

    /**
     * 일정 이름을 반환한다.
     *
     * @return 일정 이름
     */
    public String getName(){
        return this.name;
    }

    /**
     * 예상 소요 시간을 분 단위로 반환한다.
     *
     * @return 예상 소요 시간(분)
     */
    public int getEstimatedMinutes(){
        return this.estimatedMinutes;
    }

    /**
     * 예상 소요 시간을 변경한다.
     *
     * @param newEstimatedMinutes 변경할 예상 소요 시간(분)
     */
    public void setEstimatedMinutes(int newEstimatedMinutes){
        estimatedMinutes = newEstimatedMinutes;
    }

    /**
     * 일정의 마감일을 반환한다.
     *
     * @return 마감일
     */
    public LocalDate getDeadline(){
        return deadline;
    }

    /**
     * 일정의 중요도를 반환한다.
     *
     * @return 중요도
     */
    public int getImportance(){
        return importance;
    }

    /**
     * 일정의 완료 여부를 반환한다.
     *
     * @return 완료된 일정이면 true, 아니면 false
     */
    public boolean isCompleted(){
        return completed;
    }

    /**
     * 일정의 완료 상태를 현재 상태의 반대로 변경한다.
     */
    public void toggleCompleted(){
        completed = !completed;
    }
}
