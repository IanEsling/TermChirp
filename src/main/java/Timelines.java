import java.util.*;

public class Timelines {

    Map<String, Collection<String>> timelines = new HashMap<>();

    public void addToTimeline(String userName, String message) {
        if (timelines.containsKey(userName)) {
            timelines.get(userName).add(message);
        } else {
            List<String> messages = new ArrayList<>();
            messages.add(message);
            timelines.put(userName, messages);
        }
    }

    public Iterable<String> getTimelineForUser(String userName) {

        if (timelines.containsKey(userName)) {
            return timelines.get(userName);
        } else {
            return new ArrayList<>();
        }
    }
}
