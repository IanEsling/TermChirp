import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.org.fyodor.generators.Generator;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MessageRepositoryTest {

    @Mock
    Timelines timelines;

    Generator<String> userNameGenerator = TermChirpRDG.userNameGenerator;
    Generator<String> messageGenerator = TermChirpRDG.messageGenerator;
    private Generator<Deque<Chirp>> chirpStackGenerator = TermChirpRDG.generatorOfStackOfChirps();

    MessageRepository repo;

    @Before
    public void createRepo() {
        repo = new MessageRepository(timelines);
    }

    @Test
    public void userCanPostToTimelineAndEmptySetReturned() {
        String userName = userNameGenerator.next();
        String message = messageGenerator.next();
        Deque<Chirp> chirps = repo.command(userName, Command.POST_INPUT, message);
        verify(timelines).addToTimeline(userName, message);
        assertThat(chirps.size()).isEqualTo(0);
    }

    @Test
    public void usersCanSeeTheirWall() {
        given(timelines.getWallForUser(anyString()))
                .willReturn(new ArrayDeque<>());
        String userName = userNameGenerator.next();
        repo.command(userName, Command.WALL_INPUT, null);
        verify(timelines).getWallForUser(userName);
    }

    @Test
    public void userWallPostsAreInChronologicalOrder() {
        Deque<Chirp> randomStackOfRandomChirps = chirpStackGenerator.next();
        given(timelines.getWallForUser(anyString()))
                .willReturn(randomStackOfRandomChirps);
        String userName = userNameGenerator.next();
        Deque<Chirp> chirps = repo.command(userName, Command.WALL_INPUT, null);

        assertThat(chirps).isEqualTo(randomStackOfRandomChirps);
        assertThat(chirps.size()).isEqualTo(randomStackOfRandomChirps.size());
        LocalDateTime earliest = LocalDateTime.now();
        for (Chirp chirp : chirps) {
            assertThat(chirp.getDateTime()).isBefore(earliest);
            earliest = chirp.getDateTime();
        }
    }

    @Test
    public void userCanReadOtherUsersTimeline() {
        given(timelines.getTimelineForUser(anyString()))
                .willReturn(new LinkedList<>());
        String userName = userNameGenerator.next();
        repo.command(userName, null, null);
        verify(timelines).getTimelineForUser(userName);
    }

    @Test
    public void otherUserTimelinesAreInChronologicalOrder() {
        Deque<Chirp> randomStackOfRandomChirps = chirpStackGenerator.next();
        given(timelines.getTimelineForUser(anyString()))
                .willReturn(randomStackOfRandomChirps);
        String userName = userNameGenerator.next();
        Deque<Chirp> chirps = repo.command(userName, null, null);

        assertThat(chirps).containsExactlyElementsOf(randomStackOfRandomChirps);
        assertThat(chirps.size()).isEqualTo(randomStackOfRandomChirps.size());
        LocalDateTime earliest = LocalDateTime.now();
        for (Chirp chirp : chirps) {
            assertThat(chirp.getDateTime()).isBefore(earliest);
            earliest = chirp.getDateTime();
        }
    }
}