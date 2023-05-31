import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.lang.module.FindException;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public String direction = Config.DOWN;

    private PrintWriter toServer;

    public KeyHandler(PrintWriter toServer) {
        this.toServer = toServer;
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> { upPressed = true; direction = Config.UP;
                toServer.println(Config.KEY_INPUT + KeyEvent.VK_W);
            }
            case KeyEvent.VK_S -> { downPressed = true; direction = Config.DOWN;
                toServer.println(Config.KEY_INPUT + KeyEvent.VK_S);
            }
            case KeyEvent.VK_A -> { leftPressed = true; direction = Config.LEFT;
                toServer.println(Config.KEY_INPUT + KeyEvent.VK_A);
            }
            case KeyEvent.VK_D -> { rightPressed = true; direction = Config.RIGHT;
                toServer.println(Config.KEY_INPUT + KeyEvent.VK_D);
            }
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
