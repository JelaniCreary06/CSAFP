import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player extends Entity {
    protected GamePanel gamePanel;
    private KeyHandler keyHandler;

    protected BufferedImage currentCharacterFrame;
    protected BufferedImage[][] rightSideFrames, leftSideFrames, upSideFrames, downSideFrames;

    private String character;


    int rightAnimLength, leftAnimLength, downAnimLength, upAnimLength;

    int[] consecValueArray = { 0, 0, 0, 0}, animTrackerArray = { 0, 0, 0, 0}, lengthArray;

    private ClientHandler clientHandler;

    public Player(String character) throws IOException, InterruptedException {
        this.character = character;

        loadCharacterFrames(character);

    }
    public Player(String character, ClientHandler clientHandler, GamePanel gamePanel, KeyHandler keyHandler) throws IOException, InterruptedException {
        this.gamePanel = gamePanel; this.keyHandler = keyHandler; this.clientHandler = clientHandler;
        this.x = 100; this.y = 100; this.speed = 5;
        this.character = character;

        loadCharacterFrames(character);

        System.out.println("All frames loaded.");

        lengthArray = new int[] { rightSideFrames[0].length, leftSideFrames[0].length,
                downSideFrames[0].length, upSideFrames[0].length};

        rightAnimLength = rightSideFrames[0].length;
        leftAnimLength = leftSideFrames[0].length;
        downAnimLength = downSideFrames[0].length;
        upAnimLength = upSideFrames[0].length;
    }


    int idleFrameToLoad = 0, rightWalkFrame = 0, leftWalkFrame = 0, downWalkFrame = 0, upWalkFrame = 0;

    int idleConsec = 0, rightWalkConsec = 0, leftWalkConsec, downWalkConsec = 0, upWalkConsec = 0;

    public void update() {
        int currentFrameNum = 0;

        if (keyHandler.upPressed) {
            this.y -= this.speed; idleFrameToLoad = 0;

            if (upWalkConsec == 5) {
                upWalkConsec = 0;
                upWalkFrame++;
                if (upWalkFrame >= upAnimLength) upWalkFrame = 0;
                if (upSideFrames[1][upWalkFrame] == null) upWalkFrame = 0;
            }
            currentCharacterFrame = upSideFrames[1][upWalkFrame];
            upWalkConsec++; currentFrameNum = upWalkFrame;
        }
        else if (keyHandler.downPressed)  {
            this.y += this.speed; idleFrameToLoad = 0;

            if (downWalkConsec == 5) {
                downWalkConsec = 0;
                downWalkFrame++;
                if (downWalkFrame >= downAnimLength) downWalkFrame = 0;
                if (downSideFrames[1][downWalkFrame] == null) downWalkFrame = 0;
            }
            currentCharacterFrame = downSideFrames[1][downWalkFrame];
            downWalkConsec++; currentFrameNum = downWalkFrame;
        }
        else if (keyHandler.leftPressed) {
            this.x -= this.speed; idleFrameToLoad = 0;

            if (leftWalkConsec == 5) {
                leftWalkConsec = 0;
                leftWalkFrame++;
                if (leftWalkFrame >= leftAnimLength) leftWalkFrame = 0;
                if (leftSideFrames[1][leftWalkFrame] == null) leftWalkFrame = 0;
            }
            currentCharacterFrame = leftSideFrames[1][leftWalkFrame];
            leftWalkConsec++; currentFrameNum = leftWalkFrame;
        }
        else if (keyHandler.rightPressed) {
            this.x += this.speed; idleFrameToLoad = 0;

            if (rightWalkConsec == 5) {
                rightWalkConsec = 0;
                rightWalkFrame++;
                if (rightWalkFrame >= rightAnimLength) rightWalkFrame = 0;
                if (rightSideFrames[1][rightWalkFrame] == null) rightWalkFrame = 0;
            }
            currentCharacterFrame = rightSideFrames[1][rightWalkFrame];
            rightWalkConsec++; currentFrameNum = rightWalkFrame;
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

        String locationString = Config.COORDINATE  + this.x + "%%:" + this.y + "%%:"
                + keyHandler.direction + "%%:" + currentFrameNum;
        clientHandler.toServer[0].println(locationString);
    }

    protected void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.drawImage(currentCharacterFrame, this.x, this.y, null);
        //g2.fillRect(this.x, this.y, gamePanel.tileSize, gamePanel.tileSize);
    }


    protected void loadCharacterFrames(String characterName) throws IOException, InterruptedException {
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
