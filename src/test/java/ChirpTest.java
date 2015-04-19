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

    @Test
    public void checkChirpTimeStringFormat(){
        String userName = userNameGenerator.next();
        String message = messageGenerator.next();
        Chirp chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("moments ago");
        clock.setNow(now.minusMinutes(1));
        chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("moments ago");
        clock.setNow(now.minusMinutes(2));
        chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("moments ago");
        clock.setNow(now.minusMinutes(3));
        chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("moments ago");
        clock.setNow(now.minusMinutes(4));
        chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("moments ago");
        clock.setNow(now.minusMinutes(5));
        chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("5 minutes ago");
        clock.setNow(now.minusMinutes(6));
        chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("6 minutes ago");
        clock.setNow(now.minusMinutes(7));
        chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("7 minutes ago");
        clock.setNow(now.minusMinutes(8));
        chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("8 minutes ago");
        clock.setNow(now.minusMinutes(9));
        chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("9 minutes ago");
        clock.setNow(now.minusMinutes(10));
        chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("10 minutes ago");
        clock.setNow(now.minusHours(1));
        chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("1 hour ago");
        clock.setNow(now.minusHours(10));
        chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("10 hours ago");
        clock.setNow(now.minusDays(1));
        chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("1 day ago");
        clock.setNow(now.minusDays(3));
        chirp = chirpGenerator.generateChirp(userName, message);
        assertThat(chirp.toString()).contains("3 days ago");
    }

}