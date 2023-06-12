//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private String clientName;
    private String host;
    private Hashtable<String, OtherPlayers> otherPlayers;
    int port;

    PrintWriter[] toServer = new PrintWriter[]{null};
    BufferedReader[] fromServer = new BufferedReader[]{null};
    ObjectOutputStream[] objectsToServer = new ObjectOutputStream[]{null};
    ObjectInputStream[] objectsFromServer = new ObjectInputStream[]{null};


    public ClientHandler(String clientName, String host, int port) throws IOException {
        this.otherPlayers = MainRunner.otherPlayers;
        this.clientName = clientName;
        this.host = host;
        this.port = port;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public String getCommand(String str) {
        return str.substring(0, str.indexOf("}"));
    }

    public String getData(String str) {
        return str.substring(str.indexOf("}") + 1);
    }

    public void run() {
        try {
            this.socket = new Socket(this.host, this.port);
        } catch (IOException var7) {
            throw new RuntimeException(var7);
        }

        Runnable sendToServer = () -> {
            try {
                MainRunner.connectedToServer[0] = true;
                toServer[0] = new PrintWriter(this.socket.getOutputStream(), true);
                objectsToServer[0] = new ObjectOutputStream(this.socket.getOutputStream());
                PrintWriter sendServerStr = toServer[0];
                ObjectOutputStream sendServerObj = objectsToServer[0];
                sendServerStr.println("{CLIENT_NEW::}" + socket.getInetAddress());
                new Scanner(System.in);
            } catch (Exception var6) {
                String var10001 = this.host;
                System.out.println("ConnectionError:" + var10001 + ":" + this.port + "::" + Arrays.toString(var6.getStackTrace()));
                this.interrupt();
            }
        };
        Runnable recieveFromServer = () -> {
            try {
                fromServer[0] = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                objectsFromServer[0] = new ObjectInputStream(this.socket.getInputStream());
                BufferedReader strFromServer = fromServer[0];
                ObjectInputStream objFromServer = objectsFromServer[0];

                String strReceived = "";
                while(true) {
                    strReceived = strFromServer.readLine();

                    if (strReceived.indexOf("{LOCATION::NEW}") != 0) {
                        OtherPlayers plr = otherPlayers.get(strReceived.substring(0, strReceived.indexOf("]")));

                                String[] split = strReceived.substring(strReceived.indexOf("{LOCATION::NEW}") + 1).split("%%:");
                                plr.setCoordinates(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                                plr.updateFrame(split[2], Integer.parseInt(split[3]));
                    }

                    if (strReceived.indexOf("{CLIENT_NEW::}") != 0 && otherPlayers.get(strReceived.replace(Config.NEW_CLIENT, "")) != null) {
                        OtherPlayers newPlr = new OtherPlayers("Warrior", this.getData(strReceived));
                        this.otherPlayers.put(strReceived.replace(Config.NEW_CLIENT, ""), newPlr);
                    }
                    strReceived = "";
                    }

                } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(sendToServer).start();
        new Thread(recieveFromServer).start();
    }
}
