import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerHandler extends Thread {
    private int port;

    Map<String, String> connectedUsers = new Hashtable<>();
    List<Socket> socketList = new ArrayList();

    public ServerHandler(int port) {
        this.port = port;
    }

    public Map<String, String> getConnectedUsers() {
        return this.connectedUsers;
    }

    /*
    public void objectReceived(Object obj) {
        CastBlueprint castObject = (CastBlueprint) obj;

        switch (castObject.className()) {
            case "ClientUser":
                ClientUser clientObj = (ClientUser) obj;

                connectedUsers.put(clientObj.name(), clientObj);
                System.out.println(clientObj.name());
                break;

            case "Message":
                Message messageObj = (Message) obj;

                System.out.println("[Server] " + messageObj.content());
                break;
            default:
                System.out.println((String) obj);
                break;
        }
    }
     */

    public String getCommand(String str) {
        return str.substring(0, str.indexOf(Config.INDENT_PREFIX));
    }

    public String getData(String str) {
        return str.substring(str.indexOf(Config.INDENT_PREFIX)+1);
    }


    public void stringReceived(Socket sentFrom, String str) {
        if (getCommand(str).equals(getCommand(Config.NEW_CLIENT))) {
            sendToOtherClients(sentFrom, Config.NEW_CLIENT + sentFrom.getInetAddress());
        }
        if (getCommand(str).equals(getCommand(Config.NEW_MESSAGE))) {
            System.out.println("[Server]" + getData(str));
        }
        if (getCommand(str).equals(getCommand(Config.COORDINATE))) {
            sendToOtherClients(sentFrom, getData(str));
        }
    }

    public void sendToOtherClients(Socket sentFrom, String str) {
        for (Socket socket : socketList) {
            if (socket != sentFrom) {
                try (PrintWriter sendClientStr = new PrintWriter(socket.getOutputStream(), true)){
                    sendClientStr.println(socket.getInetAddress() + "]" + Config.COORDINATE + str);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    @Override
    public void run() {
        Runnable setupServer = () -> {
            try (ServerSocket serverSocket = new ServerSocket(this.port)) {
                while (true) {
                    Socket socket = serverSocket.accept();

                    connectedUsers.put(socket.getInetAddress().getHostAddress(), "");
                    socketList.add(socket);
                    MainRunner.connectedToServer[0] = true;

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

                                            stringReceived(socket, receivedStr);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                                /*
                                Runnable objReceived = () -> {
                                    while (true) {
                                        try {

                                                Object obj = objFromClient.readObject();
                                                System.out.println(obj);

                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        } catch (ClassNotFoundException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                                 */

                                new Thread(strReceived).start();
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
