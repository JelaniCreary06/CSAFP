import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ServerThread extends Thread {
    private Socket socket;
    private PrintWriter output;

    private ArrayList<ServerThread> threadList;

    public ServerThread(Socket socket, ArrayList<ServerThread> threadList) {
        this.socket = socket; this.threadList = threadList;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String outputString = input.readLine();

                if (outputString.equals("x")) break;
                System.out.println("[Server] Received: " + outputString);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("An error has occured \n" + e.getStackTrace());
        }
    }

    private void sendToAllClients(String outputString) {
        for (ServerThread serverThread : this.threadList) {
            serverThread.output.println(outputString);
        }
    }
}
