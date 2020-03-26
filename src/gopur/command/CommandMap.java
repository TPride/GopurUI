package gopur.command;

import gopur.plugin.Plugin;

public interface CommandMap {
    boolean register(Command command);
    boolean unregister(String commandName);
    boolean unregister(Command command);
    boolean dispatch(String cmd_line);
    void clearCommands();
    Command getCommand(String name);
}
