import javax.swing.SwingUtilities;
import scheduler.ui.MainFrame;

/**
 * Time Management Scheduler 애플리케이션의 시작 클래스이다.
 *
 * Swing 이벤트 디스패치 스레드에서 MainFrame을 생성하고
 * 애플리케이션의 메인 화면을 표시한다.
 */
public class Main {
    /**
     * 애플리케이션을 실행한다.
     *
     * SwingUtilities.invokeLater()를 이용하여
     * Swing UI를 이벤트 디스패치 스레드에서 생성한다.
     *
     * @param args 명령행 인자
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });

    }
}