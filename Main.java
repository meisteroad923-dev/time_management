import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import scheduler.model.FixedSchedule;
import scheduler.model.FlexibleSchedule;
import scheduler.model.BlockedTime;
import scheduler.model.RepeatType;
import scheduler.service.ScheduleManager;

public class Main {

    public static void main(String[] args) {
        ScheduleManager manager = new ScheduleManager();
/*
        // 1. 고정 일정
        FixedSchedule fixedSchedule = new FixedSchedule(
                "공학수학",
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        

        manager.addFixedSchedule(fixedSchedule);

        System.out.println(
            manager.getFixedSchedule().get(0).getStartTime()
        );

        System.out.println("=== 고정 일정 ===");
        System.out.println(fixedSchedule.getName());
        System.out.println(fixedSchedule.getStartTime());
        System.out.println(fixedSchedule.getEndTime());
        System.out.println(fixedSchedule.getRepeatType());
        System.out.println(fixedSchedule.getRepeatDays());


        // 2. 유동 일정
        FlexibleSchedule flexibleSchedule = new FlexibleSchedule(
                "A* 과제",
                120,
                LocalDate.of(2026, 10, 5),
                5
        );

        manager.addFlexibleSchedule(flexibleSchedule);
        System.out.println(
                manager.getFlexibleSchedule().get(0).getName()
        );


        System.out.println("\n=== 유동 일정 ===");
        System.out.println(flexibleSchedule.getName());
        System.out.println(flexibleSchedule.getEstimatedMinutes());
        System.out.println(flexibleSchedule.getDeadline());
        System.out.println(flexibleSchedule.getImportance());
        System.out.println(flexibleSchedule.isCompleted());
*/

        // 3. 자동 배치 금지 시간
        BlockedTime blockedTime = new BlockedTime(
                "저녁 식사",
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                RepeatType.DAILY,
                Set.of()
        );

        manager.addBlockedTime(blockedTime);
        System.out.println(
                manager.getBlockedTime().get(0).getName()
        );
        /*
        System.out.println("\n=== 배치 금지 시간 ===");
        System.out.println(blockedTime.getName());
        System.out.println(blockedTime.getStartTime());
        System.out.println(blockedTime.getEndTime());
        System.out.println(blockedTime.getRepeatType());
        System.out.println(blockedTime.getRepeatDays());
        */
    }
}