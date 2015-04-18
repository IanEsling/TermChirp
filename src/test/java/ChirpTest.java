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

}