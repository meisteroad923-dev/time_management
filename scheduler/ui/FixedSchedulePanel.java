package scheduler.ui;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Set;

import scheduler.model.FixedSchedule;
import scheduler.model.RepeatType;
import scheduler.service.ScheduleManager;

/**
 * 고정 일정의 등록, 수정, 삭제 기능을 제공하는 Swing 패널이다.
 *
 * 사용자는 일정 이름, 날짜, 시작 시간, 종료 시간,
 * 반복 유형 및 반복 요일을 입력하여 고정 일정을 관리할 수 있다.
 *
 * 등록된 고정 일정은 우측 목록에서 확인할 수 있으며,
 * 선택한 일정의 정보를 수정하거나 삭제할 수 있다.
 */
public class FixedSchedulePanel extends JPanel {

    private ScheduleManager scheduleManager;

    private JTextField nameField;
    private JTextField dateField;
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

    private DefaultListModel<FixedSchedule> listModel;
    private JList<FixedSchedule> scheduleList;

    /**
     * 고정 일정 관리 화면을 생성한다.
     *
     * 입력 영역과 등록된 고정 일정 목록을 생성하고,
     * ScheduleManager에 저장된 기존 일정 정보를 화면에 표시한다.
     *
     * @param scheduleManager 고정 일정의 등록, 수정, 삭제를 처리하는 일정 관리자
     */
    public FixedSchedulePanel(ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager;

        setLayout(new BorderLayout(20, 20));

        add(createInputPanel(), BorderLayout.CENTER);
        add(createScheduleListPanel(), BorderLayout.EAST);

        refreshList();
    }


    /**
     * 고정 일정의 정보를 입력하는 패널을 생성한다.
     *
     * 일정 이름, 날짜, 시작 시간, 종료 시간,
     * 반복 유형 및 반복 요일을 입력할 수 있으며,
     * 등록, 수정, 입력 초기화 버튼을 제공한다.
     *
     * @return 고정 일정 입력 패널
     */
    private JPanel createInputPanel() {
        JPanel container = new JPanel(new BorderLayout(10, 10));

        //제목 달기(고정 일정 등록)
        JLabel title = new JLabel( "고정 일정 등록", SwingConstants.CENTER);
        title.setFont( title.getFont().deriveFont( Font.BOLD, 22f));
        container.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel( new GridLayout(0, 2, 10, 15));
        nameField = new JTextField();
        dateField = new JTextField();
        startTimeField = new JTextField();
        endTimeField = new JTextField();
        repeatTypeBox = new JComboBox<>(RepeatType.values());

        formPanel.add(new JLabel("일정 이름"));
        formPanel.add(nameField);

        formPanel.add( new JLabel("날짜 (yyyy-MM-dd)"));
        formPanel.add(dateField);

        formPanel.add( new JLabel("시작 시간 (HH:mm)"));
        formPanel.add(startTimeField);

        formPanel.add( new JLabel("종료 시간 (HH:mm)"));
        formPanel.add(endTimeField);

        formPanel.add( new JLabel("반복 유형"));
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

        container.add( formPanel, BorderLayout.CENTER);

        // 버튼
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("등록");
        JButton updateButton = new JButton("수정");
        JButton clearButton = new JButton("입력 초기화");

        addButton.addActionListener( e -> addSchedule());
        updateButton.addActionListener( e -> updateSchedule());
        clearButton.addActionListener( e -> clearFields());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);

        container.add( buttonPanel, BorderLayout.SOUTH);

        return container;
    }

    /**
     * 등록된 고정 일정의 목록을 표시하는 패널을 생성한다.
     *
     * 각 고정 일정의 이름, 시작 시간, 종료 시간,
     * 반복 유형을 목록으로 표시하며,
     * 선택한 일정의 정보를 입력 영역에 다시 표시한다.
     * 또한 선택한 일정을 삭제할 수 있는 버튼을 제공한다.
     *
     * @return 고정 일정 현황 패널
     */
    private JPanel createScheduleListPanel() {

        JPanel panel = new JPanel( new BorderLayout(10, 10));
        panel.setPreferredSize( new Dimension(430, 0));
        JLabel title = new JLabel( "고정 일정 현황", SwingConstants.CENTER);
        title.setFont( title.getFont().deriveFont( Font.BOLD, 18f));
        panel.add( title, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();   //실제 데이터 저장
        scheduleList = new JList<>(listModel);  //데이터를 화면에 보여줌
        scheduleList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);    //한번에 일정 하나만 선택하도록
        scheduleList.setCellRenderer( 
            new DefaultListCellRenderer() {
                /**
                 * FixedSchedule 객체를 목록에 표시할 문자열 형태로 변환한다.
                 *
                 * 일정 이름, 시작 시간, 종료 시간,
                 * 반복 유형을 하나의 문자열로 구성하여 표시한다.
                 *
                 * @param list 일정 목록 컴포넌트
                 * @param value 현재 표시할 FixedSchedule 객체
                 * @param index 현재 항목의 인덱스
                 * @param isSelected 현재 항목의 선택 여부
                 * @param cellHasFocus 현재 항목의 포커스 여부
                 * @return 화면에 표시할 컴포넌트
                 */
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    FixedSchedule schedule = (FixedSchedule) value;
                    setText(schedule.getName() + " | " + schedule.getStartTime() + " ~ " + schedule.getEndTime() + " | " + schedule.getRepeatType());
                    return this;
                }
            }
        );

        // 선택한 일정을 입력칸에 표시
        scheduleList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;    //선택이 완전히 끝났을 때만 처리


            FixedSchedule schedule = scheduleList.getSelectedValue();
            if (schedule == null) return;

            nameField.setText(schedule.getName());
            dateField.setText(schedule.getDate().toString());
            startTimeField.setText(schedule.getStartTime().toString());
            endTimeField.setText(schedule.getEndTime().toString());
            repeatTypeBox.setSelectedItem(schedule.getRepeatType());

            setRepeatDays(schedule.getRepeatDays());
        });

        panel.add(new JScrollPane(scheduleList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("삭제");

        deleteButton.addActionListener(e -> deleteSchedule());
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * 입력된 정보를 이용해 새로운 고정 일정을 등록한다.
     *
     * 입력값을 FixedSchedule 객체로 변환한 뒤,
     * 기존 고정 일정과 시간이 충돌하지 않는 경우
     * ScheduleManager에 등록한다.
     *
     * 등록이 완료되면 일정 목록과 입력 필드를 갱신한다.
     */
    private void addSchedule() {
        try {
            FixedSchedule schedule = createScheduleFromInput();
            if (scheduleManager.checkConflict(schedule)) {
                JOptionPane.showMessageDialog( this, "기존 고정 일정과 시간이 충돌합니다.");
                return;
            }
            scheduleManager.addFixedSchedule(schedule);

            refreshList();
            clearFields();

            JOptionPane.showMessageDialog(this,"고정 일정이 등록되었습니다.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"입력값을 확인하세요.\n" + "날짜: yyyy-MM-dd\n" + "시간: HH:mm");
        }
    }

    /**
     * 선택된 고정 일정을 입력된 새로운 정보로 수정한다.
     *
     * 수정할 일정이 선택되지 않은 경우 안내 메시지를 표시하며,
     * 수정된 일정이 다른 고정 일정과 충돌하는 경우 수정하지 않는다.
     *
     * 수정이 성공하면 일정 목록과 입력 필드를 갱신한다.
     */
    private void updateSchedule() {

        FixedSchedule oldSchedule = scheduleList.getSelectedValue();

        if (oldSchedule == null) {
            JOptionPane.showMessageDialog(this, "수정할 일정을 선택하세요.");
            return;
        }
        try {
            FixedSchedule newSchedule = createScheduleFromInput();
            boolean success = scheduleManager.updateFixedSchedule(oldSchedule,newSchedule);
            if (!success) {
                JOptionPane.showMessageDialog(this, "다른 고정 일정과 충돌하여 수정할 수 없습니다.");
                return;
            }

            refreshList();
            clearFields();
            JOptionPane.showMessageDialog(this, "고정 일정이 수정되었습니다.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "입력값을 확인하세요.");
        }
    }

    /**
     * 현재 목록에서 선택된 고정 일정을 삭제한다.
     *
     * 일정이 선택되지 않은 경우 안내 메시지를 표시하며,
     * 삭제가 완료되면 일정 목록과 입력 필드를 갱신한다.
     */
    private void deleteSchedule() {
        FixedSchedule schedule = scheduleList.getSelectedValue();
        if (schedule == null) {
            JOptionPane.showMessageDialog(this, "삭제할 일정을 선택하세요.");
            return;
        }
        scheduleManager.removeFixedSchedule(schedule);
        refreshList();
        clearFields();
    }

    /**
     * 입력 필드에 작성된 정보를 읽어 FixedSchedule 객체를 생성한다.
     *
     * 반복 유형이 NONE인 경우 날짜 입력이 필요하며,
     * DAILY 또는 WEEKLY인 경우 날짜가 입력되지 않았다면
     * 현재 날짜를 기본값으로 사용한다.
     *
     * WEEKLY 반복 유형의 경우 하나 이상의 반복 요일이
     * 반드시 선택되어야 한다.
     *
     * @return 입력값을 기반으로 생성된 FixedSchedule 객체
     * @throws IllegalArgumentException 일정 이름이 비어 있거나,
     *         시작 시간이 종료 시간보다 늦거나 같은 경우,
     *         또는 WEEKLY 유형에서 반복 요일을 선택하지 않은 경우
     */
    private FixedSchedule createScheduleFromInput() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) throw new IllegalArgumentException();

        RepeatType repeatType = (RepeatType) repeatTypeBox.getSelectedItem();
        LocalDate date;

        // NONE이면 실제 날짜 필요
        if (repeatType == RepeatType.NONE) date = LocalDate.parse(dateField.getText().trim());
        

        // DAILY/WEEKLY는 날짜가 실제 판정에 사용되지 않으므로
        // 비어있으면 오늘 날짜 사용
        else {
            if (dateField.getText().trim().isEmpty()) {
                date = LocalDate.now();
            } else {
                date = LocalDate.parse(dateField.getText().trim());
            }
        }

        LocalTime startTime = LocalTime.parse(startTimeField.getText().trim());
        LocalTime endTime = LocalTime.parse(endTimeField.getText().trim());
        if (!startTime.isBefore(endTime)) throw new IllegalArgumentException();
        

        Set<DayOfWeek> repeatDays = getSelectedRepeatDays();

        // WEEKLY인데 요일을 하나도 선택하지 않은 경우
        if (repeatType == RepeatType.WEEKLY&& repeatDays.isEmpty()) throw new IllegalArgumentException();
        
        return new FixedSchedule( name, date, startTime, endTime, repeatType, repeatDays);
    }

    /**
     * 반복 요일 체크박스에서 현재 선택된 요일을 수집한다.
     *
     * 선택된 체크박스에 해당하는 DayOfWeek 값을
     * 하나의 집합으로 생성하여 반환한다.
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
     * 주어진 반복 요일 정보를 이용해
     * 요일 체크박스의 선택 상태를 설정한다.
     *
     * 등록된 고정 일정을 목록에서 선택했을 때
     * 해당 일정의 반복 요일을 입력 화면에 다시 표시하는 데 사용된다.
     *
     * @param days 체크박스에 표시할 반복 요일의 집합
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
     * ScheduleManager에 저장된 고정 일정 정보를 불러와
     * 화면의 고정 일정 목록을 최신 상태로 갱신한다.
     */
    public void refreshList() {
        listModel.clear();

        for (FixedSchedule schedule : scheduleManager.getFixedSchedule()) {
            listModel.addElement(schedule);
        }
    }


    /**
     * 고정 일정 입력 필드와 반복 설정을 초기 상태로 되돌린다.
     *
     * 일정 이름, 날짜, 시작 및 종료 시간을 비우고,
     * 반복 유형을 NONE으로 설정하며,
     * 모든 반복 요일 체크박스와 일정 목록 선택을 해제한다.
     */
    private void clearFields() {

        nameField.setText("");
        dateField.setText("");
        startTimeField.setText("");
        endTimeField.setText("");

        repeatTypeBox.setSelectedItem( RepeatType.NONE);

        mondayBox.setSelected(false);
        tuesdayBox.setSelected(false);
        wednesdayBox.setSelected(false);
        thursdayBox.setSelected(false);
        fridayBox.setSelected(false);
        saturdayBox.setSelected(false);
        sundayBox.setSelected(false);

        scheduleList.clearSelection();
    }
}