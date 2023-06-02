import java.io.IOException;

public class OtherPlayers extends Player {
    private String inetAddress;
    public OtherPlayers(String character, String inetAddress) throws IOException, InterruptedException {
        super(character);
        this.inetAddress = inetAddress;
    }



    public void setCoordinates(int x, int y) {
        this.x = x; this.y = y;
    }

    public void updateFrame(String direction, int frame) {
        switch (direction) {
            case Config.RIGHT -> { this.currentCharacterFrame = this.rightSideFrames[1][frame];}
            case Config.LEFT -> { this.currentCharacterFrame = this.leftSideFrames[1][frame];}
            case Config.UP -> { this.currentCharacterFrame = this.upSideFrames[1][frame];}
            case Config.DOWN -> { this.currentCharacterFrame = this.downSideFrames[1][frame];}
        }
    }

    public String getInetAddress() {
        return this.inetAddress;
    }

}
