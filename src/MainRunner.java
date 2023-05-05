import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


public class MainRunner {
    public static void main(String[]args) throws IOException, InterruptedException {
        String localhostIP = InetAddress.getLocalHost().getHostAddress(),
                ipSearchFormat = localhostIP.substring(0, localhostIP.lastIndexOf(".")),
                foundIP[] = { "" };

        ClientHandler thisClientConnection = null;
        ServerHandler mainServerHandler = null;

        Map<String, ServerHandler.ServerThread> connectedClients = null;
        ArrayList<ClientHandler> clientCheckThreads = new ArrayList<>(256);

        for (int i = 0; (i <= 255) && (foundIP[0].equals("")); i++) {
            String hostIP = ipSearchFormat + "." + i;
            if (!hostIP.equals(localhostIP)) {
                ClientHandler ch = new ClientHandler(Config.MainGamePort.num, hostIP) {
                    @Override
                    public void run() {
                        try (Socket socket = new Socket(hostIP, Config.MainGamePort.num)) {
                            if (socket.isConnected()) {
                                foundIP[0] = hostIP;
                                System.out.println("Socket found.");
                            }
                        } catch (Exception e) {
                            System.out.println("Failed to connect:: " + Arrays.toString(e.getStackTrace()));
                        }
                    }
                };

                ch.start();
                clientCheckThreads.add(ch);
            }
        }

        for (ClientHandler ch : clientCheckThreads) ch.join();

        if (foundIP[0].equals("")) {
            mainServerHandler = new ServerHandler(Config.MainGamePort.num);
            thisClientConnection = new ClientHandler(Config.MainGamePort.num, localhostIP);

            mainServerHandler.start(); thisClientConnection.start();

            connectedClients = mainServerHandler.getConnections();
            System.out.println("Server started, socket made.");
            System.out.println(connectedClients);
        } else new ClientHandler(Config.MainGamePort.num, foundIP[0]).start();
    }
}
