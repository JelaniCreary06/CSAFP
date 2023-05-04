import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class ServerHandler extends Thread {
    private List<ServerThread> connections = Collections.synchronizedList(new ArrayList<>());
    private int port;

    public ServerHandler(int port) {
        this.port = port;
    }

    public List<ServerThread> getConnections() {
        return this.connections;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ServerHandler.ServerThread serverThread = this.new ServerThread(socket, connections);

                connections.add(serverThread);
                serverThread.start();
            }
        } catch (IOException e) {
            try {
                throw new IOException(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public class ServerThread extends Thread {
        private List<ServerThread> connectionsList;
        private Socket socket;

        BufferedReader input;
        PrintWriter output;

        public ServerThread(Socket socket, List<ServerThread> connectionsList) {
            this.socket = socket; this.connectionsList = connectionsList;
        }

        @Override
        public void run() {
            try {
                input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    String outputString = input.readLine();

                    if (outputString.equals("x")) break;
                    System.out.println("[Server] Recieved a message from " +
                            outputString.substring(0, outputString.indexOf("]")) + ", {" +
                            outputString.substring(outputString.indexOf("]")+1) + " }");
                    ;
                    sendToClient("Thanks for the message, " + outputString.substring(0, outputString.indexOf("]")) + " - [Server]");
                    sendToAllClients("Message recieved.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                System.out.println("An error has occured \n" + e.getStackTrace());
            }
        }

        private void sendToClient(String outputString) {
            output.println(outputString);
        }

        private void sendToAllClients(String outputString) {
            for (ServerThread serverThread : this.connectionsList) {
                serverThread.output.println(outputString);
            }
        }
    }
}
