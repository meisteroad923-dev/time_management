package scheduler.ui;


import javax.swing.*;

import scheduler.service.AutoScheduler;
import scheduler.service.ScheduleManager;

import java.awt.*;

public class MainFrame extends JFrame {
    private ScheduleManager scheduleManager;
    private AutoScheduler autoScheduler;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private SchedulePanel schedulePanel;

    public MainFrame(){
        scheduleManager = new ScheduleManager();
        autoScheduler = new AutoScheduler();

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
/*
    private JPanel createTopPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("Time management scheduler");

        JButton scheduleButton = new JButton("일정표 보기");
        JButton fixedButton = new JButton("고정 일정 등록");
        JButton blockedButton = new JButton("배치 금지 시간 등록");
        JButton flexibleButton = new JButton("유동 일정 등록");

        scheduleButton.addActionListener(e -> {
            schedulePanel.refreshAll();
            cardLayout.show(contentPanel,"SCHEDULE");
        });     
        fixedButton.addActionListener(e -> cardLayout.show(contentPanel, "FIXED"));
        blockedButton.addActionListener(e -> cardLayout.show(contentPanel, "BLOCKED"));
        flexibleButton.addActionListener(e -> cardLayout.show(contentPanel, "FLEXIBLE"));

        panel.add(title);
        panel.add(Box.createHorizontalStrut(30));

        panel.add(scheduleButton);
        panel.add(fixedButton);
        panel.add(blockedButton);
        panel.add(flexibleButton);

        return panel;
    }
}*/

