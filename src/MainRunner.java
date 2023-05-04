import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class MainRunner {
    public static void main(String[]args) throws IOException {

            new ServerConnection(5000).start();
            new ClientConnection(5000).start();
            System.out.println(InetAddress.getLocalHost().getHostName());
    }
}
