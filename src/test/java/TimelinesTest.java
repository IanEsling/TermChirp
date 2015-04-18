import org.junit.Test;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.RDG;
import uk.org.fyodor.range.Range;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class TimelinesTest {

    Generator<String> userNameGenerator = TermChirpRDG.userNameGenerator;
    Generator<String> messageGenerator = TermChirpRDG.messageGenerator;

    Timelines timelines = new Timelines();

    @Test
    public void createTimelineForFirstPost(){
        String userName = userNameGenerator.next();
        String message = messageGenerator.next();
        assertThat(timelines.getTimelineForUser(userName)).hasSize(0);
        timelines.addToTimeline(userName, message);
        assertThat(timelines.getTimelineForUser(userName)).hasSize(1);
        assertThat(timelines.getTimelineForUser(userName).iterator().next()).isEqualTo(message);
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
        for (String message : timelines.getTimelineForUser(userName)) {
            assertThat(postedMessages).contains(message);
        }
        assertThat(timelines.getTimelineForUser(userName)).hasSize(numberOfMessagesToPost);
    }

}