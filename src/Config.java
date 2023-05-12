public enum Config {
    Game("Multiplayer Game",
            4000,
            "}",
           "{CLIENT_NEW::}", "{MESSAGE_NEW::}",
            "Enter your name here: ");

    final String name, indentPrefix, newClientSequence, nameHolderText, sendMessage;
    int port;

    Config(String name, int port, String testName, String newClientCommand, String sendMessage, String nameHolderText) {
        this.name = name; this.port = port;
        this.indentPrefix = testName;
        this.newClientSequence = newClientCommand;
        this.nameHolderText = nameHolderText;
        this.sendMessage = sendMessage;
    }
}
