import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import scheduler.model.FixedSchedule;
import scheduler.model.FlexibleSchedule;
import scheduler.model.BlockedTime;
import scheduler.model.RepeatType;

import scheduler.model.ScheduledTask;
import scheduler.model.TimeSlot;
import scheduler.model.WeeklySchedule;
import scheduler.service.AutoScheduler;
import scheduler.service.ScheduleManager;

public class Main {

    public static void main(String[] args) {
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
        
        /*
        AutoScheduler autoScheduler = new AutoScheduler();

        List<FixedSchedule> fixedSchedules = new ArrayList<>();
        List<BlockedTime> blockedTimes = new ArrayList<>();


        // =====================================================
        // 1. FixedSchedule 테스트 데이터
        // =====================================================

        // 매주 월/수 09:00 ~ 10:30
        FixedSchedule math = new FixedSchedule(
                "공학수학",
                null,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                RepeatType.WEEKLY,
                Set.of(
                        DayOfWeek.MONDAY,
                        DayOfWeek.WEDNESDAY
                )
        );

        // 매주 화요일 13:00 ~ 14:30
        FixedSchedule network = new FixedSchedule(
                "네트워크",
                null,
                LocalTime.of(13, 0),
                LocalTime.of(14, 30),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.TUESDAY)
        );

        // 특정 날짜 목요일 15:00 ~ 17:00
        FixedSchedule specialLecture = new FixedSchedule(
                "특강",
                LocalDate.of(2026, 10, 29),
                LocalTime.of(15, 0),
                LocalTime.of(17, 0),
                RepeatType.NONE,
                Set.of()
        );

        fixedSchedules.add(math);
        fixedSchedules.add(network);
        fixedSchedules.add(specialLecture);


        // =====================================================
        // 2. BlockedTime 테스트 데이터
        // =====================================================

        // 매일 점심시간 12:00 ~ 13:00
        BlockedTime lunch = new BlockedTime(
                "점심",
                null,
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                RepeatType.DAILY,
                Set.of()
        );

        // 월요일만 18:00 ~ 19:00
        BlockedTime mondayDinner = new BlockedTime(
                "저녁 약속",
                LocalDate.of(2026, 10, 26),
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                RepeatType.NONE,
                Set.of()
        );

        // 수요일 10:00 ~ 12:00
        // 공학수학 09:00 ~ 10:30과 일부 겹침
        BlockedTime overlapTest = new BlockedTime(
                "겹침 테스트",
                LocalDate.of(2026, 10, 28),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                RepeatType.NONE,
                Set.of()
        );

        // 금요일 dayStart 이전부터 걸치는 일정
        // 자동배치 범위가 08:00부터라면 08:00~09:00만 점유 처리되어야 함
        BlockedTime beforeStart = new BlockedTime(
                "아침 일정",
                LocalDate.of(2026, 10, 30),
                LocalTime.of(7, 0),
                LocalTime.of(9, 0),
                RepeatType.NONE,
                Set.of()
        );

        // 토요일 dayEnd 이후까지 걸치는 일정
        // 자동배치 범위가 23:00까지라면 22:00~23:00만 점유
        BlockedTime afterEnd = new BlockedTime(
                "늦은 일정",
                LocalDate.of(2026, 10, 31),
                LocalTime.of(22, 0),
                LocalTime.of(23, 30),
                RepeatType.NONE,
                Set.of()
        );

        blockedTimes.add(lunch);
        blockedTimes.add(mondayDinner);
        blockedTimes.add(overlapTest);
        blockedTimes.add(beforeStart);
        blockedTimes.add(afterEnd);


        // =====================================================
        // 3. 일주일 FreeSlot 계산
        // =====================================================

        LocalDate weekStart =
                LocalDate.of(2026, 10, 26); // 월요일

        // 테스트용 자동배치 가능 시간
        LocalTime dayStart = LocalTime.of(8, 0);
        LocalTime dayEnd = LocalTime.of(23, 0);

        Map<LocalDate, List<TimeSlot>> weeklyFreeSlots =
                autoScheduler.createWeeklyFreeSlots(
                        weekStart,
                        dayStart,
                        dayEnd,
                        fixedSchedules,
                        blockedTimes
                );


        // =====================================================
        // 4. 결과 출력
        // =====================================================

        System.out.println("====================================");
        System.out.println("주간 FreeSlot 테스트");
        System.out.println("기간: "
                + weekStart
                + " ~ "
                + weekStart.plusDays(6));
        System.out.println("자동배치 가능 시간: "
                + dayStart
                + " ~ "
                + dayEnd);
        System.out.println("====================================");


        // HashMap 순서가 보장되지 않으므로
        // 월요일부터 직접 날짜를 만들어서 출력
        for (int i = 0; i < 7; i++) {

            LocalDate date = weekStart.plusDays(i);

            System.out.println();
            System.out.println(
                    date + " "
                    + date.getDayOfWeek()
            );

            List<TimeSlot> slots =
                    weeklyFreeSlots.get(date);

            if (slots.isEmpty()) {
                System.out.println("  빈 시간 없음");
                continue;
            }

            for (TimeSlot slot : slots) {

                System.out.println(
                        "  "
                        + slot.getStartTime()
                        + " ~ "
                        + slot.getEndTime()
                        + "  ("
                        + slot.getDurationMinutes()
                        + "분)"
                );
            }
        }*/


        /*
        AutoScheduler autoScheduler = new AutoScheduler();

        // 자동배치 대상 주간: 2026-10-26(월) ~ 2026-11-01(일)
        LocalDate weekStart = LocalDate.of(2026, 10, 26);

        // 테스트용 하루 자동배치 가능 시간
        LocalTime dayStart = LocalTime.of(8, 0);
        LocalTime dayEnd = LocalTime.of(18, 0);


        // =====================================================
        // 1. 고정 일정
        // =====================================================

        List<FixedSchedule> fixedSchedules = new ArrayList<>();

        // 매주 월/수 09:00 ~ 10:30
        fixedSchedules.add(
            new FixedSchedule(
                "수학 수업",
                weekStart,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            )
        );

        // 매주 화요일 13:00 ~ 14:30
        fixedSchedules.add(
            new FixedSchedule(
                "네트워크 수업",
                weekStart,
                LocalTime.of(13, 0),
                LocalTime.of(14, 30),
                RepeatType.WEEKLY,
                Set.of(DayOfWeek.TUESDAY)
            )
        );


        // =====================================================
        // 2. 블록 시간
        // =====================================================

        List<BlockedTime> blockedTimes = new ArrayList<>();

        // 매일 점심시간 12:00 ~ 13:00
        blockedTimes.add(
            new BlockedTime(
                "점심시간",
                weekStart,
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                RepeatType.DAILY,
                Set.of()
            )
        );

        // 월요일 15:00 ~ 16:00 사용 불가
        blockedTimes.add(
            new BlockedTime(
                "개인 일정",
                LocalDate.of(2026, 10, 26),
                LocalTime.of(15, 0),
                LocalTime.of(16, 0),
                RepeatType.NONE,
                Set.of()
            )
        );


        // =====================================================
        // 3. 유동 일정
        // =====================================================

        List<FlexibleSchedule> flexibleSchedules = new ArrayList<>();

        flexibleSchedules.add(
            new FlexibleSchedule(
                "알고리즘 과제",
                90,
                LocalDate.of(2026, 10, 26),
                5
            )
        );

        flexibleSchedules.add(
            new FlexibleSchedule(
                "영어 공부",
                120,
                LocalDate.of(2026, 10, 27),
                4
            )
        );

        flexibleSchedules.add(
            new FlexibleSchedule(
                "수학 복습",
                60,
                LocalDate.of(2026, 10, 28),
                3
            )
        );

        flexibleSchedules.add(
            new FlexibleSchedule(
                "보고서 작성",
                180,
                LocalDate.of(2026, 10, 30),
                5
            )
        );


        // 이미 마감일이 지난 일정 → 배치 실패해야 함
        flexibleSchedules.add(
            new FlexibleSchedule(
                "기한 지난 과제",
                60,
                LocalDate.of(2026, 10, 25),
                5
            )
        );


        // 완료된 일정 → 자동배치 대상에서 아예 제외되어야 함
        FlexibleSchedule completedTask =
            new FlexibleSchedule(
                "이미 끝낸 과제",
                60,
                LocalDate.of(2026, 10, 29),
                5
            );

        completedTask.toggleCompleted();

        flexibleSchedules.add(completedTask);


        // =====================================================
        // 4. 자동배치 실행
        // =====================================================

        WeeklySchedule result =
            autoScheduler.autoSchedule(
                weekStart,
                dayStart,
                dayEnd,
                fixedSchedules,
                flexibleSchedules,
                blockedTimes
            );


        // =====================================================
        // 5. 배치 성공 결과 출력
        // =====================================================

        System.out.println();
        System.out.println("========================================");
        System.out.println("자동배치 성공");
        System.out.println("========================================");

        for (ScheduledTask task : result.getScheduledTasks()) {

            System.out.println(
                task.getFlexibleSchedule().getName()
                + " | "
                + task.getDate()
                + " "
                + task.getDate().getDayOfWeek()
                + " | "
                + task.getStartTime()
                + " ~ "
                + task.getEndTime()
                + " | "
                + task.getDurationMinutes()
                + "분"
            );
        }


        // =====================================================
        // 6. 배치 실패 결과 출력
        // =====================================================

        System.out.println();
        System.out.println("========================================");
        System.out.println("자동배치 실패");
        System.out.println("========================================");

        for (FlexibleSchedule schedule : result.getUnscheduledTasks()) {

            System.out.println(
                schedule.getName()
                + " | deadline: "
                + schedule.getDeadline()
                + " | "
                + schedule.getEstimatedMinutes()
                + "분"
            );
        }


        // =====================================================
        // 7. 개수 출력
        // =====================================================

        System.out.println();
        System.out.println("========================================");
        System.out.println(
            "배치 성공 개수: "
            + result.getScheduledTasks().size()
        );

        System.out.println(
            "배치 실패 개수: "
            + result.getUnscheduledTasks().size()
        );
        System.out.println("========================================");
        */

        AutoScheduler autoScheduler = new AutoScheduler();

        LocalDate weekStart = LocalDate.of(2026, 10, 26);

        // 자동배치 가능 시간
        LocalTime dayStart = LocalTime.of(8, 0);
        LocalTime dayEnd = LocalTime.of(18, 0);


        // =====================================================
        // FixedSchedule
        // =====================================================

        List<FixedSchedule> fixedSchedules = new ArrayList<>();

        // 월/수 09:00 ~ 10:30
        fixedSchedules.add(
            new FixedSchedule(
                "수학 수업",
                weekStart,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                RepeatType.WEEKLY,
                Set.of(
                    DayOfWeek.MONDAY,
                    DayOfWeek.WEDNESDAY
                )
            )
        );

        // 화/목 13:00 ~ 15:00
        fixedSchedules.add(
            new FixedSchedule(
                "전공 수업",
                weekStart,
                LocalTime.of(13, 0),
                LocalTime.of(15, 0),
                RepeatType.WEEKLY,
                Set.of(
                    DayOfWeek.TUESDAY,
                    DayOfWeek.THURSDAY
                )
            )
        );

        // 금요일만 08:00 ~ 11:00
        fixedSchedules.add(
            new FixedSchedule(
                "특강",
                LocalDate.of(2026, 10, 30),
                LocalTime.of(8, 0),
                LocalTime.of(11, 0),
                RepeatType.NONE,
                Set.of()
            )
        );


        // =====================================================
        // BlockedTime
        // =====================================================

        List<BlockedTime> blockedTimes = new ArrayList<>();

        // 매일 점심시간
        blockedTimes.add(
            new BlockedTime(
                "점심",
                weekStart,
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                RepeatType.DAILY,
                Set.of()
            )
        );

        // 월요일 15:00 ~ 17:00
        blockedTimes.add(
            new BlockedTime(
                "병원",
                LocalDate.of(2026, 10, 26),
                LocalTime.of(15, 0),
                LocalTime.of(17, 0),
                RepeatType.NONE,
                Set.of()
            )
        );

        // 수요일 10:30 ~ 12:00
        // 수학수업 09:00~10:30과 딱 붙어 있음
        blockedTimes.add(
            new BlockedTime(
                "미팅",
                LocalDate.of(2026, 10, 28),
                LocalTime.of(10, 30),
                LocalTime.of(12, 0),
                RepeatType.NONE,
                Set.of()
            )
        );

        // 토요일 08:00 ~ 16:00
        // 큰 구간을 막아서 긴 일정 실패 가능성 확인
        blockedTimes.add(
            new BlockedTime(
                "외출",
                LocalDate.of(2026, 10, 31),
                LocalTime.of(8, 0),
                LocalTime.of(16, 0),
                RepeatType.NONE,
                Set.of()
            )
        );


        // =====================================================
        // FlexibleSchedule
        // =====================================================

        List<FlexibleSchedule> flexibleSchedules = new ArrayList<>();

        // 마감 월요일 / 90분
        // 월요일 10:30~12:00에 정확히 들어갈 가능성이 큼
        flexibleSchedules.add(
            new FlexibleSchedule(
                "알고리즘 과제",
                90,
                LocalDate.of(2026, 10, 26),
                5
            )
        );

        // 마감 화요일
        flexibleSchedules.add(
            new FlexibleSchedule(
                "영어 발표 준비",
                120,
                LocalDate.of(2026, 10, 27),
                4
            )
        );

        // 마감 수요일, 중요도 5, 180분
        flexibleSchedules.add(
            new FlexibleSchedule(
                "네트워크 보고서",
                180,
                LocalDate.of(2026, 10, 28),
                5
            )
        );

        // 위 일정과 deadline/importance 동일
        // 120분이므로 180분짜리보다 뒤에 와야 함
        flexibleSchedules.add(
            new FlexibleSchedule(
                "자료구조 복습",
                120,
                LocalDate.of(2026, 10, 28),
                5
            )
        );

        // 짧은 일정
        flexibleSchedules.add(
            new FlexibleSchedule(
                "영단어",
                30,
                LocalDate.of(2026, 10, 29),
                2
            )
        );

        // 긴 일정
        flexibleSchedules.add(
            new FlexibleSchedule(
                "프로젝트 개발",
                300,
                LocalDate.of(2026, 10, 30),
                5
            )
        );

        // 너무 길어서 08~18 범위의 단일 FreeSlot에는
        // 들어갈 가능성이 거의 없음
        flexibleSchedules.add(
            new FlexibleSchedule(
                "초대형 과제",
                600,
                LocalDate.of(2026, 11, 1),
                5
            )
        );

        // 마감일이 이미 지남
        flexibleSchedules.add(
            new FlexibleSchedule(
                "기한 지난 과제",
                60,
                LocalDate.of(2026, 10, 25),
                5
            )
        );

        // 완료된 일정
        FlexibleSchedule completed =
            new FlexibleSchedule(
                "완료한 과제",
                120,
                LocalDate.of(2026, 10, 27),
                5
            );

        completed.toggleCompleted();

        flexibleSchedules.add(completed);


        // =====================================================
        // Auto Schedule
        // =====================================================

        WeeklySchedule result =
            autoScheduler.autoSchedule(
                weekStart,
                dayStart,
                dayEnd,
                fixedSchedules,
                flexibleSchedules,
                blockedTimes
            );


        // =====================================================
        // 결과 출력
        // =====================================================

        System.out.println();
        System.out.println("========================================");
        System.out.println("자동배치 성공");
        System.out.println("========================================");

        for (ScheduledTask task : result.getScheduledTasks()) {

            FlexibleSchedule schedule =
                task.getFlexibleSchedule();

            System.out.println(
                schedule.getName()
                + " | "
                + task.getDate()
                + " "
                + task.getDate().getDayOfWeek()
                + " | "
                + task.getStartTime()
                + " ~ "
                + task.getEndTime()
                + " | "
                + task.getDurationMinutes()
                + "분"
                + " | deadline: "
                + schedule.getDeadline()
                + " | 중요도: "
                + schedule.getImportance()
            );
        }


        System.out.println();
        System.out.println("========================================");
        System.out.println("자동배치 실패");
        System.out.println("========================================");

        for (FlexibleSchedule schedule
                : result.getUnscheduledTasks()) {

            System.out.println(
                schedule.getName()
                + " | deadline: "
                + schedule.getDeadline()
                + " | "
                + schedule.getEstimatedMinutes()
                + "분"
                + " | 중요도: "
                + schedule.getImportance()
            );
        }


        System.out.println();
        System.out.println("========================================");

        System.out.println(
            "배치 성공 개수: "
            + result.getScheduledTasks().size()
        );

        System.out.println(
            "배치 실패 개수: "
            + result.getUnscheduledTasks().size()
        );

        System.out.println("========================================");
        
            
        }
}