public class ChirpGenerator {
    private Clock clock;

    public ChirpGenerator(Clock clock) {
        this.clock = clock;
    }

    public Chirp generateChirp(String message) {
        return new Chirp(message, clock.now());
    }
}
