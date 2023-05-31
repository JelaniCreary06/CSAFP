import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class GamePanel extends JPanel implements Runnable {
    static final int originalTileSize = 16, scale = 3, FPS = Config.FPS;
    public static final int tileSize = originalTileSize * scale, maxScreenCol = 16, maxScreenRow = 12;
    static final int screenWidth = tileSize * maxScreenCol, screenHeight = tileSize * maxScreenRow;

    private Thread gameThread;
    private static volatile boolean gameRunning;

    private volatile int playerX = 100, playerY = 100, playerSpeed = 4;

    private Hashtable<Socket, Player> playerHashtable;
    private ArrayList<Player> playerArrayList;
    TileManager tileManager = new TileManager(this);

    public GamePanel(Hashtable<Socket, Player> playerHashtable, ArrayList<Player> playerArrayList) throws IOException, InterruptedException {
        this.playerHashtable = playerHashtable;
        this.playerArrayList = playerArrayList;

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

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
        for (Player plr : playerArrayList) plr.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for (Player plr : playerArrayList) plr.draw(g2);
        g2.dispose();
    }

    public int getTileSize() { return tileSize; }
}
