package com.termchirp;

import java.util.*;

public class Timelines {

    private final Map<String, Collection<String>> follows;
    private final Map<String, Deque<Chirp>> timelines;
    private final ChirpGenerator chirpGenerator;

    public Timelines(ChirpGenerator chirpGenerator) {
        this(chirpGenerator, new HashMap<>());
    }

    public Timelines(ChirpGenerator chirpGenerator, Map<String, Deque<Chirp>> timelines) {
        this.chirpGenerator = chirpGenerator;
        this.timelines = timelines;
        this.follows = new HashMap<>();
    }

    public void addToTimeline(String userName, String message) {
        Chirp chirp = chirpGenerator.generateChirp(userName, message);
        if (timelines.containsKey(userName)) {
            timelines.get(userName).push(chirp);
        } else {
            Deque<Chirp> messages = new ArrayDeque<>();
            messages.add(chirp);
            timelines.put(userName, messages);
        }
    }

    public Deque<Chirp> getTimelineForUser(String userName) {

        if (timelines.containsKey(userName)) {
            return timelines.get(userName);
        } else {
            return new ArrayDeque<>();
        }
    }

    public Deque<Chirp> getWallForUser(String userName) {
        Deque<Chirp> wall = getTimelineForUser(userName);
        if (follows.containsKey(userName)) {
            for (String followedUser : follows.get(userName)) {
                wall.addAll(getTimelineForUser(followedUser));
            }
        }
        return wall;
    }

    public Deque<Chirp> follow(String followingUser, String followedUser) {
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
