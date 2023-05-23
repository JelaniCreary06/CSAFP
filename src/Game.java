import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements Runnable {
    final int originalTileSize = 16, scale = 4, FPS = 60;
    final int tileSize = originalTileSize * scale, maxScreenCol = 16, maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol, screenHeight = tileSize * maxScreenRow;

    private static volatile boolean gameRunning;

    private KeyHandler keyHandler = new KeyHandler();
    private volatile int playerX = 100, playerY = 100, playerSpeed = 4;
    public Game() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    @Override
    public void run() {
        gameRunning = true;

        double drawInterval = 1000000000/FPS, nextDrawInterval = System.nanoTime() + drawInterval;
        Thread currentGameThread = new Thread(() -> {
            while (gameRunning) {
                System.out.println("Game running");
                update();
                repaint();

                double remainingTime = nextDrawInterval - System.nanoTime();
                try {
                    Thread.sleep((long) remainingTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        currentGameThread.start();
    }

    public void update() {
        if (keyHandler.upPressed) playerY -= playerSpeed;
        else if (keyHandler.downPressed) playerY += playerSpeed;
        else if (keyHandler.leftPressed) playerX -= playerSpeed;
        else if (keyHandler.rightPressed) playerX += playerSpeed;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.white);
        g2.fillRect(playerX, playerY, tileSize, tileSize);
        g2.dispose();
    }
}
