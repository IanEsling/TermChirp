import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleReader {

    public ConsoleReader(InputStream input, PrintStream output, CommandInterpreter commandInterpreter) {
        Scanner scanner = new Scanner(input);
        String[] command = new String[3];
        int i = 0;
       while (scanner.hasNext()) {
           command[i] = scanner.next();
           i++;
       }
        output.println(commandInterpreter.command(command));
    }
}
