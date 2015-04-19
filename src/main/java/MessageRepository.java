import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

public class MessageRepository {

    private Timelines timelines;

    public MessageRepository(Timelines timelines) {

        this.timelines = timelines;
    }

    public Collection<Chirp> command(String userName, String command, String message) {
        if (command == null) {
            return new TreeSet<>(timelines.getTimelineForUser(userName));
        } else if (Command.POST_INPUT.equals(command)) {
            timelines.addToTimeline(userName, message);
        } else if (Command.WALL_INPUT.equals(command)) {
            return new TreeSet<>(timelines.getWallForUser(userName));
        }

        return new ArrayList<>();
    }
}
