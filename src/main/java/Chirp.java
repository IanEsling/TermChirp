import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Chirp {
    private String message;
    private LocalDateTime dateTime;
    private PrettyTime prettyTime = new PrettyTime();

    public Chirp(String message, LocalDateTime dateTime) {
        this.dateTime = dateTime;
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return message +
                " (" +
                prettyTime.format(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())) +
                ")";
    }
}
