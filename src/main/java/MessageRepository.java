public class MessageRepository {

    private Timelines timelines;

    public MessageRepository(Timelines timelines) {

        this.timelines = timelines;
    }

    public String command(String userName, String command, String message) {
        if (Command.POST_INPUT.equals(command)) {
            timelines.addToTimeline(userName, message);
        } else if (command == null) {
            timelines.getTimelineForUser(userName);
        }

        return "";
    }
}
