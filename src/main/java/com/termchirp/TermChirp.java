package com.termchirp;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

public class TermChirp {

    public static final String POST_INPUT = "->";
    public static final String WALL_INPUT = "wall";
    public static final String FOLLOWS_INPUT = "follows";

    public TermChirp(InputStream input, PrintStream output) {
        this(input, output, new CommandExecutor(), new PresentableChirps(), Double.POSITIVE_INFINITY);
    }

    public TermChirp(InputStream input,
                     PrintStream output,
                     CommandExecutor commandExecutor,
                     PresentableChirps presentableChirps,
                     Double runTimes) {
        Scanner scanner = new Scanner(input);
        int runs = 0;
        while (runTimes == Double.POSITIVE_INFINITY || runs < runTimes) {
            String[] command = getInput(scanner);
            runs++;
            if (command[0] != null) {
                Collection<Chirp> chirps = commandExecutor.command(command[0], command[1], command[2]);

                Iterator<String> formattedChirps = presentableChirps.format(chirps, command[1]);
                while (formattedChirps.hasNext()) {
                    output.println(formattedChirps.next());
                }
            }
        }
    }

    private String[] getInput(Scanner scanner) {
        String[] command = new String[3];//never more than 3 elements to the input
        if (scanner.hasNextLine()) {
            //use another scanner to parse the line by whitespace
            Scanner lineScanner = new Scanner(scanner.nextLine());
            if (lineScanner.hasNext()) {
                command[0] = lineScanner.next();
                if (lineScanner.hasNext()) {
                    command[1] = lineScanner.next();
                    if (lineScanner.hasNext()) {
                        //put the rest of the input into the 3rd element, regardless of whitespace
                        lineScanner.useDelimiter("\\z");
                        command[2] = lineScanner.next().trim();
                    }
                }
                lineScanner.close();
            }
        }
        return command;
    }

    public static void main(String... args) {
        printWelcome(System.out);
        new TermChirp(System.in, System.out);
    }

    private static void printWelcome(PrintStream output) {
        //ho ho
        output.println(" _    _      _                            _          _____                   _____ _     _            \n" +
                "| |  | |    | |                          | |        |_   _|                 /  __ \\ |   (_)           \n" +
                "| |  | | ___| | ___ ___  _ __ ___   ___  | |_ ___     | | ___ _ __ _ __ ___ | /  \\/ |__  _ _ __ _ __  \n" +
                "| |/\\| |/ _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\    | |/ _ \\ '__| '_ ` _ \\| |   | '_ \\| | '__| '_ \\ \n" +
                "\\  /\\  /  __/ | (_| (_) | | | | | |  __/ | || (_) |   | |  __/ |  | | | | | | \\__/\\ | | | | |  | |_) |\n" +
                " \\/  \\/ \\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/    \\_/\\___|_|  |_| |_| |_|\\____/_| |_|_|_|  | .__/ \n" +
                "                                                                                               | |    \n" +
                "                                                                                               |_|    ");
        output.println();
        output.println("USAGE: username [->|follows|wall] [message|username]");
        output.println();
    }
}
