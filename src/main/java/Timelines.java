import java.util.*;

public class Timelines {

    Map<String, Collection<Chirp>> timelines = new HashMap<>();
    private ChirpGenerator chirpGenerator;

    public Timelines(ChirpGenerator chirpGenerator) {
        this.chirpGenerator = chirpGenerator;
    }

    public void addToTimeline(String userName, String message) {
        Chirp chirp = chirpGenerator.generateChirp(message);
        if (timelines.containsKey(userName)) {
            timelines.get(userName).add(chirp);
        } else {
            Collection<Chirp> messages = new ArrayList<>();
            messages.add(chirp);
            timelines.put(userName, messages);
        }
    }

    public Collection<Chirp> getTimelineForUser(String userName) {

        if (timelines.containsKey(userName)) {
            return timelines.get(userName);
        } else {
            return new ArrayList<>();
        }
    }

    public Collection<Chirp> getWallForUser(String userName) {
        return null;
    }
}
