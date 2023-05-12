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

        findHostServer((ArrayList<ClientHandler>) clientCheckThreads, foundIP, ipSearchFormat,localhostIP );

        int secondsWithoutName = 0;
        while (clientName[0].equals("")) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("ErrorWhenAwaitingName::" + Arrays.toString(e.getStackTrace()));
            }
            secondsWithoutName++;
        }

        System.out.println("Name inputted: " + clientName[0]);
        if (secondsWithoutName > 0) findHostServer((ArrayList<ClientHandler>) clientCheckThreads, foundIP, ipSearchFormat,localhostIP );

        if (foundIP[0].equals("")) {
            mainServerHandler = new ServerHandler(Config.Game.port);
            thisClientConnection = new ClientHandler(clientName[0], foundIP[0], Config.Game.port);

            mainServerHandler.start(); thisClientConnection.start();

            connectedClients = mainServerHandler.getConnectedUsers();
            System.out.println("Server started, socket made.");
            System.out.println(connectedClients);
        } else new ClientHandler(clientName[0], foundIP[0], Config.Game.port).start();
    }

    public static void  findHostServer(ArrayList<ClientHandler> clientCheckThreads, String foundIP[], String ipSearchFormat, String localhostIP) throws InterruptedException {
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
    }
}
