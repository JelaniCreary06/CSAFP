import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.BufferedReader;

public class ClientThread extends Thread{
    private Socket socket;
    private BufferedReader input;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String response = input.readLine();
                System.out.println("\n[Client] Response: " + response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
