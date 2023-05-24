import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends Entity {
    private GamePanel gamePanel;
    private KeyHandler keyHandler;

    private BufferedImage currentPlayerFrame;
    private BufferedImage w1, w2, w3, w4, w5, w6, w7, w8;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) throws IOException {
        this.gamePanel = gamePanel; this.keyHandler = keyHandler;
        this.x = 100; this.y = 100; this.speed = 4;
        w1 = ImageIO.read(new File("src/PlayerSprites/EnchantressWalk1.png"));
        w2 = ImageIO.read(new File("src/PlayerSprites/EnchantressWalk2.png"));
        w3 = ImageIO.read(new File("src/PlayerSprites/EnchantressWalk3.png"));
        w4 = ImageIO.read(new File("src/PlayerSprites/EnchantressWalk4.png"));
        w5 = ImageIO.read(new File("src/PlayerSprites/EnchantressWalk5.png"));
        w6 = ImageIO.read(new File("src/PlayerSprites/EnchantressWalk6.png"));
        w7 = ImageIO.read(new File("src/PlayerSprites/EnchantressWalk7.png"));
        w8 = ImageIO.read(new File("src/PlayerSprites/EnchantressWalk8.png"));

        currentPlayerFrame = w1;
    }

    private int rightAnimNum = 2;
    public void update() {
        if (keyHandler.upPressed) this.y -= this.speed;
        else if (keyHandler.downPressed) this.y += this.speed;
        else if (keyHandler.leftPressed) this.x -= this.speed;
        else if (keyHandler.rightPressed) {
            if (rightAnimNum > 4) rightAnimNum = 1;
            switch (rightAnimNum) {
                case 1 -> {currentPlayerFrame = w1;}
                case 2 -> {currentPlayerFrame = w2;}
                case 3 -> {currentPlayerFrame = w3;}
                case 4 -> {currentPlayerFrame = w4;}
            }
            rightAnimNum++;
            this.x += this.speed;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.drawImage(currentPlayerFrame, this.x, this.y, null);
        //g2.fillRect(this.x, this.y, gamePanel.tileSize, gamePanel.tileSize);
    }
}
