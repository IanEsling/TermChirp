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
    CommandInterpreter commandInterpreter;

    @Test
    public void getCommands(){
        StringReader source = new StringReader("string0 string1 string2");
        scanner = new Scanner(source);
        ConsoleReader testee = new ConsoleReader(scanner, commandInterpreter);
        verify(commandInterpreter).command("string0", "string1", "string2");
    }

}
