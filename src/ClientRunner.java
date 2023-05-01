import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientRunner {
    public static void main(String[] args) throws IOException {
        String exitCmd = "exitcmd";
        try (Socket socket = new Socket("localhost", 5000)){
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            String userInput = "", response = "", clientName = "";

            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();

            do {
                while(clientName.equals("")) {
                    System.out.print("Enter your name: ");
                    userInput = scanner.nextLine();
                    clientName = userInput;
                    output.println(userInput);
                }

                String messageFormat = ("[" + clientName + "] - ");
                System.out.print(messageFormat + "- Message: ");
                userInput = scanner.nextLine();
                if (userInput.equals(exitCmd)) break;
                output.println(messageFormat + userInput);

            } while (!userInput.equals(exitCmd));
        } catch (Exception e) {
            System.out.println("Exception occured in ClientRunner: " + e.getStackTrace());
        }
    }
}
