import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.range.Range;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ConsoleReaderTest {

    Generator<String> spacesGenerator = TermChirpRDG.spacesGenerator;
    Generator<String> userNameGenerator = TermChirpRDG.userNameGenerator;
    Generator<String> messageGenerator = TermChirpRDG.messageGenerator;
    Generator<Chirp> chirpGenerator = TermChirpRDG.chirpGenerator();
    Collection<Chirp> emptyChirps = new ArrayList<>();

    @Mock
    PrintStream output;
    @Mock
    MessageRepository messageRepository;

    @Test
    public void getPostCommand() {
        given(messageRepository.command(anyString(), anyString(), anyString()))
                .willReturn(emptyChirps);
        String userName = userNameGenerator.next();
        String message = messageGenerator.next();
        InputStream input = getUserInputAsStream(userName, Command.POST_INPUT, message);
        new ConsoleReader(input, output, messageRepository, 1d);
        verify(messageRepository).command(userName, Command.POST_INPUT, message);
        verify(output, never()).println(anyString());
    }

    @Test
    public void getFollowsCommand() {
        given(messageRepository.command(anyString(), anyString(), anyString()))
                .willReturn(emptyChirps);
        String userName = userNameGenerator.next();
        String message = userNameGenerator.next();
        InputStream input = getUserInputAsStream(userName, Command.FOLLOWS_INPUT, message);
        new ConsoleReader(input, output, messageRepository, 1d);
        verify(messageRepository).command(userName, Command.FOLLOWS_INPUT, message);
        verify(output, never()).println(anyString());
    }

    @Test
    public void getWallCommand() {
        Collection<Chirp> chirps = collectionOfChirps(Range.closed(10, 30));
        given(messageRepository.command(anyString(), anyString(), anyString()))
                .willReturn(chirps);
        String userName = userNameGenerator.next();
        InputStream input = getUserInputAsStream(userName, Command.WALL_INPUT, null);
        new ConsoleReader(input, output, messageRepository, 1d);
        verify(messageRepository).command(eq(userName), eq(Command.WALL_INPUT), isNull(String.class));
        for (Chirp chirp : chirps) {
            verify(output).println(chirp.toString());
        }
    }

    @Test
    public void getReadingCommand() {
        Collection<Chirp> chirps = collectionOfChirps(Range.closed(10, 30));
        given(messageRepository.command(anyString(), anyString(), anyString()))
                .willReturn(chirps);
        String userName = userNameGenerator.next();
        InputStream input = getUserInputAsStream(userName, null, null);
        new ConsoleReader(input, output, messageRepository, 1d);
        verify(messageRepository).command(eq(userName), isNull(String.class), isNull(String.class));
        for (Chirp chirp : chirps) {
            verify(output).println(chirp.toString());
        }
    }

    private Collection<Chirp> collectionOfChirps(Range<Integer> range) {
        return TermChirpRDG.list(chirpGenerator, range).next();
    }

    private InputStream getUserInputAsStream(String userName, String command, String message) {
        return new ByteArrayInputStream((userName +
                (command != null ? spacesGenerator.next() + command : "") +
                (message != null ? spacesGenerator.next() + message : ""))
                .getBytes());
    }
}
