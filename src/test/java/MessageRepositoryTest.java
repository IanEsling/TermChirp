import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.org.fyodor.generators.Generator;

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
    public void userCanPostToTimeline() {
        String userName = userNameGenerator.next();
        String message = messageGenerator.next();
        repo.command(userName, Command.POST_INPUT, message);
        verify(timelines).addToTimeline(userName, message);
    }

    @Test
    public void userCanReadOtherUsersTimeline() {
        String userName = userNameGenerator.next();
        repo.command(userName, null, null);
        verify(timelines).getTimelineForUser(userName);
    }
}