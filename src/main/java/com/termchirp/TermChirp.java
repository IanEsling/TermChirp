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
        this(input, output, new CommandInterpreter(), new PresentableChirps(), Double.POSITIVE_INFINITY);
    }

    public TermChirp(InputStream input,
                     PrintStream output,
                     CommandInterpreter commandInterpreter,
                     PresentableChirps presentableChirps,
                     Double runTimes) {
        Scanner scanner = new Scanner(input);

        int runs = 0;
        while (runTimes == Double.POSITIVE_INFINITY || runs < runTimes) {
            String[] command = getInput(scanner);
            runs++;
            if (command[0] != null) {
                Collection<Chirp> chirps = commandInterpreter.command(command[0], command[1], command[2]);
                Iterator<String> formattedChirps = presentableChirps.format(chirps, command[1]);
                while (formattedChirps.hasNext())
                    output.println(formattedChirps.next());
            }
        }
    }

    private String[] getInput(Scanner scanner) {
        String[] command = new String[3];
        if (scanner.hasNextLine()) {
            Scanner lineScanner = new Scanner(scanner.nextLine());
            if (lineScanner.hasNext()) {
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
        }
        return command;
    }

    public static void main(String... args) {
        new TermChirp(System.in, System.out);
    }
}
