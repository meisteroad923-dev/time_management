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
        // =========================================================
        // 1. 같은 요일 + 겹치는 시간 → true
        // =========================================================
        ScheduleManager manager1 = new ScheduleManager();

        FixedSchedule existing1 = new FixedSchedule(
                "공학수학",
                null,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        manager1.addFixedSchedule(existing1);

        FixedSchedule newSchedule1 = new FixedSchedule(
                "새 일정",
                null,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        System.out.println("1. 같은 요일 + 겹치는 시간");
        System.out.println("결과: " + manager1.checkConflict(newSchedule1));
        System.out.println("기대값: true\n");


        // =========================================================
        // 2. 같은 요일 + 안 겹치는 시간 → false
        // =========================================================
        ScheduleManager manager2 = new ScheduleManager();

        FixedSchedule existing2 = new FixedSchedule(
                "공학수학",
                null,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        manager2.addFixedSchedule(existing2);

        FixedSchedule newSchedule2 = new FixedSchedule(
                "새 일정",
                null,
                LocalTime.of(10, 30),
                LocalTime.of(12, 0),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        System.out.println("2. 같은 요일 + 안 겹치는 시간");
        System.out.println("결과: " + manager2.checkConflict(newSchedule2));
        System.out.println("기대값: false\n");


        // =========================================================
        // 3. 다른 요일 + 같은 시간 → false
        // =========================================================
        ScheduleManager manager3 = new ScheduleManager();

        FixedSchedule existing3 = new FixedSchedule(
                "월요일 수업",
                null,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        manager3.addFixedSchedule(existing3);

        FixedSchedule newSchedule3 = new FixedSchedule(
                "화요일 수업",
                null,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.TUESDAY)
        );

        System.out.println("3. 다른 요일 + 같은 시간");
        System.out.println("결과: " + manager3.checkConflict(newSchedule3));
        System.out.println("기대값: false\n");


        // =========================================================
        // 4. NONE + 같은 날짜 + 겹치는 시간 → true
        // =========================================================
        ScheduleManager manager4 = new ScheduleManager();

        FixedSchedule existing4 = new FixedSchedule(
                "병원",
                LocalDate.of(2026, 10, 15),
                LocalTime.of(13, 0),
                LocalTime.of(15, 0),
                RepeatType.NONE,
                Set.of()
        );

        manager4.addFixedSchedule(existing4);

        FixedSchedule newSchedule4 = new FixedSchedule(
                "약속",
                LocalDate.of(2026, 10, 15),
                LocalTime.of(14, 0),
                LocalTime.of(16, 0),
                RepeatType.NONE,
                Set.of()
        );

        System.out.println("4. 같은 날짜 + 겹치는 시간");
        System.out.println("결과: " + manager4.checkConflict(newSchedule4));
        System.out.println("기대값: true\n");


        // =========================================================
        // 5. NONE + 다른 날짜 + 같은 시간 → false
        // =========================================================
        ScheduleManager manager5 = new ScheduleManager();

        FixedSchedule existing5 = new FixedSchedule(
                "병원",
                LocalDate.of(2026, 10, 15),
                LocalTime.of(13, 0),
                LocalTime.of(15, 0),
                RepeatType.NONE,
                Set.of()
        );

        manager5.addFixedSchedule(existing5);

        FixedSchedule newSchedule5 = new FixedSchedule(
                "약속",
                LocalDate.of(2026, 10, 16),
                LocalTime.of(13, 0),
                LocalTime.of(15, 0),
                RepeatType.NONE,
                Set.of()
        );

        System.out.println("5. 다른 날짜 + 같은 시간");
        System.out.println("결과: " + manager5.checkConflict(newSchedule5));
        System.out.println("기대값: false\n");


        // =========================================================
        // 6. BlockedTime이 기존 FixedSchedule과 겹침 → true
        // =========================================================
        ScheduleManager manager6 = new ScheduleManager();

        FixedSchedule existing6 = new FixedSchedule(
                "공학수학",
                null,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        manager6.addFixedSchedule(existing6);

        BlockedTime blocked6 = new BlockedTime(
                "사용 금지 시간",
                null,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        System.out.println("6. BlockedTime과 FixedSchedule 충돌");
        System.out.println("결과: " + manager6.checkConflict(blocked6));
        System.out.println("기대값: true\n");


        // =========================================================
        // 7. 기존 BlockedTime과 새 FixedSchedule이 겹침
        //    FixedSchedule 우선이므로 → false
        // =========================================================
        ScheduleManager manager7 = new ScheduleManager();

        BlockedTime existingBlocked = new BlockedTime(
                "점심시간",
                null,
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        manager7.addBlockedTime(existingBlocked);

        FixedSchedule fixed7 = new FixedSchedule(
                "필수 수업",
                null,
                LocalTime.of(12, 30),
                LocalTime.of(14, 0),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        System.out.println("7. 기존 BlockedTime과 새 FixedSchedule 충돌");
        System.out.println("결과: " + manager7.checkConflict(fixed7));
        System.out.println("기대값: false");
    }
  
}