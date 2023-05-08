import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerHandler extends Thread {
    private int port;

    Map<String, ClientUser> connectedUsers = new Hashtable<>();

    public ServerHandler(int port) {
        this.port = port;
    }

    public Map<String, ClientUser> getConnectedUsers() {
        return this.connectedUsers;
    }

    public void objectRecieved(Object obj) {
        CastBlueprint castObject = (CastBlueprint) obj;

        switch (castObject.className()) {
            case "ClientUser":
                ClientUser clientObj = (ClientUser) obj;

                connectedUsers.put(clientObj.name(), clientObj);
                System.out.println(clientObj.name());
                break;

            case "Message":
                Message messageObj = (Message) obj;

                System.out.println(messageObj.content());
                break;
            default:
                System.out.println((String) obj);
                break;
        }
    }

    @Override
    public void run() {
        Runnable setupServer = () -> {
            try (ServerSocket serverSocket = new ServerSocket(this.port)) {
                while (true) {
                    Socket socket = serverSocket.accept();

                    Runnable clientInteractions = () -> {
                        try {
                                BufferedReader strFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                PrintWriter sendClientStr = new PrintWriter(socket.getOutputStream(), true);

                                ObjectInputStream objFromClient = new ObjectInputStream(socket.getInputStream());
                                ObjectOutputStream sendClientObj = new ObjectOutputStream(socket.getOutputStream());

                                Runnable strReceived = () -> {
                                    while (true) {
                                        try {
                                            String receivedStr = strFromClient.readLine();

                                            System.out.println("[Server] " + receivedStr);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                                Runnable objReceived = () -> {
                                    while (true) {
                                        try {
                                            Object receivedObj;

                                                receivedObj = objFromClient.readObject();

                                                objectRecieved(receivedObj);

                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        } catch (ClassNotFoundException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };

                                new Thread(strReceived).start();
                                new Thread(objReceived).start();
                        } catch (IOException e) {
                            System.out.println("IOError:" + this.port + "::" + Arrays.toString(e.getStackTrace()));
                            this.interrupt();
                            return;
                        }
                    };

                    new Thread(clientInteractions).start();
                }
            } catch (Exception e) {
                System.out.println("ErrorWithServerSetup:" + this.port + "::" + Arrays.toString(e.getStackTrace()));
                this.interrupt();
                return;
            }
        };

        new Thread(setupServer).start();
    }

}
