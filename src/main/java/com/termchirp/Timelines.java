package com.termchirp;

import com.termchirp.clock.Clock;
import com.termchirp.clock.LiveClock;

import java.util.*;

public class Timelines {

    private final Map<String, Collection<String>> follows;
    private final Map<String, Deque<Chirp>> timelines;
    private final Clock clock;

    public Timelines() {
        this(new LiveClock());
    }

    public Timelines(Clock clock) {
        this(clock, new HashMap<>());
    }

    public Timelines(Clock clock, Map<String, Deque<Chirp>> timelines) {
        this.clock = clock;
        this.timelines = timelines;
        this.follows = new HashMap<>();
    }

    public Collection<Chirp> addToTimeline(String userName, String message) {
        Chirp chirp = new Chirp(userName, message, clock.now());
        if (timelines.containsKey(userName)) {
            timelines.get(userName).push(chirp);
        } else {
            Deque<Chirp> messages = new ArrayDeque<>();
            messages.add(chirp);
            timelines.put(userName, messages);
        }
        return new ArrayDeque<>();
    }

    public Collection<Chirp> getTimelineForUser(String userName) {
        if (timelines.containsKey(userName)) {
            return timelines.get(userName);
        } else {
            return new ArrayDeque<>();
        }
    }

    public Collection<Chirp> getWallForUser(String userName) {
        if (follows.containsKey(userName)) {
            TreeSet<Chirp> wall = new TreeSet<>(getTimelineForUser(userName));
            for (String followedUser : follows.get(userName)) {
                wall.addAll(getTimelineForUser(followedUser));
            }
            return wall;
        } else {
            return getTimelineForUser(userName);
        }
    }

    public Collection<Chirp> follow(String followingUser, String followedUser) {
        if (follows.containsKey(followingUser)) {
            follows.get(followingUser).add(followedUser);
        } else {
            Collection<String> followedUsers = new ArrayList<>();
            followedUsers.add(followedUser);
            follows.put(followingUser, followedUsers);
        }
        return new ArrayDeque<>();
    }
}
