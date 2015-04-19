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
    private static final String TEN_SECONDS_AGO = "(10 seconds ago)";
    private static final String FIFTY_NINE_SECONDS_AGO = "(59 seconds ago)";
    private static final String ONE_MINUTE_AGO = "(1 minute ago)";
    private static final String TWO_MINUTES_AGO = "(2 minutes ago)";
    private static final String THREE_MINUTES_AGO = "(3 minutes ago)";
    private static final String FOUR_MINUTES_AGO = "(4 minutes ago)";
    private static final String FIVE_MINUTES_AGO = "(5 minutes ago)";
    private static final String AN_HOUR_AGO = "(1 hour ago)";
    private static final String FIVE_HOURS_AGO = "(5 hours ago)";
    private static final String A_DAY_AGO = "(1 day ago)";
    private static final String FIVE_DAYS_AGO = "(5 days ago)";

    LocalDateTime now = LocalDateTime.now();
    TestClock clock;
    ChirpGenerator chirpGenerator;
    PresentableChirps presentableChirps;
    Generator<String> userNameGenerator = TermChirpRDG.userNameGenerator;
    Generator<String> messageGenerator = TermChirpRDG.messageGenerator;

    @Before
    public void setupChirpGenerator() {
        clock = new TestClock(now);
        presentableChirps = new PresentableChirps(clock);
        chirpGenerator = new ChirpGenerator(clock);
    }

    @Test
    public void presentChirpsForWall() {
        String userName = userNameGenerator.next();
        String message = messageGenerator.next();
        Deque<Chirp> chirps = new ArrayDeque<>();
        chirps.push(chirpGenerator.generateChirp(userName, message));
        Iterator<String> formatted = presentableChirps.format(chirps, Command.WALL_INPUT);
        assertThat(formatted.next()).isEqualTo(String.format("%s - %s %s", userName, message, MOMENTS_AGO));
    }

    @Test
    public void presentChirpsForReading() {
        String userName = userNameGenerator.next();
        String message = messageGenerator.next();
        Deque<Chirp> chirps = new ArrayDeque<>();
        chirps.push(chirpGenerator.generateChirp(userName, message));
        Iterator<String> formatted = presentableChirps.format(chirps, null);
        assertThat(formatted.next()).isEqualTo(String.format("%s %s", message, MOMENTS_AGO));
    }

    @Test
    public void orderIsPreservedForWall(){
        checkOrderForCommand(userNameGenerator.next(), Command.WALL_INPUT);
    }

    @Test
    public void orderIsPreservedForReading(){
        checkOrderForCommand(userNameGenerator.next(), null);
    }

    private void checkOrderForCommand(String userName, String command) {
        Deque<Chirp> chirps = new ArrayDeque<>();
        String message5DaysAgo = messageGenerator.next();
        String message1DayAgo = messageGenerator.next();
        String message5HoursAgo = messageGenerator.next();
        String message1HourAgo = messageGenerator.next();
        String message5MinutesAgo = messageGenerator.next();
        String message4MinutesAgo = messageGenerator.next();
        String message3MinutesAgo = messageGenerator.next();
        String message2MinutesAgo = messageGenerator.next();
        String message1MinuteAgo = messageGenerator.next();
        String message59SecondsAgo = messageGenerator.next();
        String message10SecondsAgo = messageGenerator.next();
        String messageMomentsAgo = messageGenerator.next();

        clock.setNow(now.minusDays(5));
        chirps.push(chirpGenerator.generateChirp(userName, message5DaysAgo));
        clock.setNow(now.minusDays(1));
        chirps.push(chirpGenerator.generateChirp(userName, message1DayAgo));
        clock.setNow(now.minusHours(5));
        chirps.push(chirpGenerator.generateChirp(userName, message5HoursAgo));
        clock.setNow(now.minusHours(1));
        chirps.push(chirpGenerator.generateChirp(userName, message1HourAgo));
        clock.setNow(now.minusMinutes(5));
        chirps.push(chirpGenerator.generateChirp(userName, message5MinutesAgo));
        clock.setNow(now.minusMinutes(4));
        chirps.push(chirpGenerator.generateChirp(userName, message4MinutesAgo));
        clock.setNow(now.minusMinutes(3));
        chirps.push(chirpGenerator.generateChirp(userName, message3MinutesAgo));
        clock.setNow(now.minusMinutes(2));
        chirps.push(chirpGenerator.generateChirp(userName, message2MinutesAgo));
        clock.setNow(now.minusMinutes(1));
        chirps.push(chirpGenerator.generateChirp(userName, message1MinuteAgo));
        clock.setNow(now.minusSeconds(59));
        chirps.push(chirpGenerator.generateChirp(userName, message59SecondsAgo));
        clock.setNow(now.minusSeconds(10));
        chirps.push(chirpGenerator.generateChirp(userName, message10SecondsAgo));
        clock.setNow(now.minusSeconds(5));
        chirps.push(chirpGenerator.generateChirp(userName, messageMomentsAgo));
        clock.setNow(now);
        Iterator<String> formatted = presentableChirps.format(chirps, command);

        if (command == null) {
            assertThat(formatted.next()).isEqualTo(String.format("%s %s", messageMomentsAgo, MOMENTS_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s %s", message10SecondsAgo, TEN_SECONDS_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s %s", message59SecondsAgo, FIFTY_NINE_SECONDS_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s %s", message1MinuteAgo, ONE_MINUTE_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s %s", message2MinutesAgo, TWO_MINUTES_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s %s", message3MinutesAgo, THREE_MINUTES_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s %s", message4MinutesAgo, FOUR_MINUTES_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s %s", message5MinutesAgo, FIVE_MINUTES_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s %s", message1HourAgo, AN_HOUR_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s %s", message5HoursAgo, FIVE_HOURS_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s %s", message1DayAgo, A_DAY_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s %s", message5DaysAgo, FIVE_DAYS_AGO));
        } else if (Command.WALL_INPUT.equals(command)) {
            assertThat(formatted.next()).isEqualTo(String.format("%s - %s %s", userName, messageMomentsAgo, MOMENTS_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s - %s %s", userName, message10SecondsAgo, TEN_SECONDS_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s - %s %s", userName, message59SecondsAgo, FIFTY_NINE_SECONDS_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s - %s %s", userName, message1MinuteAgo, ONE_MINUTE_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s - %s %s", userName, message2MinutesAgo, TWO_MINUTES_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s - %s %s", userName, message3MinutesAgo, THREE_MINUTES_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s - %s %s", userName, message4MinutesAgo, FOUR_MINUTES_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s - %s %s", userName, message5MinutesAgo, FIVE_MINUTES_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s - %s %s", userName, message1HourAgo, AN_HOUR_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s - %s %s", userName, message5HoursAgo, FIVE_HOURS_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s - %s %s", userName, message1DayAgo, A_DAY_AGO));
            assertThat(formatted.next()).isEqualTo(String.format("%s - %s %s", userName, message5DaysAgo, FIVE_DAYS_AGO));
        }
        assertThat(formatted.hasNext()).isFalse();
    }
}
