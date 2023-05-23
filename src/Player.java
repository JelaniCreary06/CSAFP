import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private BufferedImage image;

    private JFrame frame;

    public Player(JFrame frame) throws IOException {
        image = ImageIO.read(new File("src/PlayerSprites/EnchantressWalk1.png"));
        this.frame = frame;

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };

        frame.add(panel);

    }
}
