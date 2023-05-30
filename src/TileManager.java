import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TileManager {
    private GamePanel gamePanel;
    private Tile[] tile;

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        tile = new Tile[10];

        getTileImage();
    }

    public void getTileImage() {
        for (int i = 0; i < tile.length; i++) {
            tile[i] = new Tile();

            try {
                tile[i].image = ImageIO.read(new File("src/Tiles/sprite_0.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(tile[0].image, 0, 0, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
    }
}
