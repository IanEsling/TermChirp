import java.util.Scanner;

public class Main {


    public static void main(String... args) {
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
