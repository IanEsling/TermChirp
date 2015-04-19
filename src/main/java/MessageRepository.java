import java.util.*;

public class MessageRepository {

    private Timelines timelines;

    public MessageRepository(Timelines timelines) {

        this.timelines = timelines;
    }

    public Deque<Chirp> command(String userName, String command, String message) {
        if (command == null) {
            return timelines.getTimelineForUser(userName);
        } else if (Command.POST_INPUT.equals(command)) {
            timelines.addToTimeline(userName, message);
        } else if (Command.WALL_INPUT.equals(command)) {
            return timelines.getWallForUser(userName);
        }

        return new LinkedList<>();
    }
}
