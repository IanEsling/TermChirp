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
    Timelines timelines;

    @Before
    public void setupTimelines() {
        clock = new TestClock(now);
        timelines = new Timelines(clock);
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
        Deque<Chirp> chirps = TermChirpRDG.generatorOfChronologicallyOrderedStackOfChirps(userName).next();
        Map<String, Deque<Chirp>> existingChirps = new HashMap<>();
        existingChirps.put(userName, chirps);
        timelines = new Timelines(clock, existingChirps);
        Collection<Chirp> wall = timelines.getWallForUser(userName);
        assertThat(wall).containsExactlyElementsOf(chirps);
    }

    @Test
    public void followingUserShowsTheirChirpsOnWall() {
        String followingUser = userNameGenerator.next();
        String followedUser = userNameGenerator.next();
        Deque<Chirp> followingUserChirps = TermChirpRDG.generatorOfChronologicallyOrderedStackOfChirps(followingUser).next();
        Deque<Chirp> followedUserChirps = TermChirpRDG.generatorOfChronologicallyOrderedStackOfChirps(followedUser).next();
        Map<String, Deque<Chirp>> existingChirps = new HashMap<>();
        existingChirps.put(followingUser, followingUserChirps);
        existingChirps.put(followedUser, followedUserChirps);
        timelines = new Timelines(clock, existingChirps);
        timelines.follow(followingUser, followedUser);
        Collection<Chirp> wallChirps = timelines.getWallForUser(followingUser);
        Collection<Chirp> allChirps = new ArrayList<>(followingUserChirps);
        allChirps.addAll(followedUserChirps);
        assertThat(wallChirps).containsAll(allChirps);
        LocalDateTime earliest = LocalDateTime.now();
        Iterator<Chirp> iter = wallChirps.iterator();
        while (iter.hasNext()) {
            Chirp chirp = iter.next();
            assertThat(chirp.getDateTime()).isBefore(earliest);
            earliest = chirp.getDateTime();
        }
    }
}