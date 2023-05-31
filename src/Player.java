import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.KeyEvent;

public class Player extends Entity {
    private GamePanel gamePanel;
    private KeyHandler keyHandler;

    private BufferedImage currentCharacterFrame;
    private BufferedImage[][] rightSideFrames, leftSideFrames, upSideFrames, downSideFrames;

    private String character = "Warrior", direction;

    int rightAnimLength, leftAnimLength, downAnimLength, upAnimLength;

    public Player(GamePanel gamePanel) throws IOException, InterruptedException {
        this.gamePanel = gamePanel;
        this.x = 100; this.y = 100; this.speed = 5;

        loadCharacterFrames(character);

        System.out.println("All frames loaded.");

        rightAnimLength = rightSideFrames[0].length;
        leftAnimLength = leftSideFrames[0].length;
        downAnimLength = downSideFrames[0].length;
        upAnimLength = upSideFrames[0].length;

        direction = Config.DOWN;
    }

    public void updateDirection(String keyPressed) {
        int keyNum = Integer.parseInt(keyPressed);
        switch (keyNum) {
            case KeyEvent.VK_W -> { direction = Config.UP; }
            case KeyEvent.VK_A -> { direction = Config.LEFT; }
            case KeyEvent.VK_S -> { direction = Config.DOWN; }
            case KeyEvent.VK_D -> { direction = Config.RIGHT; }
        }
    }
    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.drawImage(currentCharacterFrame, this.x, this.y, null);
        //g2.fillRect(this.x, this.y, gamePanel.tileSize, gamePanel.tileSize);
    }

    int idleFrameToLoad = 0, rightWalkFrame = 0, leftWalkFrame = 0, downWalkFrame = 0, upWalkFrame = 0;

    int idleConsec = 0, rightWalkConsec = 0, leftWalkConsec, downWalkConsec = 0, upWalkConsec = 0;


    public void update() {


        if (direction.equals(Config.UP)) {
            this.y -= this.speed; idleFrameToLoad = 0;

            if (upWalkConsec == 5) {
                upWalkConsec = 0;
                upWalkFrame++;
                if (upWalkFrame >= upAnimLength) upWalkFrame = 0;
                if (upSideFrames[1][upWalkFrame] == null) upWalkFrame = 0;
            }
            currentCharacterFrame = upSideFrames[1][upWalkFrame];
            upWalkConsec++;
        }
        else if (direction.equals(Config.DOWN))  {
            this.y += this.speed; idleFrameToLoad = 0;

            if (downWalkConsec == 5) {
                downWalkConsec = 0;
                downWalkFrame++;
                if (downWalkFrame >= downAnimLength) downWalkFrame = 0;
                if (downSideFrames[1][downWalkFrame] == null) downWalkFrame = 0;
            }
            currentCharacterFrame = downSideFrames[1][downWalkFrame];
            downWalkConsec++;
        }
        else if (direction.equals(Config.LEFT)) {
            this.x -= this.speed; idleFrameToLoad = 0;

            if (leftWalkConsec == 5) {
                leftWalkConsec = 0;
                leftWalkFrame++;
                if (leftWalkFrame >= leftAnimLength) leftWalkFrame = 0;
                if (leftSideFrames[1][leftWalkFrame] == null) leftWalkFrame = 0;
            }
            currentCharacterFrame = leftSideFrames[1][leftWalkFrame];
            leftWalkConsec++;
        }
        else if (direction.equals(Config.RIGHT)) {
            this.x += this.speed; idleFrameToLoad = 0;

            if (rightWalkConsec == 5) {
                rightWalkConsec = 0;
                rightWalkFrame++;
                if (rightWalkFrame >= rightAnimLength) rightWalkFrame = 0;
                if (rightSideFrames[1][rightWalkFrame] == null) rightWalkFrame = 0;
            }
            currentCharacterFrame = rightSideFrames[1][rightWalkFrame];
            rightWalkConsec++;
        }
        else {
            if (idleConsec == 12) {
                if (idleFrameToLoad == rightAnimLength) idleFrameToLoad = 0;
                if (keyHandler.direction.equals(Config.RIGHT)) {
                    if (rightSideFrames[0][idleFrameToLoad] == null) idleFrameToLoad = 0;
                    currentCharacterFrame = rightSideFrames[0][idleFrameToLoad];
                }
                if (keyHandler.direction.equals(Config.LEFT)) {
                    if (leftSideFrames[0][idleFrameToLoad] == null) idleFrameToLoad = 0;
                    currentCharacterFrame = leftSideFrames[0][idleFrameToLoad];
                }
                if (keyHandler.direction.equals(Config.DOWN)) {
                    if (downSideFrames[0][idleFrameToLoad] == null) idleFrameToLoad = 0;
                    currentCharacterFrame = downSideFrames[0][idleFrameToLoad];
                }
                if (keyHandler.direction.equals(Config.UP)) {
                    if (upSideFrames[0][idleFrameToLoad] == null) idleFrameToLoad = 0;
                    currentCharacterFrame = upSideFrames[0][idleFrameToLoad];
                }
                idleFrameToLoad++;
                idleConsec = 0;
            }
            idleConsec++;
        }
    }


    public void loadCharacterFrames(String characterName) throws IOException, InterruptedException {
        final String absolutePath = "src/PlayerSprites/" + characterName;

        Runnable loadRightSideFrames = () -> {
            List<BufferedImage> idleFrames, walkFrames;

            try {
                idleFrames = loadFrames(absolutePath, Config.RIGHT, "Idle");
                walkFrames = loadFrames(absolutePath, Config.RIGHT, "Walk");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int size = Math.max(idleFrames.size(), walkFrames.size());
            rightSideFrames = new BufferedImage[2][size];

            for (int i = idleFrames.size() - 1; i > -1; i--) {
                rightSideFrames[0][i] = idleFrames.get(i);
            }

            for (int i = walkFrames.size() - 1; i > -1; i--) {
                rightSideFrames[1][i] = walkFrames.get(i);
            }

            System.out.println("Right side frames loaded.");
        };

        Runnable loadLeftSideFrames = () -> {
            List<BufferedImage> idleFrames, walkFrames;

            try {
                idleFrames = loadFrames(absolutePath, Config.LEFT, "Idle");
                walkFrames = loadFrames(absolutePath, Config.LEFT, "Walk");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int size = Math.max(idleFrames.size(), walkFrames.size());
            leftSideFrames = new BufferedImage[2][size];

            for (int i = idleFrames.size() - 1; i > -1; i--) {
                leftSideFrames[0][i] = idleFrames.get(i);
            }

            for (int i = walkFrames.size() - 1; i > -1; i--) {
                leftSideFrames[1][i] = walkFrames.get(i);
            }

            System.out.println("Left frames loaded.");
        };

        Runnable loadDownFrames = () -> {
            List<BufferedImage> idleFrames, walkFrames;

            try {
                idleFrames = loadFrames(absolutePath, Config.DOWN, "Idle");
                walkFrames = loadFrames(absolutePath, Config.DOWN, "Walk");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int size = Math.max(idleFrames.size(), walkFrames.size());
            downSideFrames = new BufferedImage[2][size];

            for (int i = idleFrames.size() - 1; i > -1; i--) {
                downSideFrames[0][i] = idleFrames.get(i);
            }

            for (int i = walkFrames.size() - 1; i > -1; i--) {
                downSideFrames[1][i] = walkFrames.get(i);
            }

            System.out.println("Down frames loaded.");
        };

        Runnable loadUpFrames = () -> {
            List<BufferedImage> idleFrames, walkFrames;

            try {
                idleFrames = loadFrames(absolutePath, Config.UP, "Idle");
                walkFrames = loadFrames(absolutePath, Config.UP, "Walk");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int size = Math.max(idleFrames.size(), walkFrames.size());
            upSideFrames = new BufferedImage[2][size];

            for (int i = idleFrames.size() - 1; i > -1; i--) {
                upSideFrames[0][i] = idleFrames.get(i);
            }

            for (int i = walkFrames.size() - 1; i > -1; i--) {
                upSideFrames[1][i] = walkFrames.get(i);
            }

            System.out.println("Up frames loaded.");
        };

        Thread loadRightFrames = new Thread(loadRightSideFrames), loadLeftFrames = new Thread(loadLeftSideFrames),
                loadDownFramess = new Thread(loadDownFrames), loadUpFramess = new Thread(loadUpFrames);


        loadRightFrames.start(); loadLeftFrames.start(); loadDownFramess.start(); loadUpFramess.start();

        loadRightFrames.join(); loadLeftFrames.join(); loadDownFramess.join(); loadUpFramess.join();
    }

    public ArrayList<BufferedImage> loadFrames(String absolutePath, String direction, String animToGet) throws IOException {
        List<BufferedImage> framesToLoad = new ArrayList();

        String path = absolutePath + "/" + direction + "/" + animToGet,
                filesToLoad[] = new File(path).list();

        for (String fileName : filesToLoad) framesToLoad.add(ImageIO.read(new File(path + "/" + fileName)));

        System.out.println("Loaded::" + animToGet + "::" + path + " [" + filesToLoad.length + "]");
        return (ArrayList<BufferedImage>) framesToLoad;
    }
}
