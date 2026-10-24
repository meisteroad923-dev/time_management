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
        ============================================================
        TEST 1. 예상 소요시간 30분 단위 보정
        ============================================================

        System.out.println("=== 예상시간 보정 테스트 ===");

        System.out.println("30분  -> "
                + manager.adjustEstimatedTime(30)
                + " / 기대값: 30");

        System.out.println("31분  -> "
                + manager.adjustEstimatedTime(31)
                + " / 기대값: 60");

        System.out.println("59분  -> "
                + manager.adjustEstimatedTime(59)
                + " / 기대값: 60");

        System.out.println("60분  -> "
                + manager.adjustEstimatedTime(60)
                + " / 기대값: 60");

        System.out.println("61분  -> "
                + manager.adjustEstimatedTime(61)
                + " / 기대값: 90");

        System.out.println("70분  -> "
                + manager.adjustEstimatedTime(70)
                + " / 기대값: 90");

        System.out.println("119분 -> "
                + manager.adjustEstimatedTime(119)
                + " / 기대값: 120");
        */


        /*
        ============================================================
        TEST 2. FlexibleSchedule 완료 상태 토글
        ============================================================

        FlexibleSchedule schedule = new FlexibleSchedule(
                "알고리즘 과제",
                120,
                LocalDate.of(2026, 10, 30),
                5
        );

        System.out.println("=== 완료 상태 토글 테스트 ===");

        System.out.println("초기 상태: "
                + schedule.isCompleted()
                + " / 기대값: false");

        manager.updateCompletionStatus(schedule);

        System.out.println("1회 변경: "
                + schedule.isCompleted()
                + " / 기대값: true");

        manager.updateCompletionStatus(schedule);

        System.out.println("2회 변경: "
                + schedule.isCompleted()
                + " / 기대값: false");

        manager.updateCompletionStatus(schedule);

        System.out.println("3회 변경: "
                + schedule.isCompleted()
                + " / 기대값: true");
        */


        /*
        ============================================================
        TEST 3. FixedSchedule 삭제
        ============================================================

        FixedSchedule fixed1 = new FixedSchedule(
                "공학수학",
                null,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        FixedSchedule fixed2 = new FixedSchedule(
                "운영체제",
                null,
                LocalTime.of(13, 0),
                LocalTime.of(14, 30),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.TUESDAY)
        );

        manager.addFixedSchedule(fixed1);
        manager.addFixedSchedule(fixed2);

        System.out.println("=== FixedSchedule 삭제 테스트 ===");

        System.out.println("삭제 전 개수: "
                + manager.getFixedSchedule().size()
                + " / 기대값: 2");

        System.out.println("삭제 결과: "
                + manager.removeFixedSchedule(fixed1)
                + " / 기대값: true");

        System.out.println("삭제 후 개수: "
                + manager.getFixedSchedule().size()
                + " / 기대값: 1");

        System.out.println("같은 객체 다시 삭제: "
                + manager.removeFixedSchedule(fixed1)
                + " / 기대값: false");
        */


        /*
        ============================================================
        TEST 4. FlexibleSchedule 삭제
        ============================================================

        FlexibleSchedule flexible1 = new FlexibleSchedule(
                "알고리즘 과제",
                120,
                LocalDate.of(2026, 10, 30),
                5
        );

        FlexibleSchedule flexible2 = new FlexibleSchedule(
                "네트워크 공부",
                90,
                LocalDate.of(2026, 11, 1),
                3
        );

        manager.addFlexibleSchedule(flexible1);
        manager.addFlexibleSchedule(flexible2);

        System.out.println("=== FlexibleSchedule 삭제 테스트 ===");

        System.out.println("삭제 전 개수: "
                + manager.getFlexibleSchedule().size()
                + " / 기대값: 2");

        System.out.println("삭제 결과: "
                + manager.removeFlexibleSchedule(flexible1)
                + " / 기대값: true");

        System.out.println("삭제 후 개수: "
                + manager.getFlexibleSchedule().size()
                + " / 기대값: 1");

        System.out.println("같은 객체 다시 삭제: "
                + manager.removeFlexibleSchedule(flexible1)
                + " / 기대값: false");
        */


        /*
        ============================================================
        TEST 5. BlockedTime 삭제
        ============================================================

        BlockedTime blocked1 = new BlockedTime(
                "저녁 식사",
                null,
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                RepeatType.DAILY,
                Set.of()
        );

        BlockedTime blocked2 = new BlockedTime(
                "휴식",
                null,
                LocalTime.of(22, 0),
                LocalTime.of(23, 0),
                RepeatType.DAILY,
                Set.of()
        );

        manager.addBlockedTime(blocked1);
        manager.addBlockedTime(blocked2);

        System.out.println("=== BlockedTime 삭제 테스트 ===");

        System.out.println("삭제 전 개수: "
                + manager.getBlockedTime().size()
                + " / 기대값: 2");

        System.out.println("삭제 결과: "
                + manager.removeBlockedTime(blocked1)
                + " / 기대값: true");

        System.out.println("삭제 후 개수: "
                + manager.getBlockedTime().size()
                + " / 기대값: 1");

        System.out.println("같은 객체 다시 삭제: "
                + manager.removeBlockedTime(blocked1)
                + " / 기대값: false");
        */


        /*
        ============================================================
        TEST 6. FixedSchedule 정상 수정 + 충돌 수정
        ============================================================

        FixedSchedule fixed1 = new FixedSchedule(
                "공학수학",
                null,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        FixedSchedule fixed2 = new FixedSchedule(
                "운영체제",
                null,
                LocalTime.of(13, 0),
                LocalTime.of(14, 30),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        manager.addFixedSchedule(fixed1);
        manager.addFixedSchedule(fixed2);


        // 정상 수정
        FixedSchedule fixed1Updated = new FixedSchedule(
                "공학수학 수정",
                null,
                LocalTime.of(10, 30),
                LocalTime.of(12, 0),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        System.out.println("=== FixedSchedule 정상 수정 ===");

        System.out.println("수정 결과: "
                + manager.updateFixedSchedule(fixed1, fixed1Updated)
                + " / 기대값: true");

        System.out.println("수정된 이름: "
                + manager.getFixedSchedule().get(0).getName()
                + " / 기대값: 공학수학 수정");

        System.out.println("수정된 시간: "
                + manager.getFixedSchedule().get(0).getStartTime()
                + " ~ "
                + manager.getFixedSchedule().get(0).getEndTime()
                + " / 기대값: 10:30 ~ 12:00");


        // 기존 운영체제와 충돌하도록 수정
        FixedSchedule fixedConflict = new FixedSchedule(
                "충돌 일정",
                null,
                LocalTime.of(13, 30),
                LocalTime.of(15, 0),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        System.out.println("\n=== FixedSchedule 충돌 수정 ===");

        System.out.println("수정 결과: "
                + manager.updateFixedSchedule(
                        fixed1Updated,
                        fixedConflict
                )
                + " / 기대값: false");
        */


        /*
        ============================================================
        TEST 7. FlexibleSchedule 수정
        - 예상시간 재보정
        - completed 상태 유지 확인
        ============================================================

        FlexibleSchedule flexible1 = new FlexibleSchedule(
                "알고리즘 과제",
                120,
                LocalDate.of(2026, 10, 30),
                5
        );

        manager.addFlexibleSchedule(flexible1);

        // false -> true
        manager.updateCompletionStatus(flexible1);

        FlexibleSchedule flexibleUpdated = new FlexibleSchedule(
                "알고리즘 과제 수정",
                70,
                LocalDate.of(2026, 11, 1),
                4,
                flexible1.isCompleted()
        );

        System.out.println("=== FlexibleSchedule 수정 ===");

        System.out.println("수정 결과: "
                + manager.updateFlexibleSchedule(
                        flexible1,
                        flexibleUpdated
                )
                + " / 기대값: true");

        FlexibleSchedule result =
                manager.getFlexibleSchedule().get(0);

        System.out.println("수정된 이름: "
                + result.getName()
                + " / 기대값: 알고리즘 과제 수정");

        System.out.println("보정된 예상시간: "
                + result.getEstimatedMinutes()
                + " / 기대값: 90");

        System.out.println("완료 상태 유지: "
                + result.isCompleted()
                + " / 기대값: true");
        */


        /*
        ============================================================
        TEST 8. BlockedTime 정상 수정 + FixedSchedule 충돌 수정
        ============================================================

        FixedSchedule fixed = new FixedSchedule(
                "운영체제",
                null,
                LocalTime.of(13, 0),
                LocalTime.of(14, 30),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        manager.addFixedSchedule(fixed);

        BlockedTime blocked = new BlockedTime(
                "저녁 식사",
                null,
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                RepeatType.DAILY,
                Set.of()
        );

        manager.addBlockedTime(blocked);


        // 정상 수정
        BlockedTime blockedUpdated = new BlockedTime(
                "저녁 식사 수정",
                null,
                LocalTime.of(19, 0),
                LocalTime.of(20, 0),
                RepeatType.DAILY,
                Set.of()
        );

        System.out.println("=== BlockedTime 정상 수정 ===");

        System.out.println("수정 결과: "
                + manager.updateBlockedTime(
                        blocked,
                        blockedUpdated
                )
                + " / 기대값: true");

        System.out.println("수정된 시간: "
                + manager.getBlockedTime().get(0).getStartTime()
                + " ~ "
                + manager.getBlockedTime().get(0).getEndTime()
                + " / 기대값: 19:00 ~ 20:00");


        // FixedSchedule과 충돌하도록 수정
        BlockedTime blockedConflict = new BlockedTime(
                "충돌 금지시간",
                null,
                LocalTime.of(13, 30),
                LocalTime.of(14, 0),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY)
        );

        System.out.println("\n=== BlockedTime 충돌 수정 ===");

        System.out.println("수정 결과: "
                + manager.updateBlockedTime(
                        blockedUpdated,
                        blockedConflict
                )
                + " / 기대값: false");
        */
    }
}