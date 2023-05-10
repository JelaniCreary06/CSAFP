import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

public class ClientSetupForm extends JDialog {
    private JPanel titleBackgroundPanel;
    private JLabel titleLabel;
    private JButton nameInputButton;
    private JTextField nameInputField;
    private JPanel extraPanel1;
    private JPanel extraPanel2;
    private JPanel extraPanel3;
    private JPanel extraPanel4;
    private JPanel extraPanel5;

    public ClientSetupForm(String clientName[]) {

        this.titleLabel.setText(Config.Game.name);
        this.nameInputField.setText(Config.Game.nameHolderText);
        titleBackgroundPanel.setMinimumSize(new Dimension(500, 500));
        this.add(titleBackgroundPanel);
        this.setSize(500, 500);

        EventListener nameInputted = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(nameInputButton)) {
                    String name = nameInputField.getText();
                    if (!name.equals(Config.Game.nameHolderText))
                        clientName[0] = nameInputField.getText();
                }
            }
        };

        this.nameInputButton.addActionListener((ActionListener) nameInputted);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
