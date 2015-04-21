package com.termchirp;

import java.util.Collection;
import java.util.LinkedList;

import static com.termchirp.TermChirp.*;

public class CommandInterpreter {

    private Timelines timelines;

    public CommandInterpreter() {
        this(new Timelines());
    }

    public CommandInterpreter(Timelines timelines) {
        this.timelines = timelines;
    }

    public Collection<Chirp> command(String userName, String command, String message) {
        if (command == null) {
            return timelines.getTimelineForUser(userName);
        } else if (POST_INPUT.equals(command)) {
            return timelines.addToTimeline(userName, message);
        } else if (WALL_INPUT.equals(command)) {
            return timelines.getWallForUser(userName);
        } else if (FOLLOWS_INPUT.equals(command)) {
            return timelines.follow(userName, message);
        }

        return new LinkedList<>();
    }
}
