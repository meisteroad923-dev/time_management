

고정 일정
    1. 일정 이름                // String
    2. 시작 시간                // LocalTime
    3. 종료 시간                // LocalTime
    4. 반복 유형 (일/ 주 단위)  // enum
    5. 반복 요일/ 일자          // Set<DayOfWeek>
    6. 반복 없을 시 날짜        // LocalDate

유동 일정    
    1. 일정 이름                // String
    2. 예상 소요 시간           // int
    3. 마감일                   // LocalDate
    4. 중요도                   // int
    5. 완료 여부                // boolean

배치 금지 시간
    1. 일정 이름                // String
    2. 시작 시간                // LocalTime
    3. 종료 시간                // LocalTime
    4. 반복 유형 (일/ 주 단위)  // enum
    5. 반복 요일/ 일자          // Set<DayOfWeek>
    6. 반복 없을 시 날짜        // LocalDate

ScheduleManager
method
    
    private boolean isTimeOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2)   //두 일정의 시작, 종료 시간을 받아서 겹치는지 확인
    private boolean isDateOverlap(LocalDate date1, RepeatType type1, Set<DayOfWeek> days1, LocalDate date2, RepeatType type2, Set<DayOfWeek> days2) //날짜가 겹치는지 확인, 날짜가 겹치는 경우의 수는 총 4가지, 4가지 경우를 각각 확인
    public boolean checkConflict(FixedSchedule newSchedule)         //고정일정의 기존 일정과 충돌 여부 확인
    public boolean checkConflict(BlockedTime newBlockedTime)        //배치 금지 시간대가 기존 일정과 충돌 여부 확인
    public int adjuestEstimatedTime(int estimatedTime)              //예상시간을 30분 단위로 보정
    public void updateCompletionStatus(FlexibleSchedule schedule)   //유동 일정의 완료 여부를 토글로 변경