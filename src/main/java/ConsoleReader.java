import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Scanner;

public class ConsoleReader {

    int runs = 0;

    public ConsoleReader(InputStream input,
                         PrintStream output,
                         MessageRepository messageRepository) {
        this(input, output, messageRepository, Double.POSITIVE_INFINITY);
    }

    public ConsoleReader(InputStream input,
                         PrintStream output,
                         MessageRepository messageRepository,
                         Double runTimes) {
        Scanner scanner = new Scanner(input);

        while (runs < runTimes) {
            String[] command = getInput(scanner);
            runs++;
            Collection<Chirp> out = messageRepository.command(command[0], command[1], command[2]);
            for (Chirp chirp : out) {
                output.println(chirp.toString());
            }
        }
    }

    private String[] getInput(Scanner scanner) {
        String[] command = new String[3];
        if (scanner.hasNextLine()) {
            Scanner lineScanner = new Scanner(scanner.nextLine());
            command[0] = lineScanner.next();
            if (lineScanner.hasNext()) {
                command[1] = lineScanner.next();
                if (lineScanner.hasNext()) {
                    lineScanner.useDelimiter("\\z");
                    command[2] = lineScanner.next().trim();
                }
            }
            lineScanner.close();
        }
        return command;
    }
}
