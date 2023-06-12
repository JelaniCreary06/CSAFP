import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class  ServerHandler extends Thread {
    private int port;

    static Map<String, String> connectedUsers = new Hashtable<>();
    List<ServerThread> serverThreadList = new ArrayList();

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

    @Override
    public void run() {
        Runnable setupServer = () -> {
            try (ServerSocket serverSocket = new ServerSocket(this.port)) {
                MainRunner.connectedToServer[0] = true;
                while (true) {
                    Socket socket = serverSocket.accept();

                    ServerThread newClientThread = new ServerThread(socket, (ArrayList<ServerThread>) serverThreadList);

                    connectedUsers.put(socket.getInetAddress().getHostAddress(), "");
                    serverThreadList.add(newClientThread);
                    newClientThread.start();
                }
            } catch (Exception e) {
                System.out.println("ErrorWithServerSetup:" + this.port + "::" + Arrays.toString(e.getStackTrace()));
                this.interrupt();
                return;
            }
        };

        new Thread(setupServer).start();
    }

    public class ServerThread extends Thread {
        PrintWriter toClient;
        BufferedReader fromClient;

        private Socket socket;

        private ArrayList<ServerThread> serverThreadList;

        public ServerThread(Socket socket, ArrayList<ServerThread> serverThreadList) {
            this.socket = socket;  this.serverThreadList = serverThreadList;
        }

        public String getCommand(String str) {
            return str.substring(0, str.indexOf(Config.INDENT_PREFIX));
        }

        public String getData(String str) {
            return str.substring(str.indexOf(Config.INDENT_PREFIX)+1);
        }

        public void stringReceived(ServerThread sentFrom, String str) {
            if (getCommand(str).equals(getCommand(Config.NEW_CLIENT))) {
                sendToOtherClients(sentFrom, Config.NEW_CLIENT + sentFrom.socket.getInetAddress());
            }
            if (getCommand(str).equals(getCommand(Config.NEW_MESSAGE))) {
                System.out.println("[Server]" + getData(str));
            }
            if (getCommand(str).equals(getCommand(Config.COORDINATE))) {
                sendToOtherClients(sentFrom, sentFrom.socket.getInetAddress() + "]" + Config.COORDINATE + str);
            }
            if (getCommand(str).equals(getCommand(Config.NEW_CLIENT))) {
                sendToOtherClients(sentFrom, Config.NEW_CLIENT + sentFrom.socket.getInetAddress());
            }
        }

        public void sendToOtherClients(ServerThread sentFrom, String str) {
            for (ServerThread serverThread : serverThreadList) {
                if (!serverThread.equals(sentFrom)) serverThread.toClient.println(str);
            }
        }

        @Override
        public void run() {
                try {
                    fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    toClient = new PrintWriter(socket.getOutputStream(), true);

                    ObjectInputStream objFromClient = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream sendClientObj = new ObjectOutputStream(socket.getOutputStream());

                    Runnable strReceived = () -> {
                        while (true) {
                            try {
                                String receivedStr = fromClient.readLine();

                                stringReceived(this, receivedStr);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };
                    new Thread(strReceived).start();
                } catch (IOException e) {
                    System.out.println("IOError:" + "::" + Arrays.toString(e.getStackTrace()));
                    this.interrupt();
                    return;
                }
        };
    }
}

