import java.time.LocalDateTime;

public class Chirp {
    private String message;
    private LocalDateTime dateTime;

    public Chirp(String message, LocalDateTime dateTime) {
        this.dateTime = dateTime;
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
