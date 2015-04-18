import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.org.fyodor.generators.Generator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private Generator<List<Chirp>> chirpListGenerator = TermChirpRDG.generatorOfListOfChirps();

    MessageRepository repo;

    @Before
    public void createRepo() {
        repo = new MessageRepository(timelines);
    }

    @Test
    public void userCanPostToTimelineAndEmptySetReturned() {
        String userName = userNameGenerator.next();
        String message = messageGenerator.next();
        Collection<Chirp> chirps = repo.command(userName, Command.POST_INPUT, message);
        verify(timelines).addToTimeline(userName, message);
        assertThat(chirps.size()).isEqualTo(0);
    }

    @Test
    public void userCanReadOtherUsersTimeline() {
        given(timelines.getTimelineForUser(anyString()))
                .willReturn(new ArrayList<>());
        String userName = userNameGenerator.next();
        repo.command(userName, null, null);
        verify(timelines).getTimelineForUser(userName);
    }

    @Test
    public void otherUserTimelinesAreInChronologicalOrder(){
        Collection<Chirp> randomListOfRandomChirps = chirpListGenerator.next();
        given(timelines.getTimelineForUser(anyString()))
                .willReturn(randomListOfRandomChirps);
        String userName = userNameGenerator.next();
        Collection<Chirp> chirps = repo.command(userName, null, null);

        assertThat(chirps.size()).isEqualTo(randomListOfRandomChirps.size());
        assertThat(chirps).isNotEqualTo(randomListOfRandomChirps);
        LocalDateTime earliest = LocalDateTime.now();
        for (Chirp chirp : chirps) {
            assertThat(chirp.getDateTime()).isBefore(earliest);
            earliest = chirp.getDateTime();
        }
    }
}