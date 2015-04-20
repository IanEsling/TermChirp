package com.termchirp.clock;

import java.time.LocalDateTime;

public class TestClock implements Clock {

    private LocalDateTime now;

    public TestClock(LocalDateTime now) {
        this.now = now;
    }

    public void setNow(LocalDateTime newNow) {
        this.now = newNow;
    }

    @Override
    public LocalDateTime now() {
        return now;
    }
}
