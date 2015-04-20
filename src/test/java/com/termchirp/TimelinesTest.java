package com.termchirp;

import com.termchirp.clock.Clock;
import com.termchirp.clock.TestClock;
import org.junit.Before;
import org.junit.Test;
import uk.org.fyodor.generators.RDG;
import uk.org.fyodor.range.Range;

import java.time.LocalDateTime;
import java.util.*;

import static com.termchirp.TermChirpRDG.messageGenerator;
import static com.termchirp.TermChirpRDG.userNameGenerator;
import static org.assertj.core.api.Assertions.assertThat;

public class TimelinesTest {

    LocalDateTime now = LocalDateTime.now();

    Clock clock;
    ChirpGenerator chirpGenerator;
    Timelines timelines;

    @Before
    public void setupTimelines() {
        clock = new TestClock(now);
        chirpGenerator = new ChirpGenerator(clock);
        timelines = new Timelines(chirpGenerator);
    }

    @Test
    public void createTimelineForFirstPost() {
        String userName = userNameGenerator.next();
        String message = messageGenerator.next();
        assertThat(timelines.getTimelineForUser(userName)).hasSize(0);
        timelines.addToTimeline(userName, message);
        assertThat(timelines.getTimelineForUser(userName)).hasSize(1);
        assertThat(timelines.getTimelineForUser(userName).iterator().next().getMessage()).isEqualTo(message);
    }

    @Test
    public void addThenReadPosts() {
        String userName = userNameGenerator.next();
        Integer numberOfMessagesToPost = RDG.integer(Range.closed(5, 50)).next();
        Collection<String> postedMessages = new ArrayList<>(numberOfMessagesToPost);
        for (int i = 0; i < numberOfMessagesToPost; i++) {
            String message = messageGenerator.next();
            postedMessages.add(message);
            timelines.addToTimeline(userName, message);
        }
        for (Chirp chirp : timelines.getTimelineForUser(userName)) {
            assertThat(postedMessages).contains(chirp.getMessage());
        }
        assertThat(timelines.getTimelineForUser(userName)).hasSize(numberOfMessagesToPost);
    }

    @Test
    public void returnWallPosts() {
        String userName = userNameGenerator.next();
        Deque<Chirp> chirps = TermChirpRDG.generatorOfStackOfChirps().next();
        Map<String, Deque<Chirp>> existingChirps = new HashMap<>();
        existingChirps.put(userName, chirps);
        timelines = new Timelines(chirpGenerator, existingChirps);
        Deque<Chirp> wall = timelines.getWallForUser(userName);
        assertThat(wall).containsExactlyElementsOf(chirps);
    }

    @Test
    public void followingUserShowsTheirChirpsOnWall() {
        String followingUser = userNameGenerator.next();
        String followedUser = userNameGenerator.next();
        Deque<Chirp> followingUserChirps = TermChirpRDG.generatorOfStackOfChirps().next();
        Deque<Chirp> followedUserChirps = TermChirpRDG.generatorOfStackOfChirps().next();
        Map<String, Deque<Chirp>> existingChirps = new HashMap<>();
        existingChirps.put(followingUser, followingUserChirps);
        existingChirps.put(followedUser, followedUserChirps);
        timelines = new Timelines(chirpGenerator, existingChirps);
        timelines.follow(followingUser, followedUser);
        Deque<Chirp> allChirps = timelines.getWallForUser(followingUser);
        allChirps.addAll(timelines.getWallForUser(followedUser));
        assertThat(allChirps).containsExactlyElementsOf(allChirps);
    }
}