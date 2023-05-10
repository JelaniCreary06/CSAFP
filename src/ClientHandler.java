import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

public class ClientHandler extends Thread {

    private Socket socket;
    private String clientName, host;
    int port;

    public ClientHandler(String clientName, String host, int port) {
        this.clientName = clientName; this.host = host; this.port = port;
    }

    @Override
    public void run() {
        PrintWriter toServer[] = { null };
        BufferedReader fromServer[] = { null };

        ObjectOutputStream objectsToServer[] = { null };
        ObjectInputStream objectsFromServer[] = { null };

        Runnable sendToServer = () -> {
            try {
                this.socket = new Socket(this.host, this.port);

                toServer[0] = new PrintWriter(socket.getOutputStream(), true);
                objectsToServer[0] = new ObjectOutputStream(socket.getOutputStream());

                PrintWriter sendServerStr = toServer[0];
                ObjectOutputStream sendServerObj = objectsToServer[0];

                sendServerStr.println(Config.Game.newClientSequence + this.clientName);

                Scanner scanner = new Scanner(System.in);


                do {
                    String userInput = "NEWMESSAGEX";

                    System.out.print("Message: ");
                    userInput = scanner.nextLine();

                    if (!userInput.equals("NEWMESSAGEX")) sendServerStr.println(userInput);
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
                        Object objReceived = objFromServer.readObject();
                }
            } catch (IOException e) {
                System.out.println("ErrorReadingStringFromServer:" + this.host + ":" + this.port
                        + "::" + Arrays.toString(e.getStackTrace()));
            } catch (ClassNotFoundException e) {
                System.out.println("ErrorReadingClassFromServer:" + this.host + ":" + this.port
                        + "::" + Arrays.toString(e.getStackTrace()));
            } finally {
                this.interrupt();
                return;
            }
        };


        new Thread(sendToServer).start();
        new Thread(recieveFromServer).start();
    }
}
