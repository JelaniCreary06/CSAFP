import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerRunner {
    public static void main(String[] args) throws IOException {
        ArrayList<ServerThread> threadList = new ArrayList<ServerThread>();

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket, threadList);

                threadList.add(serverThread);
                serverThread.start();
            }
        }
    }
}
