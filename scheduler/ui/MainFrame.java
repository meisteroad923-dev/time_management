package scheduler.ui;

import javax.swing.*;
import java.awt.*;

import scheduler.service.AutoScheduler;
import scheduler.service.ScheduleManager;
import scheduler.service.DataStorage;
import scheduler.service.PrioritySchedulingStrategy;

/**
 * 애플리케이션의 메인 화면을 구성하는 JFrame 클래스이다.
 *
 * 저장된 일정 데이터를 불러오고 자동 배치 서비스를 초기화하며,
 * 일정표, 고정 일정, 배치 금지 시간, 유동 일정 관리 화면을
 * CardLayout을 이용하여 전환할 수 있도록 구성한다.
 */
public class MainFrame extends JFrame {
    private ScheduleManager scheduleManager;
    private AutoScheduler autoScheduler;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private SchedulePanel schedulePanel;

    /**
     * Time Management Scheduler의 메인 화면을 생성한다.
     *
     * 저장된 일정 데이터를 불러오고 기본 자동 배치 전략을 설정한 뒤,
     * 각 일정 관리 패널과 주간 일정표 패널을 초기화한다.
     */
    public MainFrame(){
        scheduleManager = DataStorage.load();
        autoScheduler = new AutoScheduler(new PrioritySchedulingStrategy());        //자동 배치 알고리즘 변경 하려면 여기서

        setTitle("Time management scheduler");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(createSidePanel(), BorderLayout.WEST);

        //화면 전환 영역(각 탭 메인 화면)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        schedulePanel = new SchedulePanel(scheduleManager,autoScheduler);

        contentPanel.add(schedulePanel, "SCHEDULE");
        contentPanel.add(new FixedSchedulePanel(scheduleManager), "FIXED");
        contentPanel.add(new BlockedTimePanel(scheduleManager), "BLOCKED");
        contentPanel.add(new FlexibleSchedulePanel(scheduleManager), "FLEXIBLE");

        add(contentPanel, BorderLayout.CENTER);
        cardLayout.show(contentPanel, "SCHEDULE");
    }

    /**
     * 화면 왼쪽에 표시되는 메뉴 패널을 생성한다.
     *
     * 일정표 보기, 고정 일정 등록, 배치 금지 시간 등록,
     * 유동 일정 등록 화면으로 이동할 수 있는 버튼을 제공한다.
     *
     * @return 왼쪽 메뉴 패널
     */
private JPanel createSidePanel() {
    JPanel panel = new JPanel();

    // 버튼을 위에서 아래 방향으로 배치
    panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
    panel.setPreferredSize(new Dimension(200, 0));
    
    JLabel title = new JLabel("Time management");
    title.setFont(title.getFont().deriveFont(Font.BOLD,18f));
    title.setAlignmentX(Component.CENTER_ALIGNMENT);


    JButton scheduleButton = new JButton("일정표 보기");
    JButton fixedButton = new JButton("고정 일정 등록");
    JButton blockedButton = new JButton("배치 금지 시간 등록");
    JButton flexibleButton = new JButton("유동 일정 등록");

    // 버튼 크기와 정렬 통일
    Dimension buttonSize = new Dimension(180, 40);

    scheduleButton.setMaximumSize(buttonSize);
    fixedButton.setMaximumSize(buttonSize);
    blockedButton.setMaximumSize(buttonSize);
    flexibleButton.setMaximumSize(buttonSize);

    scheduleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    fixedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    blockedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    flexibleButton.setAlignmentX(Component.CENTER_ALIGNMENT);

    // 일정표 화면으로 이동할 때 최신 데이터로 갱신
    scheduleButton.addActionListener(e -> {
        schedulePanel.refreshAll(); 
        cardLayout.show(contentPanel,"SCHEDULE");
    });
    fixedButton.addActionListener(e ->cardLayout.show(contentPanel, "FIXED"));
    blockedButton.addActionListener(e ->cardLayout.show(contentPanel,"BLOCKED"));
    flexibleButton.addActionListener(e ->cardLayout.show(contentPanel,"FLEXIBLE"));


    panel.add(Box.createVerticalStrut(20));
    panel.add(title);
    panel.add(Box.createVerticalStrut(30));
    panel.add(scheduleButton);
    panel.add(Box.createVerticalStrut(10));
    panel.add(fixedButton);
    panel.add(Box.createVerticalStrut(10));
    panel.add(blockedButton);
    panel.add(Box.createVerticalStrut(10));
    panel.add(flexibleButton);

    return panel;
    }
}


