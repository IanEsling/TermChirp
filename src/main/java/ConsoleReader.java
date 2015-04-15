import java.util.Scanner;

public class ConsoleReader {

    public ConsoleReader(Scanner scanner, CommandRunner commandRunner) {
        String[] command = new String[3];
        int i = 0;
       while (scanner.hasNext()) {
           command[i] = scanner.next();
           i++;
       }
        commandRunner.command(command);
    }
}
