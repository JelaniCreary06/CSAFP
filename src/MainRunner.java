import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class MainRunner {
    public static void main(String[]args) throws IOException {

            new ServerHandler(5000).start();
            new ClientHandler(5000, "192.168.1.229").start();
    }
}
