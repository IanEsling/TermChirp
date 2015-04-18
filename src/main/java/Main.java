import java.util.Scanner;

public class Main {


    public static void main(String... args) {
        new ConsoleReader(System.in, System.out, new MessageRepository());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextLine()) {
                System.out.println("reading line...");
                System.out.println(scanner.nextLine());
                System.out.println("finished reading");
            }
        }
    }
}
