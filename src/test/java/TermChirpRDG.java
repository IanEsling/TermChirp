import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.RDG;
import uk.org.fyodor.range.Range;

public class TermChirpRDG extends RDG {

    public static Generator<String> userNameGenerator = RDG.string(Range.closed(5, 15));
    public static Generator<String> messageGenerator = RDG.string(Range.closed(25, 150), Range.closed(32, 126));
    public static Generator<String> spacesGenerator = RDG.string(Range.closed(1, 10), " ");
}
