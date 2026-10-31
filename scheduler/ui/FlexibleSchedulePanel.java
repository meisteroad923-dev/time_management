package scheduler.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import scheduler.model.FlexibleSchedule;
import scheduler.service.ScheduleManager;

/**
 * 유동 일정의 등록, 수정, 삭제 및 완료 여부 변경 기능을 제공하는 Swing 패널이다.
 *
 * 사용자는 일정 이름, 예상 소요 시간, 마감일, 중요도를 입력할 수 있으며,
 * 등록된 일정은 우측 목록에서 확인하고 관리할 수 있다.
 */
public class FlexibleSchedulePanel extends JPanel {
    private ScheduleManager scheduleManager;

    private JTextField nameField;
    private JTextField durationField;
    private JTextField deadlineField;

    private JComboBox<Integer> importanceBox;

    private DefaultListModel<FlexibleSchedule> listModel;
    private JList<FlexibleSchedule> scheduleList;

    /**
     * 유동 일정 관리 화면을 생성한다.
     *
     * @param scheduleManager 유동 일정의 등록, 수정, 삭제 및
     *                        완료 상태 변경을 처리하는 일정 관리자
     */
    public FlexibleSchedulePanel(ScheduleManager scheduleManager) {

        this.scheduleManager = scheduleManager;

        setLayout(new BorderLayout(20, 20));

        add(createInputPanel(), BorderLayout.CENTER);
        add(createScheduleListPanel(), BorderLayout.EAST);

        refreshList();
        clearFields();
    }

    /**
     * 유동 일정의 정보를 입력하는 패널을 생성한다.
     *
     * 일정 이름, 예상 소요 시간, 마감일, 중요도를 입력할 수 있으며
     * 등록, 수정, 입력 초기화 버튼을 제공한다.
     *
     * @return 유동 일정 입력 패널
     */
    private JPanel createInputPanel() {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        JLabel title = new JLabel( "유동 일정 등록", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont( Font.BOLD, 22f));
        container.add( title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 15));

        nameField = new JTextField();
        durationField = new JTextField();
        deadlineField = new JTextField();

        importanceBox = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5 });

        formPanel.add(new JLabel("일정 이름"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("예상 소요 시간(분)"));
        formPanel.add(durationField);

        formPanel.add(new JLabel("마감일 (yyyy-MM-dd)"));
        formPanel.add(deadlineField);

        formPanel.add(new JLabel("중요도"));
        formPanel.add(importanceBox);

        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.add(formPanel, BorderLayout.NORTH);
        container.add(formWrapper, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("등록");
        JButton updateButton = new JButton("수정");
        JButton clearButton = new JButton("입력 초기화");

        addButton.addActionListener(e -> addSchedule());
        updateButton.addActionListener(e -> updateSchedule());
        clearButton.addActionListener(e -> clearFields());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);

        container.add( buttonPanel, BorderLayout.SOUTH);

        return container;
    }

    /**
     * 등록된 유동 일정 목록을 표시하는 패널을 생성한다.
     *
     * 목록에서 일정을 선택하면 해당 일정 정보가 입력 필드에 표시되며,
     * 완료 여부 변경 및 삭제 기능을 제공한다.
     *
     * @return 유동 일정 현황 패널
     */
    private JPanel createScheduleListPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(430, 0));

        JLabel title = new JLabel("유동 일정 현황",SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        panel.add(title, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        scheduleList = new JList<>(listModel);
        scheduleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scheduleList.setCellRenderer(new DefaultListCellRenderer() {
                    /**
                     * FlexibleSchedule 객체를 목록에 표시할 문자열 형태로 변환한다.
                     *
                     * @param list         목록 컴포넌트
                     * @param value        표시할 FlexibleSchedule 객체
                     * @param index        항목 인덱스
                     * @param isSelected   선택 여부
                     * @param cellHasFocus 포커스 여부
                     * @return 화면에 표시할 컴포넌트
                     */
                    @Override
                    public Component getListCellRendererComponent(JList<?> list,Object value,int index,boolean isSelected,boolean cellHasFocus) {
                        super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus);
                        FlexibleSchedule schedule = (FlexibleSchedule) value;
                        String completed = schedule.isCompleted()? "완료": "미완료";

                        setText(schedule.getName() + " | " + schedule.getEstimatedMinutes() + "분 | 마감 " + schedule.getDeadline() + " | 중요도 " + schedule.getImportance() + " | " + completed);
                        return this;
                    }
                });

        // 선택한 유동 일정을 입력칸에 표시
        scheduleList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            FlexibleSchedule schedule = scheduleList.getSelectedValue();
            if (schedule == null) return;

            nameField.setText(schedule.getName());
            durationField.setText(String.valueOf(schedule.getEstimatedMinutes()));
            deadlineField.setText(schedule.getDeadline().toString());
            importanceBox.setSelectedItem(schedule.getImportance());
        });

        panel.add(new JScrollPane(scheduleList),BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton completionButton = new JButton("완료 전환");
        JButton deleteButton = new JButton("삭제");

        completionButton.addActionListener(e -> toggleCompletion());
        deleteButton.addActionListener(e -> deleteSchedule());
        buttonPanel.add(completionButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * 입력된 정보를 이용하여 새로운 유동 일정을 등록한다.
     *
     * 예상 소요 시간은 ScheduleManager의 보정 규칙에 따라
     * 30분 단위로 조정되어 저장된다.
     */
    private void addSchedule(){
        try {
            FlexibleSchedule schedule = createScheduleFromInput(false);
            scheduleManager.addFlexibleSchedule(schedule);

            refreshList();
            clearFields();
            JOptionPane.showMessageDialog( this, "유동 일정이 등록되었습니다.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog( this, "입력값을 확인하세요.\n" + "마감일: yyyy-MM-dd");
        }
    }

    /**
     * 선택된 유동 일정을 입력된 새로운 정보로 수정한다.
     *
     * 기존 일정의 완료 상태는 유지된다.
     */
    private void updateSchedule() {
        FlexibleSchedule oldSchedule = scheduleList.getSelectedValue();
        if (oldSchedule == null) {
            JOptionPane.showMessageDialog(this, "수정할 일정을 선택하세요.");
            return;
        }

        try {
            FlexibleSchedule newSchedule = createScheduleFromInput(oldSchedule.isCompleted());
            boolean success = scheduleManager.updateFlexibleSchedule(oldSchedule, newSchedule);
            if (!success) {
                JOptionPane.showMessageDialog(this, "일정을 수정할 수 없습니다.");
                return;
            }
            refreshList();
            clearFields();
            JOptionPane.showMessageDialog(this, "유동 일정이 수정되었습니다.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "입력값을 확인하세요.");
        }
    }

    /**
     * 목록에서 선택된 유동 일정을 삭제한다.
     */
    private void deleteSchedule() {
        FlexibleSchedule schedule = scheduleList.getSelectedValue();
        if (schedule == null) {
            JOptionPane.showMessageDialog(this, "삭제할 일정을 선택하세요.");
            return;
        }

        scheduleManager.removeFlexibleSchedule(schedule);
        refreshList();
        clearFields();
    }

    /**
     * 선택된 유동 일정의 완료 여부를 반대로 변경한다.
     *
     * 완료된 일정은 이후 자동배치 대상에서 제외된다.
     */
    private void toggleCompletion() {
        FlexibleSchedule schedule = scheduleList.getSelectedValue();
        if (schedule == null) {
            JOptionPane.showMessageDialog(this, "완료 상태를 변경할 일정을 선택하세요.");
            return;
        }

        scheduleManager.updateCompletionStatus(schedule);
        refreshList();
    }

    /**
     * 입력 필드에 작성된 값을 읽어 FlexibleSchedule 객체를 생성한다.
     *
     * @param completed 생성할 일정의 완료 상태
     * @return 입력값을 기반으로 생성된 FlexibleSchedule 객체
     * @throws IllegalArgumentException 입력값이 올바르지 않은 경우
     */
    private FlexibleSchedule createScheduleFromInput(boolean completed) {
        String name = nameField.getText().trim();
        if (name.isEmpty()) throw new IllegalArgumentException();

        int duration = Integer.parseInt(durationField.getText().trim());
        if (duration <= 0) throw new IllegalArgumentException();
        
        LocalDate deadline = LocalDate.parse(deadlineField.getText().trim());
        int importance = (Integer) importanceBox.getSelectedItem();

        return new FlexibleSchedule(name, duration, deadline, importance, completed);
    }

    /**
     * ScheduleManager에 저장된 유동 일정 목록을
     * 화면에 최신 상태로 표시한다.
     */
    public void refreshList() {
        listModel.clear();
        for (FlexibleSchedule schedule : scheduleManager.getFlexibleSchedule()) {
            listModel.addElement(schedule);
        }
    }

    /**
     * 유동 일정 입력 필드와 목록 선택 상태를 초기화한다.
     */
    private void clearFields() {
        nameField.setText("");
        durationField.setText("");
        deadlineField.setText(LocalDate.now().toString());

        importanceBox.setSelectedIndex(0);

        scheduleList.clearSelection();
    }
}