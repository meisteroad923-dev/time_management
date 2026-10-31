package scheduler.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import scheduler.model.BlockedTime;
import scheduler.model.FixedSchedule;
import scheduler.model.FlexibleSchedule;
import scheduler.model.RepeatType;
import scheduler.model.ScheduledTask;
import scheduler.model.WeeklySchedule;

import scheduler.service.AutoScheduler;
import scheduler.service.ScheduleManager;

/**
 * 일주일 단위의 일정표를 표시하고 유동 일정 자동 배치 기능을 제공하는 패널이다.
 *
 * 사용자는 이전 주와 다음 주로 이동할 수 있으며,
 * 고정 일정과 배치 금지 시간, 자동 배치된 유동 일정을
 * 주간 일정표에서 확인할 수 있다.
 */
public class SchedulePanel extends JPanel {

    private ScheduleManager scheduleManager;
    private AutoScheduler autoScheduler;

    // 현재 화면에 표시되는 주의 월요일
    private LocalDate weekStart;

    // 자동 배치 가능 시간
    private static final LocalTime DAY_START = LocalTime.of(8, 0);
    private static final LocalTime DAY_END = LocalTime.of(23, 0);

    private JLabel weekLabel;

    private JTable scheduleTable;
    private DefaultTableModel tableModel;

    private DefaultListModel<FlexibleSchedule> flexibleListModel;
    private JList<FlexibleSchedule> flexibleList;

    // 가장 최근 자동배치 결과
    private WeeklySchedule weeklyResult;

    private Map<LocalDate, WeeklySchedule> weeklyResults = new HashMap<>(); //주별 결과 보관함
    private long lastDataVersion;

    /**
     * 주간 일정표 화면을 생성한다.
     *
     * @param scheduleManager 등록된 일정을 관리하는 일정 관리자
     * @param autoScheduler   유동 일정 자동 배치를 수행하는 자동 배치 서비스
     */
    public SchedulePanel(ScheduleManager scheduleManager, AutoScheduler autoScheduler) {
        this.scheduleManager = scheduleManager;
        this.autoScheduler = autoScheduler;
        this.lastDataVersion = scheduleManager.getDataVersion();

        weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));   //이번주 시작 날을 월요일로 고정
        setLayout(new BorderLayout(15, 15));

        JPanel scheduleArea = new JPanel(new BorderLayout());

        scheduleArea.add(createWeekControlPanel(), BorderLayout.NORTH);
        scheduleArea.add(createScheduleTablePanel(), BorderLayout.CENTER);

        add(scheduleArea, BorderLayout.CENTER);
        add(createFlexibleListPanel(), BorderLayout.EAST);

        refreshAll();
    }

    /**
     * 이전 주, 다음 주 이동 버튼과 현재 주간을 표시하는
     * 상단 제어 패널을 생성한다.
     *
     * @return 주간 이동 제어 패널
     */
    private JPanel createWeekControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));

        JButton previousButton = new JButton("< 이전 주");
        JButton currentWeekButton = new JButton("이번 주");
        JButton nextButton = new JButton("다음 주 >");

        weekLabel = new JLabel("", SwingConstants.CENTER);
        weekLabel.setFont( weekLabel.getFont().deriveFont(Font.BOLD, 18f));

        previousButton.addActionListener(e -> moveWeek(-1));
        currentWeekButton.addActionListener(e -> moveToCurrentWeek());
        nextButton.addActionListener(e -> moveWeek(1));

        panel.add(previousButton);
        panel.add(currentWeekButton);
        panel.add(nextButton);

        panel.add(Box.createHorizontalStrut(15));
        panel.add(weekLabel);
        panel.add(Box.createHorizontalStrut(15));

        

        return panel;
    }

    /**
     * 월요일부터 일요일까지의 일정을
     * 30분 단위로 표시하는 주간 일정표를 생성한다.
     *
     * @return 주간 일정표 패널
     */
    private JPanel createScheduleTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel() {
            /**
             * 주간 일정표의 셀을 사용자가 직접 수정할 수 없도록 설정한다.
             *
             * @param row 확인할 행 인덱스
             * @param column 확인할 열 인덱스
             * @return 항상 false
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        scheduleTable = new JTable(tableModel);
        scheduleTable.setRowHeight(30);
        scheduleTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        panel.add(new JScrollPane(scheduleTable), BorderLayout.CENTER);

        return panel;
    }

    /**
     * 등록된 유동 일정 목록과 자동 배치 실행 버튼을 포함하는
     * 우측 패널을 생성한다.
     *
     * @return 유동 일정 현황 패널
     */
    private JPanel createFlexibleListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(300, 0));

        JLabel title = new JLabel("유동 일정 현황", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD,18f));

        flexibleListModel = new DefaultListModel<>();
        flexibleList = new JList<>(flexibleListModel);

        flexibleList.setCellRenderer(new DefaultListCellRenderer() {
            /**
             * FlexibleSchedule 객체를 유동 일정 목록에 표시할 문자열로 변환한다.
             *
             * 일정 이름, 마감일, 예상 소요 시간 및 완료 상태를 표시한다.
             *
             * @param list 목록 컴포넌트
             * @param value 표시할 FlexibleSchedule 객체
             * @param index 항목 인덱스
             * @param isSelected 항목 선택 여부
             * @param cellHasFocus 포커스 여부
             * @return 화면에 표시할 컴포넌트
             */
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                FlexibleSchedule schedule = (FlexibleSchedule) value;

                String completed = schedule.isCompleted()? "완료": "미완료";
                setText(schedule.getName()+ " | "+ schedule.getDeadline()+ " | "+ schedule.getEstimatedMinutes()+ "분 | "+ completed);

                return this;
            }
        });

        JButton autoScheduleButton = new JButton("자동 배치 실행");
        autoScheduleButton.addActionListener(e -> runAutoSchedule());

        panel.add(title, BorderLayout.NORTH);
        panel.add( new JScrollPane(flexibleList),BorderLayout.CENTER);
        panel.add(autoScheduleButton, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * 현재 표시 중인 주를 이전 주 또는 다음 주로 이동한다.
     *
     * 이동한 주에 기존 자동 배치 결과가 존재하는 경우 해당 결과를 복원하여
     * 주간 일정표에 다시 표시한다.
     *
     * @param amount 이동할 주의 수. -1은 이전 주, 1은 다음 주
     */
    private void moveWeek(int amount) {
        weekStart = weekStart.plusWeeks(amount);
        refreshAll();
    }

    /**
     * 현재 날짜가 포함된 주로 이동한다.
     *
     * 해당 주에 기존 자동 배치 결과가 존재하면 함께 복원한다.
     */
    private void moveToCurrentWeek() {
        weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        refreshAll();
    }


    /**
     * 현재 화면에 표시 중인 주간에 대해 유동 일정 자동 배치를 실행한다.
     *
     * 고정 일정과 배치 금지 시간을 제외한 빈 시간에
     * 완료되지 않은 유동 일정을 자동으로 배치한다.
     *
     * 자동 배치 결과는 해당 주의 결과로 저장하여
     * 이후 같은 주를 다시 표시할 때 재사용한다.
     */
    private void runAutoSchedule() {
        weeklyResult = autoScheduler.autoSchedule(weekStart, DAY_START, DAY_END, scheduleManager.getFixedSchedule(), scheduleManager.getFlexibleSchedule(), scheduleManager.getBlockedTime());
        weeklyResults.put(weekStart, weeklyResult);

        refreshScheduleTable();
        refreshFlexibleList();

        StringBuilder message = new StringBuilder();

        message.append("자동 배치가 완료되었습니다.\n")
            .append("배치 성공: ")
            .append(weeklyResult.getScheduledTasks().size())
            .append("개\n")
            .append("배치 실패: ")
            .append(weeklyResult.getUnscheduledTasks().size())
            .append("개");

        if (!weeklyResult.getUnscheduledTasks().isEmpty()) {
            message.append("\n\n[배치되지 않은 일정]");
            for (FlexibleSchedule schedule
                    : weeklyResult.getUnscheduledTasks()) {

                message.append("\n- ")
                    .append(schedule.getName())
                    .append(" | ")
                    .append(schedule.getEstimatedMinutes())
                    .append("분 | 마감 ")
                    .append(schedule.getDeadline());
            }
        }

        JOptionPane.showMessageDialog(
                this,
                message.toString()
        );    
    }

    /**
     * 현재 선택된 주간의 날짜와 등록된 일정을 기반으로 주간 일정표를 다시 생성한다.
     *
     * 고정 일정은 [고정], 배치 금지 시간은 [금지], 자동 배치된 유동 일정은 [자동]으로 표시한다.
     */
    private void refreshScheduleTable() {
        String[] columns = new String[8];
        columns[0] = "시간";

        for (int i = 0; i < 7; i++) {
            LocalDate date = weekStart.plusDays(i); 
            columns[i + 1] = date.getDayOfWeek()  + " "  + date.getMonthValue()  + "/"  + date.getDayOfMonth();
        }

        tableModel.setColumnIdentifiers(columns);
        tableModel.setRowCount(0);

        LocalTime time = DAY_START;

        while (time.isBefore(DAY_END)) {
            Object[] row = new Object[8];
            row[0] = time.toString();
            for (int day = 0; day < 7; day++) {
                LocalDate date = weekStart.plusDays(day);
                row[day + 1] = getScheduleText(date, time);
            }

            tableModel.addRow(row);
            time = time.plusMinutes(30);
        }
    }

    /**
     * 특정 날짜와 시간에 표시할 일정 내용을 생성한다.
     *
     * @param date 확인할 날짜
     * @param time 확인할 시간
     * @return 해당 시간에 표시할 일정 문자열
     */
    private String getScheduleText(LocalDate date, LocalTime time) {
        StringBuilder text = new StringBuilder();

        // 고정 일정
        for (FixedSchedule schedule : scheduleManager.getFixedSchedule()) {
            if (appliesToDate(schedule.getStartDate(), schedule.getEndDate(),schedule.getRepeatType(), schedule.getRepeatDays(), date) && isTimeIncluded(time, schedule.getStartTime(), schedule.getEndTime())) {
                appendText(text, "[고정] " + schedule.getName());
            }
        }
        // 배치 금지 시간
        for (BlockedTime blockedTime : scheduleManager.getBlockedTime()) {

            if (appliesToDate(blockedTime.getStartDate(), blockedTime.getEndDate(),blockedTime.getRepeatType(), blockedTime.getRepeatDays(), date) && isTimeIncluded(time, blockedTime.getStartTime(), blockedTime.getEndTime())) {
                appendText(text, "[금지] " + blockedTime.getName());
            }
        }
        // 자동 배치된 유동 일정
        if (weeklyResult != null) {
            for (ScheduledTask task : weeklyResult.getScheduledTasks()) {
                if (task.getDate().equals(date) && isTimeIncluded(time, task.getStartTime(), task.getEndTime())) {
                    appendText(text, "[자동] " + task.getFlexibleSchedule() .getName());
                }
            }
        }
    
        return text.toString();
    }

    /**
     * 일정의 반복 유형을 기준으로 해당 일정이
     * 특정 날짜에 적용되는지 판단한다.
     *
     * @param startDate 일정 적용 시작일
    * @param endDate 일정 적용 종료일
    * @param repeatType 반복 유형
    * @param repeatDays 반복 요일 집합
    * @param targetDate 확인할 날짜
    * @return 해당 날짜에 일정이 적용되면 true
    */
    private boolean appliesToDate(LocalDate startDate, LocalDate endDate, RepeatType repeatType, Set<DayOfWeek> repeatDays, LocalDate targetDate) {
        // 적용 기간 밖
        if (targetDate.isBefore(startDate) || targetDate.isAfter(endDate)) return false;
        
        // 한 번만 적용
        if (repeatType == RepeatType.NONE) return targetDate.equals(startDate);

        // 기간 내 매일 적용
        if (repeatType == RepeatType.DAILY) return true;

        // 기간 내 선택된 요일에 적용
        if (repeatType == RepeatType.WEEKLY) return repeatDays.contains(targetDate.getDayOfWeek());

        return false;
    }

    /**
     * 30분 단위의 일정표 셀과 일정 시간 구간이 겹치는지 판단한다.
     *
     * 예를 들어 09:20~09:40 일정은
     * 09:00~09:30 셀과 09:30~10:00 셀 모두에 포함된다.
     *
     * @param time 일정표 셀의 시작 시간
     * @param startTime 일정 시작 시간
     * @param endTime 일정 종료 시간
     * @return 일정과 해당 30분 셀이 겹치면 true, 아니면 false
     */
    private boolean isTimeIncluded(LocalTime time, LocalTime startTime, LocalTime endTime) {
        LocalTime cellEnd = time.plusMinutes(30);
        return time.isBefore(endTime) && startTime.isBefore(cellEnd);
    }

    /**
     * 일정표의 한 셀에 여러 일정이 존재하는 경우 문자열을 구분하여 추가한다.
     *
     * @param builder 일정 내용을 저장할 문자열 생성기
     * @param value   추가할 일정 문자열
     */
    private void appendText(StringBuilder builder,String value) {
        if (builder.length() > 0) {
            builder.append(" / ");
        }
        builder.append(value);
    }

    /**
     * ScheduleManager에 저장된 유동 일정을 불러와
     * 우측 유동 일정 현황 목록을 갱신한다.
     */
    private void refreshFlexibleList() {
        flexibleListModel.clear();
        for (FlexibleSchedule schedule : scheduleManager.getFlexibleSchedule()) {
            flexibleListModel.addElement(schedule);
        }
    }

    /**
     * 현재 주간 표시, 일정표, 유동 일정 목록을
     * 모두 최신 상태로 갱신한다.
     */
    public void refreshAll() {
        long currentVersion = scheduleManager.getDataVersion();

        // 고정/금지/유동 일정이 실제로 변경된 경우에만
        // 기존 자동배치 결과를 무효화한다.
        if (currentVersion != lastDataVersion) {
            weeklyResults.clear();
            weeklyResult = null;

            lastDataVersion = currentVersion;
        }

        // 현재 보고 있는 주의 기존 자동배치 결과 복원
        weeklyResult = weeklyResults.get(weekStart);
        
        LocalDate weekEnd = weekStart.plusDays(6);
        weekLabel.setText(weekStart + " ~ " + weekEnd);

        refreshScheduleTable();
        refreshFlexibleList();
    }
}