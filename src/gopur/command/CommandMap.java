package gopur.command;

public interface CommandMap {
    boolean register(Command command);
    boolean dispatch(String cmd_line);
    void clearCommands();
    Command getCommand(String name);
}
