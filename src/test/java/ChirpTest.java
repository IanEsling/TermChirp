import org.junit.Test;
import uk.org.fyodor.generators.Generator;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ChirpTest {

    LocalDateTime now = LocalDateTime.now();
    ChirpGenerator chirpGenerator = new ChirpGenerator(new TestClock(now));
    Generator<String> messageGenerator = TermChirpRDG.messageGenerator;

    @Test
    public void messageStoredWithDateTime() {
        String message = messageGenerator.next();
        Chirp chirp = chirpGenerator.generateChirp(message);
        assertThat(chirp.getDateTime()).isEqualTo(now);
    }

    @Test
    public void checkChirpStringFormat(){
        String message = messageGenerator.next();
        Chirp chirp = chirpGenerator.generateChirp(message);
        assertThat(chirp.toString()).contains("moments ago");
        chirpGenerator = new ChirpGenerator(new TestClock(now.minusMinutes(1)));
        chirp = chirpGenerator.generateChirp(message);
        assertThat(chirp.toString()).contains("moments ago");
        chirpGenerator = new ChirpGenerator(new TestClock(now.minusMinutes(2)));
        chirp = chirpGenerator.generateChirp(message);
        assertThat(chirp.toString()).contains("moments ago");
        chirpGenerator = new ChirpGenerator(new TestClock(now.minusMinutes(3)));
        chirp = chirpGenerator.generateChirp(message);
        assertThat(chirp.toString()).contains("moments ago");
        chirpGenerator = new ChirpGenerator(new TestClock(now.minusMinutes(4)));
        chirp = chirpGenerator.generateChirp(message);
        assertThat(chirp.toString()).contains("moments ago");
        chirpGenerator = new ChirpGenerator(new TestClock(now.minusMinutes(5)));
        chirp = chirpGenerator.generateChirp(message);
        assertThat(chirp.toString()).contains("5 minutes ago");
        chirpGenerator = new ChirpGenerator(new TestClock(now.minusMinutes(6)));
        chirp = chirpGenerator.generateChirp(message);
        assertThat(chirp.toString()).contains("6 minutes ago");
        chirpGenerator = new ChirpGenerator(new TestClock(now.minusMinutes(7)));
        chirp = chirpGenerator.generateChirp(message);
        assertThat(chirp.toString()).contains("7 minutes ago");
        chirpGenerator = new ChirpGenerator(new TestClock(now.minusMinutes(8)));
        chirp = chirpGenerator.generateChirp(message);
        assertThat(chirp.toString()).contains("8 minutes ago");
    }

}