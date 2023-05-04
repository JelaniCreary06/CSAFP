import java.io.IOException;
import java.util.Scanner;

public class MainRunner {
    public static void main(String[]args) throws IOException {

            new ServerConnection(4000).start();
            new ClientConnection(4000).start();
    }
}
