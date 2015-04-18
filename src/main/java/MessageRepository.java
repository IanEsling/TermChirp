import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

public class MessageRepository {

    private Timelines timelines;

    public MessageRepository(Timelines timelines) {

        this.timelines = timelines;
    }

    public Collection<Chirp> command(String userName, String command, String message) {
        if (Command.POST_INPUT.equals(command)) {
            timelines.addToTimeline(userName, message);
        } else if (command == null) {
            return new TreeSet<>(timelines.getTimelineForUser(userName));
        }

        return new ArrayList<>();
    }
}
