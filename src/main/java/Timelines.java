import java.util.*;

public class Timelines {

    private final Map<String, Deque<Chirp>> timelines;
    private final ChirpGenerator chirpGenerator;

    public Timelines(ChirpGenerator chirpGenerator) {
        this(chirpGenerator, new HashMap<>());
    }

    public Timelines(ChirpGenerator chirpGenerator, Map<String, Deque<Chirp>> timelines) {
        this.chirpGenerator = chirpGenerator;
        this.timelines = timelines;
    }

    public void addToTimeline(String userName, String message) {
        Chirp chirp = chirpGenerator.generateChirp(userName, message);
        if (timelines.containsKey(userName)) {
            timelines.get(userName).push(chirp);
        } else {
            Deque<Chirp> messages = new ArrayDeque<>();
            messages.add(chirp);
            timelines.put(userName, messages);
        }
    }

    public Deque<Chirp> getTimelineForUser(String userName) {

        if (timelines.containsKey(userName)) {
            return timelines.get(userName);
        } else {
            return new ArrayDeque<>();
        }
    }

    public Deque<Chirp> getWallForUser(String userName) {
        return getTimelineForUser(userName);
    }
}
