package com.termchirp;

import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.RDG;
import uk.org.fyodor.range.Range;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class TermChirpRDG extends RDG {

    public static Generator<String> userNameGenerator = RDG.string(Range.closed(5, 15));
    public static Generator<String> messageGenerator = RDG.string(Range.closed(25, 150), Range.closed(32, 126));
    public static Generator<String> spacesGenerator = RDG.string(Range.closed(1, 10), " ");

    public static class DequeGenerator<T extends Comparable<T>> implements Generator<Deque<T>> {

        private final Generator<List<T>> listGeneratorOfT;

        public DequeGenerator(final Generator<List<T>> listGeneratorOfT) {
            this.listGeneratorOfT = listGeneratorOfT;
        }

        @Override
        public Deque<T> next() {
            //should probably get Fyodor to do this kind of thing on its own
            List<T> list = listGeneratorOfT.next();
            Collections.sort(list);
            Collections.reverse(list);
            Deque<T> stack = new LinkedList<>();
            for (T t : list) {
                stack.push(t);
            }

            return stack;
        }
    }

    public static Generator<Deque<Chirp>> generatorOfChronologicallyOrderedStackOfChirps(String userName) {
        return generatorOfChronologicallyOrderedStackOfChirps(userName, Range.closed(10, 30));
    }

    public static Generator<Deque<Chirp>> generatorOfChronologicallyOrderedStackOfChirps(String userName, Range<Integer> range) {
        return new DequeGenerator<>(RDG.list(chirpGenerator(userName), range));
    }

    public static Generator<Chirp> chirpGenerator(String userName) {
        return new Generator<Chirp>() {
            Generator<String> messageGenerator = TermChirpRDG.messageGenerator;

            public Chirp next() {
                return new Chirp(userName,
                        messageGenerator.next(),
                        LocalDateTime.now().minusMinutes(RDG.longVal(Range.closed(0l, 7200l)).next()));
            }
        };
    }
}
