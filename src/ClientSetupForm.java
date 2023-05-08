import javax.swing.*;
import java.awt.*;

public class ClientSetupForm extends JFrame {
    private JPanel titleBackgroundPanel;
    private JLabel titleLabel;
    private JButton nameInputButton;
    private JTextField nameInputField;
    private JPanel extraPanel1;
    private JPanel extraPanel2;
    private JPanel extraPanel3;
    private JPanel extraPanel4;
    private JPanel extraPanel5;

    public ClientSetupForm() {
        this.titleLabel.setText(Config.MainGame.name);

        titleBackgroundPanel.setMinimumSize(new Dimension(500, 500));
        this.add(titleBackgroundPanel);

        this.setSize(500, 500);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
