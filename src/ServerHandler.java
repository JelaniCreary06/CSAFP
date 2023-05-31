import javax.lang.model.type.ArrayType;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerHandler extends Thread {
    private int port;
    private GamePanel gamePanel;

    Map<Socket, Player> connectedUsers = new Hashtable<>();
    List<Player> playerList = new ArrayList();

    public ServerHandler(int port) {
        this.port = port;
    }

    public Map<Socket, Player> getConnectedUsers() {
        return this.connectedUsers;
    }

    public ArrayList<Player> getPlayerList() {
        return (ArrayList<Player>) this.playerList;
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

    public void stringReceived(String str, Socket currentSocket) throws IOException, InterruptedException {
        if (getCommand(str).equals(getCommand(Config.NEW_CLIENT))) {
            Player toAdd = new Player(gamePanel);
            this.connectedUsers.put(currentSocket, toAdd);
            this.playerList.add(toAdd);
        }
        if (getCommand(str).equals(getCommand(Config.NEW_MESSAGE))) {
            System.out.println("[Server]" + getData(str));
        }
        if (getCommand(str).equals(getCommand(Config.KEY_INPUT))) {
            connectedUsers.get(currentSocket).updateDirection(getData(str));
        }
    }
    @Override
    public void run() {
        Runnable setupServer = () -> {
            try (ServerSocket serverSocket = new ServerSocket(this.port)) {
                MainRunner.connectedToServer[0] = true;
                gamePanel = new GamePanel((Hashtable<Socket, Player>) this.connectedUsers, (ArrayList<Player>) this.playerList);

                JFrame gameWindow = new JFrame();
                gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameWindow.setResizable(false);

                gameWindow.add(gamePanel);
                gameWindow.pack();

                gameWindow.setLocationRelativeTo(null);
                gameWindow.setVisible(true);
                while (true) {
                    Socket currentSocket = serverSocket.accept();

                    Runnable clientInteractions = () -> {
                        try {
                                BufferedReader strFromClient = new BufferedReader(new InputStreamReader(currentSocket.getInputStream()));
                                PrintWriter sendClientStr = new PrintWriter(currentSocket.getOutputStream(), true);

                                ObjectInputStream objFromClient = new ObjectInputStream(currentSocket.getInputStream());
                                ObjectOutputStream sendClientObj = new ObjectOutputStream(currentSocket.getOutputStream());

                                Runnable strReceived = () -> {
                                    while (true) {
                                        try {
                                            String receivedStr = strFromClient.readLine();

                                            stringReceived(receivedStr, currentSocket);
                                        } catch (IOException | InterruptedException e) {
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
