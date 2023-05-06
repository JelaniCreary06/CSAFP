import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FirewallBypass {
    private static final String ALLOW_TCP_PORT = "netsh advfirewall firewall add rule name=\"%s\" dir=in action=allow protocol=TCP localport=%d";

    private static final String FIREWALL_ENABLE = "netsh advfirewall set currentprofile state on";
    private static final String FIREWALL_DISABLE = "netsh advfirewall set currentprofile state off";

    public static void enableFirewall() {
        executeCommandAsLocalSystem(FIREWALL_ENABLE);
    }

    public static void disableFirewall() {
        executeCommandAsLocalSystem(FIREWALL_DISABLE);
    }

    public static void allowIncomingTCP(int port, String name) {
        String rule = String.format(ALLOW_TCP_PORT, name, port);
        executeCommandAsLocalSystem(rule);
    }

    public static void runAsAdministrator(String command) throws IOException {
        String[] cmd = {"cmd", "/c", "runas", "/user:Administrator", command};
        Runtime.getRuntime().exec(cmd);
    }

    private static void executeCommandAsLocalSystem(String command) {
        List<String> commandList = new ArrayList<>();
        commandList.add("cmd");
        commandList.add("/c");
        commandList.add(command);

        try {
            ProcessBuilder builder = new ProcessBuilder(commandList);
            builder.redirectErrorStream(true);
            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            Process process = builder.start();
            process.waitFor();
        } catch (Exception e) {
            throw new RuntimeException("Error executing command: " + command, e);
        }
    }
}