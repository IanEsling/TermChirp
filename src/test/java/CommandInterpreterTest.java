import org.junit.Before;
import org.junit.Test;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.RDG;
import uk.org.fyodor.range.Range;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandInterpreterTest {

    Generator<String> stringGenerator = RDG.string(Range.closed(5, 15));
    CommandInterpreter commandInterpreter;

    @Before
    public void runner() {
        commandInterpreter = new CommandInterpreter();
    }

    @Test
    public void returnsPostCommand() {
        assertThat(commandInterpreter.command(stringGenerator.next(),
                Command.POST_INPUT,
                stringGenerator.next()))
                .isEqualTo(Command.Post);
    }

    @Test
    public void returnsWallCommand() {
        assertThat(commandInterpreter.command(stringGenerator.next(),
                Command.WALL_INPUT))
                .isEqualTo(Command.Wall);
    }

    @Test
    public void returnsFollowsCommand() {
        assertThat(commandInterpreter.command(stringGenerator.next(),
                Command.FOLLOWS_INPUT,
                stringGenerator.next()))
                .isEqualTo(Command.Follows);
    }

    @Test
    public void returnsReadingCommand() {
        assertThat(commandInterpreter.command(stringGenerator.next()))
                .isEqualTo(Command.Reading);
    }
}