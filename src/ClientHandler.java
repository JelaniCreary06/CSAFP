import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private int port; private String host;

    public ClientHandler(int port, String host) {
        this.port = port; this.host = host;
    }

    @Override
    public void run() {
        String exitCmd = "exitcmd";
        try (Socket socket = new Socket(this.host, this.port)){
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            String userInput = "", response = "", clientName = "";
            boolean sentFirstMessage = false;

            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();

            do {
                while(clientName.equals("")) {
                    System.out.print("Enter your name: ");
                    userInput = scanner.nextLine();
                    clientName = userInput;
                }

                String messageFormat = (clientName + "]");
                if (!sentFirstMessage) {
                    System.out.print("Message: ");
                    sentFirstMessage = true;
                }
                userInput = scanner.nextLine();
                if (userInput.equals(exitCmd)) break;
                output.println(messageFormat + " " + userInput);



            } while (!userInput.equals(exitCmd));
        } catch (Exception e) {
            System.out.println("Exception occured in ClientRunner: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public class ClientThread extends Thread{
        private Socket socket;
        private BufferedReader input;


        public ClientThread(Socket socket) throws IOException {
            this.socket = socket;
            this.input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String response = input.readLine();
                    System.out.println("[Client] Received: " + response);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
