import java.io.InputStream;
import java.io.PrintStream;
import java.util.Deque;
import java.util.Iterator;
import java.util.Scanner;

public class TermChirp {

    PresentableChirps presentableChirps = new PresentableChirps();

    public TermChirp(InputStream input,
                     PrintStream output,
                     MessageRepository messageRepository) {
        this(input, output, messageRepository, Double.POSITIVE_INFINITY);
    }

    public TermChirp(InputStream input,
                     PrintStream output,
                     MessageRepository messageRepository,
                     Double runTimes) {
        Scanner scanner = new Scanner(input);

        int runs = 0;
        while (runs < runTimes) {
            String[] command = getInput(scanner);
            runs++;
            Deque<Chirp> chirps = messageRepository.command(command[0], command[1], command[2]);
            Iterator<String> formattedChirps = presentableChirps.format(chirps, command[1]);
            while (formattedChirps.hasNext())
                output.println(formattedChirps.next());
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

    public static void main(String... args) {
        LiveClock clock = new LiveClock();
        ChirpGenerator chirpGenerator = new ChirpGenerator(clock);
        Timelines timelines = new Timelines(chirpGenerator);
        MessageRepository messageRepository = new MessageRepository(timelines);

        new TermChirp(System.in, System.out, messageRepository);
    }
}
