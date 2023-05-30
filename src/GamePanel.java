import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable {
    static final int originalTileSize = 16, scale = 3, FPS = Config.FPS;
    public static final int tileSize = originalTileSize * scale, maxScreenCol = 16, maxScreenRow = 12;
    static final int screenWidth = tileSize * maxScreenCol, screenHeight = tileSize * maxScreenRow;

    private Thread gameThread;
    private static volatile boolean gameRunning;

    private KeyHandler keyHandler = new KeyHandler();
    private volatile int playerX = 100, playerY = 100, playerSpeed = 4;

    private Player player = new Player(this, keyHandler);
    TileManager tileManager = new TileManager(this);

    public GamePanel() throws IOException, InterruptedException {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        new Thread(this).start();
        gameRunning = true;
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
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        tileManager.draw(g2);
        player.draw(g2);
        g2.dispose();
    }

    public int getTileSize() { return tileSize; }
}
