public enum Command {

    Wall {
        @Override
        public boolean canHandle(String command) {
            return WALL_INPUT.equals(command);
        }
    }, Post {
        @Override
        public boolean canHandle(String command) {
            return POST_INPUT.equals(command);
        }
    }, Follows {
        @Override
        public boolean canHandle(String command) {
            return FOLLOWS_INPUT.equals(command);
        }
    }, Reading {
        @Override
        public boolean canHandle(String command) {
            return command == null;
        }
    };

    public static Command commandFor(String command) {
        for (Command c : Command.values()) {
            if (c.canHandle(command)) {
                return c;
            }
        }
        throw new UnsupportedOperationException("unknown command " + command);
    }

    public abstract boolean canHandle(String command);

    public static final String POST_INPUT = "->";
    public static final String WALL_INPUT = "wall";
    public static final String FOLLOWS_INPUT = "follows";
}
