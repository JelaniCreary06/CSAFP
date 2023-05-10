import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
public class MainRunner {
    public static void main(String[]args) throws InterruptedException, UnknownHostException {
        String clientName[] = {""};
        new ClientSetupForm(clientName);

        String localhostIP = InetAddress.getLocalHost().getHostAddress(),
                ipSearchFormat = localhostIP.substring(0, localhostIP.lastIndexOf(".")),
                foundIP[] = { "" };

        ClientHandler thisClientConnection = null;
        ServerHandler mainServerHandler = null;

        Map<String, String> connectedClients = null;
        List<ClientHandler> clientCheckThreads = new ArrayList<>(256);

        for (int i = 0; (i <= 255) && (foundIP[0].equals("")); i++) {
            String hostIP = ipSearchFormat + "." + i;
            if (!hostIP.equals(localhostIP)) {
                ClientHandler ch = new ClientHandler("Rob", hostIP, Config.Game.port) {
                    @Override
                    public void run() {
                        try (Socket socket = new Socket(hostIP, Config.Game.port)) {
                            if (socket.isConnected()) {
                                foundIP[0] = hostIP;
                                System.out.println("Socket found.");
                            }
                        } catch (Exception e) {
                            System.out.println("Failed to connect::" + Arrays.toString(e.getStackTrace()));
                        }
                        this.interrupt();
                        return;
                    }
                };

                ch.start();
                clientCheckThreads.add(ch);
            }
        }

        for (ClientHandler ch : clientCheckThreads) ch.join();

        if (foundIP[0].equals("")) {
            mainServerHandler = new ServerHandler(Config.Game.port);
            thisClientConnection = new ClientHandler("Rob", foundIP[0], Config.Game.port);

            mainServerHandler.start(); thisClientConnection.start();

            connectedClients = mainServerHandler.getConnectedUsers();
            System.out.println("Server started, socket made.");
            System.out.println(connectedClients);
        } else new ClientHandler("Rob", foundIP[0], Config.Game.port).start();


    }
}
