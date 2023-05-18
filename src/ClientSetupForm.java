import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

public class ClientSetupForm extends JDialog {
    private JPanel titleBackgroundPanel;
    private JLabel titleLabel;
    private JButton nameInputButton ;
    private JTextField nameInputField;
    private JPanel extraPanel1;
    private JPanel extraPanel2;
    private JPanel extraPanel3;
    private JPanel extraPanel4;
    private JPanel extraPanel5;

    public ClientSetupForm(String clientName[]) {
        String nameHolderText = "Enter name here:";

        this.titleLabel.setText(Config.GAME_NAME);
        this.nameInputField.setText(nameHolderText);
        titleBackgroundPanel.setMinimumSize(new Dimension(500, 500));
        this.add(titleBackgroundPanel);
        this.setSize(500, 500);

        EventListener nameInputted = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(nameInputButton)) {
                    String name = nameInputField.getText();
                    if (!name.equals(nameHolderText)) {
                        clientName[0] = nameInputField.getText();
                        System.out.println("Name entered: " + clientName[0]);
                    }
                }
            }
        };

        this.nameInputButton.addActionListener((ActionListener) nameInputted);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
