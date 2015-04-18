import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.RDG;
import uk.org.fyodor.range.Range;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ConsoleReaderTest {

    Generator<String> spacesGenerator = RDG.string(Range.closed(1, 10), " ");
    Generator<String> userNameGenerator = RDG.string(Range.closed(5, 15));
    Generator<String> messageGenerator = RDG.string(Range.closed(25, 150), Range.closed(32, 126));

    @Mock
    PrintStream output;
    @Mock
    MessageRepository messageRepository;

    @Test
    public void getPostCommand() {
        given(messageRepository.command(anyString(), anyString(), anyString()))
                .willReturn("");
        String userName = userNameGenerator.next();
        String message = messageGenerator.next();
        InputStream input = getUserInputAsStream(userName, Command.POST_INPUT, message);
        new ConsoleReader(input, output, messageRepository, 1d);
        verify(messageRepository).command(userName, Command.POST_INPUT, message);
        verify(output).println("");
    }

    @Test
    public void getFollowsCommand() {
        given(messageRepository.command(anyString(), anyString(), anyString()))
                .willReturn("");
        String userName = userNameGenerator.next();
        String message = userNameGenerator.next();
        InputStream input = getUserInputAsStream(userName, Command.FOLLOWS_INPUT, message);
        new ConsoleReader(input, output, messageRepository, 1d);
        verify(messageRepository).command(userName, Command.FOLLOWS_INPUT, message);
        verify(output).println("");
    }

    @Test
    public void getWallCommand() {
        given(messageRepository.command(anyString(), anyString(), anyString()))
                .willReturn("");
        String userName = userNameGenerator.next();
        InputStream input = getUserInputAsStream(userName, Command.WALL_INPUT, null);
        new ConsoleReader(input, output, messageRepository, 1d);
        verify(messageRepository).command(eq(userName), eq(Command.WALL_INPUT), isNull(String.class));
        verify(output).println("");
    }

    @Test
    public void getReadingCommand() {
        given(messageRepository.command(anyString(), anyString(), anyString()))
                .willReturn("");
        String userName = userNameGenerator.next();
        InputStream input = getUserInputAsStream(userName, null, null);
        new ConsoleReader(input, output, messageRepository, 1d);
        verify(messageRepository).command(eq(userName), isNull(String.class), isNull(String.class));
        verify(output).println("");
    }

    private InputStream getUserInputAsStream(String userName, String command, String message) {
        return new ByteArrayInputStream((userName +
                (command!=null ? spacesGenerator.next() + command : "") +
                (message!=null ? spacesGenerator.next() + message : ""))
                .getBytes());
    }
}
