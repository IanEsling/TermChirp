package com.termchirp;

import com.termchirp.clock.TestClock;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static com.termchirp.TermChirpRDG.userNameGenerator;
import static com.termchirp.TermChirpRDG.messageGenerator;

public class ChirpTest {

    LocalDateTime now = LocalDateTime.now();
    TestClock clock;

    @Before
    public void setupChirpGenerator() {
        clock = new TestClock(now);
    }

    @Test
    public void messageStoredWithDateTime() {
        String message = messageGenerator.next();
        String userName = userNameGenerator.next();
        Chirp chirp = new Chirp(userName, message, clock.now());
        assertThat(chirp.getDateTime()).isEqualTo(now);
    }
}