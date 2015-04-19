import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.range.Range;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TermChirpTest {

    public static final String PRESENTABLE_OUTPUT = "Quack";
    Generator<String> spacesGenerator = TermChirpRDG.spacesGenerator;
    Generator<String> userNameGenerator = TermChirpRDG.userNameGenerator;
    Generator<String> messageGenerator = TermChirpRDG.messageGenerator;
    Deque<Chirp> emptyChirps = new ArrayDeque<>();
    List<String> presentableOutput;

    @Mock
    PrintStream output;
    @Mock
    MessageRepository messageRepository;
    @Mock
    PresentableChirps presentableChirps;

    @Before
    public void presentableOutput(){
        presentableOutput = new ArrayList<>();
        presentableOutput.add(PRESENTABLE_OUTPUT);
        given(presentableChirps.format(any(), anyString()))
                .willReturn(presentableOutput.iterator());

    }

    @Test
    public void getPostCommand() {
        given(presentableChirps.format(any(), anyString()))
                .willReturn(new ArrayList<String>().iterator());
        given(messageRepository.command(anyString(), anyString(), anyString()))
                .willReturn(emptyChirps);
        String userName = userNameGenerator.next();
        String message = messageGenerator.next();
        InputStream input = getUserInputAsStream(userName, Command.POST_INPUT, message);
        new TermChirp(input, output, messageRepository, presentableChirps, 1d);
        verify(messageRepository).command(userName, Command.POST_INPUT, message);
        verify(output, never()).println(anyString());
    }

    @Test
    public void getFollowsCommand() {
        given(presentableChirps.format(any(), anyString()))
                .willReturn(new ArrayList<String>().iterator());
        given(messageRepository.command(anyString(), anyString(), anyString()))
                .willReturn(emptyChirps);
        String userName = userNameGenerator.next();
        String message = userNameGenerator.next();
        InputStream input = getUserInputAsStream(userName, Command.FOLLOWS_INPUT, message);
        new TermChirp(input, output, messageRepository, presentableChirps, 1d);
        verify(messageRepository).command(userName, Command.FOLLOWS_INPUT, message);
        verify(output, never()).println(anyString());
    }

    @Test
    public void getWallCommand() {
        Deque<Chirp> chirps = collectionOfChirps(Range.closed(10, 30));
        given(messageRepository.command(anyString(), anyString(), anyString()))
                .willReturn(chirps);
        String userName = userNameGenerator.next();
        InputStream input = getUserInputAsStream(userName, Command.WALL_INPUT, null);
        new TermChirp(input, output, messageRepository, presentableChirps, 1d);
        verify(messageRepository).command(eq(userName), eq(Command.WALL_INPUT), isNull(String.class));
        verify(output).println("Quack");
    }

    @Test
    public void getReadingCommand() {
        Deque<Chirp> chirps = collectionOfChirps(Range.closed(10, 30));
        given(messageRepository.command(anyString(), anyString(), anyString()))
                .willReturn(chirps);
        String userName = userNameGenerator.next();
        InputStream input = getUserInputAsStream(userName, null, null);
        new TermChirp(input, output, messageRepository, presentableChirps, 1d);
        verify(messageRepository).command(eq(userName), isNull(String.class), isNull(String.class));
        verify(output).println(PRESENTABLE_OUTPUT);
    }

    private Deque<Chirp> collectionOfChirps(Range<Integer> range) {
        return TermChirpRDG.generatorOfStackOfChirps(range).next();
    }

    private InputStream getUserInputAsStream(String userName, String command, String message) {
        return new ByteArrayInputStream((userName +
                (command != null ? spacesGenerator.next() + command : "") +
                (message != null ? spacesGenerator.next() + message : ""))
                .getBytes());
    }
}
