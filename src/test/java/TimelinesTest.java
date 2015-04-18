import org.junit.Before;
import org.junit.Test;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.RDG;
import uk.org.fyodor.range.Range;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class TimelinesTest {

    LocalDateTime now = LocalDateTime.now();
    Generator<String> userNameGenerator = TermChirpRDG.userNameGenerator;
    Generator<String> messageGenerator = TermChirpRDG.messageGenerator;

    Clock clock;
    ChirpGenerator chirpGenerator;
    Timelines timelines;

    @Before
    public void setupTimelines(){
        clock = new TestClock(now);
        chirpGenerator = new ChirpGenerator(clock);
        timelines = new Timelines(chirpGenerator);
    }

    @Test
    public void createTimelineForFirstPost(){
        String userName = userNameGenerator.next();
        String message = messageGenerator.next();
        assertThat(timelines.getTimelineForUser(userName)).hasSize(0);
        timelines.addToTimeline(userName, message);
        assertThat(timelines.getTimelineForUser(userName)).hasSize(1);
        assertThat(timelines.getTimelineForUser(userName).iterator().next().getMessage()).isEqualTo(message);
    }

    @Test
    public void addThenReadPosts(){
        String userName = userNameGenerator.next();
        Integer numberOfMessagesToPost = RDG.integer(Range.closed(5, 50)).next();
        Collection<String> postedMessages = new ArrayList<>(numberOfMessagesToPost);
        for (int i = 0; i < numberOfMessagesToPost; i++) {
            String message = messageGenerator.next();
            postedMessages.add(message);
            timelines.addToTimeline(userName, message);
        }
        for (Chirp chirp : timelines.getTimelineForUser(userName)) {
            assertThat(postedMessages).contains(chirp.getMessage());
        }
        assertThat(timelines.getTimelineForUser(userName)).hasSize(numberOfMessagesToPost);
    }

}