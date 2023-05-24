import java.awt.*;

public class Player extends Entity {
    private GamePanel gamePanel;
    private KeyHandler keyHandler;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel; this.keyHandler = keyHandler;
        this.x = 100; this.y = 100; this.speed = 4;
    }

    public void update() {
        if (keyHandler.upPressed) this.y -= this.speed;
        else if (keyHandler.downPressed) this.y += this.speed;
        else if (keyHandler.leftPressed) this.x -= this.speed;
        else if (keyHandler.rightPressed) this.x += this.speed;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.fillRect(this.x, this.y, gamePanel.tileSize, gamePanel.tileSize);
    }
}
