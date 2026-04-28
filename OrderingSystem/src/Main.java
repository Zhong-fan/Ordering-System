import ui.LoginFrame;
import ui.MainFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            MainFrame mainFrame = new MainFrame();

            new LoginFrame(mainFrame);
        });
    }
}
    