import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PlayerTesterRunner {
    public static void main(String[] args) throws IOException {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);


        JFrame frame = new JFrame();
        Player p = new Player(frame);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}
