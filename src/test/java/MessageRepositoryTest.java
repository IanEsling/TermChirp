import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.org.fyodor.generators.Generator;

import java.time.LocalDateTime;
import java.util.Collection;

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
    }

    @Test
    public void userCanReadOtherUsersTimeline() {
        Collection<Chirp> randomListOfRandomChirps = TermChirpRDG.generatorOfListOfChirps().next();
        given(timelines.getTimelineForUser(anyString()))
                .willReturn(randomListOfRandomChirps);
        String userName = userNameGenerator.next();
        Collection<Chirp> chirps = repo.command(userName, null, null);
        verify(timelines).getTimelineForUser(userName);
        
        assertThat(chirps.size()).isEqualTo(randomListOfRandomChirps.size());
        assertThat(chirps).isNotEqualTo(randomListOfRandomChirps);
        LocalDateTime earliest = LocalDateTime.now();
        for (Chirp chirp : chirps) {
            assertThat(chirp.getDateTime()).isBefore(earliest);
            earliest = chirp.getDateTime();
        }
    }
}