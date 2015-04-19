import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Chirp implements Comparable<Chirp> {
    private final String message;
    private final String userName;
    private final LocalDateTime dateTime;
    private final PrettyTime prettyTime = new PrettyTime();

    public Chirp(String userName, String message, LocalDateTime dateTime) {
        this.userName = userName;
        this.dateTime = dateTime;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message +
                " (" +
                prettyTime.format(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())) +
                ")";
    }

    public String toStringWithUser() {
        return  userName +
                " - " +
                message +
                " (" +
                prettyTime.format(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())) +
                ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chirp chirp = (Chirp) o;

        if (!dateTime.equals(chirp.dateTime)) return false;
        if (!message.equals(chirp.message)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + dateTime.hashCode();
        return result;
    }

    @Override
    public int compareTo(Chirp that) {
        return this.dateTime.isBefore(that.dateTime) ? 1 : -1;
    }
}
