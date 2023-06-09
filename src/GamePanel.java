import javax.print.attribute.standard.MediaSize;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

public class GamePanel extends JPanel implements Runnable {
    static final int originalTileSize = 16, scale = 4, FPS = Config.FPS;
    static final int tileSize = originalTileSize * scale, maxScreenCol = 16, maxScreenRow = 12;
    static final int screenWidth = tileSize * maxScreenCol, screenHeight = tileSize * maxScreenRow;

    private Thread gameThread;
    private static volatile boolean gameRunning;

    private KeyHandler keyHandler = new KeyHandler();
    private volatile int playerX = 100, playerY = 100, playerSpeed = 4;

    private Player player;

    private Hashtable<String, OtherPlayers> otherPlayers;

    public GamePanel(Hashtable<String, OtherPlayers> otherPlayers, String character, ClientHandler clientHandler, KeyHandler keyHandler) throws IOException, InterruptedException {
        this.player = new Player(character, clientHandler, this, keyHandler);
        this.otherPlayers = otherPlayers;
        otherPlayers.put("def", new OtherPlayers("Warrior"));
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        new Thread(this).start();
        gameRunning = true;
    }

    public GamePanel(OtherPlayers plr) {
        this.player = plr;
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/60, nextDrawInterval = System.nanoTime() + drawInterval;

        while (gameRunning) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawInterval - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if (remainingTime < 0) remainingTime = 0;
                Thread.sleep((long) remainingTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            nextDrawInterval += drawInterval;

        }
    }

    public void update() {

        if (!(player instanceof OtherPlayers)) player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        otherPlayers.forEach( (k,v) -> {
            g2.drawImage(v.currentCharacterFrame, v.x, v.y, null);
        });
        player.draw(g2);
        g2.dispose();
    }
}
