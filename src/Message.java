import java.io.Serializable;
import java.util.Date;

public class Message extends CastBlueprint implements Serializable {
    private ClientUser author;
    private String content, timeSent;

    public Message(ClientUser author, String content) {
        super("Message");
        this.author = author; this.content = content;
        this.timeSent = "" + new Date(System.currentTimeMillis());
    }

    public ClientUser author() {
        return this.author;
    }

    public String content() {
        return this.content;
    }

    public String timeSent() {
        return this.timeSent;
    }

    @Override
    public String toString() {
        return "[" + this.author.name() + "]: " + this.content + ", Sent: " + this.timeSent;
    }
}
