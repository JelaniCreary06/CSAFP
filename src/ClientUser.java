import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientUser extends CastBlueprint implements Serializable {
    private Socket socket;
    private String name, currentIP;

    private List<Message> messages = new ArrayList<>();

    public ClientUser(Socket socket, String name) {
        super("ClientUser");
        this.socket = socket; this.name = name;
        this.currentIP = socket.getInetAddress().getHostAddress();
    }

    public void name(String name) { this.name = name; }
    public String name() { return this.name; }

    public Socket socket() { return this.socket; }
    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addMessages(List<Message> messages) {
        messages.addAll(messages);
    }

    public List<Message> getMessages() { return this.messages; }


    @Override
    public String toString() {
        return this.name + "::" + this.currentIP;
    }
}