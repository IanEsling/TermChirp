import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.StringReader;
import java.util.Scanner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ConsoleTweetTest {

    Scanner scanner;
    @Mock
    CommandRunner commandRunner;

    @Test
    public void getCommands(){
        scanner = new Scanner(new StringReader("string0 string1 string2"));
        ConsoleReader testee = new ConsoleReader(scanner, commandRunner);
        verify(commandRunner).command("string0", "string1", "string2");
    }

}
