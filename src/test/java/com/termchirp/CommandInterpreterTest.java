package com.termchirp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.org.fyodor.generators.Generator;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

import static com.termchirp.TermChirpRDG.messageGenerator;
import static com.termchirp.TermChirpRDG.userNameGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CommandInterpreterTest {

    @Mock
    Timelines timelines;

    CommandInterpreter repo;

    @Before
    public void createRepo() {
        repo = new CommandInterpreter(timelines);
    }

    @Test
    public void userCanPostToTimelineAndEmptySetReturned() {
        String userName = userNameGenerator.next();
        String message = messageGenerator.next();
        Collection<Chirp> chirps = repo.command(userName, TermChirp.POST_INPUT, message);
        verify(timelines).addToTimeline(userName, message);
        assertThat(chirps.size()).isEqualTo(0);
    }

    @Test
    public void usersCanSeeTheirWall() {
        given(timelines.getWallForUser(anyString()))
                .willReturn(new ArrayDeque<>());
        String userName = userNameGenerator.next();
        repo.command(userName, TermChirp.WALL_INPUT, null);
        verify(timelines).getWallForUser(userName);
    }

    @Test
    public void userWallPostsAreInChronologicalOrder() {
        String userName = userNameGenerator.next();
        Generator<Deque<Chirp>> chirpStackGenerator = TermChirpRDG.generatorOfChronologicallyOrderedStackOfChirps(userName);
        Deque<Chirp> randomStackOfRandomChirps = chirpStackGenerator.next();
        given(timelines.getWallForUser(anyString()))
                .willReturn(randomStackOfRandomChirps);
        Collection<Chirp> chirps = repo.command(userName, TermChirp.WALL_INPUT, null);

        assertThat(chirps).isEqualTo(randomStackOfRandomChirps);
        assertThat(chirps.size()).isEqualTo(randomStackOfRandomChirps.size());
        LocalDateTime earliest = LocalDateTime.now();
        for (Chirp chirp : chirps) {
            assertThat(chirp.getDateTime()).isBefore(earliest);
            earliest = chirp.getDateTime();
        }
    }

    @Test
    public void userCanReadOtherUsersTimeline() {
        given(timelines.getTimelineForUser(anyString()))
                .willReturn(new LinkedList<>());
        String userName = userNameGenerator.next();
        repo.command(userName, null, null);
        verify(timelines).getTimelineForUser(userName);
    }

    @Test
    public void otherUserTimelinesAreInChronologicalOrder() {
        String userName = userNameGenerator.next();
        Generator<Deque<Chirp>> chirpStackGenerator = TermChirpRDG.generatorOfChronologicallyOrderedStackOfChirps(userName);
        Deque<Chirp> randomChirps = chirpStackGenerator.next();
        given(timelines.getTimelineForUser(anyString()))
                .willReturn(randomChirps);
        Collection<Chirp> chirps = repo.command(userName, null, null);

        assertThat(chirps).containsExactlyElementsOf(randomChirps);
        assertThat(chirps.size()).isEqualTo(randomChirps.size());
        LocalDateTime earliest = LocalDateTime.now();
        for (Chirp chirp : chirps) {
            assertThat(chirp.getDateTime()).isBefore(earliest);
            earliest = chirp.getDateTime();
        }
    }

    @Test
    public void usersCanFollowOtherUsers(){
        String followingUser = userNameGenerator.next();
        String followedUser = userNameGenerator.next();
        given(timelines.follow(anyString(), anyString()))
                .willReturn(new LinkedList<>());
        repo.command(followingUser, TermChirp.FOLLOWS_INPUT, followedUser);
        verify(timelines).follow(followingUser, followedUser);
    }
}