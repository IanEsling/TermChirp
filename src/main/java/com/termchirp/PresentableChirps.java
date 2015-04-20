package com.termchirp;

import com.termchirp.clock.Clock;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;

public class PresentableChirps {

    private Clock clock;
    private final PrettyTime prettyTime = new PrettyTime();

    public PresentableChirps(Clock clock) {
        this.clock = clock;
    }

    public Iterator<String> format(Deque<Chirp> chirps, String command) {

        Deque<String> presentable = new ArrayDeque<>(chirps.size());
        Iterator<Chirp> iter = chirps.iterator();
        while (iter.hasNext()) {
            if (TermChirp.WALL_INPUT.equals(command)) {
                presentable.push(formatForWall(iter.next()));
            } else {
                presentable.push(formatForReading(iter.next()));
            }
        }
        return presentable.descendingIterator();

    }

    private String formatForReading(Chirp chirp) {
        return String.format("%s (%s)",
                chirp.getMessage(),
                prettyPrint(chirp.getDateTime()));
    }

    private String formatForWall(Chirp chirp) {
        return String.format("%s - %s (%s)",
                chirp.getUserName(),
                chirp.getMessage(),
                prettyPrint(chirp.getDateTime()));
    }

    private String prettyPrint(LocalDateTime dateTime) {
        long diff = clock.now().toInstant(ZoneOffset.UTC).toEpochMilli() - dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        if (diff >= 1000 * 60 * 5) {
            return prettyTime.format(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
        } else if (diff > 1000 * 60 * 3.5) {
            return "4 minutes ago";
        } else if (diff > 1000 * 60 * 2.5) {
            return "3 minutes ago";
        } else if (diff > 1000 * 60 * 1.5) {
            return "2 minutes ago";
        } else if (diff >= 1000 * 60) {
            return "1 minute ago";
        } else if (diff >= 1000 * 10) {
            return String.format("%d seconds ago", diff/1000);
        } else {
            return "moments ago";
        }
    }
}
