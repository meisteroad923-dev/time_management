package scheduler.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
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

    /**
     * 주간 일정표 화면을 생성한다.
     *
     * @param scheduleManager 등록된 일정을 관리하는 일정 관리자
     * @param autoScheduler   유동 일정 자동 배치를 수행하는 자동 배치 서비스
     */
    public SchedulePanel(ScheduleManager scheduleManager, AutoScheduler autoScheduler) {
        this.scheduleManager = scheduleManager;
        this.autoScheduler = autoScheduler;

        weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));   //이번주 시작 날을 월요일로 고정
        setLayout(new BorderLayout(15, 15));

        add(createWeekControlPanel(), BorderLayout.NORTH);
        add(createScheduleTablePanel(), BorderLayout.CENTER);
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
        JPanel panel = new JPanel();
        JButton previousButton = new JButton("< 이전 주");
        JButton nextButton = new JButton("다음 주 >");

        weekLabel = new JLabel("", SwingConstants.CENTER);
        weekLabel.setFont( weekLabel.getFont().deriveFont(Font.BOLD, 18f));

        previousButton.addActionListener(e -> moveWeek(-1));
        nextButton.addActionListener(e -> moveWeek(1));

        panel.add(previousButton);
        panel.add(weekLabel);
        panel.add(nextButton);

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
     * 주가 변경되면 기존 자동 배치 결과를 제거하고
     * 새로운 주간 일정표를 다시 표시한다.
     *
     * @param amount 이동할 주의 수. -1은 이전 주, 1은 다음 주
     */
    private void moveWeek(int amount) {
        weekStart = weekStart.plusWeeks(amount);
        weeklyResult = null;
        refreshAll();
    }

    /**
     * 현재 화면에 표시 중인 주간에 대해 유동 일정 자동 배치를 실
     *
     * 고정 일정과 배치 금지 시간을 제외한 빈 시간에 완료되지 않은 유동 일정을 자동으로 배치한다.
     */
    private void runAutoSchedule() {
        weeklyResult = autoScheduler.autoSchedule(weekStart, DAY_START, DAY_END, scheduleManager.getFixedSchedule(), scheduleManager.getFlexibleSchedule(), scheduleManager.getBlockedTime());

        refreshScheduleTable();
        refreshFlexibleList();

        JOptionPane.showMessageDialog(this,"자동 배치가 완료되었습니다.\n" + "배치 성공: " + weeklyResult.getScheduledTasks().size() + "개\n" + "배치 실패: " + weeklyResult.getUnscheduledTasks().size() + "개");
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
            if (appliesToDate(schedule.getDate(), schedule.getRepeatType(), schedule.getRepeatDays(), date) && isTimeIncluded(time, schedule.getStartTime(), schedule.getEndTime())) {
                appendText(text, "[고정] " + schedule.getName());
            }
        }
        // 배치 금지 시간
        for (BlockedTime blockedTime : scheduleManager.getBlockedTime()) {

            if (appliesToDate(blockedTime.getDate(), blockedTime.getRepeatType(), blockedTime.getRepeatDays(), date) && isTimeIncluded(time, blockedTime.getStartTime(), blockedTime.getEndTime())) {
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
     * @param scheduleDate 일정에 저장된 날짜
     * @param repeatType   일정의 반복 유형
     * @param repeatDays   반복 요일 집합
     * @param targetDate   확인할 날짜
     * @return 해당 날짜에 일정이 적용되면 true
     */
    private boolean appliesToDate(LocalDate scheduleDate, RepeatType repeatType, Set<DayOfWeek> repeatDays, LocalDate targetDate) {
        if (repeatType == RepeatType.NONE) return scheduleDate.equals(targetDate);
        if (repeatType == RepeatType.DAILY) return true;
        if (repeatType == RepeatType.WEEKLY) return repeatDays.contains(targetDate.getDayOfWeek());
        return false;
    }

    /**
     * 특정 시간이 일정의 시작 시간 이상이고, 종료 시간보다 이전인지 판단한다.
     *
     * @param time      확인할 시간
     * @param startTime 일정 시작 시간
     * @param endTime   일정 종료 시간
     * @return 일정 시간 범위에 포함되면 true
     */
    private boolean isTimeIncluded(LocalTime time, LocalTime startTime, LocalTime endTime) {
        return !time.isBefore(startTime) && time.isBefore(endTime);
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
        LocalDate weekEnd = weekStart.plusDays(6);
        weekLabel.setText(weekStart + " ~ " + weekEnd);

        refreshScheduleTable();
        refreshFlexibleList();
    }
}