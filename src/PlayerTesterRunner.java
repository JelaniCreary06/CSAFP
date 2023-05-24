import javax.swing.*;
import java.io.IOException;

public class PlayerTesterRunner {
    public static void main(String[] args) throws IOException {
        JFrame gameWindow = new JFrame();
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setResizable(false);

        GamePanel screen = new GamePanel();
        gameWindow.add(screen);
        gameWindow.pack();

        gameWindow.setLocationRelativeTo(null);
        gameWindow.setVisible(true);

        /*
        JFrame frame = new JFrame();
        Player p = new Player(frame);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

         */
    }
}
