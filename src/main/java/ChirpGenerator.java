public class ChirpGenerator {
    private Clock clock;

    public ChirpGenerator(Clock clock) {
        this.clock = clock;
    }

    public Chirp generateChirp(String userName, String message) {
        return new Chirp(userName, message, clock.now());
    }
}
