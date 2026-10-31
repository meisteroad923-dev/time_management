[테스트 데이터 사용 방법]

프로그램은 실행 위치의 scheduleData.dat 파일을 읽습니다.

테스트하려는 데이터 파일 하나를 선택한 후,
파일 이름을 scheduleData.dat 로 변경하여
ssd_ip_cp4_2026f_233343_app.jar 파일과 같은 폴더에 둡니다.

예)
test_auto_schedule.dat
        ↓ 이름 변경
scheduleData.dat

실행:
java -jar ssd_ip_cp4_2026f_233343_app.jar

[테스트 데이터 설명]

1. test_basic.dat
   - 기본 일정 등록 및 조회 테스트용

2. test_conflict.dat
   - 고정 일정과 배치 금지 시간의 충돌 확인용

3. test_auto_schedule.dat
   - 유동 일정 자동 배치 기능 확인용

※ 한 번에 하나의 테스트 데이터만 scheduleData.dat라는 이름으로 사용합니다.
※ 기존 scheduleData.dat 파일이 있다면 테스트 전에 백업하거나 다른 이름으로 변경하십시오.
※ scheduleData.dat 파일이 없으면 빈 데이터 상태로 시작합니다.

