import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class PresentableChirps {

    public Iterator<String> format(Deque<Chirp> chirps, String command) {

            Deque<String> formatted = new ArrayDeque<>(chirps.size());
            Iterator<Chirp> iter = chirps.iterator();
            while (iter.hasNext()) {
                formatted.push(iter.next().toString());
            }
            return formatted.descendingIterator();
        
    }
}
