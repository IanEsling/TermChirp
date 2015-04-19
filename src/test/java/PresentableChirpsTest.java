import org.junit.Before;
import org.junit.Test;
import uk.org.fyodor.generators.Generator;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

public class PresentableChirpsTest {

    private static final String MOMENTS_AGO = "(moments ago)";
    private static final String AN_HOUR_AGO = "(1 hour ago)";
    private static final String FIVE_HOURS_AGO = "(5 hours ago)";
    private static final String A_DAY_AGO = "(1 day ago)";
    private static final String FIVE_DAYS_AGO = "(5 days ago)";

    LocalDateTime now = LocalDateTime.now();
    TestClock clock;
    ChirpGenerator chirpGenerator;
    Generator<String> userNameGenerator = TermChirpRDG.userNameGenerator;
    Generator<String> messageGenerator = TermChirpRDG.messageGenerator;
    PresentableChirps presentableChirps = new PresentableChirps();

    @Before
    public void setupChirpGenerator() {
        clock = new TestClock(now);
        chirpGenerator = new ChirpGenerator(clock);
    }

    @Test
    public void presentChirpsForReading() {
        String message = messageGenerator.next();
        Deque<Chirp> chirps = new ArrayDeque<>();
        chirps.push(chirpGenerator.generateChirp(message));
        Iterator<String> formatted = presentableChirps.format(chirps, null);
        assertThat(formatted.next()).isEqualTo(String.format("%s %s", message, MOMENTS_AGO));
    }

    @Test
    public void orderIsPreservedForReading(){
        checkOrderForCommand(null);
    }

    private void checkOrderForCommand(String command) {
        Deque<Chirp> chirps = new ArrayDeque<>();
        String message5DaysAgo = messageGenerator.next();
        String message1DayAgo = messageGenerator.next();
        String message5HoursAgo = messageGenerator.next();
        String message1HourAgo = messageGenerator.next();
        String messageMomentsAgo = messageGenerator.next();

        clock.setNow(now.minusDays(5));
        chirps.push(chirpGenerator.generateChirp(message5DaysAgo));
        clock.setNow(now.minusDays(1));
        chirps.push(chirpGenerator.generateChirp(message1DayAgo));
        clock.setNow(now.minusHours(5));
        chirps.push(chirpGenerator.generateChirp(message5HoursAgo));
        clock.setNow(now.minusHours(1));
        chirps.push(chirpGenerator.generateChirp(message1HourAgo));
        clock.setNow(now);
        chirps.push(chirpGenerator.generateChirp(messageMomentsAgo));
        Iterator<String> formatted = presentableChirps.format(chirps, command);

        assertThat(formatted.next()).isEqualTo(String.format("%s %s", messageMomentsAgo, MOMENTS_AGO));
        assertThat(formatted.next()).isEqualTo(String.format("%s %s", message1HourAgo, AN_HOUR_AGO));
        assertThat(formatted.next()).isEqualTo(String.format("%s %s", message5HoursAgo, FIVE_HOURS_AGO));
        assertThat(formatted.next()).isEqualTo(String.format("%s %s", message1DayAgo, A_DAY_AGO));
        assertThat(formatted.next()).isEqualTo(String.format("%s %s", message5DaysAgo, FIVE_DAYS_AGO));
        assertThat(formatted.hasNext()).isFalse();
    }
}
