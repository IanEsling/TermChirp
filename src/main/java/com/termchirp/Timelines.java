package com.termchirp;

import com.termchirp.clock.Clock;
import com.termchirp.clock.LiveClock;

import java.util.*;

public class Timelines {

    private final Clock clock;
    //username mapped to all the users they follow
    private final Map<String, Collection<String>> follows;
    //use a stack (last in first out) to store user's chirps in the same order they want to see them
    private final Map<String, Deque<Chirp>> timelines;

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
            Deque<Chirp> messages = new LinkedList<>();
            messages.add(chirp);
            timelines.put(userName, messages);
        }
        return new LinkedList<>();
    }

    public Collection<Chirp> getTimelineForUser(String userName) {
        Collection<Chirp> timeline = timelines.get(userName);
        if (timeline == null) {
            return new LinkedList<>();
        } else {
            return timeline;
        }
    }

    public Collection<Chirp> getWallForUser(String userName) {
        if (follows.containsKey(userName)) {
            //use a treeset to sort all chirps from our user and the users they follow by date
            TreeSet<Chirp> wall = new TreeSet<>(getTimelineForUser(userName));
            for (String followedUser : follows.get(userName)) {
                wall.addAll(getTimelineForUser(followedUser));
            }
            return wall;
        } else {
            //if they're not following anyone their chirps are already in order
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
        return new LinkedList<>();
    }
}
