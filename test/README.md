# Time Management Scheduler — 교수자 직접 검증 안내

## 사용 방법
각 TEST는 교수자가 직접 조작할 수 있도록 **초기 데이터(.dat)** 와 **실행 절차**를 분리했습니다.

1. `test/` 폴더의 해당 `.dat` 파일을 JAR 파일과 같은 폴더로 복사하고 파일명을 `scheduleData.dat`로 변경합니다.
2. `java -jar ssd_ip_cp4_2026f_233343_app.jar` 명령으로 실행합니다.
3. 해당 `TEST*.md`에 적힌 순서대로 직접 등록·수정·삭제·자동 배치하고 결과를 확인합니다.
4. 다음 TEST로 넘어갈 때는 **프로그램을 완전히 종료한 후** 다음 `.dat`로 교체합니다.

> 주의: `.dat`는 Java 직렬화로 생성되므로 텍스트 파일 이름만 `.dat`로 바꾸면 작동하지 않습니다. 개발자가 해당 입력 항목을 실제 JAR에서 등록한 뒤 생성된 `scheduleData.dat`를 복사해야 합니다. 각 파일은 **교수자가 조작하기 전 초기 상태**만 저장합니다.

## 4개 파일
| TEST | 초기 데이터 파일 | 검증 |
|---|---|---|
| 1 | `01_basic_crud_recurrence.dat` | 등록·수정·삭제, 반복, 30분 올림 |
| 2 | `02_conflict_boundary.dat` | Fixed/Blocked 양방향 충돌, 1분 겹침, 접점 경계 |
| 3 | `03_auto_schedule_priority.dat` | 배치 시작 요일, 우선순위, 회피, 실패·캐시 |
| 4 | `04_completion_persistence.dat` | 완료 처리, 완료 항목 제외, 변경 반영, 재실행 저장 |

테스트에 사용하는 공통 주: **2026-10-26(월) ~ 2026-11-01(일)**. 일정 등록 시 별도로 적지 않은 완료 상태는 모두 미완료입니다. 선택한 날짜의 **반복 없음(NONE)** 은 종료일이 자동으로 시작일과 동일합니다.

## 개발자가 `.dat` 초기 파일을 만드는 방법
1. **기존 데이터 백업**: `Copy-Item .\scheduleData.dat .\scheduleData_original_backup.dat -ErrorAction SilentlyContinue`
2. 프로그램 종료 후 `Remove-Item .\scheduleData.dat -Force -ErrorAction SilentlyContinue`
3. JAR 실행 → 해당 TEST 문서의 **초기 상태**만 등록 → 종료
4. `Copy-Item .\scheduleData.dat .\test\01_basic_crud_recurrence.dat -Force` 처럼 테스트명에 맞춰 보관
5. 각 TEST마다 2~4번을 반복합니다.
6. **교수자 실습 단계는 초기 파일에 반영하지 마세요.** 교수자가 직접 실행해 볼 수 있어야 합니다.

## 판정 방법
각 단계의 '기대 결과'와 실제 결과가 같으면 PASS, 다르면 FAIL입니다. 시험 중 프로그램 데이터를 바꾼 뒤 다시 같은 TEST를 시작하려면 원본 `.dat`를 다시 `scheduleData.dat`로 복사해야 합니다.

## TEST 4 저장 확인 주의
TEST 4의 '종료 후 재실행' 단계에서는 데이터를 교체하지 않은 채 같은 `scheduleData.dat`로 재실행해야 합니다. 자동 배치 결과 캐시가 아닌 **원본 일정과 완료 상태**의 저장을 검증합니다.
