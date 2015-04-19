import org.junit.Before;
import org.junit.Test;
import uk.org.fyodor.generators.Generator;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ChirpTest {

    Generator<String> messageGenerator = TermChirpRDG.messageGenerator;
    Generator<String> userNameGenerator = TermChirpRDG.userNameGenerator;
    LocalDateTime now = LocalDateTime.now();
    TestClock clock;
    ChirpGenerator chirpGenerator;

    @Before
    public void setupChirpGenerator(){
        clock = new TestClock(now);
        chirpGenerator = new ChirpGenerator(clock);
    }

    @Test
    public void messageStoredWithDateTime() {
        String message = messageGenerator.next();
        String userName = userNameGenerator.next();
        Chirp chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.getDateTime()).isEqualTo(now);
    }
}