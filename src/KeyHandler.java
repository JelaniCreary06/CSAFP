import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.module.FindException;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public String direction = Config.RIGHT;

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> { upPressed = true; direction = Config.UP; }
            case KeyEvent.VK_S -> { downPressed = true; direction = Config.DOWN; }
            case KeyEvent.VK_A -> { leftPressed = true; direction = Config.LEFT; }
            case KeyEvent.VK_D -> { rightPressed = true; direction = Config.RIGHT; }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> { upPressed = false; }
            case KeyEvent.VK_S -> { downPressed = false; }
            case KeyEvent.VK_A -> { leftPressed = false; }
            case KeyEvent.VK_D -> { rightPressed = false; }
        }
    }
}
