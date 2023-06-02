import com.sun.tools.javac.Main;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ClientHandler extends Thread {

    private Socket socket;
    private String clientName, host;

    private ArrayList<OtherPlayers> otherPlayers = (ArrayList<OtherPlayers>) MainRunner.otherPlayers;
    int port;

    public ClientHandler(String clientName, String host, int port) throws IOException {
        this.clientName = clientName; this.host = host; this.port = port;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public String getCommand(String str) {
        return str.substring(0, str.indexOf(Config.INDENT_PREFIX));
    }

    public String getData(String str) {
        return str.substring(str.indexOf(Config.INDENT_PREFIX)+1);
    }


    @Override
    public void run() {
        PrintWriter toServer[] = { null };
        BufferedReader fromServer[] = { null };

        ObjectOutputStream objectsToServer[] = { null };
        ObjectInputStream objectsFromServer[] = { null };
        try {
            socket = new Socket(this.host, this.port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Runnable sendToServer = () -> {
            try {
                MainRunner.connectedToServer[0] = true;

                toServer[0] = new PrintWriter(socket.getOutputStream(), true);
                objectsToServer[0] = new ObjectOutputStream(socket.getOutputStream());

                PrintWriter sendServerStr = toServer[0];
                ObjectOutputStream sendServerObj = objectsToServer[0];

                sendServerStr.println(Config.NEW_CLIENT + this.clientName);

                KeyHandler clientKeyHandler[] = new KeyHandler[1];


                new Thread( () -> {
                    clientKeyHandler[0] = new KeyHandler(toServer[0]);
                }).start();

                Scanner scanner = new Scanner(System.in);

                do {

                } while (true);

            } catch (Exception e) {
                System.out.println("ConnectionError:" + this.host
                        + ":" + this.port + "::" + Arrays.toString(e.getStackTrace()));
                this.interrupt();
                return;
            }
        };

        Runnable recieveFromServer = () -> {
            try {
                fromServer[0] = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                objectsFromServer[0] = new ObjectInputStream(socket.getInputStream());

                BufferedReader strFromServer = (BufferedReader) fromServer[0];
                ObjectInputStream objFromServer = (ObjectInputStream) objectsFromServer[0];

                while (true) {
                        String strReceived = strFromServer.readLine();

                        if (strReceived.indexOf(Config.COORDINATE) != 0) {
                            for (OtherPlayers plr : otherPlayers) {
                                if (plr.getInetAddress().equals(strReceived.substring(0, strReceived.indexOf("]")))) {
                                    String split[] = strReceived.substring(strReceived.indexOf(Config.COORDINATE)+1).split("%%:");

                                    plr.setCoordinates(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                                    plr.updateFrame(split[2], Integer.parseInt(split[3]));

                                    //this.x + "%%:" + this.y + "%%:"
                                    //                + keyHandler.direction + "%%:" + currentFrameNum;
                                }
                                break;
                            }
                        }
                        if (strReceived.indexOf(Config.NEW_CLIENT) != 0) {
                            otherPlayers.add(new OtherPlayers("Warrior", getData(strReceived)));
                        }

                }
            } catch (IOException e) {
                System.out.println("ErrorReadingStringFromServer:" + this.host + ":" + this.port
                        + "::" + Arrays.toString(e.getStackTrace()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };


        new Thread(sendToServer).start();
        new Thread(recieveFromServer).start();
    }
}
