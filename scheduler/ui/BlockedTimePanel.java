package scheduler.ui;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Set;

import scheduler.model.BlockedTime;
import scheduler.model.RepeatType;
import scheduler.service.ScheduleManager;

/**
 * 유동 일정의 자동 배치를 금지할 시간대를
 * 등록, 수정, 삭제하는 Swing 패널이다.
 *
 * 사용자는 시간대 이름, 날짜, 시작 시간, 종료 시간,
 * 반복 유형 및 반복 요일을 설정할 수 있다.
 */
public class BlockedTimePanel extends JPanel {
    private ScheduleManager scheduleManager;

    private JTextField nameField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField startTimeField;
    private JTextField endTimeField;

    private JComboBox<RepeatType> repeatTypeBox;

    private JCheckBox mondayBox;
    private JCheckBox tuesdayBox;
    private JCheckBox wednesdayBox;
    private JCheckBox thursdayBox;
    private JCheckBox fridayBox;
    private JCheckBox saturdayBox;
    private JCheckBox sundayBox;

    private DefaultListModel<BlockedTime> listModel;
    private JList<BlockedTime> blockedTimeList;

    /**
     * 배치 금지 시간 관리 화면을 생성한다.
     *
     * @param scheduleManager 배치 금지 시간의 등록, 수정,
     *                        삭제를 처리하는 일정 관리자
     */
    public BlockedTimePanel(ScheduleManager scheduleManager) {

        this.scheduleManager = scheduleManager;

        setLayout(new BorderLayout(20, 20));

        add(createInputPanel(), BorderLayout.CENTER);
        add(createBlockedTimeListPanel(), BorderLayout.EAST);

        refreshList();
    }

    /**
     * 배치 금지 시간 정보를 입력할 패널을 생성한다.
     *
     * @return 배치 금지 시간 입력 패널
     */
    private JPanel createInputPanel() {

        JPanel container = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel("배치 금지 시간 등록", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        container.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 15));

        nameField = new JTextField();
        startDateField = new JTextField();
        endDateField =new JTextField();
        startTimeField = new JTextField();
        endTimeField = new JTextField();

        repeatTypeBox = new JComboBox<>(RepeatType.values());

        formPanel.add(new JLabel("시간대 이름"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("적용 시작일 (yyyy-MM-dd)"));
        formPanel.add(startDateField);

        formPanel.add(new JLabel("적용 종료일 (yyyy-MM-dd)"));
        formPanel.add(endDateField);

        formPanel.add(new JLabel("시작 시간 (HH:mm)"));
        formPanel.add(startTimeField);

        formPanel.add(new JLabel("종료 시간 (HH:mm)"));
        formPanel.add(endTimeField);

        formPanel.add(new JLabel("반복 유형"));
        formPanel.add(repeatTypeBox);

        // 반복 요일
        JPanel dayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        mondayBox = new JCheckBox("월");
        tuesdayBox = new JCheckBox("화");
        wednesdayBox = new JCheckBox("수");
        thursdayBox = new JCheckBox("목");
        fridayBox = new JCheckBox("금");
        saturdayBox = new JCheckBox("토");
        sundayBox = new JCheckBox("일");

        dayPanel.add(mondayBox);
        dayPanel.add(tuesdayBox);
        dayPanel.add(wednesdayBox);
        dayPanel.add(thursdayBox);
        dayPanel.add(fridayBox);
        dayPanel.add(saturdayBox);
        dayPanel.add(sundayBox);

        formPanel.add(new JLabel("반복 요일"));
        formPanel.add(dayPanel);

        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.add(formPanel, BorderLayout.NORTH);
        container.add(formWrapper, BorderLayout.CENTER);

        // 등록, 수정 버튼 생성
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("등록");
        JButton updateButton = new JButton("수정");
        JButton clearButton = new JButton("입력 초기화");

        addButton.addActionListener(e -> addBlockedTime());
        updateButton.addActionListener(e -> updateBlockedTime());
        clearButton.addActionListener(e -> clearFields());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);

        container.add(buttonPanel, BorderLayout.SOUTH);

        return container;
    }

    /**
     * 등록된 배치 금지 시간의 목록을 표시할 패널을 생성한다.
     *
     * 목록에서 시간대를 선택하면 해당 정보가
     * 입력 영역에 표시되며 삭제할 수도 있다.
     *
     * @return 배치 금지 시간 현황 패널
     */
    private JPanel createBlockedTimeListPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(430, 0));
        JLabel title = new JLabel("배치 금지 시간 현황", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        panel.add(title, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        blockedTimeList = new JList<>(listModel);
        blockedTimeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        blockedTimeList.setCellRenderer(
                new DefaultListCellRenderer() {
                    /**
                     * BlockedTime 객체를 목록에 표시할 문자열
                     *
                     * @param list         목록 컴포넌트
                     * @param value        표시할 BlockedTime 객체
                     * @param index        항목의 인덱스
                     * @param isSelected   항목 선택 여부
                     * @param cellHasFocus 포커스 여부
                     * @return 목록에 표시할 컴포넌트
                     */
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                            boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        BlockedTime blockedTime = (BlockedTime) value;
                        setText(blockedTime.getName() + " | " + blockedTime.getStartDate() + "~" + blockedTime.getEndDate() + " | "
                                + blockedTime.getStartTime() + " ~ " + blockedTime.getEndTime() + " | " + blockedTime.getRepeatType());

                        return this;
                    }
                });

        // 선택한 시간대를 입력창에 다시 표시
        blockedTimeList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting())
                return;

            BlockedTime blockedTime = blockedTimeList.getSelectedValue();

            if (blockedTime == null)
                return;

            nameField.setText(blockedTime.getName());
            startDateField.setText(blockedTime.getStartDate().toString());
            endDateField.setText(blockedTime.getEndDate().toString());
            startTimeField.setText(blockedTime.getStartTime().toString());
            endTimeField.setText(blockedTime.getEndTime().toString());
            repeatTypeBox.setSelectedItem(blockedTime.getRepeatType());

            setRepeatDays(blockedTime.getRepeatDays());
        });

        panel.add(new JScrollPane(blockedTimeList), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("삭제");
        deleteButton.addActionListener(e -> deleteBlockedTime());
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * 입력된 정보를 이용하여 새로운 배치 금지 시간을 등록한다.
     *
     * 고정 일정 또는 기존 배치 금지 시간과 충돌하는 경우
     * 등록하지 않는다.
     */
    private void addBlockedTime() {

        try {
            BlockedTime blockedTime = createBlockedTimeFromInput();
            if (scheduleManager.checkConflict(blockedTime)) {
                JOptionPane.showMessageDialog(this, "기존 일정 또는 배치 금지 시간과 충돌합니다.");
                return;
            }
            scheduleManager.addBlockedTime(blockedTime);
            refreshList();
            clearFields();
            JOptionPane.showMessageDialog(this,"배치 금지 시간이 등록되었습니다.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog( this, "입력값을 확인하세요.\n" + "시작/종료 날짜: yyyy-MM-dd\n" + "시간: HH:mm");
        }
    }

    /**
     * 현재 선택된 배치 금지 시간을
     * 입력된 새로운 정보로 수정한다.
     *
     * 다른 일정과 충돌하는 경우 수정하지 않는다.
     */
    private void updateBlockedTime() {

        BlockedTime oldBlockedTime = blockedTimeList.getSelectedValue();

        if (oldBlockedTime == null) {
            JOptionPane.showMessageDialog( this, "수정할 배치 금지 시간을 선택하세요.");
            return;
        }

        try {
            BlockedTime newBlockedTime = createBlockedTimeFromInput();
            boolean success = scheduleManager.updateBlockedTime(oldBlockedTime,newBlockedTime);
            if (!success) {
                JOptionPane.showMessageDialog( this, "다른 일정과 충돌하여 수정할 수 없습니다.");
                return;
            }
            refreshList();
            clearFields();
            JOptionPane.showMessageDialog( this, "배치 금지 시간이 수정되었습니다.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog( this, "입력값을 확인하세요.");
        }
    }

    /**
     * 현재 선택된 배치 금지 시간을 삭제한다.
     */
    private void deleteBlockedTime() {
        BlockedTime blockedTime = blockedTimeList.getSelectedValue();

        if (blockedTime == null) {
            JOptionPane.showMessageDialog( this, "삭제할 배치 금지 시간을 선택하세요.");
            return;
        }
        scheduleManager.removeBlockedTime(blockedTime);

        refreshList();
        clearFields();
    }

    /**
     * 입력된 정보를 이용하여 BlockedTime 객체를 생성한다.
     *
     * @return 입력값을 기반으로 생성된 배치 금지 시간
     * @throws IllegalArgumentException 입력값이 올바르지 않은 경우
     */
    private BlockedTime createBlockedTimeFromInput() {
        String name =  nameField.getText().trim();
        if (name.isEmpty()) throw new IllegalArgumentException();

        RepeatType repeatType =(RepeatType) repeatTypeBox.getSelectedItem();

        // 적용 기간 입력
        LocalDate startDate =LocalDate.parse(startDateField.getText().trim());
        LocalDate endDate =LocalDate.parse(endDateField.getText().trim());

        // 시작일 > 종료일이면 잘못된 입력
        if (startDate.isAfter(endDate)) throw new IllegalArgumentException();
        
        // NONE은 하루짜리 일정이므로 시작일과 종료일이 같아야 함
        if (repeatType == RepeatType.NONE && !startDate.equals(endDate))throw new IllegalArgumentException();

        LocalTime startTime =LocalTime.parse(startTimeField.getText().trim());
        LocalTime endTime =LocalTime.parse(endTimeField.getText().trim());

        // 종료시간은 시작시간보다 늦어야 함
        if (!startTime.isBefore(endTime))  throw new IllegalArgumentException();

        Set<DayOfWeek> repeatDays = getSelectedRepeatDays();

        // WEEKLY는 최소 한 요일 필요
        if (repeatType == RepeatType.WEEKLY && repeatDays.isEmpty()) throw new IllegalArgumentException();

        return new BlockedTime(name, startDate, endDate, startTime, endTime, repeatType, repeatDays);
    }

    /**
     * 반복 요일 체크박스에서 선택된 요일을 수집한다.
     *
     * @return 선택된 반복 요일의 집합
     */
    private Set<DayOfWeek> getSelectedRepeatDays() {
        Set<DayOfWeek> days = EnumSet.noneOf(DayOfWeek.class);
        if (mondayBox.isSelected())days.add(DayOfWeek.MONDAY);
        if (tuesdayBox.isSelected())days.add(DayOfWeek.TUESDAY);
        if (wednesdayBox.isSelected())days.add(DayOfWeek.WEDNESDAY);
        if (thursdayBox.isSelected())days.add(DayOfWeek.THURSDAY);
        if (fridayBox.isSelected())days.add(DayOfWeek.FRIDAY);
        if (saturdayBox.isSelected())days.add(DayOfWeek.SATURDAY);
        if (sundayBox.isSelected())days.add(DayOfWeek.SUNDAY);

        return days;
    }

    /**
     * 주어진 반복 요일에 따라
     * 각 요일 체크박스의 상태를 설정한다.
     *
     * @param days 화면에 표시할 반복 요일 집합
     */
    private void setRepeatDays(Set<DayOfWeek> days) {
        mondayBox.setSelected(days.contains(DayOfWeek.MONDAY));
        tuesdayBox.setSelected(days.contains(DayOfWeek.TUESDAY));
        wednesdayBox.setSelected(days.contains(DayOfWeek.WEDNESDAY));
        thursdayBox.setSelected(days.contains(DayOfWeek.THURSDAY));
        fridayBox.setSelected(days.contains(DayOfWeek.FRIDAY));
        saturdayBox.setSelected(days.contains(DayOfWeek.SATURDAY));
        sundayBox.setSelected(days.contains(DayOfWeek.SUNDAY));
    }

    /**
     * ScheduleManager에 저장된 배치 금지 시간 목록을
     * 화면에 최신 상태로 표시한다.
     */
    public void refreshList() {
        listModel.clear();
        for (BlockedTime blockedTime : scheduleManager.getBlockedTime()) {
            listModel.addElement(blockedTime);
        }
    }

    /**
     * 배치 금지 시간 입력 필드와 반복 설정을
     * 초기 상태로 되돌린다.
     */
    private void clearFields() {

        nameField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        startTimeField.setText("");
        endTimeField.setText("");

        repeatTypeBox.setSelectedItem(RepeatType.NONE);

        mondayBox.setSelected(false);
        tuesdayBox.setSelected(false);
        wednesdayBox.setSelected(false);
        thursdayBox.setSelected(false);
        fridayBox.setSelected(false);
        saturdayBox.setSelected(false);
        sundayBox.setSelected(false);

        blockedTimeList.clearSelection();
    }
}