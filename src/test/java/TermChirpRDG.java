import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.RDG;
import uk.org.fyodor.range.Range;

import java.time.LocalDateTime;
import java.util.List;

public class TermChirpRDG extends RDG {

    public static Generator<String> userNameGenerator = RDG.string(Range.closed(5, 15));
    public static Generator<String> messageGenerator = RDG.string(Range.closed(25, 150), Range.closed(32, 126));
    public static Generator<String> spacesGenerator = RDG.string(Range.closed(1, 10), " ");

    public static Generator<List<Chirp>> generatorOfListOfChirps(){
        return generatorOfListOfChirps(Range.closed(10, 30));
    }

    public static Generator<List<Chirp>> generatorOfListOfChirps(Range<Integer> range) {
        return RDG.list(chirpGenerator(), range);
    }

    public static Generator<Chirp> chirpGenerator() {
        return new Generator<Chirp>() {
            Generator<String> messageGenerator = TermChirpRDG.messageGenerator;

            public Chirp next() {
                return new Chirp(messageGenerator.next(),
                        LocalDateTime.now().minusMinutes(RDG.longVal(Range.closed(0l, 7200l)).next()));
            }
        };
    }
}
