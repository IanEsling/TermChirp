import org.junit.Test;
import uk.org.fyodor.generators.Generator;

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
    public void addConsecutivePosts(){
        String userName = userNameGenerator.next();
        timelines.addToTimeline(userName, messageGenerator.next());
        timelines.addToTimeline(userName, messageGenerator.next());
        timelines.addToTimeline(userName, messageGenerator.next());
        timelines.addToTimeline(userName, messageGenerator.next());
        assertThat(timelines.getTimelineForUser(userName)).hasSize(4);
    }
}