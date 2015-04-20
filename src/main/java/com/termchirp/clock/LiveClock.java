package com.termchirp.clock;

import java.time.LocalDateTime;

public class LiveClock implements Clock {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
