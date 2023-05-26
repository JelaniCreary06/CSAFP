import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player extends Entity {
    private GamePanel gamePanel;
    private KeyHandler keyHandler;

    private BufferedImage currentCharacterFrame;
    private BufferedImage rightSideFrames[][];

    private String character = "Warrior";


    int rightAnimLength;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) throws IOException, InterruptedException {
        this.gamePanel = gamePanel; this.keyHandler = keyHandler;
        this.x = 100; this.y = 100; this.speed = 5;

        loadCharacterFrames(character);

        System.out.println("All frames loaded.");

        rightAnimLength = rightSideFrames[0].length;
    }


    int idleFrameToLoad = 0, rightWalkFrame = 0, leftWalkFrame = 0,
            currentFrame = 0, updateOn = 0, normalUpdate = 5, idleUpdate = 12;

    int idleConsec = 0, rightWalkConsec = 0;


    public void update() {
        currentFrame++;

        if (keyHandler.upPressed) {
            updateOn = normalUpdate; idleFrameToLoad = 0;
            this.y -= this.speed;
        }
        else if (keyHandler.downPressed)  {
            updateOn = normalUpdate; idleFrameToLoad = 0;
            this.y += this.speed;
        }
        else if (keyHandler.leftPressed) {
            this.x -= this.speed;
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
            rightWalkConsec++;
            System.out.println("WalkingRight+::" + this.x +";" + this.y);
        }
        else {
            if (idleConsec == 12) {
                if (keyHandler.direction.equals(Config.RIGHT)) {
                    if (idleFrameToLoad == rightAnimLength - 2) idleFrameToLoad = 0;
                    if (rightSideFrames[0][idleFrameToLoad] == null) idleFrameToLoad = 0;
                }

                currentCharacterFrame = rightSideFrames[0][idleFrameToLoad];
                idleFrameToLoad++;
                idleConsec = 0;
            }
            idleConsec++;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.drawImage(currentCharacterFrame, this.x, this.y, null);
        //g2.fillRect(this.x, this.y, gamePanel.tileSize, gamePanel.tileSize);
    }


    public void loadCharacterFrames(String characterName) throws IOException, InterruptedException {
        final String endFormat = ".png", absolutePath = "src/PlayerSprites/" + characterName,
                rightFramesPath = absolutePath + "/Right", rightWalkPath = rightFramesPath + "/Walk", rightIdlePath = rightFramesPath + "/Idle",

                leftFramesPath = absolutePath + "/Left",
                upFramesPath = absolutePath + "/Up", downFramesPath = absolutePath + "/Down";

        Runnable loadRightSideFrames = () -> {
            List<BufferedImage> idleFrames, walkFrames;

            try {
                idleFrames = loadFrames(rightIdlePath, endFormat);
                walkFrames = loadFrames(rightWalkPath, endFormat);
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

        Thread loadRightFrames = new Thread(loadRightSideFrames);
        loadRightFrames.start();

        loadRightFrames.join();
    }

    public ArrayList<BufferedImage> loadFrames(String path, String end) throws IOException {
        List<BufferedImage> framesToLoad = new ArrayList();

        String animToGet = path.substring(path.lastIndexOf("/")+1),
                filesToLoad[] = new File(path).list();

        for (String fileName : filesToLoad) framesToLoad.add(ImageIO.read(new File(path + "/" + fileName)));

        System.out.println("Loaded::" + animToGet + "::" + path + " [" + filesToLoad.length + "]");
        return (ArrayList<BufferedImage>) framesToLoad;
    }
}
