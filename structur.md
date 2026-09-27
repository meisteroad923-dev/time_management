

고정 일정
    1. 일정 이름                // String
    2. 시작 시간                // LocalTime
    3. 종료 시간                // LocalTime
    4. 반복 유형 (일/ 주 단위)  // enum
    5. 반복 요일/ 일자          // Set<DayOfWeek>

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