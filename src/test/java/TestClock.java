import java.time.LocalDateTime;

public class TestClock implements Clock {

    private LocalDateTime now;

    public TestClock(LocalDateTime now) {
        this.now = now;
    }

    @Override
    public LocalDateTime now() {
        return now;
    }
}
