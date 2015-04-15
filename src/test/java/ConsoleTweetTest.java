import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.RDG;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ConsoleTweetTest {

    Generator<Command> randomCommand = RDG.value(Command.values());

    @Mock
    CommandInterpreter commandInterpreter;
    @Mock
    PrintStream output;

    @Test
    public void getCommands() {
        Command command = randomCommand.next();
        given(commandInterpreter.command(anyString(), anyString(), anyString()))
                .willReturn(command);
        InputStream input = new ByteArrayInputStream("string0 string1 string2".getBytes());
        new ConsoleReader(input, output, commandInterpreter);
        verify(commandInterpreter).command("string0", "string1", "string2");
        verify(output).println(command);
    }

}
