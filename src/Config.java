public enum Config {
    MainGame("Multiplayer Game",
            4000,
            "Rob",
            "add");

    String name, testName, newClientCommand;
    int port;

    Config(String name, int port, String testName, String newClientCommand) {
        this.name = name; this.port = port;
        this.testName = testName;
        this.newClientCommand = newClientCommand;
    }
}
