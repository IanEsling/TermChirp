package com.termchirp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.org.fyodor.range.Range;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static com.termchirp.TermChirpRDG.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TermChirpTest {

    public static final String PRESENTABLE_OUTPUT = "Quack";
    Deque<Chirp> emptyChirps = new ArrayDeque<>();
    List<String> presentableOutput;

    @Mock
    PrintStream output;
    @Mock
    CommandExecutor commandExecutor;
    @Mock
    PresentableChirps presentableChirps;

    @Before
    public void presentableOutput() {
        presentableOutput = new ArrayList<>();
        presentableOutput.add(PRESENTABLE_OUTPUT);
        given(presentableChirps.format(any(), anyString()))
                .willReturn(presentableOutput.iterator());
    }

    @Test
    public void getPostCommand() {
        given(presentableChirps.format(any(), anyString()))
                .willReturn(new ArrayList<String>().iterator());
        given(commandExecutor.command(anyString(), anyString(), anyString()))
                .willReturn(emptyChirps);
        String userName = userNameGenerator.next();
        String message = messageGenerator.next();
        InputStream input = getUserInputAsStream(userName, TermChirp.POST_INPUT, message);
        new TermChirp(input, output, commandExecutor, presentableChirps, 1d);
        verify(commandExecutor).command(userName, TermChirp.POST_INPUT, message);
        verify(output, never()).println(anyString());
    }

    @Test
    public void getFollowsCommand() {
        given(presentableChirps.format(any(), anyString()))
                .willReturn(new ArrayList<String>().iterator());
        given(commandExecutor.command(anyString(), anyString(), anyString()))
                .willReturn(emptyChirps);
        String userName = userNameGenerator.next();
        String message = userNameGenerator.next();
        InputStream input = getUserInputAsStream(userName, TermChirp.FOLLOWS_INPUT, message);
        new TermChirp(input, output, commandExecutor, presentableChirps, 1d);
        verify(commandExecutor).command(userName, TermChirp.FOLLOWS_INPUT, message);
        verify(output, never()).println(anyString());
    }

    @Test
    public void getWallCommand() {
        String userName = userNameGenerator.next();
        Deque<Chirp> chirps = collectionOfChirps(userName, Range.closed(10, 30));
        given(commandExecutor.command(anyString(), anyString(), anyString()))
                .willReturn(chirps);
        InputStream input = getUserInputAsStream(userName, TermChirp.WALL_INPUT, null);
        new TermChirp(input, output, commandExecutor, presentableChirps, 1d);
        verify(commandExecutor).command(eq(userName), Matchers.eq(TermChirp.WALL_INPUT), isNull(String.class));
        verify(output).println("Quack");
    }

    @Test
    public void getReadingCommand() {
        String userName = userNameGenerator.next();
        Deque<Chirp> chirps = collectionOfChirps(userName, Range.closed(10, 30));
        given(commandExecutor.command(anyString(), anyString(), anyString()))
                .willReturn(chirps);
        InputStream input = getUserInputAsStream(userName, null, null);
        new TermChirp(input, output, commandExecutor, presentableChirps, 1d);
        verify(commandExecutor).command(eq(userName), isNull(String.class), isNull(String.class));
        verify(output).println(PRESENTABLE_OUTPUT);
    }

    @Test
    public void noExceptionOnEmptyLine(){
        InputStream input = new ByteArrayInputStream(" ".getBytes());
        new TermChirp(input, output, commandExecutor, presentableChirps, 1d);
        verify(commandExecutor, never()).command(anyString(), anyString(), anyString());
        verify(output, never()).print(anyString());
    }

    private Deque<Chirp> collectionOfChirps(String userName, Range<Integer> range) {
        return TermChirpRDG.generatorOfChronologicallyOrderedStackOfChirps(userName, range).next();
    }

    private InputStream getUserInputAsStream(String userName, String command, String message) {
        return new ByteArrayInputStream((userName +
                (command != null ? spacesGenerator.next() + command : "") +
                (message != null ? spacesGenerator.next() + message : ""))
                .getBytes());
    }
}
