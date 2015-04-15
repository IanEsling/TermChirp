public class CommandInterpreter {

    public Command command(String... input) {
        String entry = input.length > 1 ? input[1] : null;
        for (Command command : Command.values()) {
            if (command.canHandle(entry)) {
                return command;
            }
        }
        return null;
    }
}
